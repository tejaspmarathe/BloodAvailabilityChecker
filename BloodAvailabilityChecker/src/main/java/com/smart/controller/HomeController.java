package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	/*@RequestMapping("/test")
	@ResponseBody
	public String test() {
		
		User user = new User();
		user.setName("Rohit Sharma");
		user.setEmail("rohit@gmail.com");
		
		userRepository.save(user);
		return "Working..";
	}*/
	
	@RequestMapping("/")
	public String index(Model m) {
		m.addAttribute("title","Home-Blood Availability Checker");
		return "home";
	}
	
	@RequestMapping("/home")
	public String home(Model m) {
		m.addAttribute("title","Home-Blood Availability Checker");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model m) {
		m.addAttribute("title","About-Blood Availability Checker");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model m) {
		
		m.addAttribute("title","Register-Blood Availability Checker");
		m.addAttribute("user", new User());
		return "signup";
	}
	
	//handler for registering the user
	@RequestMapping(value = "/do_register",method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult bindingResult,
			@RequestParam(value = "agreement",defaultValue = "false")boolean agreement,
			HttpSession httpSession,Model model) {
		
		try {
			
			if(bindingResult.hasErrors()) {
				System.out.println("ERROR"+bindingResult.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			//user.setRole("ROLE_USER");
			user.setStatus("Active");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			if(user.getRole().equalsIgnoreCase("ROLE_BLOODBANK")) {
				user.setStatus("Pending");
			}
			
			System.out.println("User :: "+user);
			
			User result=this.userRepository.save(user);
			
			httpSession.setAttribute("message", new Message("Successfully registered !!","alert-success"));
			model.addAttribute("user",new User());
			return "signup";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			httpSession.setAttribute("message", new Message("Something went wrong !!"+e.getMessage(),"alert-danger"));
			return "signup";
		}
	}
	
	//handler for customer login
	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title","Login Page");
		return "login";
		
	}
}
