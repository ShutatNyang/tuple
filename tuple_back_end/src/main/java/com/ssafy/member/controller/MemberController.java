package com.ssafy.member.controller;

import com.ssafy.board.model.service.BoardService;
import com.ssafy.comment.model.service.CommentService;
import com.ssafy.member.model.MemberDto;
import com.ssafy.member.model.service.MemberService;
import com.ssafy.util.JwtUtil;
import com.ssafy.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/member")
@Tag(name = "MemberController", description = "로그인, 로그아웃, 회원가입, 회원정보수정, 회원탈퇴 기능 처리")
public class MemberController {

	private final MemberService memberService;
	private final BoardService boardService;
	private final CommentService commentService;

	@Autowired
	public MemberController(MemberService memberService, BoardService boardService, CommentService commentService) {
		this.memberService = memberService;
		this.boardService = boardService;
		this.commentService = commentService;
	}

	@PostMapping("/getMemberInfo")
	@Operation(summary = "회원정보조회", description = "JWT를 통해 회원의 정보를 조회합니다.")
	public ResponseEntity<?> getMemberInfo(@RequestHeader("Authorization") String token){

		token = token.replace("Bearer ", "");
		if(token.trim().isEmpty()){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		if (!JwtUtil.validateToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token is invalid or expired");
		}

		try{
			MemberDto memberDto = memberService.getInfo(token);

			if(memberDto == null){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			return ResponseEntity.ok(memberDto);

		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/login")
	@Operation(summary = "로그인", description = "유저의 로그인 정보를 인증하고 성공 시 JWT를 발급합니다.")
	public ResponseEntity<?> login(@RequestParam Map<String, String> map, HttpServletResponse response) {
		try {
			Map<String, String> tokens = memberService.login(map);

			if (tokens != null) {
				if ("on".equals(map.get("remember"))) {
					Cookie rememberCookie = new Cookie("rememberId", map.get("email"));
					rememberCookie.setMaxAge(60 * 60 * 24 * 365 * 29);
					response.addCookie(rememberCookie);
				}
				return ResponseEntity.ok(tokens);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
			}
		}
		catch (SQLException e) {
			log.error("로그인 중 오류 발생", e);
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/logout")
	@Operation(summary = "로그아웃", description = "유저의 Refresh Token을 무효화합니다.")
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken) {
		try {
			memberService.invalidateRefreshToken(accessToken);
			return ResponseEntity.ok("로그아웃 성공");
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 요청입니다.");
		}
	}

	@PostMapping("/regist")
	@Operation(summary = "회원가입", description = "새로운 유저를 등록하고 회원가입 절차를 완료합니다.")
	public ResponseEntity<?> regist(@ModelAttribute MemberDto memberDto) {
		try {
			int cnt = memberService.regist(memberDto);

			log.debug("회원가입 TABLE ID : " + memberDto.getId());
			if (cnt == 1) {
				return ResponseEntity.ok(memberDto.getId());
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@PostMapping("/regist/oauth2/kakao")
	@Operation(summary = "카카오 회원가입", description = "카카오 OAuth를 이용하여 회원가입을 처리합니다.")
	public ResponseEntity<?> kakaoSignup(@RequestParam("code") String code) {
		try {

			// 1. Kakao Access Token 가져오기
			String accessToken = memberService.getKakaoAccessToken(code);

			// 2. Kakao 사용자 정보 가져오기
			Map<String, String> userInfo = memberService.getKakaoUserInfo(accessToken);

			// 3. 회원가입 처리
			boolean isRegistered = memberService.registerKakaoUser(userInfo);

			if (isRegistered) {
				return ResponseEntity.ok("회원가입 성공");
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 사용자입니다.");
			}
		} catch (Exception e) {
			log.error("카카오 회원가입 실패: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 처리 중 오류 발생");
		}
	}




	@PostMapping("/modify")
	@Operation(summary = "회원 정보 수정", description = "바꾸고자하는 유저의 정보를 입력받아 수정합니다.")
	public ResponseEntity<?> update(@RequestHeader("Authorization") String authorizationHeader, @RequestBody MemberDto memberDto) {
		try {
			memberService.update(memberDto, authorizationHeader);
			return ResponseEntity.ok("회원 정보가 수정되었습니다.");
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		} catch (SQLException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@PostMapping("/delete")
	@Operation(summary = "회원탈퇴", description = "유저의 정보를 영구적으로 삭제합니다.")
	public ResponseEntity<?> delete(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(value = "email") String email) {
		try {
			memberService.delete(email, authorizationHeader);
			return ResponseEntity.ok("회원 탈퇴 성공");
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		} catch (SQLException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/idCheck/{email}")
	@Operation(summary = "중복 아이디 확인", description = "유저의 회원 가입 시 같은 아이디가 있는지 확인합니다.")
	public ResponseEntity<?> idCheck(@PathVariable(value = "email") String email) {
		try {
			Map<String, Boolean> map = new HashMap<>();
			map.put("bool", memberService.idCheck(email));

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
			return ResponseEntity.ok().headers(headers).body(map);
		} catch (SQLException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/passwordCheck")
	@Operation(summary = "비밀번호 확인", description = "유저의 개인 정보 확인 시 비밀번호를 확인합니다.")
	public ResponseEntity<?> passwordCheck(HttpSession httpSession, @RequestParam(value = "password") String userPassword) {
		MemberDto loginUser = (MemberDto) httpSession.getAttribute("loginUser");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
		return ResponseEntity.ok().headers(headers).body(PasswordUtil.checkPassword(userPassword, loginUser.getPassword()));
	}

	@PostMapping("/newPassword")
	@Operation(summary = "임시 비밀번호 발급", description = "알파벳 대/소문자, 숫자로 이루어진 8자리의 새로운 임시 비밀번호를 발급합니다.")
	public ResponseEntity<?> newPassword(@RequestParam Map<String, String> map) {
		String email = map.get("email");
		String userName = map.get("userName");

		try {
			String newPassword = memberService.generateAndSetNewPassword(email, userName); // 서비스 호출로 비밀번호 생성 및 저장

			if (newPassword != null) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
				return ResponseEntity.ok().headers(headers).body(newPassword);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 정보를 찾을 수 없습니다.");
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/refresh")
	@Operation(summary = "Access Token 갱신", description = "유효한 Refresh Token을 사용하여 새로운 Access Token을 발급합니다.")
	public ResponseEntity<?> refreshAccessToken(@RequestHeader("refreshToken") String refreshToken) {
		try {
			String newAccessToken = memberService.refreshAccessToken(refreshToken);
			return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 Refresh Token입니다.");
		}
	}


	@PostMapping("/find-email")
	@Operation(summary = "이메일 찾기", description = "이름과 전화번호로 이메일을 찾습니다.")
	public ResponseEntity<?> findEmail(@RequestParam("name") String name, @RequestParam("phone") String phone) {
		try {
			String email = memberService.findEmail(name, phone);
			if(email.isEmpty()){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
			}
			return ResponseEntity.ok(email);
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
		}
	}

	@PostMapping("/getEmailFromToken")
	@Operation(summary = "토큰에서 이메일 추출", description = "AccessToken에서 이메일을 파싱해 반환합니다.")
	public ResponseEntity<?> getEmailFromToken(@RequestHeader("accessToken") String token) {
		try {
			String email = memberService.getEmailFromToken(token);

			if(email.isEmpty()){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
			}
			return ResponseEntity.ok(email);
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
		}
	}

	@PostMapping("/getMemberIdFromEmail")
	@Operation(summary = "이메일로 회원 아이디 찾기", description = "이메일로 회원 아이디 찾아 반환합니다.")
	public ResponseEntity<?> getMemberIdFromEmail(@RequestParam("email") String email) {
		try {
			int id = boardService.getMemberIdByEmail(email);

			return ResponseEntity.ok(id);
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
		}
	}


	@GetMapping("/getEmailByMemberId")
	public ResponseEntity<String> getEmailByMemberId(@RequestParam("ownerId") int id) {
		try {
			String email = commentService.getEmailByMemberId(id);
			return ResponseEntity.ok(email);
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
		}
	}
}
