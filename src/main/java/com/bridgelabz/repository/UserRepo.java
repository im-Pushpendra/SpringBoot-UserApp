package com.bridgelabz.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.entities.UserModel;
import com.bridgelabz.loginDto.RegisterDto;

@Repository
public interface UserRepo extends JpaRepository<UserModel,Integer>{

	Optional<UserModel> findByEmail(String email);
	Optional<UserModel> findByEmailAndPassword(String email, String password);
	List<UserModel> findAllById(int id);
	UserModel findByRole(String role);
	void save(RegisterDto dto);

}
