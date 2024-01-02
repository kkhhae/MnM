package com.movie.service;


import java.util.Collections;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.movie.domain.MovieVO;
import com.movie.domain.MovieWantVO;
import com.movie.domain.ReviewVO;
import com.movie.domain.GenreVO;
import com.movie.domain.MoviePagerDTO;
import com.movie.persistence.MovieMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

private final MovieMapper movieMapper;
	

	//영화 전체 가져오기
	@Override
	public List<MovieVO> getMovies(MoviePagerDTO page) {
		return movieMapper.getMovies(page);
	}
	//최신영화로 가져오기
	@Override
	public List<MovieVO> getRecentMovies(MoviePagerDTO page) {
		return movieMapper.getRecentMovies(page);
	}

	//검색기능할때 사용( 영화가 몇 개 인지 movie_no 값)
	@Override
	public int getMovieCount(MoviePagerDTO page) {
		return movieMapper.getMovieCount(page);
	}

	
	//영화 1개 가져오기
	@Override
	public MovieVO getOneMovie(Long movie_no) {
		return movieMapper.getOneMovie(movie_no);
	}

	//영화 등록하기
	@Override
	public void inputMovie(MovieVO vo) {
		movieMapper.addMovie(vo);
	}

	//영화 수정하기
	@Override
	public void updateMovie(MovieVO movie_no) {
		movieMapper.updateMovie(movie_no);
		
	}
	
	//영화 번호(movie_no) 가져오기
	@Override
	public Long getMovieNo(String movie_title) {
		return movieMapper.getMovieNo(movie_title);
	}


	//영화 삭제
	@Override
	public void deleteMovie(Long movie_no) {
		movieMapper.deleteMovie(movie_no);
	}

	//장르가져오기
	@Override
    public List<GenreVO> getAllGenres() {
        return movieMapper.getAllGenres();
    }

	//영화 리뷰 가져오기
	@Override
	public ReviewVO getReviewByMovieNo(Long movie_no) {
		return movieMapper.getReviewByMovieNo(movie_no);
	}
	
	//영화에 맞는 ott링크 가져오기
	@Override
	public String searchOttLinkFromDB(@Param("movie_ott")String movie_ott, @Param("movie_ott_link")String movie_ott_link) {
	    String ottLink = movieMapper.searchOttLink(movie_ott_link, movie_ott);
	    return ottLink;
	}
	
	
	//영화 검색기능
		@Override
		public List<MovieVO> searchByMovieTitle(String keyword) {
			return movieMapper.searchByMovieTitle(keyword);
		}

		@Override
		public List<MovieVO> searchByMBTIName(String keyword) {
			return movieMapper.searchByMBTIName(keyword);
		}

		@Override
		public List<MovieVO> searchByKeyword(String sel, String keyword) {
			if (sel.equals("movie_title")) {
	            return movieMapper.searchByMovieTitle(keyword);
	        } else if (sel.equals("mbti_name")) {
	            return movieMapper.searchByMBTIName(keyword);
	        }
	        return Collections.emptyList(); // 검색 카테고리가 올바르지 않을 경우 빈 리스트 반환
	    }
	
	
	
		//영화 찜 기능들
				@Override
			    public int addWantMovie(MovieWantVO want) {
			        return movieMapper.insertWant(want);
			    }

			    @Override
			    public List<MovieWantVO> getMovieWantByMember(Long member_no) {
			        return movieMapper.getMovieWantByMember(member_no);
			    }

			    @Override
			    public boolean removeWantMovie(MovieWantVO want) {
			        return movieMapper.deleteWant(want.getMovie_no(), want.getMember_no());
			    }

			    @Override
			    public MovieWantVO getByMovieWithMember(MovieWantVO want) {
			        return movieMapper.getByMovieWithMember(want);
			    }
	
	
	
	
}

	

	
	
	
	

		
	


