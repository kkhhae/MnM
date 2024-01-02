package com.movie.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.movie.domain.FollowVO;
import com.movie.domain.MemberVO;

@Mapper
public interface FollowMapper {

	 // 팔로우하기
    void addFollow(FollowVO followVO);
    
    // 언팔하기
    void removeFollow(FollowVO followVO);
    
    // 팔로우중인 리스트
    List<MemberVO> getFollowingList(MemberVO memberVO);
    
    // 나를 팔로우 하는 유저 리스트
    List<MemberVO> getFollowerList(MemberVO memberVO);
	
    
    
    // 상대가 나를 팔로우 하는지 결과를 숫자로 리턴 
    int getFollowingStatus(@Param("follower") long follower,@Param("following") long following);
	 
    int getFollowerCount(long follower);
    
    int getFollowingCount(long following);
	 
}
