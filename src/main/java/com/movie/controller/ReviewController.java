package com.movie.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.movie.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.movie.service.FollowService;
import com.movie.service.MemberService;
import com.movie.service.MovieService;
import com.movie.service.ReplyService;
import com.movie.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/review/*")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
	
	// 생성자 주입받기
	private final ReviewService reviewService;
	private final ReplyService replyService;
	private final MemberService memberService;
	private final FollowService followService;
	private final MovieService movieService;

	// 리뷰 추가 (영화 상세페이지)
	@ResponseBody
	@PostMapping(value="new", consumes = "application/json",
			produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> insertReview(@RequestBody ReviewVO review) {
		log.info("======== review new POST");
		log.info("review : {}", review);
		
		int result = -1;
		// 새글등록
		ReviewVO reviewCheck = reviewService.getMyMoiveReview(review.getMember_no(), review.getMovie_no());
		// 현재 계정, 영화로 리뷰가 없을 경우
		if (reviewCheck == null) {
			result = reviewService.insert(review);
			log.info("new review result : {}", review);
		}
		
		return result == 1 ? 
				new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	// 내 리뷰 요청 (영화 상세페이지) 
	@ResponseBody
	@GetMapping(value="{member_no}/{movie_no}",
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ReviewVO> getMyReview(@PathVariable("member_no") Long member_no,
			@PathVariable("movie_no") Long movie_no) {
		log.info("======== review myreview GET");
		log.info("member_no : {}", member_no);
		log.info("movie_no : {}", movie_no);
		
		return new ResponseEntity<>(reviewService.getMyMoiveReview(member_no, movie_no), 
				HttpStatus.OK);
	}
	
	// 리뷰 수정
	@ResponseBody
	@PutMapping(value="put/{review_no}")
	public ResponseEntity<String> modify(@RequestBody ReviewVO review,
			@PathVariable Long review_no) {
		log.info("======== replies {reply_no} PUT");
		log.info("review_no : {} ", review_no);
		log.info("review : {} ", review);

		int result = 0;
		ReviewVO newReview = review;
		result = reviewService.update(newReview);
		
		return result == 1 ?
				new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
	}
	
	// 리뷰 목록 요청 (영화 상세페이지) 
	@ResponseBody
	@GetMapping(value="list/{movie_no}", 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<ReviewVO>> getList(@PathVariable Long movie_no) {
		log.info("======== review list/{movie_no} GET");
		log.info("movie_no : {}", movie_no);
		
		return new ResponseEntity<>(reviewService.getPopularReviewAsMovie(movie_no), 
				HttpStatus.OK);
	}

    // 빈 객체도 항상 필요
    @ModelAttribute("member")
    public MemberVO loginMember(HttpSession session, Model model) {
        MemberVO member = new MemberVO();
        MemberVO loginMember = (MemberVO)session.getAttribute("member");
        if(loginMember != null) {
            member = loginMember;
        }
        model.addAttribute("member_no", member.getMember_no());
        return member;
    }

	// 리뷰 목록
	@GetMapping("list")
	public String reviewList(MemberVO member, Model model, HttpSession session,
			HttpServletRequest request, @ModelAttribute("pager") ReviewPager pager) {
		log.info("======== list GET : review/list");
		log.info("pager : {}", pager);
		// 리스트로 돌아가기전에 좋아요 관련 리스트 보고 있었다면 다시 보내주기
		if(pager.getListCheck() == 1) {
			likeList(member, model, session, request, pager);
			return null;
		}
		
		// 현재 로그인중인 유저정보 가져오기
        member = (MemberVO)session.getAttribute("member");
        Long member_no = 0L;
        if(member != null) {
            member_no = member.getMember_no();
        }
        log.info("member : {}", member);

		// 리뷰 목록 가져오기 + 페이징 처리
		List<ReviewVO> list = new ArrayList<ReviewVO>();
		int total = 0;
		log.info("pager : {}", pager);
		
		if(pager.getEmail() != null && pager.getEmail().length() > 0) {
		    // 가져온 email 값에 해당하는 유저 리뷰 리스트 (listCheck == 0)
			log.info("email not null");
			
			// 이메일의 해당하는 유저의 고유번호 가져오기
			Long pageMemberNo = memberService.getMember(pager.getEmail()).getMember_no();

			// 작성자의 글 개수 가져오기
			total = reviewService.getReviewCountAsWriter(pageMemberNo);
			if(total < pager.getListSize()) {
				log.info("total < listSize");
				pager.setPageNum(1);
			}
			
			// 페이징 처리 (작성자 것만)
			list = reviewService.getListAsWriter(pager, pageMemberNo);
			
			// 작성자 정보 보내주기
			model.addAttribute("pageMember", memberService.getMemberByNo(pageMemberNo));
			
		} else if(pager.getListCheck() > 0 && pager.getMovie_no() != null && pager.getMovie_no() > 0) {
		    // 가져온 movie_no 값에 해당하는 영화의 리뷰 리스트 (listCheck == 2)
			log.info("movie_no not null");
			Long movie_no = pager.getMovie_no();
			
			// 영화에 해당하는 리뷰 개수 가져오기
			total = reviewService.getReviewCountAsMovie(movie_no);
			if(total < pager.getListSize()) {
				log.info("total < listSize");
				pager.setPageNum(1);
			}
			// 페이징 처리
			// 영화 고유번호 주고 해당 영화의 리뷰들 가져오기
			list = reviewService.getListAsMovie(pager, movie_no);
			
			// 영화 정보 보내주기
			model.addAttribute("movie", movieService.getOneMovie(movie_no));
			
		// email, movie_no 없는 전체 리뷰 리스트 (listCheck == 0)
		} else {
			total = reviewService.getReviewCount(); // 글 개수 가져오기
			// 페이징 처리
			list = reviewService.getList(pager);
        }
		log.info("list : {}", list);
        // 글 목록 뷰에 전달
		model.addAttribute("list", list);
		// 페이징처리요소 뷰에 전달
		model.addAttribute("pageDTO", new ReviewPageDTO(pager, total));

		// 유저 고유번호 주고 어떤 리뷰 좋아요 눌렀는지 체크
		List<ReviewLikeVO> likeList = reviewService.liked(member_no);
		// Map<Long, Integer> (review_no, like_check)
		Map<Long, Integer> lmap = new HashMap<Long, Integer>();
		if(likeList != null) { // 좋아요 누른 리뷰가 있다면
			for(int i = 0; i < likeList.size(); i++) {
				// List에서 리뷰고유번호, 좋아요상태 꺼내기
				lmap.put(likeList.get(i).getReview_no(), likeList.get(i).getLike_check());
            }
			model.addAttribute("lmap", lmap);
            log.info("lmap : {}", lmap);
			// ${lmap.get(review_no)}으로 꺼내서 좋아요 된 리뷰들 가져오기
        }

		// 리뷰에 해당하는 댓글 개수 보내주기 // (리뷰고유번호, 댓글개수) 담기 위한 map 객체 만들어 놓기
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		// 리뷰 목록에 해당하는 만큼 반복
		for(int i = 0; i < list.size(); i++) {
			// 리뷰 한개씩 고유번호 가져와 reviewNo 변수에 담기
			Long review_no = list.get(i).getReview_no();
			// (리뷰고유번호)에 해당하는 댓글 개수 relpyCount 변수에 담기
			int replyCount = replyService.getReplyCount(review_no);
			// (review_no, replyCount) 형식으로 넣기
			map.put(review_no , replyCount); // [1=0] [2=25] [3=25] ...
		}
		model.addAttribute("map", map); // view에서 map.get(해당리뷰고유번호)로 찾기
		return "review/list";
	}
	
	// 리뷰 상세 페이지
	@GetMapping("{review_no}") // 리뷰 고유 번호 받고
	public String reviewContent(@PathVariable Long review_no,
			Model model, ReviewLikeVO likeVO, HttpServletRequest request, MemberVO member,
			@ModelAttribute("pager") ReviewPager pager, @ModelAttribute("reply") ReplyVO reply) {
		log.info("======== rno GET : review/content");
		log.info("review_no : {}", review_no);
		log.info("likeVO : {}", likeVO);
		log.info("pager : {}", pager);
		
		// 리뷰 고유 번호로 리뷰 가져와 보내주기
		ReviewVO review = reviewService.getReview(review_no);
		log.info("review : {}", review);
		model.addAttribute("review", review);
		
		// 리뷰에 해당하는 영화제목 보내주기
		String movieTitle = reviewService.getTitle(review.getMovie_no());
		model.addAttribute("movieTitle", movieTitle);

		// 리뷰 작성자 정보 보내주기
		MemberVO writerInfo = memberService.getMemberByNo(review.getMember_no());
		model.addAttribute("writerInfo", writerInfo);

		// 팔로우 상태
		int followStatus = 0;

		// 로그인 상태 확인
        // 현재 로그인중인 유저정보 가져오기
        MemberVO loginMember = (MemberVO) request.getSession().getAttribute("member");
        if(loginMember != null) {
            member = loginMember;
        }
        log.info("member : {}", member);
		if(member.getMember_no() > 0) {
			// 필요한 유저 정보 보내주기
            model.addAttribute("member_no", member.getMember_no());
			model.addAttribute("loginNickname", member.getMember_nickname());
			// 로그인한 계정으로 해당 리뷰 좋아요 눌렀는지 확인
			likeVO.setReview_no(review_no);
			likeVO.setMember_no(member.getMember_no());
			log.info("likeVO : {}", likeVO);
			// DB 요청해서 좋아요상태 보내주기
			int likeCheck = reviewService.likeCheckForMember(likeVO);
			log.info("likeCheck : {}", likeCheck);
			likeVO.setLike_check(likeCheck);
			// likeVO에 현재 로그인계정, 현재 리뷰, 좋아요 상태 담아서 보내주기
			// 로그인아이디 고유번호, 작성자 고유번호로 팔로우상태 가져오기
            followStatus = followService.getFollowStatus(member.getMember_no(), review.getMember_no());
        }
        model.addAttribute("likeVO", likeVO);

		// 작성자 팔로우 관련 상태 보내주기
		model.addAttribute("followStatus", followStatus);
		model.addAttribute("followingCount", followService.getFollowingCount(review.getMember_no()));
		
		// 현재 리뷰 댓글 개수 보내주기
		model.addAttribute("replyCount", replyService.getReplyCount(review_no));
		
		return "review/content"; // 상세 페이지로 이동
	}
	
	// 리뷰 수정 페이지
	@GetMapping("modify")
	public String reviewModifyForm(@RequestParam("rno") Long review_no, Model model,
			@ModelAttribute("pager") ReviewPager pager, HttpServletRequest request) {
		log.info("======== modify GET : review/modify");
		log.info("review_no : {}", review_no);
		log.info("pager : {}", pager);
		
		MovieVO movie = (MovieVO)request.getSession().getAttribute("movie");
		log.info("movie : {}", movie);
		
		// 수정폼으로 이동할때 원래 입력 되어있는 값 그대로 가져가기
		ReviewVO review = reviewService.getReview(review_no);
		model.addAttribute("review", review);
		
		return "review/modify";
	}
	
	// 리뷰 수정 처리
	@PostMapping("modify")
	public String reviewModifyPro(ReviewVO review,
			@ModelAttribute("pager") ReviewPager pager) {
		log.info("======== modify POST : review/modify");
		log.info("review : {}", review);
		log.info("pager : {}", pager);
		
		// 별점이 0 ~ 5 사이일때
		if(review.getReview_star() >= 0 && review.getReview_star() <= 5) {
			reviewService.update(review); // 수정처리
		}
		
		log.info("======== redirect : review : {}",review.getReview_no());
		return "redirect:/review/" + review.getReview_no() + pager.getParameterQuery();
	}
	
	// 리뷰 삭제 처리
	@PostMapping("delete")
	public String reviewDelete(HttpServletRequest request, Long review_no,
			@ModelAttribute("pager") ReviewPager pager) {
		log.info("======== delete POST : review/delete");
		log.info("review_no : {}", review_no);
		log.info("pager : {}", pager);
		
        // 리뷰 고유번호로 리뷰 가져오기
        ReviewVO review = reviewService.getReview(review_no);
        log.info("get review : {}", review);

		// 로그인 상태 확인
		MemberVO member = (MemberVO)request.getSession().getAttribute("member");
		log.info("member : {}", member);
		
		// 리뷰 작성자와 로그인 계정이 같다면 삭제 요청
		if(review.getMember_no() == member.getMember_no()) {
			// 리뷰 삭제 처리
			int result = reviewService.delete(review_no);
			if(result != 1) {
				// 삭제 실패시 보던 리뷰로 이동
				return "redirect:/review/" + review_no + pager.getParameterQuery();
			}
		}
		// 리스트 보던 페이지로 이동
		return "redirect:/review/list" + pager.getParameterQuery();
	}
	
	// 좋아요 관련
	// 해당유저의 좋아요 누른 리뷰 목록
	@GetMapping("likelist")
	public String likeList(MemberVO member, Model model, HttpSession session,
			HttpServletRequest request, ReviewPager pager) {
		log.info("======== likelist GET : review/likelist");
		log.info("pager : {}", pager);
		
		// 현재 로그인중인 유저정보 가져오기
		member = (MemberVO)session.getAttribute("member");
		log.info("member : {}", member);
		Long member_no = 0L;
		if(member != null) {
			member_no = member.getMember_no();
		}
		
		// 이메일의 해당하는 유저의 고유번호 가져오기
		Long pageMemberNo = memberService.getMember(pager.getEmail()).getMember_no();
		// 유저가 좋아요 누른 리뷰들 가져오기
		// 페이징 처리
		List<ReviewVO> list = reviewService.getListOfLikes(pager, pageMemberNo);
		log.info("list : {}", list);
		if (list != null) {
			model.addAttribute("list", list); // 글목록 가져와 뷰에전달
			int total = list.size(); // 글 개수 가져오기
			pager.setListCheck(1);
			// 페이징처리요소 뷰에 전달
			model.addAttribute("pageDTO", new ReviewPageDTO(pager, total));
			// 해당 유저 정보 보내주기
			model.addAttribute("pageMember" , memberService.getMemberByNo(pageMemberNo));
		}
		
		// 로그인 유저의 고유번호 주고 어떤 리뷰 좋아요 눌렀는지 체크
		List<ReviewLikeVO> likeList = reviewService.liked(member_no);
		// Map<Long, Integer> (review_no, like_check)
		Map<Long, Integer> lmap = new HashMap<Long, Integer>();
		if(likeList != null) { // 좋아요 누른 리뷰가 있다면
			for(int i = 0; i < likeList.size(); i++) {
				// List에서 리뷰고유번호, 좋아요상태 꺼내기
				lmap.put(likeList.get(i).getReview_no(), likeList.get(i).getLike_check());
			}
			model.addAttribute("lmap", lmap);
			// ${lmap.get(review_no)}으로 꺼내서 좋아요 된 리뷰들 가져오기
		}
		
		// 리뷰에 해당하는 댓글 개수 보내주기
		// (리뷰고유번호, 댓글개수) 담기 위한 map 객체 만들어 놓기
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		// 리뷰 목록에 해당하는 만큼 반복
		for(int i = 0; i < list.size(); i++) {
			// 리뷰 한개씩 고유번호 가져와 reviewNo 변수에 담기
			Long review_no = list.get(i).getReview_no();
			// (리뷰고유번호)에 해당하는 댓글 개수 relpyCount 변수에 담기
			int replyCount = replyService.getReplyCount(review_no);
			// (review_no, replyCount) 형식으로 넣기
			map.put(review_no , replyCount); // [1=0] [2=25] [3=25] ...
		}
		model.addAttribute("map", map); // view에서 map.get(해당리뷰고유번호)로 찾기
		
		return "review/list";
	}
	
	// 리뷰 좋아요
	@ResponseBody
	@GetMapping(value = "like", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ReviewLikeDTO> getLikeVO(@RequestParam("rno") Long review_no,
			HttpServletRequest request) {
		log.info("======== like GET");
		log.info("review_no : {}", review_no);
		
		// 로그인 상태 확인
		MemberVO member = (MemberVO)request.getSession().getAttribute("member");
		// 좋아요 상태 담을 객체
		ReviewLikeVO likeVO = new ReviewLikeVO();
		// (좋아요개수, List<좋아요상태>) 담을 객체
		ReviewLikeDTO likeDTO;
		
		if(member != null) { // 로그인상태
			likeVO.setMember_no(member.getMember_no());
			likeVO.setReview_no(review_no);
			likeVO.setLike_check(reviewService.likeCheckForMember(likeVO));
			// 좋아요 상태 전부 설정 후 DTO객체에 담을 List에 담아주기
			List<ReviewLikeVO> likeList = new ArrayList<ReviewLikeVO>();
			likeList.add(likeVO);
			likeDTO = new ReviewLikeDTO(reviewService.getReview(review_no).getReview_like(), likeList);
		} else { // 비로그인 상태
			return null;
		}
		
		return new ResponseEntity<>(likeDTO, HttpStatus.OK);
	}
	
	// 리뷰 좋아요 요청 처리
	@ResponseBody
	@PostMapping(value = "like", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ReviewLikeVO> requestLike(@RequestBody ReviewLikeVO likeVO) {
		log.info("======== like POST");
		log.info("ReviewLikeVO : {}", likeVO);
		
		// 좋아요 상태 확인
		int likeCheck = reviewService.likeCheckForMember(likeVO);
		boolean result = false;
		
		// 좋아요o 상태
		if(likeCheck != 0) {
			log.info("liked");
			result = reviewService.disLike(likeVO);
			log.info("disLike : {}", result);
			likeCheck = reviewService.likeCheckForMember(likeVO);
		} else { // 좋아요x 상태
			log.info("not like");
			result = reviewService.addLike(likeVO);
			log.info("addLike : {}", result);
			likeCheck = reviewService.likeCheckForMember(likeVO);
		}
		if(result == false) { // 서비스 실패시
			log.info("null");
			return null;
		}
		
		likeVO.setLike_check(likeCheck);
		log.info("ReviewLikeVO : {}", likeVO);
		return new ResponseEntity<>(likeVO, HttpStatus.OK);
	}
	
}
