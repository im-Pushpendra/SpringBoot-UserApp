package com.bridgelabz.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@Entity
public class UserModel {
	private boolean isLogin=false;
	private boolean isVerify=false;

	private String name;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String contact;
	private String password;
	private String email;
	private String role;
	private String otp;
	private LocalDateTime otpExpTime;
}
