package com.movie.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.movie.domain.MemberVO;
import com.movie.domain.VisitVO;

@Mapper
public interface MemberMapper {

	// CRUD 기반 처리 + 기타기능
	
	// 회원가입 처리 (Create)
	public void insertMember(MemberVO member); 
	
	// 회원 로그인 (email, pw, 확인) 
	// 회원 email 중복 확인 =>
	// MemberVO 리턴타입
	public MemberVO check(MemberVO member);
	
	// 비밀번호 수정 (Update)
	public void updatePw(MemberVO member);
	
	// 회원 한명 정보 가져오기 
	public MemberVO getMember(String member_email);
		
	// 닉네임으로 회원정보 가져오기
	List<MemberVO> getMemberByNickname(String nickname);
	
	// 수정된 회원 정보 업데이트 하기
	public void updateMember(MemberVO member);

	// 정보 수정시 비번 일치 여부 확인
	MemberVO idPwCheck(MemberVO member);
	
	// 회원 탈퇴 처리
	public void deleteMember(MemberVO member);
	
	// 방명록 저장
	public void insertVisit(VisitVO visit);
	
	// 방명록 리스트 가져오기
    List<VisitVO> getVisitList(long member_no);
    
    // 방명록 답글 저장
    public void insertVisitReply(VisitVO visit);
    
    // 방명록 답글 리스트 가져오기
    List<VisitVO> getVisitReplyList(long visit_no);
    
    // 방명록 답글 삭제
    public void deleteVisitReply(Long visit_no);
    
    // 방명록 내용 수정
	// 넘어오는 값 2개 이상일때는 @Param 어노테이션을 사용하여 MyBatis가 SQL 쿼리와 매개변수를 정확하게 매핑할수 있도록
    // @param 어노테이션을 지정하고 어노테이션에 지정한 이름으로 xml에서 사용
	// visit_no와 content 두 개의 매개변수를 사용하며, 이들은 MyBatis XML 매퍼에서도 동일한 이름으로 사용
    public void updateVisitContent(@Param("visit_no")Long visit_no, @Param("content")String content);
    
    // 방명록 내용 삭제
    public void deleteVisit(Long visit_no);
    
    // 이미지 업로드
    public void updateMemberImagePath(@Param("member_no") long member_no, @Param("imagePath") String imagePath);
    
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
	
	// 모든 유저의 mbti정보 가져오기
	public List<String> getAllMemberMbtiTypes();
	
}
