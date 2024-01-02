package com.movie.domain;

import lombok.Data;

@Data
public class FollowResultVO {

	private boolean isFollowing; // == following 최종 내가 이사람을 팔로우 하는지 안하는지 (한다 true, 안한다 false)
	private int followerCount; // 팔로우 하는 상대의 팔로우 수 
	private int followingCount;// 팔로우 하는 상대의 팔로잉 수 
	
}
