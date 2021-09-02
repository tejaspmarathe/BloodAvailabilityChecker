package com.smart.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.BLoodStockRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/bloodrequestor")
public class BloodRequestorController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BLoodStockRepository bLoodStockRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String username = principal.getName();
		System.out.println("Username :: " + username);

		User user = userRepository.getUserByUserName(username);
		System.out.println("User :: " + user);

		model.addAttribute(user);

		System.out.println("Username: 'User' is default in spring boot..");
	}
	
	
	// direct to bloodrequestor indexx
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		System.out.println("Role:: " + user.getRole());
		// model.addAttribute("user",user);
		model.addAttribute("title", "Bloodrequestor Dashboard");
		return "bloodrequestor/dashboard";
	}
	
	
	
	@GetMapping("/check_blood_availability")
	public String checkBloodAvailability() {
		return "bloodrequestor/check_blood_availability";	
	}
	
	
	
	
	
	
	
	
	
	
	// profile handler
		@GetMapping("/profile")
		public String yourProfile(Model model) {

			model.addAttribute("title", "Profile Page");
			return "bloodrequestor/profile";
		}

		// Open Setting Handler
		@GetMapping("/settings")
		public String openSettings() {
			return "bloodrequestor/settings";
		}

		// change password handler
		@PostMapping("/change-password")
		public String changePassword(@RequestParam("oldPassword") String oldPassword,
				@RequestParam("newPassword") String newPassword, Model model, Principal principal, HttpSession session) {

			System.out.println("oldPassword ::" + oldPassword + " , newPassword ::" + newPassword);

			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);
			String currentPassword = user.getPassword();
			System.out.println("currentPassword :: " + currentPassword);

			if (this.passwordEncoder.matches(oldPassword, currentPassword)) {
				// change password
				user.setPassword(this.passwordEncoder.encode(newPassword));
				this.userRepository.save(user);
				session.setAttribute("message", new Message("Your password is successfully changed..", "success"));
			} else {
				// return with error message
				session.setAttribute("message", new Message("Please enter correct old password!!", "danger"));
				return "redirect:/bloodrequestor/settings";
			}

			return "redirect:/bloodrequestor/index";
		}
}
