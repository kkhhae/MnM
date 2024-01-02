package com.movie.domain;

import lombok.Data;

@Data
public class MovieListVO {

	private Long movie_no;			//영화 시퀀스 넘버
	private String movie_title;		//영화 제목	
	private Long movie_readcount;	//조회수
	
	
}
