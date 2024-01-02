package com.movie.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.movie.domain.MoviePagerDTO;
import com.movie.domain.ReviewLikeVO;
import com.movie.domain.ReviewPager;
import com.movie.domain.ReviewVO;

@Mapper
public interface ReviewMapper {

	// 리뷰 관련
	// 추가
	public int insert(ReviewVO review);
	
	// 목록 가져오기
	public List<ReviewVO> getList(ReviewPager pager);
	
	// 리뷰 전체 개수
	public int getReviewCount();
	
	// 목록 가져오기 (해당 작성자것만)
	public List<ReviewVO> getListAsWriter(
			@Param("pager") ReviewPager pager,
			@Param("pageMemberNo") Long pageMemberNo);


	// 리뷰 전체 개수 (해당 작성자것만)
	public int getReviewCountAsWriter(Long pageMemberNo);
	
	// 작성자, 영화로 리뷰 찾기
	public ReviewVO getMyMoiveReview(
			@Param("member_no") Long member_no,
			@Param("movie_no") Long movie_no);
	
	// 리뷰 한개 가져오기
	public ReviewVO getReview(Long review_no);
	
	// 글 조회수 올리기 
	public void updateReadcount(Long review_no);
	
	// 수정
	public int update(ReviewVO review);
	
	// 삭제
	public int delete(Long review_no);
	
	// 리뷰에 해당하는 영화 제목 가져오기
	public String getTitle(Long movie_no);
	
	// 메인 페이지에 보여줄 인기 리뷰 리스트
	public List<ReviewVO> mainPageReview(MoviePagerDTO pager);
	
	// 해당 영화의 인기 리뷰 4개 가져오기
	public List<ReviewVO> getPopularReviewAsMovie(Long movie_no);
	
	// 목록 가져오기 (해당 영화것만)
	public List<ReviewVO> getListAsMovie(
			@Param("pager") ReviewPager pager,
			@Param("movie_no") Long movie_no);




	
	// 리뷰 전체 개수 (해당 영화것만)
	public int getReviewCountAsMovie(Long movie_no);
	
	
	// 좋아요 관련
	// 좋아요 +1
	public int plusLike(Long review_no);
	
	// 좋아요 -1
	public int minusLike(Long review_no);
	
	// 좋아요 누른 사람 추가
	public int addLike(ReviewLikeVO likeVO);
	
	// 좋아요 취소 한 사람 삭제
	public int disLike(ReviewLikeVO likeVO);
	
	// 해당 유저의 좋아요 누른 리뷰 체크(리스트에서 사용)
	public List<ReviewLikeVO> liked(Long member_no);
	
	// 로그인 유저가 해당(상세페이지에서 사용) 
	public ReviewLikeVO likeCheckForMember(ReviewLikeVO likeVO);
	
	// 해당 유저의 좋아요 누른 리뷰 목록 가져오기
	public List<ReviewVO> getListOfLikes(
			@Param("pager") ReviewPager pager,
			@Param("pageMemberNo") Long pageMemberNo);
	
	// 리뷰 삭제시 좋아요 상태 전부 삭제
	public void disLikeAll(Long review_no);
	
}
