package com.shoppingmall.qna;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shoppingmall.item.ItemService;
import com.shoppingmall.users.UserService;
import com.shoppingmall.users.Users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class QnaController {

	@Autowired
	private QnaService qnaService;
	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;

	@GetMapping("/check-login-status")
	public ResponseEntity<String> checkLoginStatus(Model model, HttpServletRequest request, Long itemIdValue) {
		HttpSession session = request.getSession(false);

		if (session != null && session.getAttribute("loggedInUser") != null) {
			
			Users loggedInUser = (Users) session.getAttribute("loggedInUser");			
			session.setAttribute("itemid", itemIdValue);		
			Long usersId = loggedInUser.getUsersid();

			model.addAttribute("usersid", usersId);
			System.out.println("usersId:" + usersId);
			return ResponseEntity.ok().body("로그인됨");
		} else {
		
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인되지 않음");
		}
	}

	@PostMapping("/popup")
	public String showPopup(@RequestParam("itemid") Long itemId, Model model, HttpServletRequest request) {	
		Long itemIdValue = Long.parseLong(request.getParameter("itemid"));
		ResponseEntity<String> loginStatusResponse = checkLoginStatus(model, request, itemIdValue);
		if (loginStatusResponse.getStatusCode() == HttpStatus.OK) {
			model.addAttribute("itemIdValue", itemIdValue);
			System.out.println("itemId: " + itemIdValue);
			return "item/popup"; 
		} else {
			return "redirect:/login"; 
		}
	}

}
