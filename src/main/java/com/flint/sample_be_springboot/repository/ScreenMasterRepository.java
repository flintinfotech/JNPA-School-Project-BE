package com.flint.sample_be_springboot.repository;

import com.flint.sample_be_springboot.entity.ScreenMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScreenMasterRepository extends JpaRepository<ScreenMaster, Long> {

    Optional<ScreenMaster> findByScreenName(String screenName);

}
