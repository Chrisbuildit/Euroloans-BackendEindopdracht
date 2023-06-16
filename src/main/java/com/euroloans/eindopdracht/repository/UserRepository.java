package com.euroloans.eindopdracht.repository;

import com.euroloans.eindopdracht.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
