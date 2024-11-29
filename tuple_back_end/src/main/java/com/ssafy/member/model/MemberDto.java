package com.ssafy.member.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDto {
	private int id;
	private String email;
	private String password;
	private String phone;
	private String role;
	private String nickname;
	private int age;
	private String gender;
	private String profile_img;
	private String description;
	private String delete_time;
	private String created_time;
	private String updated_time;
	private String access_token;
	private String refresh_token;
	private String name;
	private int isAuthorized;
}
