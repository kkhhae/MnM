package com.movie.service;

import java.util.List;

import com.movie.domain.FollowVO;
import com.movie.domain.MemberVO;

public interface FollowService {
	
	// 다른사람 팔로우하는 기능을 수행
 	boolean followUser(FollowVO followVO);
 	
 	// 팔로우 취소하는 기능
    void unfollowUser(FollowVO followVO);
    
    
    // 나를 팔로우 중인 목록
    List<MemberVO> getFollowerList(MemberVO memberVO);

    
    // 내가 팔로우중인 목록
    List<MemberVO> getFollowingList(MemberVO memberVO);
    
    
    
    
    
    
    
    
    // 검색한 사람들을 내가 팔로우 하는지/안하는지 결과 목록 가져오기 
    List<Integer> getFollowingStatus(long follower, List<MemberVO> findList);
    
    // 1:1로 팔로우 여부 조회 
    int getFollowStatus(long myMemberNo, long targetNo);
	    
	// 팔로우 카운트 : 내가 팔로우 하는 사람수 
    int getFollowerCount(long follower);
    // 팔로잉 카운트 : 나를 팔로우하는 사람수 
	int getFollowingCount(long following);
}
