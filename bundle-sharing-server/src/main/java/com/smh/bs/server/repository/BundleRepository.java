package com.smh.bs.server.repository;

import com.smh.bs.server.model.Bundle;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

public interface BundleRepository extends ReactiveMongoRepository<Bundle,String> {
}
