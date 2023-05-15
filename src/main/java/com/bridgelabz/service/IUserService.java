package com.bridgelabz.service;

import java.util.List;

import com.bridgelabz.entities.UserModel;
import com.bridgelabz.loginDto.LoginDto;
import com.bridgelabz.loginDto.RegisterDto;

public interface IUserService {

	RegisterDto addUser(RegisterDto dto);

	UserModel fetchuser(int id);

	RegisterDto fetchuser1(int id);
	
	UserModel deleteUser(int id);

	UserModel updateUser(int id, RegisterDto dto);
	
	String login(LoginDto lto);

	String loginToken(LoginDto lto);

	LoginDto decode(String token);

	UserModel updateUserWithToken(String token, RegisterDto dto);

	String logout(LoginDto loginObject);

	List<UserModel> admin(String token);

	String verifyWithOTP(String otp, String mail);
}
