package com.bridgelabz.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.entities.UserModel;
import com.bridgelabz.loginDto.LoginDto;
import com.bridgelabz.loginDto.RegisterDto;
import com.bridgelabz.mailcontrol.EmailSenderService;
import com.bridgelabz.repository.UserRepo;
import com.bridgelabz.utility.JWTutility;
import com.bridgelabz.exception.UserExceptions;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	UserRepo repo;
	@Autowired
	ModelMapper modelmapper;
	@Autowired
	JWTutility jwt;
	@Autowired
	EmailSenderService senderService;
	
	Random random=new Random();

	String tokenLambda;

	@Override
	public RegisterDto addUser(RegisterDto dto) {
		Optional<UserModel> email = repo.findByEmail(dto.getEmail());
		if (email.isEmpty()) {
			UserModel user = modelmapper.map(dto, UserModel.class);
			int i = 100000+random.nextInt(899999);
			String otp=Integer.toString(i);
			user.setOtp(otp);
			senderService.sendEmail(dto.getEmail(), "OTP Verification", otp);
//			LocalDateTime currentTime=LocalDateTime.now();
			LocalDateTime newOtpExpTime=LocalDateTime.now().plusMinutes(5);
			user.setOtpExpTime(newOtpExpTime);
			repo.save(user);
			senderService.sendEmail(user.getEmail(), "Registration Success", "Thanks for Registration.");
			return dto;
		} else {			
			throw new UserExceptions("Email & Conatact Already Exists");
		}
	}

	@Override
	public UserModel fetchuser(int id) {
		UserModel R = repo.findById(id).get();
		return R;
	}

	@Override
	public RegisterDto fetchuser1(int id) {
		Optional<UserModel> R = repo.findById(id);
		if (R.isEmpty()) {
			throw new UserExceptions("Id is invalid");
		} else {
			RegisterDto R1 = modelmapper.map(R, RegisterDto.class);
			return R1;
		}
	}

	@Override
	public UserModel deleteUser(int id) {
		Optional<UserModel> R = repo.findById(id);
		if (!R.isPresent()) {
			throw new UserExceptions("Id not Present");
		} else {
			repo.deleteById(id);
			return null;
		}
	}

	@Override
	public UserModel updateUser(int id, RegisterDto dto) {
//		user.setId(id);
		UserModel R = repo.findById(id).get();
		UserModel R1 = modelmapper.map(dto, UserModel.class);
		R1.setId(R.getId());
//		R1.setLogin(R.isLogin());
//		UserModel user1=modelmapper.map(repo.findById(id).get(), UserModel.class);
		if (!R.isLogin()) {
			throw new UserExceptions("User is not Logged IN");
		} else {
			repo.save(R1);
			return R1;
		}
	}

	// via Lambda
	@Override
	public String login(LoginDto lto) {
		repo.findByEmailAndPassword(lto.getEmail(), lto.getPassword()).ifPresent(user -> {
			tokenLambda = jwt.generateToken(lto);
			user.setLogin(true);
			repo.save(user);
		});
		return tokenLambda;
	}

	@Override
	public String loginToken(LoginDto lto) {
		UserModel email = repo.findByEmail(lto.getEmail()).get();
		if (email != null && email.getPassword().equals(lto.getPassword())) {
			String token = jwt.generateToken(lto);
			email.setLogin(true);
			repo.save(email);
			return token;
		} else {
			throw new UserExceptions("Email or Password are Wrong.");
		}
	}

	@Override
	public LoginDto decode(String token) {
		LoginDto jwtToken = jwt.decode(token);
		if (!jwtToken.equals(null)) {
			return jwtToken;
		} else {
			throw new UserExceptions("Email or Password are Wrong.");
		}
	}

	@Override
	public UserModel updateUserWithToken(String token, RegisterDto dto) {
		LoginDto loginObject = jwt.decode(token);
		UserModel um = repo.findByEmailAndPassword(loginObject.getEmail(), loginObject.getPassword()).get();
		if (um == null) {
			throw new UserExceptions("Email or Password are Wrong.");
		}
		UserModel R1 = modelmapper.map(dto, UserModel.class);
		R1.setId(um.getId());
		R1.setLogin(true);
		repo.save(R1);
		senderService.sendEmail("pushpendraa.638@gmail.com", "Updation Successful", "Thanks for Updating.");
		return R1;
	}

	@Override
	public String logout(LoginDto loginObject) {
		UserModel um = repo.findByEmailAndPassword(loginObject.getEmail(), loginObject.getPassword()).get();
		if (um == null) {
			throw new UserExceptions("Email or Password are Wrong.");
		}
		um.setLogin(false);
		repo.save(um);
		return "Logged out";
	}

	@Override
	public List<UserModel> admin(String token) {
		LoginDto loginObject = jwt.decode(token);
		UserModel um = repo.findByEmailAndPassword(loginObject.getEmail(), loginObject.getPassword()).get();
		if (um.getRole().equals("admin")) {
			return repo.findAll();
		} else if (um.getRole().equals("user")) {
			return repo.findAllById(um.getId());
		}else
		throw new UserExceptions("Email or Password are Wrong.");
	}


	@Override
	public String verifyWithOTP(String otp, String mail) {
		UserModel user = repo.findByEmail(mail).get();
		if (user.getOtp().equals(otp)) {
			if(user.getOtpExpTime().isBefore(LocalDateTime.now())) {
			user.setVerify(true);
			user.setOtp(null);
			user.setRole("user");
			repo.save(user);
			senderService.sendEmail(mail, "Verified", "You are Verified Now.");
			}
			return "User Verified";
		} else {
			throw new UserExceptions("OTP in invalid.");
		}
	}
}
