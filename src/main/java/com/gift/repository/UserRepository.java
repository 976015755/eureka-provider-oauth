package com.gift.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.gift.dao.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findFirstByUsername(String username);
}
