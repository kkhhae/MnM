package com.movie.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.domain.FollowVO;
import com.movie.domain.MemberVO;
import com.movie.persistence.FollowMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FollowServiceImpl implements FollowService {

	@Autowired
	private FollowMapper followMapper;

	// 다른사람 팔로우하는 기능을 수행
	@Override
    public boolean followUser(FollowVO followVO) {
		boolean followStatus = false; // 팔로우 안함 
		int result = followMapper.getFollowingStatus(followVO.getMember_no_follower(), followVO.getMember_no_following());
		// 팔로우 이미 했다 (result = 1) --> 팔로우 취소 
		if(result == 1) {
			followMapper.removeFollow(followVO);
		}else {
			// 팔로우 한적 없다 (result = 0) --> 팔로우 하기 
			followMapper.addFollow(followVO);
			followStatus = true; // 이제는 팔로우 한다 
		}
		return followStatus;
    }
    // 팔로우 취소하는 기능
	 @Override
	    public void unfollowUser(FollowVO followVO) {
	        followMapper.removeFollow(followVO);
	        
	    }
    // 내가 팔로우중인 목록
    @Override
    public List<MemberVO> getFollowingList(MemberVO memberVO) {
        return followMapper.getFollowingList(memberVO);
    }
    // 나를 팔로우 중인 목록
    @Override
    public List<MemberVO> getFollowerList(MemberVO memberVO) {
        return followMapper.getFollowerList(memberVO);
    }
    // 검색한 사람들을 내가 팔로우 하는지/안하는지 결과 목록 가져오기
	@Override
	public List<Integer> getFollowingStatus(long follower, List<MemberVO> findList) {
		List<Integer> result = new ArrayList<Integer>();
		for(MemberVO vo : findList) {
			int count = followMapper.getFollowingStatus(follower, vo.getMember_no());
			result.add(count);
		}
		log.info("service getFollowingStatus result : {}", result);
		return result;
	}
	// 팔로우 카운트 
	@Override
	public int getFollowerCount(long follower) { // 대상의 member_no 
		return followMapper.getFollowerCount(follower);
	}
	// 팔로우 당하는 카운트 
	@Override
	public int getFollowingCount(long following) { // 대상의 member_no 
		return followMapper.getFollowingCount(following);
	}
	@Override
	public int getFollowStatus(long myMemberNo, long targetNo) {
		
		return followMapper.getFollowingStatus(myMemberNo, targetNo);
	}
	
	
	


}
