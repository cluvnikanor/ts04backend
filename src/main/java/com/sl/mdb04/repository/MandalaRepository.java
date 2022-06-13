package com.sl.mdb04.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sl.mdb04.utils.Mandala;

public interface MandalaRepository extends MongoRepository<Mandala, String> {
}
