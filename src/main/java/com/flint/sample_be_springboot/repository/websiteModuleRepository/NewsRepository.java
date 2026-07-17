package com.flint.sample_be_springboot.repository.websiteModuleRepository;

import com.flint.sample_be_springboot.entity.websiteModuleEntities.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long>, JpaSpecificationExecutor<NewsEntity> {
}
