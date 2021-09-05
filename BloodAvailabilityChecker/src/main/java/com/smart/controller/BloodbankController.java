package com.smart.controller;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.BLoodStockRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.BloodStock;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class BloodbankController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BLoodStockRepository bLoodStockRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	//method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String username=principal.getName();
		System.out.println("Username :: "+username);

		User user=userRepository.getUserByUserName(username);
		System.out.println("User :: "+user);

		model.addAttribute(user);

		System.out.println("Username: 'User' is default in spring boot..");
	}

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		String name=principal.getName();
		User user=userRepository.getUserByUserName(name);
		System.out.println("Role:: "+user.getRole());
		model.addAttribute("title","User Dashboard");
		return "bloodbank/user_dashboard";
	}

	//open add bloodstock form handler
	@GetMapping("/add-bloodstock")
	public String openAddBloodStockForm(Model model, Principal principal,HttpSession session) {
		BloodStock checkBloodStock=null;
		String name=principal.getName();
		User user=userRepository.getUserByUserName(name);

		checkBloodStock=this.bLoodStockRepository.findBloodStockByUser(user.getId());
		if(checkBloodStock==null) {
			model.addAttribute("title","Add bloodstock");
			model.addAttribute("bloodStockdetails",new BloodStock());
			return "bloodbank/add_bloodstock_form";
		}
		else {
			session.setAttribute("message", new Message("Blood Stock Details Already Exist!! Please Update ","danger"));
			return "redirect:/user/show_bloodstock/0";
		}

	}

	//processing add bloodstock
	@PostMapping("/process-bloodstock")
	public String processBloodStock(@ModelAttribute BloodStock bloodStock,Model model, Principal principal,HttpSession session) {

		try {
			String name=principal.getName();
			User user=userRepository.getUserByUserName(name);

			bloodStock.setUser(user);

			//this.userRepository.save(user);
			bLoodStockRepository.save(bloodStock);
			System.out.println("User :: "+user);
			System.out.println("Data ::"+bloodStock);
			System.out.println("User Details Added Successfully..");
			model.addAttribute("title","View bloodstock");
			session.setAttribute("message", new Message("Your BloodStock is added Successfully!","success"));
			return "redirect:/user/show_bloodstock/0";

		} catch (Exception e) {
			System.out.println("ERROR :: "+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong!! try again..","danger"));
			return "bloodbank/add_bloodstock_form";
		}
	}

	//View BloodStock handler
	//per page =3[n]
	//current page = 0[page]
	@GetMapping("/show_bloodstock/{page}")
	public String viewBloodStockPage(@PathVariable ("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title","View Blood Stock");

		String name=principal.getName();
		User user=userRepository.getUserByUserName(name);
		Pageable pageable=PageRequest .of(page, 3);

		Page<BloodStock> bloodStockDetails=this.bLoodStockRepository.findBloodStockByUser(user.getId(),pageable);

		model.addAttribute("bloodStockDetails",bloodStockDetails);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",bloodStockDetails.getTotalPages());
		return "bloodbank/show_bloodStock";
	}

	//Showing particular BloodStock details
	@RequestMapping("/{bloodstockid}/bloodstock")
	public String showBloodStockDetail(@PathVariable ("bloodstockid") Integer bloodstockid, Model model, Principal principal) {

		String name=principal.getName();
		User user=userRepository.getUserByUserName(name);

		Pageable pageable=PageRequest .of(0, 3);

		Page<BloodStock> bloodStockDetails=this.bLoodStockRepository.findBloodStockByUser(user.getId(),pageable);

		model.addAttribute("bloodStockDetails",bloodStockDetails);
		model.addAttribute("currentPage",0);
		model.addAttribute("totalPages",bloodStockDetails.getTotalPages());
		return "bloodbank/show_bloodStock";
	}

	@GetMapping("/delete/{bloodstockid}")
	public String deleteBloodStock(@PathVariable ("bloodstockid") Integer bloodstockid, Model model, Principal principal, HttpSession session) {

		String name=principal.getName();
		User user=userRepository.getUserByUserName(name);

		Optional<BloodStock> bloodStockOptional = this.bLoodStockRepository.findById(bloodstockid);
		BloodStock bloodStockDetails = bloodStockOptional.get();

		if(bloodStockDetails!=null && user.getId()==bloodStockDetails.getUser().getId()) {
			this.bLoodStockRepository.deleteById(bloodstockid);
			System.out.println("Deleted..");
			session.setAttribute("message", new Message("BloodStock deleted successfully..!","danger"));
		}
		return "redirect:/user/show_bloodstock/0";
	}

	//open update form handler
	@PostMapping("/update-bloodstock/{bloodstockid}")
	public String openUpdateForm(@PathVariable ("bloodstockid") Integer bloodstockid,Model model) {

		System.out.println("Inside openUpdateForm()..");
		model.addAttribute("title","Update Blood Stock Details");

		BloodStock bloodStockDetails= this.bLoodStockRepository.findById(bloodstockid).get();
		model.addAttribute("bloodStockDetails",bloodStockDetails);
		return "bloodbank/update_form";

	}

	//update BloodStock handler
	@PostMapping(value="/process-update")
	public String updateHandler(@ModelAttribute BloodStock bloodStockDetails, Principal principal,Model model,HttpSession session) {

		try {
			//image
			System.out.println("bloodStock::"+bloodStockDetails);

			String name=principal.getName();
			User user=userRepository.getUserByUserName(name);

			bloodStockDetails.setUser(user);
			this.bLoodStockRepository.save(bloodStockDetails);
			session.setAttribute("message", new Message("Blood Stock Details is updated","success"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/user/"+bloodStockDetails.getBloodstockid()+"/bloodstock";
	}

	//Your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model) {

		model.addAttribute("title","Profile Page");
		return "bloodbank/profile";
	}

	//Open Setting Handler
	@GetMapping("/settings")
	public String openSettings() {
		return "bloodbank/settings";
	}

	//change password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Model model, Principal principal,HttpSession session) {

		System.out.println("oldPassword ::"+oldPassword+" , newPassword ::"+newPassword);

		String name=principal.getName();
		User user=userRepository.getUserByUserName(name);
		String currentPassword=user.getPassword();
		System.out.println("currentPassword :: "+currentPassword);

		if(this.passwordEncoder.matches(oldPassword, currentPassword)){
			if(newPassword!=null && newPassword!="") {
			//change password
			user.setPassword(this.passwordEncoder.encode(newPassword));
			this.userRepository.save(user);
			session.setAttribute("message",new Message("Your password is successfully changed..","success"));
			}else {
				session.setAttribute("message", new Message("New password field should not be blank!!", "danger"));
				return "redirect:/user/settings";
			}
		}
		else {
			//return with error message
			session.setAttribute("message",new Message("Please enter correct old password!!","danger"));
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
	}
}
