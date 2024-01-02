package com.movie.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewLikeDTO {
	
	private Long likeCount;				// 좋아요 개수  
	private List<ReviewLikeVO> list;	// 리뷰 좋아요 관련 리스트
	
	/*
		생성자
		ReviewLikeDTO(likeCount, List<ReviewLikeVO>)
	*/
}
