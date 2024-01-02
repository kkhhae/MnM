package com.movie.domain;

import lombok.Data;

@Data
public class GenreVO {
	
	private Integer genre_no;			//장르 고유번호
	private String movie_genre_no;
	private String movie_genre_name;	//장르 이름
}
