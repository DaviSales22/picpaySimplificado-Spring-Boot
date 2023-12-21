package com.picpaysimplificado.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.services.UserService;

@RestController()
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userservice;
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody UserDTO user){
		User newUser = userservice.createUser(user);
		return new ResponseEntity<>(newUser, HttpStatus.CREATED); 
	}
	
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> users = this.userservice.getAllUsers();
		return new ResponseEntity<>(users,HttpStatus.OK);
	}
}
