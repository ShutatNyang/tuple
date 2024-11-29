package com.ssafy.member.model.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import com.ssafy.exception.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.ssafy.util.JwtUtil;
import com.ssafy.member.model.MemberDto;
import com.ssafy.member.model.mapper.MemberMapper;
import com.ssafy.util.PasswordUtil;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class MemberServiceImpl implements MemberService {

	private final MemberMapper memberMapper;

	@Autowired
	public MemberServiceImpl(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}


	@Override
	public MemberDto getInfo(String token) throws Exception {
		String emailByToken = JwtUtil.getEmailFromToken(token);
		return memberMapper.getInfo(emailByToken);
	}

	@Override
	public int regist(MemberDto memberDto) throws SQLException {
		// 필수 입력 필드 확인
		if (Objects.isNull(memberDto.getEmail()) || Objects.isNull(memberDto.getPassword())) {
			throw new IllegalArgumentException("이메일과 비밀번호는 필수 입력 항목입니다.");
		}

		String hashedPassword = PasswordUtil.hashPassword(memberDto.getPassword());
		memberDto.setPassword(hashedPassword);
		return memberMapper.regist(memberDto);
	}

	@Override
	public Boolean idCheck(String email) throws SQLException {
		if (Objects.isNull(email) || email.trim().isEmpty()) {
			throw new IllegalArgumentException("사용자 EMAIL은 필수입니다.");
		}
		return memberMapper.idCheck(email) != 0;
	}

	@Override
	public Map<String, String> login(Map<String, String> map) throws SQLException {
		MemberDto loginUser = loginInternal(map);

		if(loginUser.getIsAuthorized() == 0){
			throw new NotAuthorizedException("미인증 계정");
		}

        String accessToken = JwtUtil.generateAccessToken(loginUser.getEmail());
        String refreshToken = JwtUtil.generateRefreshToken(loginUser.getEmail());

        saveRefreshToken(loginUser.getEmail(), refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

	@Override
	public int update(MemberDto member, String authorizationHeader) throws SQLException {
		String accessToken = authorizationHeader.replace("Bearer ", "");
		String email = JwtUtil.getEmailFromToken(accessToken);

		validateOwnership(email, member.getEmail());
		return memberMapper.modify(member);
	}

	@Override
	public int delete(String email, String authorizationHeader) throws SQLException {
		String accessToken = authorizationHeader.replace("Bearer ", "");
		String loginUserEmail = JwtUtil.getEmailFromToken(accessToken);
		validateOwnership(loginUserEmail, email);
		return memberMapper.delete(email);
	}

	@Override
	public String refreshAccessToken(String refreshToken) {
		if (!JwtUtil.validateToken(refreshToken)) {
			throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
		}
		String email = JwtUtil.getEmailFromToken(refreshToken.replace("Bearer ", ""));
		return JwtUtil.generateAccessToken(email);
	}

	@Override
	public void invalidateRefreshToken(String accessToken) {
		String email = JwtUtil.getEmailFromToken(accessToken.replace("Bearer ", ""));
		memberMapper.updateTokens(email, null, null);
	}

	@Override
	public void saveRefreshToken(String email, String refreshToken) throws SQLException {
		if (Objects.isNull(email) || Objects.isNull(refreshToken)) {
			throw new IllegalArgumentException("이메일과 Refresh Token은 필수 입력 항목입니다.");
		}
		memberMapper.updateTokens(email, null, refreshToken);
	}

	@Override
	public String generateAndSetNewPassword(String email, String userName) throws SQLException {
		// 1. 임시 비밀번호 생성
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder newPassword = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			int index = random.nextInt(chars.length());
			newPassword.append(chars.charAt(index));
		}

		String password = newPassword.toString();

		// 2. 사용자 검증
		MemberDto member = memberMapper.findByEmail(email);
		if (member == null || !member.getName().equals(userName)) {
			return null; // 회원 정보가 없거나 이름이 일치하지 않음
		}

		// 3. 비밀번호 암호화 후 저장
		String hashedPassword = PasswordUtil.hashPassword(password);
		member.setPassword(hashedPassword);

		if (memberMapper.modifyPassword(member) == 1) {
			return password; // 성공 시 새 비밀번호 반환
		} else {
			throw new RuntimeException("비밀번호 변경에 실패했습니다.");
		}
	}

	@Override
	public String getKakaoAccessToken(String authorizationCode) throws Exception {

		RestTemplate restTemplate = new RestTemplate();

		// 요청 헤더와 파라미터 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "b518e823e14f5f456d68c9419e9f83a7");
		params.add("redirect_uri", "https://localhost/member/login/oauth2/kakao");
		params.add("code", authorizationCode);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		// Kakao API 호출
		ResponseEntity<Map> response = restTemplate.postForEntity(
				"https://kauth.kakao.com/oauth/token", request, Map.class
		);

		Map<String, Object> responseBody = response.getBody();

		if (responseBody != null) {
			return (String) responseBody.get("access_token");
		} else {
			throw new RuntimeException("카카오 Access Token을 가져올 수 없습니다.");
		}
	}

	@Override
	public Map<String, String> getKakaoUserInfo(String accessToken) throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		// 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);

		HttpEntity<Void> request = new HttpEntity<>(headers);

		// Kakao 사용자 정보 API 호출
		ResponseEntity<Map> response = restTemplate.exchange(
				"https://kapi.kakao.com/v2/user/me", HttpMethod.GET, request, Map.class
		);

		Map<String, Object> responseBody = response.getBody();

		// 사용자 정보 추출
		if (responseBody != null) {
			Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
			Map<String, Object> properties = (Map<String, Object>) responseBody.get("properties");

			Map<String, String> userInfo = new HashMap<>();
			userInfo.put("email", (String) kakaoAccount.get("email"));
			userInfo.put("nickname", (String) properties.get("nickname"));
			userInfo.put("profile_img", (String) properties.get("profile_image"));
			userInfo.put("provider_id", String.valueOf(responseBody.get("id")));

			return userInfo;
		} else {
			throw new RuntimeException("카카오 사용자 정보를 가져올 수 없습니다.");
		}
	}

	@Override
	public boolean registerKakaoUser(Map<String, String> userInfo) throws SQLException {
		String email = userInfo.get("email");
		String nickname = userInfo.get("nickname");

		// 1. 중복 확인
		if (memberMapper.findByEmail(email) != null) {
			throw new IllegalArgumentException("이미 등록된 사용자입니다.");
		}

		// 2. 회원가입 처리
		MemberDto newMember = new MemberDto();
		newMember.setEmail(email);
		newMember.setName(nickname); // Kakao 닉네임을 회원 이름으로 사용
		newMember.setPassword(""); // 소셜 로그인은 비밀번호 사용하지 않음

		int result = memberMapper.regist(newMember);

		return result == 1;
	}

	@Override
	public String findEmail(String name, String phone) throws Exception {
		return memberMapper.findEmail(name, phone);
	}

	@Override
	public String getEmailFromToken(String token) throws Exception {
		return JwtUtil.getEmailFromToken(token);
	}


	private MemberDto loginInternal(Map<String, String> map) throws SQLException {

		if (Objects.isNull(map.get("email")) || Objects.isNull(map.get("password"))) {
			throw new IllegalArgumentException("이메일과 비밀번호는 필수 입력 항목입니다.");
		}

		MemberDto storedMember = memberMapper.login(map);

		if (storedMember != null && PasswordUtil.checkPassword(map.get("password"), storedMember.getPassword())) {
			return storedMember;
		}
		throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
	}

	private void validateOwnership(String tokenEmail, String resourceEmail) {
		if (!tokenEmail.equals(resourceEmail)) {
			throw new RuntimeException("자신의 계정만 접근할 수 있습니다.");
		}
	}
}
