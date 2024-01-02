package com.movie.domain;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@ToString
public class ReviewPageDTO {

	private final int PAGENUM_SIZE = 10; // 한페이지에 보여줄 페이지번호의 개수 
	
	private int startPage;			// 현재 페이지의 첫번째 번호
	private int endPage; 			// 현재 페이지의 마지막 번호
	private boolean prev, next; 	// 이전, 다음
	
	private int total; 			// 리뷰의 전체 개수
	private ReviewPager pager;	// pageNum, listSize

    private List<Integer> pageNumList; // 페이지 번호 뿌릴것 미리 리스트에 담기
	
	
	public ReviewPageDTO(ReviewPager pager, int total) {
		this.pager = pager;
		this.total = total;
		
		// 마지막번호 = ((올림)(현재페이지) / 한페이지번호개수) * 한페이지번호개수
		// 					(22 / 10) = 2.2 올림 => 3
		// 					3 * 10 = 30 => endPage = 30
		this.endPage = (int)(Math.ceil((double)pager.getPageNum() / PAGENUM_SIZE)) * PAGENUM_SIZE;
		// 첫번째번호 = (마지막번호) - (한페이지번호개수 - 1)
		// 					30 - (10 - 1) = 21 = > startPage = 21
		this.startPage = this.endPage - (PAGENUM_SIZE - 1);
		// 페이지번호 = 21 ~ 30
		
		// 리스트의 마지막 번호 = (올림)((리뷰전체개수) / (한페이지리뷰개수))
		//								432 / 30 = 14.4 올림 => realEnd 15 = 총 15페이지
		int realEnd = (int)(Math.ceil((double)total / pager.getListSize()));
		if(this.endPage > realEnd) {
			this.endPage = realEnd; 
		}
		
		this.prev = this.startPage > 1;		// 첫번째번호가 1보다 클경우 
		this.next = this.endPage < realEnd;	// 마지막번호가 전체의 마지막 번호보다 작을경우

        // pageNumList 숫자 채우기
        pageNumList = IntStream.rangeClosed(startPage, endPage)
                .boxed().collect(Collectors.toList());
		
	}
	
	
}
