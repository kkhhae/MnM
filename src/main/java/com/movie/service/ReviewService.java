package com.movie.service;

import java.util.List;

import com.movie.domain.MoviePagerDTO;
import com.movie.domain.ReviewLikeVO;
import com.movie.domain.ReviewPager;
import com.movie.domain.ReviewVO;

public interface ReviewService {
	
	// 리뷰 관련
	// 추가
	public int insert(ReviewVO review);
	
	// 목록 가져오기
	public List<ReviewVO> getList(ReviewPager pager);
	
	// 리뷰 전체 개수
	public int getReviewCount();
	
	// 목록 가져오기 (해당 작성자것만)
	public List<ReviewVO> getListAsWriter(ReviewPager pager, Long pageMemberNo);
	
	// 리뷰 전체 개수 (해당 작성자것만)
	public int getReviewCountAsWriter(Long pageMemberNo);
	
	// 작성자, 영화로 리뷰 찾기
	public ReviewVO getMyMoiveReview(Long member_no, Long movie_no);
	
	// 리뷰 한개 가져오기
	public ReviewVO getReview(Long review_no);
	
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
	public List<ReviewVO> getListAsMovie(ReviewPager pager, Long movie_no);
	
	// 리뷰 전체 개수 (해당 영화것만)
	public int getReviewCountAsMovie(Long movie_no);

	
	
	// 좋아요 관련
	// 좋아요 누른 사람 추가
	public boolean addLike(ReviewLikeVO likeVO);
	
	// 좋아요 취소 한 사람 삭제
	public boolean disLike(ReviewLikeVO likeVO);
	
	// 해당 유저의 좋아요 누른 리뷰 체크(리스트에서 사용)
	public List<ReviewLikeVO> liked(Long member_no);
	
	// 로그인 유저가 해당 리뷰를 좋아요 눌렀는지 체크(상세페이지에서 사용)
	public int likeCheckForMember(ReviewLikeVO likeVO);
	
	// 해당 유저의 좋아요 누른 리뷰 목록 가져오기
	public List<ReviewVO> getListOfLikes(ReviewPager pager, Long pageMemberNo);
	
	
	
	
	
	
	
	
	
	// 로그인한 유저의 리뷰 리스트 가져오기
	public List<ReviewVO> getLoggedInUserReviews(Long member_no);

	// 다른 유저의 리뷰 리스트 가져오기
	public List<ReviewVO> getOtherUserReviews(long member_no);
}
