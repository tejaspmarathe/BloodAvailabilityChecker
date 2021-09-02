package com.smart.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/loginrequesthandler")
public class LoginController {

	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/mapping")
	public String dashboard(Model model, Principal principal, HttpSession session) {
	
		String name=principal.getName();
		User user=userRepository.getUserByUserName(name);
		
		if(user.getRole().equalsIgnoreCase("ROLE_ADMIN")) {
			System.out.println("Role ::"+user.getRole());
			model.addAttribute("title","User Dashboard");
			model.addAttribute("user",user);
			return "admin/dashboard";
		} else if(user.getRole().equalsIgnoreCase("ROLE_BLOODBANK")){
			
			if(user.getStatus().equalsIgnoreCase("Active")) {
				System.out.println("Role ::"+user.getRole());
				model.addAttribute("title","BloodBank Dashboard");
				model.addAttribute("user",user);
				return "bloodbank/user_dashboard";
			}else {
				return "redirect:/signin?status="+user.getName()+", Your account status is "+user.getStatus()+". Please Contact System Admin..!";
			}
		}else {
//			System.out.println("Role ::"+user.getRole());
//			model.addAttribute("title","User Dashboard");
//			model.addAttribute("user",user);
//			return "bloodrequestor/dashboard";
			
			if(user.getStatus().equalsIgnoreCase("Active")) {
				System.out.println("Role ::"+user.getRole());
				model.addAttribute("title","BloodRequestor Dashboard");
				model.addAttribute("user",user);
				return "bloodrequestor/dashboard";
			}else {
				return "redirect:/signin?status="+user.getName()+", Your account status is "+user.getStatus()+". Please Contact System Admin..!";
			}
			
			
			
		}
		
	}
}
