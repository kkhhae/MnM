package com.movie.domain;

import lombok.Data;

@Data
public class MovieWantVO {

	private Long movieWant_no;	// PK
	private Long member_no;		// 회원 번호
	private Long movie_no;		// 영화 번호
	private Long mno;			// 찜 추가 시 +1
	
}
