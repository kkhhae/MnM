package com.movie.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.movie.domain.MemberVO;
import com.movie.domain.MoviePager;
import com.movie.domain.MoviePagerDTO;
import com.movie.domain.MovieVO;
import com.movie.domain.ReviewVO;
import com.movie.service.MovieService;
import com.movie.service.ReviewService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class HomeController {

	@Autowired
	private MovieService movieService;

	@Autowired
	private ReviewService reviewService;

//	// 시간되면 관리자용 페이지로 사용
//	 @GetMapping("/main")
//	    public String mainPage() {
//	        return "/main"; // main.jsp 또는 main.html 등의 뷰 페이지를 반환
//	    }


	//메인페이지 호출
	@GetMapping("home")
	public String gethome(Model model, MoviePagerDTO pager, HttpSession session) {

		//전체 영화 가져오기(페이징처리)
		List<MovieVO> list = movieService.getMovies(pager);
		log.info("pager:{}",list);
		model.addAttribute("pager", new MoviePager(pager, movieService.getMovieCount(pager)));

		//영화 전체 담기
		model.addAttribute("list", list);
		log.info("list:{}",list);

		//회원 정보 담기
		MemberVO vo = (MemberVO)session.getAttribute("member");
		model.addAttribute("member", vo);

		//최신 영화 담기
		List<MovieVO> recentList = movieService.getRecentMovies(pager);
		model.addAttribute("recentList", recentList);
		log.info("recentList {}",recentList);

		//mbti검색된 영화 담기(추천)
		if(vo != null) {
			List<MovieVO> searchResult = movieService.searchByMBTIName(vo.getMember_mbti());
			model.addAttribute("searchResult", searchResult);
		}
		// 인기 리뷰 담기
		List<ReviewVO> popularReview = reviewService.mainPageReview(pager);
		model.addAttribute("popularReview", popularReview);
		log.info("popularReview : {}", popularReview);



		return "/home";
	}







}
