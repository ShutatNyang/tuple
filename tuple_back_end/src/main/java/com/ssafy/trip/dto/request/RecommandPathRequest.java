package com.ssafy.trip.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommandPathRequest {
	private String startId; // Optional: 시작 ID
	private String endId;   // Optional: 끝 ID

	@NotNull
	private List<String> waypointIds; // 경유지 또는 전체 경로에 포함된 모든 지점
}
