package com.dmsBackend.repository;

import com.dmsBackend.entity.CategoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryMasterRepository extends JpaRepository<CategoryMaster,Integer> {
    List<CategoryMaster> findByActive(boolean active);
}
