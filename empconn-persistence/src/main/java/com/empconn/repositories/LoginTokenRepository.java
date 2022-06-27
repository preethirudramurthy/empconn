package com.empconn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.empconn.persistence.entities.LoginToken;

@Repository
public interface LoginTokenRepository extends JpaRepository<LoginToken, String> {

}
