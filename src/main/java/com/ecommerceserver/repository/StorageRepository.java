package com.ecommerceserver.repository;

import com.ecommerceserver.model.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<StorageEntity, Long> {

    StorageEntity findOneByPath(String path);
}
