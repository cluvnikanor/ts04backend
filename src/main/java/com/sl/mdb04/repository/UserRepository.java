package com.sl.mdb04.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sl.mdb04.utils.User;

public interface UserRepository extends MongoRepository<User, String> {
	List<User> findByEmail(String email);

	ArrayList<User> findByEmailAndPassword(String email, String password);

	ArrayList<User> findByMandalaId(String mandalaId);

	void deleteByEmail(String email);

}
