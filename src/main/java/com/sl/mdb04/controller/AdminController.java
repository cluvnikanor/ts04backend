package com.sl.mdb04.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sl.mdb04.repository.DeletedUserRepository;
import com.sl.mdb04.repository.MandalaRepository;
import com.sl.mdb04.repository.StartPageRepository;
import com.sl.mdb04.repository.UserRepository;
import com.sl.mdb04.utils.Constants;
import com.sl.mdb04.utils.DeletedUser;
import com.sl.mdb04.utils.Mandala;
import com.sl.mdb04.utils.PublicUser;
import com.sl.mdb04.utils.StartPage;
import com.sl.mdb04.utils.TokenManager;
import com.sl.mdb04.utils.User;

@CrossOrigin(origins = Constants.ORIGINS)
@RestController
@RequestMapping("/api")
public class AdminController {
	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MandalaRepository mandalaRepository;
	@Autowired
	private StartPageRepository startPageRepository;
	@Autowired
	private DeletedUserRepository deletedUserRepository;

	@GetMapping("/viewPublicUsers")
	public ResponseEntity<List<PublicUser>> getPublicUsers() {
		List<User> users = new ArrayList<User>();
		userRepository.findAll().forEach(users::add);
		List<PublicUser> publicUsers = new ArrayList<PublicUser>();
		users.forEach(user -> publicUsers.add(user.publicCasting()));
		return new ResponseEntity<>(publicUsers, HttpStatus.OK);
	}

	@GetMapping("/viewAllUsers")
	public ResponseEntity<List<User>> getAllUsers(@RequestParam(name = "t") String adminToken) {
		if (!tokenManager.adminValidation(adminToken)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		List<User> users = new ArrayList<User>();
		userRepository.findAll().forEach(users::add);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/viewFreeUsers")
	public ResponseEntity<List<PublicUser>> getFreeUsers(@RequestParam(name = "t") String adminToken) {
		if (!tokenManager.adminValidation(adminToken)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		List<User> users = new ArrayList<User>();
		userRepository.findByMandalaId("").forEach(users::add);
		List<PublicUser> publicUsers = new ArrayList<PublicUser>();
		users.forEach(user -> publicUsers.add(user.publicCasting()));
		return new ResponseEntity<>(publicUsers, HttpStatus.OK);
	}

	@GetMapping("/viewDeletedUsers")
	public ResponseEntity<List<User>> getDeletedUsers(@RequestParam(name = "t") String adminToken) {
		if (!tokenManager.adminValidation(adminToken)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		List<DeletedUser> deletedUsers = new ArrayList<DeletedUser>();
		deletedUserRepository.findAll().forEach(deletedUsers::add);
		List<User> users = new ArrayList<User>();
		deletedUsers.forEach(deletedUser -> users.add(new User(deletedUser)));
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@DeleteMapping("/deleteUser")
	public ResponseEntity<String> deleteUser(@RequestParam(name = "id") String userId,
			@RequestParam(name = "t") String adminToken) {
		if (!tokenManager.adminValidation(adminToken)) {
			return new ResponseEntity<String>("UNAUTHORIZED", HttpStatus.OK);
		}
		Optional<User> _user = userRepository.findById(userId);
		if (userId.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			deletedUserRepository.save(_user.get().deletedCasting());
			Optional<Mandala> _mandala = mandalaRepository.findById(_user.get().getMandalaId());
			if (!_mandala.isEmpty()) {
				Mandala mandala = _mandala.get();
				mandala.removeUser(_user.get().getMandalaIndex());
				mandalaRepository.save(mandala);
			}
			userRepository.deleteById(userId);
			return new ResponseEntity<String>("User deleted", HttpStatus.OK);
		}
	}

	@PostMapping("/updateStartPage")
	public ResponseEntity<String> updateStartPage(@RequestBody StartPage startPage,
			@RequestParam(name = "t") String adminToken) {
		if (!(tokenManager.getUserEmail(adminToken).equals(Constants.ADMINEMAIL))) {
			return new ResponseEntity<String>("UNAUTHORIZED", HttpStatus.OK);
		}
		startPageRepository.save(startPage);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@GetMapping("/startPage")
	public ResponseEntity<StartPage> startPage() {
		return new ResponseEntity<StartPage>(startPageRepository.findAll().get(0), HttpStatus.OK);
	}

}
