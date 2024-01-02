package com.movie.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.movie.domain.MovieVO;
import com.movie.domain.MovieWantVO;
import com.movie.domain.ReviewVO;
import com.movie.domain.GenreVO;
import com.movie.domain.MoviePagerDTO;

public interface MovieService {

		
		// 영화리스트 가져오기
		public List<MovieVO> getMovies(MoviePagerDTO page);	 
		
		//최신영화로 가져오기
		public List<MovieVO> getRecentMovies(MoviePagerDTO page);
		
		//글 검색 추가
		public int getMovieCount(MoviePagerDTO page);
		
		//영화 한개 가져오기 
		public MovieVO getOneMovie(Long movie_no);
		
		//영화 수정하기
		public void updateMovie(MovieVO movie_no);
		
		//영화 저장
		public void inputMovie(MovieVO movie_no);
		
		//영화 삭제
		public void deleteMovie(Long movie_no);
		
		
		//영화 이름으로 영화번호 가져오기 
		public Long getMovieNo(String movie_title);

		//장르 리스트 가져오기
		List<GenreVO> getAllGenres();
		
	
		//영화 리뷰 가져오기
		ReviewVO getReviewByMovieNo(Long movie_no);
	
		//영화 ott링크 가져오기
		public String searchOttLinkFromDB(@Param("movie_ott")String movie_ott, @Param("movie_ott_link")String movie_ott_link);
		
		
		// 검색 기능 관련 로직 추가
		// 타이틀로 영화 검색
		public List<MovieVO> searchByMovieTitle(String keyword);
		// mbti로 영화 검색
		public List<MovieVO> searchByMBTIName(String keyword);
		public List<MovieVO> searchByKeyword(String sel, String keyword);
		
		
		//영화 찜
				public int addWantMovie(MovieWantVO want);
				public List<MovieWantVO> getMovieWantByMember(Long member_no);
				public boolean removeWantMovie(MovieWantVO want);
				public MovieWantVO getByMovieWithMember(MovieWantVO movie);
		
		
		
		
		
}
