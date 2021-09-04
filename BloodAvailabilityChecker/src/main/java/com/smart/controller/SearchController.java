package com.smart.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.BLoodStockRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.User;

@RestController
public class SearchController {

	@Autowired
	private UserRepository userRepository;

	//Search Handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable ("query") String query,Principal principal){

		//User user=this.userRepository.getUserByUserName(principal.getName());

		System.out.println("query ::"+query);
		String role="ROLE_BLOODBANK";
		List<User> users = this.userRepository.findDistinctByLocationContainingAndRole(query,role);

		List<User> filteredList=filterUniqueRecords(users);

		return ResponseEntity.ok(filteredList);
	}


	private List<User> filterUniqueRecords(List<User> list) {

		ArrayList<User> users = null;
		try {
			list.sort((User s1, User s2)->s1.getLocation().compareTo(s2.getLocation())); 

			Map<String, User> map=new HashMap<String, User>();

			for (User user : list) {
				if(!map.containsKey(user.getLocation())) {
					map.put(user.getLocation(), user);
				}
			}

			users = new ArrayList<User>(map.values());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}
}
