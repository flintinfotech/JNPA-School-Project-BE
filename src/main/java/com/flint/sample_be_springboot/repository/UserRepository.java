package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUserName(String userName);

    Optional<UserEntity> findByUserNameAndUserIdNot(String userName, Long userId);

}
