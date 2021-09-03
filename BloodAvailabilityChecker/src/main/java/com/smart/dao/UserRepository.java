package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	@Query("select u from User u where u.email=:email")
	public User getUserByUserName(@Param("email") String email);

	@Query("from User as u where u.role=:role")
	public Page<User> findUserByRole(String role, Pageable pageable);
	
//	@Query("from User as u where u.location=:location")
//	public Page<User> getUserByLocation(String location, Pageable pageable);

	@Query("from User as u where u.location=:location")
	public List<User> getUserByLocation(String location);
	
//	@Query("select u from User u where u.location=:location")
//	public List<User> getUserByLocation(String location);
	
	//search
	public List<User> findDistinctByLocationContainingAndRole(String location, String role);

	@Query("select u from User u where u.role=:role")
	public List<User> findbyRole(String role);
	
}
