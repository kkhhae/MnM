package com.movie.domain;


import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Data;


@Data
public class MemberVO {

	private long member_no;				// 멤버 시퀀스 넘버
	private String member_pw;			// 멤버 로그인 비밀번호
	private String member_nickname;		// 멤버 노출용 닉네임
	private String member_email;			// 멤버 로그인 이메일
	private String member_mbti;			// 멤버 MBTI
	private LocalDateTime member_reg;		// 멤버 가입일
	private String member_status;		// 멤버 상태(활성, 비활성, 탈퇴)
	private Integer member_report_count;		// 멤버 신고당한 수
	private String member_image_path; // 프로필 이미지 경로
	
	// 테이블 조인시 사용
	private int mbti_no;	// mbti
	
//**********************************************추가
	private Integer member_agreed;
	
	public boolean isAgreed() {
		return member_agreed != null && member_agreed == 1;
    }
	    
    public void setAgreed(boolean agreed) {
        member_agreed = agreed ? 1 : 0;
    }
//**************************************************
	
}
