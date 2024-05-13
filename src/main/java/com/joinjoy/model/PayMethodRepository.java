package com.joinjoy.model;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joinjoy.model.bean.PayMethod;

public interface PayMethodRepository extends JpaRepository<PayMethod, Integer> {

}
