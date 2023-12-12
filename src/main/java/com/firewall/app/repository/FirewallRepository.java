package com.firewall.app.repository;

import com.firewall.app.entity.FirewallRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirewallRepository extends MongoRepository<FirewallRule, String> {
}
