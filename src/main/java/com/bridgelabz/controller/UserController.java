package com.bridgelabz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.Response;
import com.bridgelabz.entities.UserModel;
import com.bridgelabz.loginDto.LoginDto;
import com.bridgelabz.loginDto.RegisterDto;
import com.bridgelabz.service.IUserService;

//@Controller
//@ResponseBody
//  ||
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	IUserService userService;
	@Autowired
	Response response;

//	@RequestMapping("/hello")
	@GetMapping("/hello")
	public static String retunHello() {
		return "Hello";
	}

	
	@PostMapping("/addUser")
	public ResponseEntity<Response> addUser(@RequestBody RegisterDto dto) {
		RegisterDto user = userService.addUser(dto);
		response.setMsg("Added Successfully");
		response.setObject(user);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	
	@GetMapping("/fetchUser")
	public UserModel fetch(@RequestParam int id) {
		UserModel user = userService.fetchuser(id);
		return user;
	}

	
	@GetMapping("/fetchUser2/{id}")
	public ResponseEntity<Response> fetch1(@PathVariable int id) {
		RegisterDto user = userService.fetchuser1(id);
		response.setMsg("fetched Successfully");
		response.setObject(user);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	
	@DeleteMapping("/deleteUser")
	public ResponseEntity<Response> deleteUser(@RequestParam int id) {
		UserModel user = userService.deleteUser(id);
		response.setMsg("Deleted Successfully");
		response.setObject(user);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	
	@PutMapping("/updateUser/{id}")
	public ResponseEntity<Response> updateUser(@PathVariable int id, @RequestBody RegisterDto dto) {
		UserModel user = userService.updateUser(id, dto);
		if (user != null) {
			response.setMsg("Updated Successfully");
			response.setObject(user);
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		}
		response.setMsg("data Not Found");
		response.setObject(user);
		return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
	}

	
	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody LoginDto lto) {
		String login = userService.login(lto);
		response.setMsg("Login Successfully");
		response.setObject(login);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	
	@PostMapping("/login2")
	public ResponseEntity<Response> loginGenerateToken(@RequestBody LoginDto lto) {
		String login = userService.loginToken(lto);
		response.setMsg("Login Successfully");
		response.setObject(login);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	
	@PostMapping("/decode")
	public ResponseEntity<Response> decode(@RequestParam String token) {
		LoginDto lto = userService.decode(token);
		response.setMsg("Decoded Successfully");
		response.setObject(lto);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	
	@PutMapping("/updateUser2")
	public ResponseEntity<Response> updateUserWithToken(@RequestHeader String token, @RequestBody RegisterDto dto) {
		UserModel user = userService.updateUserWithToken(token, dto);
		response.setMsg("Updated Successfully");
		response.setObject(user);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	
	@PutMapping("/logout")
	public ResponseEntity<Response> logout(@RequestBody LoginDto loginObject) {
		String login = userService.logout(loginObject);
		response.setMsg("Logout Successfully");
		response.setObject(login);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	
	@GetMapping("/admin")
	public ResponseEntity<Response> adminRole(@RequestHeader String token) {
		List<UserModel> login = userService.admin(token);
		response.setMsg("Fetched Successfully");
		response.setObject(login);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	
	@GetMapping("/verifyOTP")
	public ResponseEntity<Response> isVerifyWithOTP(@RequestParam String otp, String mail) {
		String verify = userService.verifyWithOTP(otp, mail);
		response.setMsg("Verified Successfully");
		response.setObject(verify);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}
