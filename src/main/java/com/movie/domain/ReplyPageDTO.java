package com.movie.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyPageDTO {

	private int replyCount;			// 댓글 전체 개수
	private List<ReplyVO> list; 	// 댓글 목록
	
}
