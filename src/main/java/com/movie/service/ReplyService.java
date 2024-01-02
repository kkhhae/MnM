package com.movie.service;

import com.movie.domain.ReplyPageDTO;
import com.movie.domain.ReplyVO;
import com.movie.domain.ReviewPager;

// 댓글 서비스 인터페이스 
public interface ReplyService {

	// 댓글 신규 등록
	public int insert(ReplyVO reply);
	
	// 댓글의 답글 등록 
	public int addReReply(ReplyVO reply);
	
	// 댓글 한개 조회 
	public ReplyVO getReply(Long reply_no);
	
	// 댓글 삭제 
	public int delete(Long reply_no);
	
	// 본문에 해당하는 댓글 모두 삭제 
	public int deleteReplies(Long review_no);
	
	// 댓글 조회 + 페이징 처리 
	public ReplyPageDTO getListWithPaging(Long review_no, ReviewPager pager);
	
	// 댓글 전체 개수 
	public int getReplyCount(Long review_no);
	
	// 댓글 수정
	public int updateReply(ReplyVO newReply);
	
}
