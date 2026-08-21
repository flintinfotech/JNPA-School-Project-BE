package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUserName(String userName);

    Optional<UserEntity> findByMobileNo(String userName);

    Optional<UserEntity> findByUserNameAndUserIdNot(String userName, Long userId);

    Optional<UserEntity> findByStudentEntity_StudentId(Long studentId);

}
