package com.ictasl.java.projectinventory.Persistence.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ictasl.java.projectinventory.Persistence.entity.User;

import java.util.List;


@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    Page<User> findAllByActive(int status, Pageable pageable);
    List<User> findAllByActive(int status);
}
