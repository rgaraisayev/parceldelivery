package com.guavapay.deliveryservice.repository;

import com.guavapay.deliveryservice.entity.User;
import com.guavapay.deliveryservice.enumeration.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findUserByEmail(String email);

    public List<User> findAllByRole(Role role, Pageable pageable);
}
