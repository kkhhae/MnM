package com.movie.domain;

import java.util.List;

import lombok.Data;

@Data
public class MovieVO {

	private Long movie_no;			//영화 시퀀스 넘버
	private String movie_title;		//영화 제목
	private Long movie_date;		//영화 개봉연도
	private String movie_genre;		//영화 장르	
	private String movie_summary;	//영화 줄거리
	private String movie_actor;		//영화배우(,나열)
	private String movie_director;	//영화감동(,나열)
	private Long movie_wantcount;	//찜한 수
	private Long movie_readcount;	//조회수 (보류)
	private String movie_link;		//영화포스터 링크 
	
	private String movie_ott;		//영화 ott 이름
	private String movie_ott_link;	//영화 ott 링크
	
	private int movie_rnum;				//영화페이지 조회수

	private List<String> genreNames;	//장르 리스트 가져오기 , 장르숫자 -> 장르명 변환용
	
	private ReviewVO review;    	//영화의 리뷰 별점 관련 서비스를 위한 생성자 처리
}
