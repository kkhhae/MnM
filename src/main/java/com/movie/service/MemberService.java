package com.movie.service;

import java.util.List;
import java.util.Map;

import com.movie.domain.MemberVO;
import com.movie.domain.VisitVO;

//회원 프로젝트 비지니스 로직처리하는 Model, Service 인터페이스
public interface MemberService {
	
	
	// 회원가입 처리
	public void addMember(MemberVO member);
		
	// 로그인 처리
	// member정보 받아서 넘기고 boolean타입으로 리턴
	// true 면 DB에 일치하는 정보 있다-> 로그인 처리
	// false 면 DB에 일치하는 정보 x 
	public MemberVO check(MemberVO member); 
	
	// 정보수정시 비번 일치여부 체크
	public boolean idPwCheck(MemberVO member);
	
	// 비번 일치 시 변경된 정보 업데이트
	public void updateMember(MemberVO member);
	
	// email 중복 확인 
	// controller로 리턴해주는 타입이 boolean
	public boolean mailAvail(String email);
		
	// 비밀번호 수정 처리
	public void updatePw(MemberVO member);

	// 회원 정보 한개 가져오기 
	public MemberVO getMember(String member_email);
	
	// 입력값과 회원닉네임이 일치하는 회원정보 가져오기
	List<MemberVO> getMemberByNickname(String nickname);
	
	// 회원정보 탈퇴 처리
	public boolean deleteMember(MemberVO member);
	
	// 방명록 글 저장
	public void saveVisit(VisitVO visit);  //VO 데이터 담아옴 
	
	// 방명록 글 리스트 가져오기
	public List<VisitVO> getVisitList(long member_no);
	
	// 방명록 답글 저장
	public void saveVisitReply(VisitVO visit);

	// 방명록 답글 리스트 가져오기
	public List<VisitVO> getVisitReplyList(long visit_no);
	
	// 방명록 답글 삭제
	public void deleteVisitReply(Long visit_no);
	
	// 방명록 수정 처리
    public void updateVisitContent(Long visit_no, String content);

	// 방명록 삭제 처리
    public void deleteVisit(Long visit_no);
	
	// 이미지 업로드
    public void updateMemberImagePath(long member_no, String imagePath);
	
    // 고유번호로 회원정보 가져오기
    public MemberVO getMemberByNo(Long member_no);
    
    // 회원의 찜한 영화 개수
 	public int movieWantCount(Long member_no);
 	
 	// 회원의 좋아요 누른 리뷰 개수
 	public int reviewLikeCount(Long member_no);
 	
 	// 회원의 작성 리뷰 개수
 	public int writtenReviewCount(Long member_no);
 	
 	// 회원의 작성 리뷰 평균 별점
 	public double reviewStarAvg(Long member_no);
	
 	// 가입한 mbti 유형별 회원수 가져오기
  	public Map<String, Integer> getMbtiCounts();
	
}
