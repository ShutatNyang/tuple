package com.ssafy.member.model.service;

import java.sql.SQLException;
import java.util.Map;

import com.ssafy.member.model.MemberDto;

public interface MemberService {

	MemberDto getInfo(String token) throws Exception;

	int regist(MemberDto member) throws SQLException;

	Boolean idCheck(String email) throws SQLException;

	Map<String, String> login(Map<String, String> map) throws SQLException;

	int update(MemberDto member, String authorizationHeader) throws SQLException;

	int delete(String email, String authorizationHeader) throws SQLException;

	String refreshAccessToken(String refreshToken);

	void invalidateRefreshToken(String email);

	void saveRefreshToken(String email, String refreshToken) throws SQLException;

	String generateAndSetNewPassword(String email, String userName) throws SQLException;


	String getKakaoAccessToken(String authorizationCode) throws Exception;	

	Map<String, String> getKakaoUserInfo(String accessToken) throws Exception;

	boolean registerKakaoUser(Map<String, String> userInfo) throws SQLException;

	String findEmail(String name, String phone) throws Exception;

	String getEmailFromToken(String token) throws Exception;
}
