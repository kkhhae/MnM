package com.movie.domain;

import lombok.Data;

@Data
public class OttVO {

	
	private Long movie_ott_no;	//ott목록 고유번호
	private Long movie_no;		//해당영화
	private String ott;			//ott 이름 ex)쿠팡
	private String ott_link;	//ott 연결 링크
	
}
