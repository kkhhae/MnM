package com.movie.controller;

import java.util.List;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import com.movie.domain.FollowResultVO;
import com.movie.domain.FollowVO;
import com.movie.domain.MemberVO;
import com.movie.service.FollowService;
import com.movie.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/follow/*")
@Slf4j
public class FollowController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private FollowService followService;

	// 팔로우 상세 페이지요청
	@GetMapping("follow")
	public String follow(Model model, HttpSession session) {
		MemberVO myVO = (MemberVO) session.getAttribute("member");

		List<MemberVO> followerList = followService.getFollowerList(myVO);
		List<MemberVO> followingList = followService.getFollowingList(myVO);
		model.addAttribute("followerList", followerList);
		model.addAttribute("followingList", followingList);




		return "follow/follow";
	}

//	// 팔로우 상세에서 유저 닉넴임, mbti 클릭시 유저의 마이페이지로 이동
//	@GetMapping("otherMypage")
//	public String otherMypage(String email, Model model, HttpSession session) {
//	    MemberVO otherMember = memberService.getMember(email);
//	    MemberVO myMember = (MemberVO) session.getAttribute("member");
//
//	    if (otherMember != null) {
//	        model.addAttribute("otherMember", otherMember);
//	        model.addAttribute("followerCount", followService.getFollowerCount(otherMember.getMember_no()));
//	        model.addAttribute("followingCount", followService.getFollowingCount(otherMember.getMember_no()));
//	        model.addAttribute("followStatus", followService.getFollowStatus(myMember.getMember_no(), otherMember.getMember_no()));
//	    }
//
//	    return "member/otherMypage";
//	}
//





	// ajax 요청 : 요청시 전달해준 데이터를 활용해 로직 처리를하고 결과 데이터를 반환 (jsp뷰 리턴x)
	// 로그인 유저 (follower)가 다른 유저(following)를 팔로우하는 기능
	@PostMapping(value="add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<FollowResultVO> followUser(@RequestBody FollowVO followVO) {
		// followVO : member_no_follower (로그인한 나), member_no_following (내가 팔로우하는 사람)

		FollowResultVO resultVO = new FollowResultVO(); // 응답으로 보내줄 결과 데이터 담아줄 객체 생성

		boolean followStatus = followService.followUser(followVO); // 토글방식으로 처리 해줌. 팔로우/언팔 하기
		resultVO.setFollowing(followStatus); // isFollowing 변수에 체우기 (최종 상태가 팔로잉하고있는지 안하고 있는지)

		// 해당 사람의 팔로잉, 팔로우 카운드 조회해 VO에 담기
		// 팔로우 카운트 : 관심있는사람이 팔로잉 하는사람수 : member_no_follower 컬럼의 값이 내가 지금 관심 있는 사람
		resultVO.setFollowerCount(followService.getFollowerCount(followVO.getMember_no_following()));
		// 팔로잉 카운드 : 관심있는사람을 팔로우 하는사람수 : member_no_following 컬럼의 값이 내가 지금 관심있는 사람
		resultVO.setFollowingCount(followService.getFollowingCount(followVO.getMember_no_following()));

		// 데이터만 응답 (ResponseEntity타입객체 안에 FollowResultVO 객체 담아서 리턴)
		return new ResponseEntity<FollowResultVO>(resultVO, HttpStatus.OK);
	}






}
