package com.demo.wrapper.repository;

import com.demo.wrapper.dao.ReceivedAssetDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivedAssetRepository extends JpaRepository<ReceivedAssetDetails, String> {
}
