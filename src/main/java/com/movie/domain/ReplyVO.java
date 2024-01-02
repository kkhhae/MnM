package com.movie.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReplyVO {

	private Long reply_no; 			// 댓글 고유번호 
	private Long review_no; 		// 리뷰 고유번호 
	private Long member_no;			// 댓글 작성자 고유번호
	private String reply_content; 	// 댓글 내용 
	private LocalDateTime reply_reg; 	// 댓글 작성시간
	private int reply_group;		// 댓글 그룹 (댓글의 답글 위치확인)
	private int reply_step;			// 댓글 순서
	private int reply_level;		// 댓글 단계 (답글의 tap 정도)
	
	// 테이블 조인시 사용
	private String member_nickname;	// 유저 닉네임
	private String member_email;	// 유저 이메일
	
}
