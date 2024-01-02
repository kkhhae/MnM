package com.movie.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewPager {

	private int pageNum; 	// 현재 보는 페이지
	private int listSize;	// 한페이지에 보여줄 리뷰 개수
	
	private String email;	// 해당 유저의 이메일
	private Long movie_no;	// 해당 영화의 고유번호
	private int listCheck;
	// 리스트 유형 : {1 == likelist}, {2 == movie/content} {3 == movie/home} else {reviewlist}
	
	public ReviewPager() {
		this(1, 20); // 1페이지 20개씩 으로 초기화
	}
	
	public ReviewPager(int pageNum, int listSize) {
		this.pageNum = pageNum; 
		this.listSize = listSize;
	}
	
	// 위 변수=값 들을 쿼리스트링으로 변환해주는 메서드 
	public String getParameterQuery() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("pageNum", this.pageNum)
				.queryParam("listSize", this.listSize)
				.queryParam("email", this.email.replace(",", ""))
				.queryParam("movie_no", this.movie_no)
				.queryParam("listCheck", this.listCheck)
				;
		System.out.println("pageInfo uri query string " + builder.toUriString());
		// ?pageNum=1&listSize=20&email=&movie_no=&listCheck=
		
		return builder.toUriString();
	}
	
	
	
}
