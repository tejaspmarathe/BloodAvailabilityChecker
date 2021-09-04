package com.smart.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smart.entities.BloodStock;

public interface BLoodStockRepository  extends JpaRepository<BloodStock, Integer>{

	@Query("from BloodStock as c where c.user.id=:userId")
	public Page<BloodStock> findBloodStockByUser(int userId, Pageable pageable);

	//search
	/*public List<BloodStock> findByNameContainingAndUser(String keyword, User user);*/

	@Query("from BloodStock as bs where bs.user.id=:userId")
	public BloodStock findBloodStockByUser(int userId);

	@Query("delete BloodStock bs where bs.user.id=:userId")
	public BloodStock deleteBloodStockByUserId(int userId);
}
	