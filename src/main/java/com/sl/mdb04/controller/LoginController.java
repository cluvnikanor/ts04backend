package com.sl.mdb04.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sl.mdb04.repository.UserRepository;
import com.sl.mdb04.utils.Constants;
import com.sl.mdb04.utils.PublicUser;
import com.sl.mdb04.utils.TokenManager;
import com.sl.mdb04.utils.User;
import com.sl.mdb04.utils.UserResponse;

@CrossOrigin(origins = Constants.ORIGINS)
@RestController
@RequestMapping("/api")
public class LoginController {
	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/tokens")
	public ResponseEntity<Map<UUID, String>> tokens() {
		return new ResponseEntity<>(tokenManager.tokenMap, HttpStatus.OK);
	}


	@GetMapping("/login")
	public ResponseEntity<UserResponse> userLogin(@RequestParam(name = "email") String email,
			@RequestParam(name = "password") String password) {
		if (userRepository.findByEmailAndPassword(email, password).isEmpty()) {
			return new ResponseEntity<UserResponse>(new UserResponse("Username or password incorrect"), HttpStatus.OK);
		}
		UUID uuid = UUID.randomUUID();
		tokenManager.addUser(uuid, email);
		if (email.equals(Constants.ADMINEMAIL)) {
			return new ResponseEntity<UserResponse>(new UserResponse(uuid.toString(), "Hello Admin"), HttpStatus.OK);
		}
		return new ResponseEntity<UserResponse>(
				new UserResponse(uuid.toString(), "Welcome " + userRepository.findByEmail(email).get(0).getName()),
				HttpStatus.OK);
	}

	@GetMapping("/logout")
	public ResponseEntity<String> loguot(@RequestParam(name = "t") String token) {
		if (!tokenManager.validateUser(token))
			return new ResponseEntity<>(HttpStatus.OK);
		tokenManager.removeUser(token);
		return new ResponseEntity<String>("Log out", HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponse> customerRegister(@RequestBody User user) {
		if (!(userRepository.findByEmail(user.getEmail()).size() == 0)) {
			return new ResponseEntity<UserResponse>(new UserResponse("Can't add user: email is used"), HttpStatus.OK);
		}
		if (user.getName() == "" || user.getSite() == "" || user.getPhone() == "" || user.getEmail() == ""
				|| user.getPassword() == "") {
			return new ResponseEntity<UserResponse>(new UserResponse("Can't add user: fill all values"), HttpStatus.OK);
		}
		userRepository.save(user);
		UUID uuid = UUID.randomUUID();
		tokenManager.addUser(uuid, user.getEmail());
		if (user.getEmail().equals(Constants.ADMINEMAIL)) {
			return new ResponseEntity<UserResponse>(new UserResponse(uuid.toString(), "Hello Admin"), HttpStatus.OK);
		}
		return new ResponseEntity<UserResponse>(new UserResponse(uuid.toString(), "Welcome " + user.getName()),
				HttpStatus.OK);
	}

	@GetMapping("/user")
	public ResponseEntity<PublicUser> getUser(@RequestParam(name = "t") String token) {
		if (!tokenManager.validateUser(token))
			return new ResponseEntity<>(HttpStatus.OK);
		User user = userRepository.findByEmail(tokenManager.getUserEmail(token)).get(0);
		return new ResponseEntity<PublicUser>(user.publicCasting(), HttpStatus.OK);
	}

}
