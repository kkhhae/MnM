package com.movie.service;


import org.springframework.stereotype.Service;

import com.movie.domain.ReplyPageDTO;
import com.movie.domain.ReplyVO;
import com.movie.domain.ReviewPager;
import com.movie.persistence.ReplyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

	// 생성자로 주입받기 
	private final ReplyMapper replyMapper;

	// 신규 댓글 저장 
	@Override
	public int insert(ReplyVO reply) {
		return replyMapper.insert(reply);
	}

	// 댓글에 답글달기 
	@Override
	public int addReReply(ReplyVO reply) {
		// 기존에추가된 댓글의답글들이 있으면, 
		// 그 답글들의 reply_step을 하나씩 올려주기 
		replyMapper.upadateStep(reply);
		
		// 답글 저장 : step과 level은 넘어온값 + 1 로 수정해서 insert
		// 넘어온 step,level(답글다는 대상의 step,level값) 
		// -> 이거보다 밑에 달려야하니
		// 넘어온 값에 +1 해서 저장하다 
		reply.setReply_step(reply.getReply_step() + 1);
		reply.setReply_level(reply.getReply_level() + 1);
		return replyMapper.addReReply(reply);		
	}

	// 댓글 한개 가져오기
	@Override
	public ReplyVO getReply(Long reply_no) {
		return replyMapper.getReply(reply_no);
	}

	// 삭제
	@Override
	public int delete(Long reply_no) {
		return replyMapper.delete(reply_no);
	}

	// 리뷰고유번호에 해당하는 댓글 전체 삭제
	@Override
	public int deleteReplies(Long review_no) {
		return replyMapper.deleteReplies(review_no);
	}

	// 댓글 페이징 DTO로 리턴해주는 댓글 목록 조회 
	@Override
	public ReplyPageDTO getListWithPaging(Long review_no, ReviewPager pager) {
		// 생성자 매개변수1 : review_no에 해당하는 전체 댓글 개수 구한 값
		// 생성자 매개변수2 : review_no, pager 참고하여 댓글 목록 리스트
		return new ReplyPageDTO(replyMapper.getReplyCount(review_no), 
				replyMapper.getListWithPaging(review_no, pager));
	}

	// 댓글 개수
	@Override
	public int getReplyCount(Long review_no) {
		return replyMapper.getReplyCount(review_no);
	}

	// 댓글 수정
	@Override
	public int updateReply(ReplyVO newReply) {
		return replyMapper.updateReply(newReply);
	}
	
}
