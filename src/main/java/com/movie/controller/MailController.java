package com.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.movie.service.MailSendService;


@Controller
@RequestMapping("/member/*")
public class MailController {

	@Autowired
	private MailSendService mailService;

	//이메일 인증
	@ResponseBody
	@GetMapping("/mailCheck")
	public String mailCheck(String email) {
		System.out.println("이메일 인증 요청이 들어옴!");
		System.out.println("이메일 인증 이메일 : " + email);
		// 이메일값 jsp에서 전달 받아서 mailService의 매개변수로 전송
		return mailService.joinEmail(email);
	}












}