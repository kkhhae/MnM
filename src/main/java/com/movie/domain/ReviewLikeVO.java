package com.movie.domain;


import lombok.Data;

@Data
public class ReviewLikeVO {
	
	private Long like_no;	// 좋아요 고유번호
	private Long review_no;	// 리뷰 고유번호
	private Long member_no;	// 멤버 고유번호
	private int like_check; // 좋아요 상태 : 0 || 1
	
}
