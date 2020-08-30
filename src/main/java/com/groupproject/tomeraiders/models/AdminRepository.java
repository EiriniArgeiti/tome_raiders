package com.groupproject.tomeraiders.models;

import com.groupproject.tomeraiders.models.data.Admin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findByUsername(String username);

}