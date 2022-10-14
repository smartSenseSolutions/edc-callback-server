package com.demo.wrapper.repository;

import com.demo.wrapper.dao.SentAssetDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentAssetRepository extends JpaRepository<SentAssetDetails, String> {
}
