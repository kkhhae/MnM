package com.movie.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.movie.domain.FollowVO;
import com.movie.domain.MemberVO;
import com.movie.domain.ReviewPager;
import com.movie.domain.ReviewVO;
import com.movie.domain.VisitVO;
import com.movie.service.FollowService;
import com.movie.service.MemberService;
import com.movie.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member/*")
@Slf4j
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private FollowService followService;

	@Autowired
	private ReviewService reviewService;

	@Value("${file.dir}") // application.properties 에 저장한 파일 저장 경로 가져오기
	private String uploadPath;


//	  // 회원가입 폼 요청처리
//	   @GetMapping("/signup")
//	   public String signup() {
//	      return "member/signup";
//	   }


//	   // 회원가입 폼에서 post방식으로 넘어본 정보(MemberVO) 담아서 DB에 저장 요청
//	   // 후 main 페이지로 이동
//	   @PostMapping("/signup")
//	   public String signupPro(HttpSession session, MemberVO member) {
//		   // 세션에 MemberVO 객체 저장
//		   session.setAttribute("member", member);
//		   log.info("signup member : {}", member);
//		   return "redirect:/main"; // main 페이지로 리다이렉트
//		}

	// 헤더의 회원가입 폼에서 post방식으로 넘어본 정보(MemberVO) 담아서 DB에 저장 요청
	// 후 main 페이지로 이동
	@PostMapping("signup")
	public String signupPro(MemberVO member, HttpSession session) {
		// 멤버 정보 저장 요청
		memberService.addMember(member);
		// 세션에 MemberVO를 저장 -> signup 에서 받아온 정보에서 이메일 값 뽑아서 getMember 요청해서 한명의 회원정보 가져오기
		MemberVO findMember = memberService.getMember(member.getMember_email());// 수정(마이페이지)
		session.setAttribute("member", findMember); // 수정

		return "member/signupPro";
	}
//
//	   // 로그인 폼 요청
//		@GetMapping("login")
//		public String loginForm() {
//			return "member/login";
//		}

//		 //로그인 처리
//		@PostMapping("login")
//		public String loginPro(MemberVO member, HttpSession session, Model model){
//			// service에서 정보유무 관련된 결과 값(t/f)
//			boolean result = memberService.check(member);
//			// 정보가 있으면 email값을 세션영역에 저장해서 회원전용 main페이지 호출시 사용
//			if(result == true) {
//				 // 로그인 성공한 경우
//		        // 로그인한 회원의 정보를 MemberVO 로 DB에서 가져와
//		        MemberVO loggedInMember = memberService.getMember(member.getMember_email());
//
//		     // 세션에 "member" 속성에 로그인한 회원 정보를 저장
//				session.setAttribute("member", loggedInMember);
//				//session.setAttribute("sid", member.getMember_email());
//			}



	// 로그인 처리
	@PostMapping("login")
	public String loginPro(MemberVO member, HttpSession session,HttpServletRequest request, Model model){
		// service에서 정보유무 관련된 결과 값(VO / null)
		MemberVO loggedInMember = memberService.check(member);
		log.info("loggedInMember: {}", loggedInMember);

		if(loggedInMember != null) { // 이메일-비번 일치
			log.info("loggedInMember: {}", loggedInMember.getMember_status());
			if (loggedInMember.getMember_status().equalsIgnoreCase("ACTIVE")) {
				// 세션에 "member" 속성에 로그인한 회원 정보를 저장
				session.setAttribute("member", loggedInMember);
				//************************************************************************** 기능이 안돼고 있음.... (수정 필요_23.07.21)
				// 이전 페이지 주소를 세션에 저장
				String prevPage = request.getHeader("Referer");
				model.addAttribute("prevPage", prevPage);
				//session.setAttribute("prevPage", referer);
				//**************************************************************************
			}
			model.addAttribute("alertType", loggedInMember.getMember_status());
		}else { // 이메일-비번 불일치
			model.addAttribute("alertType", "INVALID");
		}
		// alertType에 active/invalid값
		return "member/loginPro";
	}



	// 로그 아웃 처리 -> session에 저장된 id값 삭제
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/home";
	}

	// 메일 중복확인
	@PostMapping("mailAvail")
	@ResponseBody // ajax요청에 대한 처리
	public String mailAvail(String email){
		log.info("mailAvail 요청 Id : {}", email);
		// 전달받은 id값이 DB에 존재하는지 여부 검사
		boolean result = memberService.mailAvail(email);
		log.info("mailAvail reault : {}", result);
		String str = "";
		if(result==true) { // --> 'null'==true
			str="true";
		}else if(result == false){
			str="false";
		}
		log.info("mailAvail str : {}", str);

		return str;
	}

//		// 비밀번호 잊을을 때
//		// 비밀번호 변경 폼 요청 처리
//		@GetMapping("pw_modify")
//		public String pwModify() {
//			return "member/pw_modify";
//		}
	// pw_modifyPro 페이지로 이동해서 해당 이메일 받고 전송메일 전송해서
	// 확인메일 받으면 DB에 있는 정보 변경(update)처리

	// 검토 후 맞으면 변경페이지로 이동 아니면 이전페이지 요청
	// 비밀번호 변경 후 로그인 페이지 호출
	@PostMapping("pw_modify")
	public String pw_modifyPro(Model model ,MemberVO member) {
		memberService.updatePw(member);
		return"/member/pwmodifyPro";
	}


	// mypage 요청처리
	@GetMapping("mypage")
	public String mypage(Model model, HttpSession session) {// 세션에서 사용자 정보 가져오기
		MemberVO member = (MemberVO)session.getAttribute("member");
		if (member != null) {
			String nickname = member.getMember_nickname();   String mbti = member.getMember_mbti();
			//모델에 사용자 정보 추가
			model.addAttribute("nickname", nickname);        model.addAttribute("mbti", mbti);
			// 이미지파일 경로 추가
			model.addAttribute("profileImageFileName", member.getMember_image_path());
			model.addAttribute("followerCount", followService.getFollowerCount(member.getMember_no()));// 나를 친추한 사람
			model.addAttribute("followingCount", followService.getFollowingCount(member.getMember_no()));// 내가 친추한 사람
			// 해당 페이지 주인의 활동정보
			Long pageMemberNo = member.getMember_no();
			// 작성 리뷰 개수
			int reviewCount = memberService.writtenReviewCount(pageMemberNo);
			model.addAttribute("reviewCount", reviewCount);
			if(reviewCount > 0) {
				// 리뷰 평균 별점
				model.addAttribute("starAvg", memberService.reviewStarAvg(pageMemberNo));}
			// 찜한 작품 개수
			model.addAttribute("wantCount", memberService.movieWantCount(pageMemberNo));
			// 좋아요 누른 리뷰 개수
			model.addAttribute("likeCount", memberService.reviewLikeCount(pageMemberNo));
			// 로그인한 유저의 리뷰 리스트 가져오기
			List<ReviewVO> loggedInUserReviews = reviewService.getLoggedInUserReviews(member.getMember_no());
			model.addAttribute("loggedInUserReviews", loggedInUserReviews);}
		// 방명록 리스트 가져오기
		List<VisitVO> visitList = memberService.getVisitList(member.getMember_no());
		model.addAttribute("visitList", visitList);
		// mypage.jsp로 이동
		return "member/mypage";}


	// 검색한 유저의 마이페이지로 이동
	@GetMapping("otherMypage")
	public String otherMypage(String email, Model model, HttpSession session) {
		MemberVO otherMember = memberService.getMember(email);
		MemberVO myMember = (MemberVO)session.getAttribute("member");

		log.info("otherMember : {}", otherMember);
		if(otherMember != null) {
			// 로그인 안한 상태에서 다른 사람 mypage 접근을 위함
			if(myMember!=null) {
				model.addAttribute("followStatus", followService.getFollowStatus(myMember.getMember_no(), otherMember.getMember_no()));
			}
			model.addAttribute("otherMember", otherMember);
			// 이미지파일 경로 추가
			model.addAttribute("profileImageFileName", otherMember.getMember_image_path());
			model.addAttribute("followerCount", followService.getFollowerCount(otherMember.getMember_no()));//내가 팔로우
			model.addAttribute("followingCount", followService.getFollowingCount(otherMember.getMember_no()));//남이 날 팔로우

			// 해당페이지 주인의 활동 정보
			Long pageMemberNo = otherMember.getMember_no();
			// 작성 리뷰 개수
			int reviewCount = memberService.writtenReviewCount(pageMemberNo);
			model.addAttribute("reviewCount", reviewCount);
			if(reviewCount > 0) {
				// 리뷰 평균 별점
				model.addAttribute("starAvg", memberService.reviewStarAvg(pageMemberNo));
			}
			// 찜한 작품 개수
			model.addAttribute("wantCount", memberService.movieWantCount(pageMemberNo));
			// 좋아요 누른 리뷰 개수
			model.addAttribute("likeCount", memberService.reviewLikeCount(pageMemberNo));


			// 다른 사용자의 리뷰 리스트 가져오기
			List<ReviewVO> otherUserReviews = reviewService.getOtherUserReviews(otherMember.getMember_no());
			model.addAttribute("otherUserReviews", otherUserReviews);

		}
		// 아더 마이페이지에서 방명록 리스트 가져오기
		List<VisitVO> otherVisitList = memberService.getVisitList(otherMember.getMember_no());
		model.addAttribute("otherVisitList", otherVisitList);

		return "member/otherMypage";
	}

	// MovieController 검색기능에 같이 추가함 23.07.21
//		// 닉네임으로 다른 멤버 조회
//	    @GetMapping("otherpage")
//	    public String showOtherPage(Model model, @RequestParam("nickname") String nickname, HttpSession session) {
//	    	MemberVO myVO = (MemberVO)session.getAttribute("member");
//	        // 닉네임을 기준으로 해당 유저 정보를 가져온다는 가정하에 아래의 코드를 추가합니다.
//	    	// 닉네임 검색어 를 기준으로 부합되는 회원 목록 전부 가져옴
//	        List<MemberVO> members = memberService.getMemberByNickname(nickname);
//	        if (members != null) { // 닉네임에 부합되는 사람들이 하나라도 있으면
//
//	        	// 해당 사람들을 팔로우 했는지 여부 DB가서 가져오기 ( select count(*) from relation where member_no_follower=#{나} and member_no_following=#{목록에서 한명씩}
//	        	List<Integer> followStatus = followService.getFollowingStatus(myVO.getMember_no(), members);
//	        	model.addAttribute("members", members);
//	        	model.addAttribute("followStatus", followStatus);
//	        	// 상대들의 팔로워 수와 팔로잉 수 조회하여 모델에 추가
//	        	List<Integer> followerCounts = new ArrayList<Integer>();
//	        	List<Integer> followingCounts = new ArrayList<Integer>();
//	        	for (MemberVO member : members) {
//	                int followerCount = followService.getFollowerCount(member.getMember_no());
//	                int followingCount = followService.getFollowingCount(member.getMember_no());
//	                followerCounts.add(followerCount);
//	                followingCounts.add(followingCount);
//	            }
//	            model.addAttribute("followerCounts", followerCounts);
//	            model.addAttribute("followingCounts", followingCounts);
//
//
//	        }
//	        return "member/otherpage";
//	    }


//	    // 유저찾기 버튼 누르면 검색페이지로 이동
//	 	@GetMapping("searchUser")
//	 	public String searchUser() {
//	 	    return "member/otherpage";
//	 	}


	// 회원 정보 수정 폼 (로그인 했다라는 전제하에 작업)
	@GetMapping("modify")
	public String modifyForm(HttpSession session, Model model) {
		MemberVO loggedInMember = (MemberVO) session.getAttribute("member");
		if (loggedInMember != null) {
			// 세션에 있는 회원 정보를 수정 페이지로 전달
			model.addAttribute("member", loggedInMember);
			return "member/modify"; // view에서는 전달받은 데이터를 화면에 뿌린다
		}
		// 로그인되어 있지 않은 경우 메인 페이지로 리다이렉트
		return "redirect:/main";
	}


	// 회원 정보 수정 처리
	@PostMapping("modify")
	public String modifyPro(@ModelAttribute("member") MemberVO modifiedMember,
							HttpSession session,
							Model model,
							HttpServletRequest request) {
		// 세션에서 로그인한 회원 정보 가져오기
		MemberVO loggedInMember = (MemberVO) session.getAttribute("member");

		// 입력한 비밀번호와 비밀번호 확인 값이 일치하는지 확인
		String passwordConfirmation = request.getParameter("member_pwCh");

		if (modifiedMember.getMember_pw().equals(passwordConfirmation)) {
			// 입력한 비밀번호와 일치하는 경우 회원 정보 수정
			// 세션에 저장된 회원 정보의 비밀번호와 입력한 비밀번호가 일치하는지 확인
			if (loggedInMember != null && loggedInMember.getMember_pw().equals(modifiedMember.getMember_pw())) {
				// 회원 정보 수정에 필요한 필드들을 modifiedMember 객체에서 가져와서 업데이트 처리
				loggedInMember.setMember_nickname(modifiedMember.getMember_nickname());
				loggedInMember.setMember_mbti(modifiedMember.getMember_mbti());
				// 회원 정보 업데이트 처리
				memberService.updateMember(loggedInMember);
				// 수정 성공 여부를 모델에 추가
				model.addAttribute("check", true);
			} else {
				// 입력한 비밀번호와 일치하지 않는 경우 수정 실패로 처리
				model.addAttribute("check", false);
			}
		} else {
			// 비밀번호와 비밀번호 확인 값이 일치하지 않는 경우 수정 실패로 처리
			model.addAttribute("check", false);
		}

		return "/member/modifyPro";
	}

	// 회원 탈퇴 폼으로 이동
	@GetMapping("delete")
	public String deleteForm(Model model, HttpSession session) {
		// 세션에서 사용자 정보 가져오기
		MemberVO member = (MemberVO)session.getAttribute("member");
		log.info("deletemember : {}", member);
		if (member != null) {
			String nickname = member.getMember_nickname();
			String mbti = member.getMember_mbti();
			String email = member.getMember_email();
			LocalDateTime reg = member.getMember_reg();
			//모델에 사용자 정보 추가
			model.addAttribute("nickname", nickname);
			model.addAttribute("mbti", mbti);
			model.addAttribute("reg", reg);
			model.addAttribute("email", email);
		}
		// delete.jsp로 이동
		return "member/delete";
	}

	// 회원 탈퇴 처리(회원 상태를 탈퇴상태로 바꿈 : DELETE)
	@PostMapping("delete")
	public String deletePro(MemberVO member, HttpSession session, Model model) {
		// 세션에서 로그인한 회원 정보 가져오기
		MemberVO loggedInMember = (MemberVO)session.getAttribute("member");
		log.info("loggedInMember : {}", loggedInMember);
		log.info("Input Password: {}", member.getMember_pw());
		// 세션에 저장된 회원 정보의 비밀번호와 입력한 비밀번호가 일치하는지 확인
		if (loggedInMember != null && loggedInMember.getMember_pw().equals(member.getMember_pw())) {
			// 입력한 비밀번호와 일치하는 경우 회원 상태 delete로 변경
			loggedInMember.setMember_status("DELETE");
			boolean deletionSuccess = memberService.deleteMember(loggedInMember); // 회원 정보 삭제 메소드 호출

			if (deletionSuccess) {
				// 회원 삭제 성공
				model.addAttribute("check", true);
				session.invalidate(); // 세션 속성 다 삭제
			} else {
				// 회원 삭제 실패
				model.addAttribute("check", false);
			}
		} else {
			// 비밀번호가 일치하지 않는 경우
			model.addAttribute("check", false);
		}

		return "member/deletePro";
	}

	// 방명록 글작성 처리
	@PostMapping("write") // 폼 주소
	public String saveVisit(VisitVO visit, HttpSession session, Model model,
							@RequestParam("otherMemberNo") long otherMemberNo,
							@RequestParam("otherMemberEmail") String otherMemberEmail) {//마이페이지 주인 고유번호, 이메일값 jsp로 받아옴
		log.info("otherMemberNo: {}", otherMemberNo); //

		// 세션에서 사용자 정보 가져오기
		MemberVO myMember = (MemberVO)session.getAttribute("member");

		// 마이페이지 주인 정보 가져오기 !!!!!!!!!!!!!!!!!!!!!!!!!!!
		//MemberVO otherMember = (MemberVO) session.getAttribute("otherMember");
		//log.info("myMember : {}" , myMember);
		//log.info("otherMember : {}" , otherMember);
		// visit 객체에 리시버, 샌더 정보 설정
		visit.setReceiver(otherMemberNo); // 방명록 주인 번호 jsp에서 가져옴
		visit.setSender(myMember.getMember_no()); // 글쓴이 고유번호
		// 내용 전달
		String content = visit.getContent(); // JSP에서 전달된 "content" 값
		visit.setContent(content); // VisitVO 객체에 설정
		// 비지니스 로직처리 호출
		memberService.saveVisit(visit); // 가져온 VO 데이터 담아서 전달

		return "redirect:/member/otherMypage?email=" + otherMemberEmail;
	}



	// 방명록 작성글 수정처리
	@PostMapping("/updateVisit")
	public String updateVisit(Long visit_no, String content, String email) {
		log.info("visit_no : {}", visit_no);
		log.info("content : {}", content);

		memberService.updateVisitContent(visit_no, content);
		// 수정 성공 시, 해당 마이페이지로 리다이렉트
		return "redirect:/member/otherMypage?email=" + email;
	}


	// 방명록 작성글 삭제처리
	@PostMapping("/deleteVisit")
	public ResponseEntity<String> deleteVisit(Long visit_no) {
		// 여기에서 visit_no를 이용하여 해당 방명록 글을 삭제하는 서비스 메서드 호출
		memberService.deleteVisit(visit_no);

		// 삭제가 성공적으로 이루어지면 200 OK 상태 코드를 반환
		return ResponseEntity.ok().build();
	}





	// 방명록 답글작성기능
	@PostMapping("reply")
	@ResponseBody // 비동기 요청에 대한 응답을 JSON 형태로 반환
	public void saveVisitReply(VisitVO visit,
							   HttpSession session) {

		log.info("visit vo : {}" , visit); //  방명록 고유번호랑 답글 내용이 담김

		//MemberVO loggedInMember = (MemberVO) session.getAttribute("member");
		//if (loggedInMember != null) {
		// 방명록 답글 저장을 위해 visit 객체에 리시버, 샌더 정보 설정
		//visit.setReceiver(otherMemberNo); // 내 방명록에글쓴사람의 고유번호
		//visit.setSender(loggedInMember.getMember_no()); // 방명록에 답글쓴주인

		// 방명록 답글 저장을 위한 비즈니스 로직 처리
		memberService.saveVisitReply(visit);
	}



	// 방명록 답글 삭제 기능
	@PostMapping("deleteReply")
	@ResponseBody
	public void deleteVisitReply(@RequestParam("visit_no") Long visit_no) {
		// visit_no(방명록의 고유번호)를 사용하여 답글 삭제를 수행하는 비즈니스 로직 호출
		memberService.deleteVisitReply(visit_no);
	}



	// 파일 업로드 창으로 이동
	@GetMapping("/uploadForm")
	public String showUploadForm() {
		return "member/uploadForm";
	}



	// html 화면에 이미지 띄워주기 위한 경로 요청
	@ResponseBody
	@GetMapping("/images/{fileName}")
	public Resource getImages(@PathVariable String fileName) throws MalformedURLException {
		return new UrlResource("file:" + uploadPath+fileName);
	}


	// 프로필 이미지 수정 창으로 이동
	@GetMapping("/updateImg")
	public String updateImgForm(HttpSession session, Model model) {
		// 세션에서 사용자 정보 가져오기
		MemberVO member = (MemberVO)session.getAttribute("member");
		log.info("member : {}", member);
		if (member != null) {
			// 이미지파일 경로 추가
			model.addAttribute("profileImageFileName", member.getMember_image_path());
		}
		return "member/updateForm";
	}


	// 파일 업로드 요청 처리
	@PostMapping("/upload")
	public String handleFileUpload(MultipartFile imageFile,
								   HttpSession session,
								   HttpServletRequest request) {
		// 업로드할 경로 설정 (프로필 이미지를 저장할 경로)
		//String uploadPath = request.getRealPath("/resources/imgs");
		//log.info("uploadPath : {}", uploadPath);
		//String uploadPath = "F:\\ljm\\sprworkspace\\workspace\\movie\\src\\main\\webapp\\resources\\imgs\\";

		// 세션에서 MemberVO 객체 가져오기
		MemberVO member = (MemberVO) session.getAttribute("member");
		log.info("member : {}", member);
		// 파일 업로드 처리
		if (!imageFile.isEmpty()) {
			try {
				// UUID로 고유한 파일 이름 생성
				String uuid = UUID.randomUUID().toString();
				String extension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
				String fileName = uuid + "_" + member.getMember_no() + extension;

				// 업로드할 파일 생성
				File destFile = new File(uploadPath + fileName);

				// 파일 업로드
				imageFile.transferTo(destFile);

				// 데이터베이스에 이미지 저장
				if (member != null) {
					long member_no = member.getMember_no();
					memberService.updateMemberImagePath(member_no, fileName);
				}
				// 업로드된 이미지 이름를 세션에 저장
				member.setMember_image_path(fileName);
				session.setAttribute("member", member); // 세션에 저장된 MemberVO 이미지 체운것으로 업데이트
			} catch (IOException e) {
				e.printStackTrace();
				// 파일 업로드 실패 처리
			}
		}
		// 파일 업로드 후 마이페이지로 이동
		return "redirect:/member/mypage";
	}


	// 프로필 이미지 수정
	@PostMapping("updateImg")
	public String updateImgPro(MultipartFile imageFile,
							   HttpSession session,
							   HttpServletRequest request) {

		MemberVO member = (MemberVO)session.getAttribute("member");
		// 기존 이미지 파일명 가져오기
		String oldFileName = member.getMember_image_path();
		// 기존 실제 파일 삭제
		String path = "F:/ljm/springboot/upload/"; // 파일 저장 폴더 경로 찾기
		log.info("path : {}", path);
		File f = new File(path, oldFileName); // (저장폴더명, 파일명)알아서 경로 연결해줌
		f.delete(); // 이전 이미지 실제 파일 삭제

		// 파일 업로드 처리
		if (!imageFile.isEmpty()) {
			try {
				// UUID로 고유한 파일 이름 생성
				String uuid = UUID.randomUUID().toString();
				String extension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
				String fileName = uuid + "_" + member.getMember_no() + extension;

				// 업로드할 파일 생성
				File destFile = new File(uploadPath + fileName);

				// 파일 업로드
				imageFile.transferTo(destFile);

				// 데이터베이스에 이미지 저장
				if (member != null) {
					long member_no = member.getMember_no();
					memberService.updateMemberImagePath(member_no, fileName);
				}
				// 업로드된 이미지 이름를 세션에 저장
				member.setMember_image_path(fileName);
				session.setAttribute("member", member); // 세션에 저장된 MemberVO 이미지 체운것으로 업데이트
			} catch (IOException e) {
				e.printStackTrace();
				// 파일 업로드 실패 처리
			}
		}// if

		return "redirect:/member/mypage";
	}

	@GetMapping("/mbti-count")
	@ResponseBody // json 형태로 반환
	public Map<String, Integer> getMbtiCounts() {

		Map<String, Integer> mbtiCounts = memberService.getMbtiCounts();

		log.info("MBTI Counts: {}", mbtiCounts);

		return memberService.getMbtiCounts();
	}







}
