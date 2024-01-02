package com.movie.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.domain.MemberVO;
import com.movie.domain.VisitVO;
import com.movie.persistence.FollowMapper;
import com.movie.persistence.MemberMapper;



@Service
public class MemberServiceImpl implements MemberService{

	// 생성자 자동주입
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private FollowMapper followMapper;
	
	
	@Override
	public void addMember(MemberVO member) {
		// 회원 등록 처리
		memberMapper.insertMember(member); 
	}
	
	// 로그인 처리 
	@Override
	public MemberVO check(MemberVO member) {
		
		// memberCheck가 null이면 email, pw 불일치 
		// 일치하면 해당 회원 정보 VO 체워짐 
		MemberVO memberCheck = memberMapper.check(member);
		
		return memberCheck;
	}
	
	// mail 중복 확인
	@Override
	public boolean mailAvail(String email) {
		boolean result = false;
		MemberVO findMail = memberMapper.getMember(email);
		// findMail == null : DB에 해당하는 id 없음 -> result = true리턴
		// findMail != null : DB에 해당하는 id 있음 -> result = false리턴
		if(findMail == null) {
			result = true;
		}
		return result;
	}

	// 비번 찾기
	@Override
	public void updatePw(MemberVO member) {
		memberMapper.updatePw(member);
	}
	
	// 회원 닉네임으로 일치하는 회원정보 가져오기
	@Override
	    public List<MemberVO> getMemberByNickname(String nickname) {
	        return memberMapper.getMemberByNickname(nickname);
	    }
	

	@Override
	public MemberVO getMember(String member_email) {
		
		return memberMapper.getMember(member_email);
	}

	@Override
	public boolean idPwCheck(MemberVO member) {
		boolean result = false;
		// 로그인 처리: DB에서 확인
	    // 아이디와 비밀번호가 일치하는 경우 checked에 회원 정보가 반환됨
	    MemberVO checked = memberMapper.idPwCheck(member);
	    if (checked != null) { // 아이디와 비밀번호가 일치하는 경우
	        result = true; // 결과를 true로 변경
	    }
	    
	    return result; // 결과 반환
	}
	
	@Override
	public void updateMember(MemberVO member) {
	    memberMapper.updateMember(member);
	}


	// 회원 정보 탈퇴 처리
	@Override
	public boolean deleteMember(MemberVO member) {
		  try {
	            // 회원 정보 탈퇴 로직 구현
	            memberMapper.deleteMember(member);
	            return true; // 삭제 성공
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false; // 삭제 실패
	        }
	}
	
	// 방명록 글저장
	@Override
	public void saveVisit(VisitVO visit) {
		memberMapper.insertVisit(visit);
	}

	// 마이페이지 방명록 리스트 가져오기
	@Override
	public List<VisitVO> getVisitList(long member_no) {
		return memberMapper.getVisitList(member_no);
	}

	// 방명록 답글저장
	@Override
	public void saveVisitReply(VisitVO visit) {
		memberMapper.insertVisitReply(visit);
	}

	// 방명록 답글리스트 가져오기
	@Override
	public List<VisitVO> getVisitReplyList(long visit_no) {
		return memberMapper.getVisitReplyList(visit_no);
	}

	// 방명록 답글 삭제
	@Override
	public void deleteVisitReply(Long visit_no) {
		memberMapper.deleteVisitReply(visit_no);
	}
	
	// 방명록 수정 처리
	@Override
	public void updateVisitContent(Long visit_no, String content) {
		memberMapper.updateVisitContent(visit_no, content);
	}

	// 방명록 삭제 처리
	@Override
	public void deleteVisit(Long visit_no) {
		memberMapper.deleteVisit(visit_no);
		
	}

	// 이미지 업로드
	@Override
	public void updateMemberImagePath(long member_no, String imagePath) {
		memberMapper.updateMemberImagePath(member_no, imagePath);
	}

	// 회원 고유번호로 일치하는 회원정보 가져오기
	@Override
	public MemberVO getMemberByNo(Long member_no) {
		return memberMapper.getMemberByNo(member_no);
	}
	
	// 회원의 찜한 영화 개수
	@Override
	public int movieWantCount(Long member_no) {
		return memberMapper.movieWantCount(member_no);
	}
	
	// 회원의 좋아요 누른 리뷰 개수
	@Override
	public int reviewLikeCount(Long member_no) {
		return memberMapper.reviewLikeCount(member_no);
	}
	
	// 회원의 작성 리뷰 개수
	@Override
	public int writtenReviewCount(Long member_no) {
		return memberMapper.writtenReviewCount(member_no);
	}
	
	// 회원의 작성 리뷰 평균 별점
	@Override
	public double reviewStarAvg(Long member_no) {
		return memberMapper.reviewStarAvg(member_no);
	}

	// 가져온 mbti 유형별 분류하고 카운트
	@Override
	public Map<String, Integer> getMbtiCounts() {
		Map<String, Integer> mbtiCounts = new HashMap<>();
        List<String> allMbtiTypes = memberMapper.getAllMemberMbtiTypes(); // MemberMapper 사용
     // 모든 MBTI 카운트를 0으로 초기화.
        mbtiCounts.put("IJ", 0);
        mbtiCounts.put("IP", 0);
        mbtiCounts.put("EP", 0);
        mbtiCounts.put("EJ", 0);
        mbtiCounts.put("no", 0);
        // 가져온 MBTI 정보를 기반으로 카운트를 계산.
        for (String mbtiType : allMbtiTypes) {
            switch (mbtiType) {
                case "istj": case "isfj": case "infj": case "intj":
                    mbtiCounts.put("IJ", mbtiCounts.get("IJ") + 1);
                    break;
                case "istp": case "isfp": case "infp": case "intp":
                    mbtiCounts.put("IP", mbtiCounts.get("IP") + 1);
                    break;
                case "estp": case "esfp": case "enfp": case "entp":
                    mbtiCounts.put("EP", mbtiCounts.get("EP") + 1);
                    break;
                case "estj": case "esfj": case "enfj": case "entj":
                    mbtiCounts.put("EJ", mbtiCounts.get("EJ") + 1);
                    break;
                default:
                    mbtiCounts.put("no", mbtiCounts.get("no") + 1);
            }
        }
		return mbtiCounts;
	}


		
	

	

}
