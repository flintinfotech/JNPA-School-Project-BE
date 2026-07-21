package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.UserInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformationEntity, Long>, JpaSpecificationExecutor<UserInformationEntity> {

    Optional<UserInformationEntity> findByUserEntity_UserId(Long userId);

}
