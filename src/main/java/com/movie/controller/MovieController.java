package com.movie.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.movie.domain.MovieVO;
import com.movie.domain.MovieWantVO;
import com.movie.domain.ReviewVO;
import com.movie.domain.MoviePagerDTO;
import com.movie.domain.GenreVO;
import com.movie.domain.MemberVO;
import com.movie.domain.MoviePager;
import com.movie.service.FollowService;
import com.movie.service.MemberService;
import com.movie.service.MovieService;
import com.movie.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/movie/*")
@Slf4j
@RequiredArgsConstructor
public class MovieController {

	private final MovieService movieService;
	private final ReviewService reviewService;
	private final MemberService memberService;
	private final FollowService followService;



	//영화 리스트 페이지
	@GetMapping("list")
	public String list(Model model, MoviePagerDTO pager, MovieVO movie, GenreVO genre, HttpSession session) {

		//전체 영화 가져오기(페이징처리)
		List<MovieVO> list = movieService.getMovies(pager);
		model.addAttribute("list", list);
		//페이징처리 담기
		model.addAttribute("pager", new MoviePager(pager, movieService.getMovieCount(pager)));

		return "movie/list";
	}

	//영화 작성 페이지
	@GetMapping("inputMovie")
	public String inputMovie() {
		return "movie/inputMovie";
	}

	//기존에 있던 영화 중복처리하기(아직안함)
	//영화 작성
	@PostMapping("inputMovie")
	public String inputMoviePro(MovieVO movie) {
		log.info("MovieVO : {}",movie);

		movieService.inputMovie(movie);

		Long movie_no = movieService.getMovieNo(movie.getMovie_title());
		log.info("movie_no:{}",movie_no);

		return "redirect:/movie/list";
	}

	//영화 상세 페이지
	@GetMapping("content")
	public String content(Long movie_no, Model model,
						  MoviePagerDTO pager, HttpServletRequest request, MemberVO member_no) {
		log.info("movie_no:{}",movie_no);
		//영화 하나 정보 가져오기
		MovieVO movie = movieService.getOneMovie(movie_no);
		log.info("movie:{}",movie);
		//페이징 처리
		model.addAttribute("pager", new MoviePager(pager, movieService.getMovieCount(pager)));
		model.addAttribute("movie",movie);

		//장르명 가져오기
		List<GenreVO> genreList = movieService.getAllGenres();
		log.info("genreList : {}",genreList);
		model.addAttribute("genreList", genreList);

		MemberVO movieMember = memberService.getMemberByNo(member_no.getMember_no());
		model.addAttribute("member", movieMember);

		// 로그인 상태 확인 후 해당계정 리뷰 보내주기
        MemberVO member = new MemberVO();
		MemberVO loginMember = (MemberVO)request.getSession().getAttribute("member");
		if(loginMember != null) {
            member = loginMember;
			ReviewVO myReview = reviewService.getMyMoiveReview(member.getMember_no(), movie_no);
			log.info("content member : {}", myReview);
			model.addAttribute("myReview", myReview);
		}
        model.addAttribute("loginMember", member);

		// 영화의 OTT 정보 검색
		String movieOttLink = movie.getMovie_ott_link();

		if (movieOttLink == null || movieOttLink.isEmpty()) {
			movieOttLink = "";
		}
		else {
			// 링크들 쉼표(,)로 구분하여 배열로 나누기
			String[] ottLinks = movieOttLink.split(",");
			String[] movieOttArray = movie.getMovie_ott().split(","); // movie_ott도 배열로 담기 위해 추가

			List<MovieVO> movieOttList = new ArrayList<>();
			for (int i = 0; i < ottLinks.length; i++) {
				MovieVO movieOttAdd = new MovieVO();
				movieOttAdd.setMovie_ott(movieOttArray[i]); // 배열로 설정된 movie_ott 값을 사용
				movieOttAdd.setMovie_ott_link(ottLinks[i].trim());
				movieOttFind(movieOttAdd); // 영화의 OTT 정보 검색
				movieOttList.add(movieOttAdd);

				// null 체크하여 처리
				if (movieOttAdd.getMovie_ott() == null) {
					movieOttAdd.setMovie_ott("");
					movieOttAdd.setMovie_ott_link("");
				}
			}
			model.addAttribute("movieOttList", movieOttList);
		}
		return "movie/content";
	}


	//영화 수정 페이지 호출
	@GetMapping("modify")
	public String modify(@ModelAttribute("movie_no")Long movie_no, Model model,
						 @ModelAttribute("pager") MoviePagerDTO pager) {

		//영화 수정시 사용할 로직 호출(영화 하나 가져오기)
		MovieVO movie = movieService.getOneMovie(movie_no);
		//불러온 값 뷰에 기존 정보 담기
		model.addAttribute("movie",movie);

		//페이징 처리 정보 담기
		model.addAttribute("pager", new MoviePager(pager, movieService.getMovieCount(pager)));

		//장르명 가져오기
		List<GenreVO> genreList = movieService.getAllGenres();
		log.info("genreList : {}",genreList);
		model.addAttribute("genreList", genreList);
		log.info("model : {} ", model);

		return "movie/modify";
	}

	//수정처리
	@PostMapping("modify")
	public String modifyPro(MovieVO movie_no, MoviePagerDTO pager, Model model) {

		//영화 수정시 사용할 로직 호출(영화 하나 가져오기)
		log.info("modify pro no: {}", movie_no);

		//처리를 위한 로직 호출(DB에 정보 수정하게)
		movieService.updateMovie(movie_no);

		model.addAttribute("pager", new MoviePager(pager, movieService.getMovieCount(pager)));
		return "redirect:/movie/content" +  pager.getParameterToQueryString() + "&movie_no="+ movie_no.getMovie_no() ;
	}

	//삭제 처리
	@PostMapping("delete")
	public String delete(Long movie_no, MoviePagerDTO pager)	{
		log.info("delete pro : {}", movie_no);

		//비지니스 로직 호출(글 고유번호 주고 삭제 처리)
		movieService.deleteMovie(movie_no);

		return "redirect:/movie/list" + pager.getParameterToQueryString();
	}



	public List<MovieVO> movieOttFind(MovieVO movie) {
		List<MovieVO> movieOttList = new ArrayList<>();

		// Tving 링크에서 OTT 이름 추출
		if (movie.getMovie_ott_link().contains("tving")) {
			String tvingLink = movieService.searchOttLinkFromDB("tving", movie.getMovie_ott_link());
			MovieVO tvingVO = new MovieVO();
			tvingVO.setMovie_ott("티빙");
			tvingVO.setMovie_ott_link(tvingLink);
			movieOttList.add(tvingVO);
		}

		// Wavve 링크에서 OTT 이름 추출
		if (movie.getMovie_ott_link().contains("wavve")) {
			String wavveLink = movieService.searchOttLinkFromDB("wavve", movie.getMovie_ott_link());
			MovieVO wavveVO = new MovieVO();
			wavveVO.setMovie_ott("웨이브");
			wavveVO.setMovie_ott_link(wavveLink);
			movieOttList.add(wavveVO);
		}

		// DisneyPlus 링크에서 OTT 이름 추출
		if (movie.getMovie_ott_link().contains("disney")) {
			String disneyLink = movieService.searchOttLinkFromDB("disney", movie.getMovie_ott_link());
			MovieVO disneyVO = new MovieVO();
			disneyVO.setMovie_ott("디즈니플러스");
			disneyVO.setMovie_ott_link(disneyLink);
			movieOttList.add(disneyVO);
		}

		// 쿠팡플레이 링크에서 OTT 이름 추출
		if (movie.getMovie_ott_link().contains("coupangplay")) {
			String coupangPlayLink = movieService.searchOttLinkFromDB("coupangplay", movie.getMovie_ott_link());
			MovieVO coupangPlayVO = new MovieVO();
			coupangPlayVO.setMovie_ott("쿠팡플레이");
			coupangPlayVO.setMovie_ott_link(coupangPlayLink);
			movieOttList.add(coupangPlayVO);
		}

		// 넷플릭스 링크에서 OTT 이름 추출
		if (movie.getMovie_ott_link().contains("netflix")) {
			String netflixLink = movieService.searchOttLinkFromDB("coupangplay", movie.getMovie_ott_link());
			MovieVO netflixVO = new MovieVO();
			netflixVO.setMovie_ott("넷플릭스");
			netflixVO.setMovie_ott_link(netflixLink);
			movieOttList.add(netflixVO);
		}

		// 시리즈온 링크에서 OTT 이름 추출
		if (movie.getMovie_ott_link().contains("serieson")) {
			String seriesonLink = movieService.searchOttLinkFromDB("serieson", movie.getMovie_ott_link());
			MovieVO seriesonVO = new MovieVO();
			seriesonVO.setMovie_ott("시리즈온");
			seriesonVO.setMovie_ott_link(seriesonLink);
			movieOttList.add(seriesonVO);
		}

		// 왓챠 링크에서 OTT 이름 추출
		if (movie.getMovie_ott_link().contains("watcha")) {
			String watchaLink = movieService.searchOttLinkFromDB("watcha", movie.getMovie_ott_link());
			MovieVO watchaVO = new MovieVO();
			watchaVO.setMovie_ott("왓챠");
			watchaVO.setMovie_ott_link(watchaLink);
			movieOttList.add(watchaVO);
		}


		return movieOttList;
	}



	//검색페이지(영화 + mbti)
	@GetMapping("movie/searchList")
	public String search(@RequestParam("sel") String sel, @RequestParam("keyword") String keyword, Model model, MoviePagerDTO pager, HttpSession session) {
		System.out.println("******************************검색 요청 들어옴!!!");
		System.out.println("category : " + sel);
		System.out.println("keyword :" + keyword);

		List<MovieVO> searchResult = null;


		// 영화 제목으로 영화 리스트 요청
		if (sel.equals("movie_title")) {
			searchResult = movieService.searchByMovieTitle(keyword);
		}
		// mbti로 일치하는 영화 리스트 요청
		else if (sel.equals("member_mbti")) {
			searchResult = movieService.searchByMBTIName(keyword);
		}
		// 유저로 검색-> 유저리스트 요청 -> 로그인 상태에서 검색가능 -> 로그아웃 한상태에서 유저 검색되어 목록 나오도록 설정 필요 _23.07.21
		else if(sel.equals("member_user")) {
			// 닉네임을 기준으로 해당 유저 정보를 가져온다는 가정하에 아래의 코드를 추가합니다.
			// 닉네임 검색어 를 기준으로 부합되는 회원 목록 전부 가져옴
			List<MemberVO> members = memberService.getMemberByNickname(keyword);
			// 로그인 한 사람의 session정보를 MemberVO 타입의 myVO에 담기
			MemberVO myVO = (MemberVO)session.getAttribute("member");
			if (members != null) { // 닉네임에 부합되는 사람들이 하나라도 있으면
				if(myVO!=null) {// 로그인 한 상태이면~
					// 로그인 한 사람이 해당 사람들을 팔로우 했는지 여부 DB가서 가져오기
					// ( select count(*) from relation where member_no_follower=#{나} and member_no_following=#{목록에서 한명씩}
					List<Integer> followStatus = followService.getFollowingStatus(myVO.getMember_no(), members);
					log.info("followStatus : {}",followStatus);
					model.addAttribute("followStatus", followStatus);	// +(추가적으로) 나의 팔로우 여부 확인해서 view에 전달
				}
				// 로그인한 상태 아닐때 => myVO == null
				// 상대들의 팔로워 수와 팔로잉 수 조회하여 모델에 추가
				// + model에 검색된 유저 정보들 (members) 같이 담아서 view에 전달
				List<Integer> followerCounts = new ArrayList<Integer>();
				List<Integer> followingCounts = new ArrayList<Integer>();
				for (MemberVO member : members) {
					int followerCount = followService.getFollowerCount(member.getMember_no());
					int followingCount = followService.getFollowingCount(member.getMember_no());
					followerCounts.add(followerCount);
					followingCounts.add(followingCount);
				}
				model.addAttribute("members", members); 					// 검색된 유저 정보
				model.addAttribute("followerCounts", followerCounts);		// 팔로워 수
				model.addAttribute("followingCounts", followingCounts);		// 팔로잉 수
			}// 해당 닉네임이 존재할 경우 end
			return "/member/otherpageList";
		}

		model.addAttribute("searchList", searchResult);
		model.addAttribute("pager", new MoviePager(pager, searchResult.size()));

		return "/movie/searchList";
	}

	/*
	//메인페이지 호출
	@GetMapping("home")
	public String gethome(Model model, MoviePagerDTO pager) {

		//전체 영화 가져오기(페이징처리)
		List<MovieVO> list = movieService.getMovies(pager);
		log.info("pager:{}",list);
		model.addAttribute("pager", new MoviePager(pager, movieService.getMovieCount(pager)));

		//영화 전체 담기
		model.addAttribute("list", list);
		log.info("list:{}",list);

		//최신 영화 담기
		List<MovieVO> recentList = movieService.getRecentMovies(pager);
		model.addAttribute("recentList", recentList);
		log.info("recentList {}",recentList);

		// 인기 리뷰 담기
		List<ReviewVO> popularReview = reviewService.mainPageReview(pager);
		model.addAttribute("popularReview", popularReview);
		log.info("popularReview : {}", popularReview);

		return "/home";
	}
	 */
	//영화 찜 기능 추가
	// 찜하기
	@PostMapping("movie/movieWant")
	public ResponseEntity<String> like(@RequestBody MovieWantVO vo, HttpSession session) {
		MemberVO member = (MemberVO) session.getAttribute("member");
		if (member == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// MemberVO 객체에서 member_no 필드를 가져옵니다.
		Long memberNo = member.getMember_no();
		vo.setMember_no(memberNo);

		//찜 추가
		int count = movieService.addWantMovie(vo);
		log.info("addMovieLike result : {}", count);
		return count > 0 ? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 찜 목록 확인
	@GetMapping("/movieWant/{member_no}")
	public ResponseEntity<List<MovieWantVO>> getLikes(@PathVariable Long member_no) {
		List<MovieWantVO> list = movieService.getMovieWantByMember(member_no);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	// 찜 삭제
	@DeleteMapping("movie/movieWant")
	public ResponseEntity<String> unlike(@RequestBody MovieWantVO vo, HttpSession session) {
		MemberVO member = (MemberVO) session.getAttribute("member");
		if (member == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// MemberVO 객체에서 member_no 필드를 가져옵니다.
		Long memberNo = member.getMember_no();
		vo.setMember_no(memberNo);

		//찜 취소
		boolean result = movieService.removeWantMovie(vo);
		log.info("deleteMovieLike result : {}", result);
		return result ? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}



}
	
