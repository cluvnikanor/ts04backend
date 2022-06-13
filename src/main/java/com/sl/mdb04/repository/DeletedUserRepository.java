package com.sl.mdb04.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sl.mdb04.utils.DeletedUser;

public interface DeletedUserRepository extends MongoRepository<DeletedUser, String> {

}
