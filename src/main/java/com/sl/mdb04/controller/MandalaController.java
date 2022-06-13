package com.sl.mdb04.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sl.mdb04.repository.UserRepository;
import com.sl.mdb04.repository.MandalaRepository;
import com.sl.mdb04.utils.Constants;
import com.sl.mdb04.utils.Mandala;
import com.sl.mdb04.utils.PublicUser;
import com.sl.mdb04.utils.TokenManager;
import com.sl.mdb04.utils.User;

@CrossOrigin(origins = Constants.ORIGINS)
@RestController
@RequestMapping("/api")
public class MandalaController {
	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MandalaRepository mandalaRepository;

	@GetMapping("/mandalas")
	public ResponseEntity<List<Mandala>> getAllManadalas() {
		List<Mandala> mandalas = new ArrayList<Mandala>();
		mandalaRepository.findAll().forEach(mandalas::add);

//		LocalDateTime timeOn = LocalDateTime.of(2022, 5, 11, 12, 03);
//		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
//		ses.schedule(() -> createManadala(), LocalDateTime.now().until(timeOn, ChronoUnit.MILLIS),
//				TimeUnit.MILLISECONDS);
//		ses.schedule(() -> createManadala(), 10000, TimeUnit.MILLISECONDS);

//		if (mandalas.isEmpty()) {
//		return new ResponseEntity<List<Mandala>>(mandalas,HttpStatus.NO_CONTENT);
//	}
		return new ResponseEntity<>(mandalas, HttpStatus.OK);
	}

	@GetMapping("/mandala/{id}")
	public ResponseEntity<Mandala> getManadala(@PathVariable("id") String mandalaId) {
		Optional<Mandala> _mandala = mandalaRepository.findById(mandalaId);
//		if (_mandala.equals(null)) {
		if (_mandala.isEmpty()) {
			return new ResponseEntity<Mandala>(HttpStatus.OK);
		}
		return new ResponseEntity<Mandala>(_mandala.get(), HttpStatus.OK);
	}

//	@GetMapping("/userMandala/{id}")
//	public ResponseEntity<Mandala> getUserManadala(@PathVariable("id") String userId) {
//		Optional<Mandala> _mandala = mandalaRepository.findByUserId(userId);
////		if (_mandala.equals(null)) {
//		if (_mandala.isEmpty()) {
//			return new ResponseEntity<Mandala>(HttpStatus.OK);
//		}
//		return new ResponseEntity<Mandala>(_mandala.get(), HttpStatus.OK);
//	}

	@PostMapping("/addMandala")
	public ResponseEntity<String> createManadala(@RequestParam(name = "t") String adminToken,
			@RequestBody PublicUser sun) {
		if (!tokenManager.adminValidation(adminToken)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		Mandala mandala = new Mandala();
		mandala.addUser(sun, 0);
		mandalaRepository.save(mandala);
		return new ResponseEntity<String>(mandala.getId(), HttpStatus.OK);
	}

//	@GetMapping("/addMandala/{id}")
//	public ResponseEntity<String> createManadala(@PathVariable("id") String mandalaId) {
//		Mandala mandala = new Mandala();
//		mandala.setId(mandalaId);
//		mandalaRepository.save(mandala);
//		return new ResponseEntity<String>("Mandala created", HttpStatus.OK);
//	}

	@DeleteMapping("/deleteMandala")
	public ResponseEntity<String> endManadala(@RequestParam(name = "id") String mandalaId,
			@RequestParam(name = "t") String token) {
		StringJoiner message = new StringJoiner("/n");
		if (!tokenManager.adminValidation(token) || mandalaRepository.findById(mandalaId).get().getPublicUsers()[0]
				.getId().equals(userRepository.findByEmail(tokenManager.getUserEmail(token)).get(0).getId())) {
			return new ResponseEntity<String>("UNAUTHORIZED", HttpStatus.OK);
		}
		try {
			userRepository.findByMandalaId(mandalaId).stream().forEach(user -> {
				user.setMandalaId("");
				user.setMandalaIndex(-1);
				userRepository.save(user);
			});
		} catch (Exception e1) {
			message.add((CharSequence) e1);
		}
		try {
			mandalaRepository.deleteById(mandalaId);
		} catch (Exception e2) {
			message.add((CharSequence) e2);
			return new ResponseEntity<String>(message.toString(), HttpStatus.OK);
		}
		return new ResponseEntity<String>("Mandala deleted " + message.toString(), HttpStatus.OK);
	}

	@PostMapping("/takeRole")
	public ResponseEntity<String> takeRole(@RequestParam(name = "t") String token, @RequestParam(name = "i") int index,
			@RequestParam(name = "m") String mandalaId) {
		if (token.isEmpty() || (!tokenManager.validateUser(token)))
			return new ResponseEntity<String>("User not found", HttpStatus.OK);
		User user = userRepository.findByEmail(tokenManager.getUserEmail(token)).get(0);
		Optional<Mandala> _mandala = mandalaRepository.findById(mandalaId);
//		if (_mandala.equals(null)) {
		if (_mandala.isEmpty()) {
			return new ResponseEntity<String>("Mandala not found", HttpStatus.OK);
		}
		if (!_mandala.get().getPublicUsers()[index].getId().equals("")) {
			return new ResponseEntity<String>("Role already registered", HttpStatus.OK);
		}
		user.setMandalaId(mandalaId);
		user.setMandalaIndex(index);
		userRepository.save(user);
		Mandala mandala = _mandala.get();
		mandala.addUser(user.publicCasting(), index);
		mandalaRepository.save(mandala);
		return new ResponseEntity<String>(user.getName() + "added successfully", HttpStatus.OK);
	}

	@PostMapping("/takeSunRole")
	public ResponseEntity<String> takeSunRole(@RequestParam(name = "t") String token,
			@RequestParam(name = "m") String mandalaId, @RequestBody Date timeOut) {
		if (token.isEmpty() || (!tokenManager.validateUser(token)))
			return new ResponseEntity<String>("User not found", HttpStatus.OK);
		User user = userRepository.findByEmail(tokenManager.getUserEmail(token)).get(0);
		if (user.getMandalaIndex() != 0) {
			return new ResponseEntity<String>("User isn`t sun", HttpStatus.OK);
		}
		Optional<Mandala> _mandala = mandalaRepository.findById(mandalaId);
//		if (_mandala.equals(null)) {
		if (_mandala.isEmpty()) {
			return new ResponseEntity<String>("Mandala not found", HttpStatus.OK);
		}
		if (!_mandala.get().getPublicUsers()[0].getId().equals(user.getId())) {
			return new ResponseEntity<String>("User isn`t sun", HttpStatus.OK);
		}
		Mandala mandala = _mandala.get();
		mandala.setTimeOut(timeOut);
		mandalaRepository.save(mandala);
		LocalDate timeOutDate = timeOut.toInstant().atZone(ZoneId.of(Constants.LOCALZONEID)).toLocalDate();
		final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
		ses.schedule(() -> {
			PublicUser user1 = mandala.getPublicUsers()[1];
			PublicUser user2 = mandala.getPublicUsers()[2];
//			PublicUser user1 = new PublicUser();
//			PublicUser user2 = new PublicUser();
			endManadala(mandalaId, token);// It`s a problem, you should not use any token to do that
			Mandala mandala1 = new Mandala();
			if (user1 != null) {
				mandala1.addUser(user1, 0);
			}
			Mandala mandala2 = new Mandala();
			if (user2 != null) {
				mandala2.addUser(user2, 0);
			}
			mandalaRepository.save(mandala1);
			mandalaRepository.save(mandala2);
		}, LocalDateTime.now().until(timeOutDate, ChronoUnit.MILLIS), TimeUnit.MILLISECONDS);
		return new ResponseEntity<String>(user.getName() + "added successfully", HttpStatus.OK);
	}

}
