package com.movie.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VisitVO {

	private Long visit_no;			// 방명록 고유번호
	private Long receiver;			// 마이페이지 주인 member_no
	private Long sender;			// 방명록을 쓴 사람 member_no
	private String content;			// 방명록 내용 (신고당하면 내용변경)
	private String visit_checked;	// 방명록 주인이 댓글달았는지 (확인여부)	
	private String recontent; 		// 주인의 댓글내용
	private Long is_comment;		// 댓글 확인여부 0이면 본문글, 1이면 주인댓글
	private LocalDateTime visit_date;	// 방명록 쓴 날짜.
	
	private String member_nickname; // 회원 닉네임
    private String member_mbti; // 회원 MBTI
    private long member_no;		// 회원 고유번호
    
	
	
}
