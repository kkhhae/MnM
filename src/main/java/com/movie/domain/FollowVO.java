package com.movie.domain;

import lombok.Data;

@Data
public class FollowVO {
	
	    private long relation_no;  // 릴레이션 번호
	    private long member_no_following;  // 팔로잉 멤버 고유번호 (상대)
	    private long member_no_follower;  // 팔로워 멤버 고유번호(나)
	    
	

}
