package com.movie.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.domain.ReplyPageDTO;
import com.movie.domain.ReplyVO;
import com.movie.domain.ReviewPager;
import com.movie.service.ReplyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 댓글 컨트롤러
@RestController
@RequestMapping("/replies/*")
@RequiredArgsConstructor
@Slf4j
public class ReplyController {

	// 생성자 주입받기
	private final ReplyService replyService;

	@PostMapping(value="new", consumes = "application/json",
			produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> register(@RequestBody ReplyVO reply) {
		log.info("======== replies new POST");
		log.info("reply : {}", reply);

		int result = -1;
		// 새글등록 : ReplyVO reply의 reply_no == null
		if(reply.getReply_no() == null) {
			result = replyService.insert(reply);
			log.info("new reply result : {}", result);
		} else {
			// 댓글의 답글 : ReplyVO reply의 reply_no != null
			result = replyService.addReReply(reply);
			log.info("add reeply result : {}", result);
		}

		return result == 1 ?
				new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 댓글 목록 요청 + 페이징처리
	@GetMapping(value="list/{review_no}/{page}",
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ReplyPageDTO> getList(
			@PathVariable Long review_no,
			@PathVariable int page) {
		log.info("======== replies list/{review_no}/{page} GET");
		log.info("review_no : {}", review_no);
		log.info("page : {}", page);

		ReviewPager pager = new ReviewPager(page, 10);
		log.info("getList pager : {}", pager);

		return new ResponseEntity<>(replyService.getListWithPaging(review_no, pager),
				HttpStatus.OK);
	}

	// 댓글 삭제
	@DeleteMapping(value="{reply_no}")
	public ResponseEntity<String> delete(@PathVariable Long reply_no) {
		log.info("======== replies {reply_no} DELETE");
		log.info("reply_no : {} ", reply_no);

		return replyService.delete(reply_no) == 1 ?
				new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 댓글 수정
	@PutMapping(value="{reply_no}")
	public ResponseEntity<String> modify(@RequestBody ReplyVO reply,
										 @PathVariable Long reply_no) {
		log.info("======== replies {reply_no} PUT");
		log.info("reply_no : {} ", reply_no);
		log.info("reply : {} ", reply);

		int result = 0;
		ReplyVO newReply = reply;
		result = replyService.updateReply(newReply);

		return result == 1 ?
				new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
