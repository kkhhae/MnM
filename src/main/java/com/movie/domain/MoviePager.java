package com.movie.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public class MoviePager {
	
		//페이지 번호
		private final int PAGENUM_SIZE = 12; 
		
		private int startPage; 	//시작페이지번호
		private int endPage;	//마지막페이지 번호
		private boolean prev,next;	//이전, 다음 보여줄 지 여부
		
		private int total;	//전체 글의 개수
		private MoviePagerDTO pager;	//moviePager에 넣었던 변수호출용
		
//		public MoviePager(MoviePagerDTO pager) {
//			this.pager = pager;
//		}
		
		public MoviePager(MoviePagerDTO pager, int total)	{
			this.pager = pager;
			this.total = total;

			//끝, 시작 페이지 설정
			this.endPage = (int)(Math.ceil( pager.getPageNum() /(double)PAGENUM_SIZE)) * PAGENUM_SIZE;
			this.startPage = this.endPage - (PAGENUM_SIZE-1);
			
			int realEnd = (int)(Math.ceil((double)total/pager.getListSize()));
			
			//마지막페이지는 실제 마지막 페이지보다 크게, 넘어가면 그 페이지가 실제 마지막페이지로
			if(this.endPage>realEnd) {
				this.endPage = realEnd;
			}
			
			//페이지 앞 뒤로 이동하기
			this.prev = this.startPage > 1;
			this.next = this.endPage < realEnd;
			
			log.info("startPage : {}",startPage);
			log.info("endPage : {}", endPage);
			log.info("realEnd : {}",realEnd);
			log.info("prev : {}",prev);
			log.info("next: {}",next);
			log.info("pageInfo: {}", pager);
			
		}
		
		
}
