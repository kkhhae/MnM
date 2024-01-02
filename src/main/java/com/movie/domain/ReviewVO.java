package com.movie.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewVO {
	
	private Long review_no;			// 리뷰 고유번호
	private Long movie_no;			// 영화 고유번호
	private Long member_no;			// 회원 고유번호
	private String review_content;	// 리뷰 내용
	private Integer review_star;	// 별점
	private Integer review_mbti;	// 작성자 MBTI
	private Long review_like;		// 리뷰 좋아요 개수
	private LocalDateTime review_reg;	// 리뷰 작성 날짜
	private Long review_readcount;	// 리뷰 조회수
	
	// 테이블 조인 시 필요
	private String movie_title;		// 영화 제목
	private String member_nickname;	// 유저 닉네임
	private String member_mbti;		// 유저 MBTI
	private String member_email;	// 유저 이메일
	
}
