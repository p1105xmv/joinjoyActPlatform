package com.joinjoy.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joinjoy.model.bean.ViolationRecord;
@Repository
public interface ViolationRecordRepository extends JpaRepository<ViolationRecord, Integer>{

}
