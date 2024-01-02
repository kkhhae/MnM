package com.movie.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.movie.domain.ReplyVO;
import com.movie.domain.ReviewPager;

// 댓글 Mapper
@Mapper
public interface ReplyMapper {

	// 신규 댓글 저장 
	public int insert(ReplyVO reply);
	
	// 댓글의답글 reply_step 수정 
	public int upadateStep(ReplyVO reply);
	
	// 댓글의 답글 저장 
	public int addReReply(ReplyVO reply);
	
	// 댓글 한개 조회 
	public ReplyVO getReply(Long reply_no);
	
	// 댓글 삭제 
	public int delete(Long reply_no);
	
	// 본문 글에 해당하는 모든 댓글 삭제 
	public int deleteReplies(Long review_no);
	
	// 댓글 목록 조회 (+ 페이징처리) 
	public List<ReplyVO> getListWithPaging(
			@Param("review_no") Long review_no, 
			@Param("pager") ReviewPager pager);
	
	// 댓글 전체 개수 
	public int getReplyCount(Long review_no);
	
	// 댓글 수정
	public int updateReply(ReplyVO newReply);
	
}
