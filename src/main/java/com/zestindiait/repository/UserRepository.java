package com.zestindiait.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zestindiait.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

}
