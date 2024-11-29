package com.ssafy.member.model.mapper;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.member.model.MemberDto;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface MemberMapper {
	//멤버 정보 가져오기
	MemberDto getInfo(String email) throws Exception;

	// 회원 가입
	int regist(MemberDto member) throws SQLException;

	// 아이디 중복체크 중복 있으면 0 없으면 1
	int idCheck(String userId) throws SQLException;

	// 로그인
	MemberDto login(Map<String, String> map) throws SQLException;

	// 회원 정보 수정
	int modify(MemberDto member) throws SQLException;

	//신규비밀번호 발급으로 정보 수정
	int modifyPassword(MemberDto member) throws SQLException;

	// 회원 탈퇴
	int delete(String userId) throws SQLException;

	MemberDto findByEmail(String email) throws SQLException;

	void updateTokens(String email, String accessToken, String refreshToken);

	void updateAccessToken(String email, String newAccessToken);

	boolean registerKakaoUser(Map<String, String> userInfo) throws SQLException;




	MemberDto findOauthUser(@Param("provider") String provider, @Param("providerId") String providerId);

	int insertOauthInfo(@Param("memberId") Long memberId, @Param("provider") String provider, @Param("providerId") String providerId);


	String findEmail(String name, String phone) throws Exception;
}
