package com.groupproject.tomeraiders.models;

import com.groupproject.tomeraiders.models.data.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}