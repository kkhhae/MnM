package com.movie.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//페이징 처리하기
//1페이지 당 10항목
//검색기능 추가
@Getter
@Setter
@ToString
public class MoviePagerDTO {
	
	//페이징 처리할때 쓸 변수들
	private int pageNum;	//페이지번호
	private int listSize;	//한페이지 게시글 수
	
	//검색기능때 쓸 변수들
	private String sel;		//검색 조건 설정
	private String keyword;	//검색 키워드 설정
	
	
	public MoviePagerDTO() {
		this(1,12);		//1페이지 당 12항목 설정
	}
	
	//설정
	public MoviePagerDTO(int pageNum, int listSize) {
		this.pageNum = pageNum;
		this.listSize = listSize;
	}
	

	//검색기능 추가
	public String[] getSelArr() { //Mapper.xml -> selArr
		return sel == null ? new String[] {} : sel.split("");
	}
	
	//#16 각 변수의 이름과 값을 쿼리스트링 형태로 변환해서 리턴해주는 메서드
	public String getParameterToQueryString() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("pageNum", this.pageNum)
				.queryParam("listSize", this.listSize)
				.queryParam("sel", this.sel)
				.queryParam("keyword", this.keyword);
		
		
		System.out.println("uri String : "+ builder.toUriString());

		//문자열로 리턴 : ?pageNum = 1&listSize=10
		return builder.toUriString();
	}
}
