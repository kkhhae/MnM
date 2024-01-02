package com.movie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.domain.MoviePagerDTO;
import com.movie.domain.ReviewLikeVO;
import com.movie.domain.ReviewPager;
import com.movie.domain.ReviewVO;
import com.movie.persistence.ReplyMapper;
import com.movie.persistence.ReviewMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

	// 생성자 주입받기
	private final ReviewMapper reviewMapper;
	private final ReplyMapper replyMapper;
	
	// 리뷰 관련
	// 추가
	@Override
	public int insert(ReviewVO review) {
		return reviewMapper.insert(review);
	}
	
	// 목록 가져오기 
	@Override
	public List<ReviewVO> getList(ReviewPager pager) {
		return reviewMapper.getList(pager);
	}
	
	// 리뷰 전체 개수
	@Override
	public int getReviewCount() {
		return reviewMapper.getReviewCount();
	}
	
	// 목록 가져오기 (해당 작성자것만)
	@Override
	public List<ReviewVO> getListAsWriter(ReviewPager pager, Long pageMemberNo) {
		return reviewMapper.getListAsWriter(pager, pageMemberNo);
	}
	
	// 리뷰 개수 (해당 작성자것만)
	@Override
	public int getReviewCountAsWriter(Long pageMemberNo) {
		return reviewMapper.getReviewCountAsWriter(pageMemberNo);
	}
	
	// 작성자, 영화로 리뷰 찾기
	@Override
	public ReviewVO getMyMoiveReview(Long member_no, Long movie_no) {
		return reviewMapper.getMyMoiveReview(member_no, movie_no);
	}


	// 리뷰 한개 가져오기
	@Override
	public ReviewVO getReview(Long review_no) {
		// 조회수 올리기
		reviewMapper.updateReadcount(review_no);
		return reviewMapper.getReview(review_no);
	}
	
	// 수정
	@Override
	public int update(ReviewVO review) {
		return reviewMapper.update(review);
	}

	// 삭제
	@Override
	public int delete(Long review_no) {
		int result = reviewMapper.delete(review_no);
		if(result == 1) {
			// 리뷰삭제 성공시 해당 댓글, 좋아요 상태 전부 삭제
			replyMapper.deleteReplies(review_no);
			reviewMapper.disLikeAll(review_no);
		}
		return result;
	}
	
	// 리뷰에 해당하는 영화 제목 가져오기
	public String getTitle(Long movie_no) {
		return reviewMapper.getTitle(movie_no);
	}
	
	// 메인 페이지에 보여줄 인기 리뷰 리스트
	@Override
	public List<ReviewVO> mainPageReview(MoviePagerDTO pager) {
		return reviewMapper.mainPageReview(pager); 
	}
	
	// 해당 영화의 인기 리뷰 4개 가져오기
	@Override
	public List<ReviewVO> getPopularReviewAsMovie(Long movie_no) {
		return reviewMapper.getPopularReviewAsMovie(movie_no);
	}

	// 목록 가져오기 (해당 영화것만)
	@Override
	public List<ReviewVO> getListAsMovie(ReviewPager pager, Long movie_no) {
		return reviewMapper.getListAsMovie(pager, movie_no);
	}

	// 리뷰 전체 개수 (해당 영화것만)
	@Override
	public int getReviewCountAsMovie(Long movie_no) {
		return reviewMapper.getReviewCountAsMovie(movie_no);
	}
	
	
	// 좋아요 관련
	// 좋아요 누른 사람 추가
	@Override
	public boolean addLike(ReviewLikeVO likeVO) {
		boolean result = false;
		int rs = reviewMapper.addLike(likeVO);
		if(rs == 1) {
			// 추가 성공시
			// 해당리뷰의 좋아요 +1
			rs = reviewMapper.plusLike(likeVO.getReview_no());
			if(rs == 1) {
				log.info("plus like success");
				result = true;
			}
		}
		return result;
	}

	// 좋아요 누른 사람 삭제
	@Override
	public boolean disLike(ReviewLikeVO likeVO) {
		boolean result = false;
		int rs = reviewMapper.disLike(likeVO);
		if(rs == 1) {
			// 삭제 성공시
			// 해당리뷰의 좋아요 -1
			rs = reviewMapper.minusLike(likeVO.getReview_no());
			if(rs == 1) {
				log.info("minus like success");
				result = true;
			}
		}
		return result;
	}

	// 해당 유저의 좋아요 누른 리뷰 체크(리스트에서 사용)
	@Override
	public List<ReviewLikeVO> liked(Long member_no) {
		return reviewMapper.liked(member_no);
	}

	// 로그인 유저가 해당 리뷰를 좋아요 눌렀는지 체크(상세페이지에서 사용)
	@Override
	public int likeCheckForMember(ReviewLikeVO likeVO) {
		likeVO = reviewMapper.likeCheckForMember(likeVO);
		// 좋아요x 상태일 경우 0 리턴
		if(likeVO == null) {
			return 0;
		}
		return likeVO.getLike_check();
	}

	// 해당 유저의 좋아요 누른 리뷰 목록 가져오기
	@Override
	public List<ReviewVO> getListOfLikes(ReviewPager pager, Long pageMemberNo) {
		return reviewMapper.getListOfLikes(pager, pageMemberNo);
	}
	
	
	
	
	
	
	
	
	
	@Override
	public List<ReviewVO> getLoggedInUserReviews(Long member_no) {
	    ReviewPager pager = new ReviewPager();
	    pager.setPageNum(1); // 1페이지부터 가져옵니다.
	    pager.setListSize(10); // 한 페이지에 10개씩 가져옵니다.

	    return reviewMapper.getListAsWriter(pager, member_no);
	}

	@Override
	public List<ReviewVO> getOtherUserReviews(long member_no) {
		ReviewPager pager = new ReviewPager();
		pager.setPageNum(1); // 1페이지부터 가져옵니다.
		pager.setListSize(10); // 한 페이지에 10개씩 가져옵니다.

		return reviewMapper.getListAsWriter(pager, member_no);
	}


}
