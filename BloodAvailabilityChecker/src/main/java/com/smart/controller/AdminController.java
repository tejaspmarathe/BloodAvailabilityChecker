package com.smart.controller;

import java.security.Principal;

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
@RequestMapping("/admin")
public class AdminController {

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

	// direct to admin indexx
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		System.out.println("Role:: " + user.getRole());
		// model.addAttribute("user",user);
		model.addAttribute("title", "Admin Dashboard");
		return "admin/dashboard";
	}

	// show all bloodbank to admin
	@GetMapping("/show_bloodbank/{page}")
	public String viewBloodBankPage(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "View Users");
		Pageable pageable = PageRequest.of(page, 3);

		String role = "ROLE_BLOODBANK";
		Page<User> bloodbank = this.userRepository.findUserByRole(role, pageable);

		model.addAttribute("bloodbank", bloodbank);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", bloodbank.getTotalPages());
		return "admin/show_bloodbank";
	}

	// show all bloodrequestork to admin
	@GetMapping("/show_bloodrequestor/{page}")
	public String viewBloodRequestorPage(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "View Blood Requestor");
		Pageable pageable = PageRequest.of(page, 3);

		String role = "ROLE_USER";
		Page<User> users = this.userRepository.findUserByRole(role, pageable);

		model.addAttribute("users", users);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", users.getTotalPages());
		return "admin/show_bloodrequestor";
	}

	// update bloodbank status to active
	@PostMapping("/update-status-approve/{uid}")
	public String updateStatusApprove(@PathVariable("uid") Integer uid, Model model, Principal principal,
			HttpSession session) {

		System.out.println("Inside updateStatus()");
		User user = this.userRepository.findById(uid).get();
		user.setStatus("Active");
		this.userRepository.save(user);
		System.out.println("User status updated" + user.getStatus());
		session.setAttribute("message", new Message(user.getName() + " Activated successfully..!", "success"));
		return "redirect:/admin/show_bloodbank/0";
	}

	// update bloodbank status to block
	@PostMapping("update-status-block/{uid}")
	public String updateStatusReject(@PathVariable("uid") Integer uid, Model model, Principal principal,
			HttpSession session) {

		System.out.println("Inside updateStatus()");
		User user = this.userRepository.findById(uid).get();
		user.setStatus("Blocked");
		this.userRepository.save(user);
		System.out.println("User status updated" + user.getStatus());
		session.setAttribute("message", new Message(user.getName() + " Blocked successfully..!", "danger"));
		return "redirect:/admin/show_bloodbank/0";
	}

	// delete bloodbank
	@GetMapping("/delete-bloodbank/{uid}")
	public String deleteBloodBank(@PathVariable("uid") Integer uid, Model model, Principal principal,
			HttpSession session) {

		User user1 = this.userRepository.findById(uid).get();

		// String name=principal.getName();
		// User user=userRepository.getUserByUserName(name);
		BloodStock bloodStockDetails = this.bLoodStockRepository.findBloodStockByUser(uid);
		if (bloodStockDetails != null) {
			this.bLoodStockRepository.deleteById(bloodStockDetails.getBloodstockid());
		}

		this.userRepository.deleteById(uid);

		System.out.println("Deleted..");
		session.setAttribute("message", new Message(user1.getName() + " deleted successfully..!", "danger"));
		return "redirect:/admin/show_bloodbank/0";
	}

	// update blood requester status to active
	@PostMapping("/update-status-bloodrequestor-active/{uid}")
	public String updateBloodRequestorStatusActive(@PathVariable("uid") Integer uid, Model model, Principal principal,
			HttpSession session) {

		System.out.println("Inside updateStatus()");
		User user = this.userRepository.findById(uid).get();
		user.setStatus("Active");
		this.userRepository.save(user);
		System.out.println("User status updated" + user.getStatus());
		session.setAttribute("message", new Message(user.getName() + " Activated successfully..!", "success"));
		return "redirect:/admin/show_bloodrequestor/0";
	}

	// update blood requester status to block
	@PostMapping("/update-status-bloodrequestor-block/{uid}")
	public String updateBloodRequestorStatusBlock(@PathVariable("uid") Integer uid, Model model, Principal principal,
			HttpSession session) {

		System.out.println("Inside updateStatus()");
		User user = this.userRepository.findById(uid).get();
		user.setStatus("Blocked");
		this.userRepository.save(user);
		System.out.println("User status updated" + user.getStatus());
		session.setAttribute("message", new Message(user.getName() + " Blocked successfully..!", "danger"));
		return "redirect:/admin/show_bloodrequestor/0";
	}

	// delete blood requester
	@GetMapping("/delete-bloodrequestor/{uid}")
	public String deleteBloodRequestor(@PathVariable("uid") Integer uid, Model model, Principal principal,
			HttpSession session) {
		User user1 = this.userRepository.findById(uid).get();

		this.userRepository.deleteById(uid);

		System.out.println("Deleted..");
		session.setAttribute("message", new Message(user1.getName() + " deleted successfully..!", "danger"));
		return "redirect:/admin/show_bloodrequestor/0";
	}

	// admin profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model) {

		model.addAttribute("title", "Profile Page");
		return "admin/profile";
	}

	// Open Setting Handler
	@GetMapping("/settings")
	public String openSettings() {
		return "admin/settings";
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
			if (newPassword != null && newPassword != "") {
				// change password
				user.setPassword(this.passwordEncoder.encode(newPassword));
				this.userRepository.save(user);
				session.setAttribute("message", new Message("Your password is successfully changed..", "success"));
			} else {
				session.setAttribute("message", new Message("New password field should not be blank!!", "danger"));
				return "redirect:/admin/settings";
			}
		} else {
			// return with error message
			session.setAttribute("message", new Message("Please enter correct old password!!", "danger"));
			return "redirect:/admin/settings";
		}

		return "redirect:/admin/index";
	}
}
