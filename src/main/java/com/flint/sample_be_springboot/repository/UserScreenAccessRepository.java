package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.UserScreenAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserScreenAccessRepository extends JpaRepository<UserScreenAccessEntity, Long> {

    List<UserScreenAccessEntity> findByUser_UserId(Long userId);

    boolean existsByUser_UserIdAndScreen_ScreenId(Long userId,
                                                  Long screenId);

    Optional<UserScreenAccessEntity>
    findByUser_UserIdAndScreen_ScreenId(Long userId,
                                        Long screenId);

    void deleteByUser_UserIdAndScreen_ScreenId(Long userId,
                                               Long screenId);

    void deleteByUser_UserId(Long userId);
}