package com.empconn.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.empconn.persistence.entities.Vertical;
import com.empconn.repositories.VerticalRepository;

@Service
public class VerticalService {

	@Autowired
	private VerticalRepository verticalRepository;
	
	public ResponseEntity<Object> createVertical() {
		Vertical vertical = new Vertical();
		vertical.setCreatedBy(1l);
		vertical.setCreatedOn(new Timestamp(new Date().getTime()));
		vertical.setIsActive(Boolean.TRUE);
		vertical.setName("test");
		vertical.setVerticalId(1);
		
		Vertical savedVertical = verticalRepository.save(vertical);
		
		if(verticalRepository.findById(savedVertical.getVerticalId()).isPresent())
			return ResponseEntity.ok("User Created Successfully");
		else 
			return ResponseEntity.unprocessableEntity().body("Failed Creating User as Specified");
	}
	
}
