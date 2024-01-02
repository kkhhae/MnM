package com.movie.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.movie.domain.MovieVO;
import com.movie.domain.MovieWantVO;
import com.movie.domain.ReviewVO;
import com.movie.domain.GenreVO;
import com.movie.domain.MoviePagerDTO;
@Mapper
public interface MovieMapper {

		//영화 리스트 가져오기 + 페이징처리(목록)
		public List<MovieVO> getMovies(MoviePagerDTO page);
		//영화 최신으로 가져오기
		public List<MovieVO> getRecentMovies(MoviePagerDTO page);
		
		//영화 검색할때 번호가져오기
		public int getMovieCount(MoviePagerDTO page);
		//영화 하나 가져오기
		public MovieVO getOneMovie(Long movie_no);
		
		
		//영화 수정하기
		public void updateMovie(MovieVO movie_no);
		//영화 추가하기
		public void addMovie(MovieVO vo);
		//영화 삭제하기
		public void deleteMovie(Long movie_no);
		
		//영화번호가져오기
		public Long getMovieNo(String title);
		
		//장르 리스트 가져오기
		List<GenreVO> getAllGenres();
		
		
		//영화 리뷰 가져오기
		ReviewVO getReviewByMovieNo(Long movie_no);
		
		//영화 ott링크 가져오기
		String searchOttLink(@Param("movie_ott_link") String movie_ott_link, 
				@Param("movie_ott") String movie_ott);
		
		// 영화 검색
		public List<MovieVO> searchByMovieTitle(String keyword);
		public List<MovieVO> searchByMBTIName(String keyword);

		//영화 찜기능
				//찜하기 ( 추가 )
				public int insertWant(MovieWantVO want);
				// 찜 목록 확인
				public List<MovieWantVO> getMovieWantByMember(Long member_no);
				// 찜 목록 1개 삭제
				public boolean deleteWant(@Param("movie_no")Long movie_no, @Param("member_no")Long member_no);
				// 이미 찜한 영화인지 조회
				public MovieWantVO getByMovieWithMember(@Param("want")MovieWantVO want);
		
		
		
		
}
 