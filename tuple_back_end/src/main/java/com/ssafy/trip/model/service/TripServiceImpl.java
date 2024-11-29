package com.ssafy.trip.model.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.trip.dto.response.AttractionDTO;
import com.ssafy.trip.dto.response.ContentTypeDto;
import com.ssafy.trip.dto.response.GugunsDto;
import com.ssafy.trip.dto.response.SidosDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.trip.dto.request.TripListRequest;
import com.ssafy.trip.model.TripDto;
import com.ssafy.trip.model.mapper.TripMapper;
import com.ssafy.util.BoardSize;
import com.ssafy.util.PageNavigation;
import com.ssafy.util.Trie;

@Service
public class TripServiceImpl implements TripService {

    private TripMapper tripMapper;
    
    @Autowired
    public TripServiceImpl(TripMapper tripMapper) {
    	this.tripMapper = tripMapper;
    }

    @Override
    public List<TripDto> searchListAreaInfo() {
        return tripMapper.selectListAreaInfo();
    }

    @Override
    public List<TripDto> searchTripList(TripListRequest tripListRequest){
    	return tripMapper.selectTripList(tripListRequest);
    }

    @Override
    public List<TripDto> searchListContentType() {
        return tripMapper.selectListContentType();
    }

    @Override
    public TripDto viewTrip(int no) {
        return tripMapper.selectTrip(no);
    }

    @Override
	public PageNavigation makePageNavigation(Map<String, String> map) throws Exception {
		PageNavigation pageNavigation = new PageNavigation();

		int listSize = BoardSize.LIST.getBoardSize();
		int navigationSize = BoardSize.NAVIGATION.getBoardSize();
		int currentPage = Integer.parseInt(map.get("pgno"));

		pageNavigation.setCurrentPage(currentPage);
		pageNavigation.setNaviSize(navigationSize);

		// Prepare parameters for querying
		Map<String, Object> param = new HashMap<>();
		String code = map.get("code");
		String type = map.get("type");
		String name = map.get("name");

		param.put("code", code);
		param.put("type", type);
		param.put("name", name);

		int totalCount = tripMapper.getTotalTripCount(param);
		pageNavigation.setTotalCount(totalCount);

		int totalPageCount = (totalCount - 1) / listSize + 1;
		pageNavigation.setTotalPageCount(totalPageCount);

		boolean startRange = currentPage <= navigationSize;
		pageNavigation.setStartRange(startRange);

		boolean endRange = (totalPageCount - 1) / navigationSize * navigationSize < currentPage;
		pageNavigation.setEndRange(endRange);

		pageNavigation.makeNavigator();

		return pageNavigation;

	}

	private Trie trie;

	@Override
	public List<String> getTrieList(Map<String, Object> map) {
		trie = new Trie();

		// map에서 keyword와
		String keyword = (String) map.get("name");

		// DB에서 해당하는 관광지 리스트를 가져옴
		List<String> attractions = tripMapper.selectListTripKeyword(map);

		// Trie에 모든 관광지 추가
		for (String attraction : attractions) {
			trie.insert(attraction.toLowerCase());
		}

		List<String> results = new ArrayList<>();
		List<String> tempResults = trie.searchByPrefix(keyword.toLowerCase());
		Collections.shuffle(tempResults);
		if (tempResults.size() > 10)
			results = tempResults.subList(0, 10);
		else
			results = tempResults;
		return results;
	}

	@Override
	public List<TripDto> getNearTrip(String latitude, String longitude) {
	    List<TripDto> list = tripMapper.selectNearTrip(latitude, longitude);

	    
	    Double curLatitude = Double.parseDouble(latitude);
	    Double curLongitude = Double.parseDouble(longitude);
	    

	    // 4개 미만이면 그대로 반환
	    if (list.size() < 4) {
	        return list;
	    }

	    // 거리 계산하여 정렬하기 위한 리스트 생성
	    List<TripDistance> attractionDistances = new ArrayList<>();
	    for (TripDto tripDto : list) {
	    	if(tripDto.getLatitude().equals(latitude) && tripDto.getLongitude().equals(longitude)){
	    		continue;
	    	}

	        double distance = calculateDistance(curLatitude, curLongitude, 
	            Double.parseDouble(tripDto.getLatitude()), 
	            Double.parseDouble(tripDto.getLongitude()));
	        attractionDistances.add(new TripDistance(tripDto, distance));
	    }

	    // 거리 기준으로 정렬
	    attractionDistances.sort(Comparator.comparingDouble(TripDistance::getDistance));

	    // 상위 4개 Attraction 추출
	    List<TripDto> result = new ArrayList<>();
	    for (int i = 0; i < 4; i++) {
	        result.add(attractionDistances.get(i).getTrip());
	    }

	    return result;
	}

	// 거리 계산 메서드
	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
	    final int R = 6371; // 지구 반지름 (km)
	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    return R * c; // 반환 단위: km
	}

	// Attraction과 거리 정보를 담는 클래스
	private static class TripDistance {
	    private TripDto tripDto;
	    private double distance;

	    public TripDistance(TripDto tripDto, double distance) {
	        this.tripDto = tripDto;
	        this.distance = distance;
	    }

	    public TripDto getTrip() {
	        return tripDto;
	    }

	    public double getDistance() {
	        return distance;
	    }
	}

	@Override
	public List<TripDto> findOptimalPath(Integer startNo, List<Integer> waypointNos, Integer endNo) throws Exception {
		// 시작/끝 지점이 없으면 처리
		if (startNo == null && !waypointNos.isEmpty()) {
			startNo = waypointNos.get(0); // 첫 번째 경유지를 시작 지점으로
		}
		if (endNo == null && !waypointNos.isEmpty()) {
			endNo = waypointNos.get(waypointNos.size() - 1); // 마지막 경유지를 도착 지점으로
		}

		// 전체 지점 리스트 구성
		List<Integer> allPoints = new ArrayList<>();
		if (startNo != null) allPoints.add(startNo);
		if (waypointNos != null) allPoints.addAll(waypointNos);
		if (endNo != null) allPoints.add(endNo);

		int n = allPoints.size();
		double[][] dist = new double[n][n];

		// 모든 지점 간 거리 계산
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				dist[i][j] = dist[j][i] = haversine(
						Double.parseDouble(tripMapper.selectTrip(allPoints.get(i)).getLatitude()),
						Double.parseDouble(tripMapper.selectTrip(allPoints.get(i)).getLongitude()),
						Double.parseDouble(tripMapper.selectTrip(allPoints.get(j)).getLatitude()),
						Double.parseDouble(tripMapper.selectTrip(allPoints.get(j)).getLongitude())
				);
			}
		}

		// DP + 비트마스킹 (외판원 문제 해결)
		double[][] dp = new double[n][(1 << n)];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < (1 << n); j++) {
				dp[i][j] = Double.MAX_VALUE;
			}
		}
		dp[0][1] = 0;

		for (int visited = 1; visited < (1 << n); visited++) {
			for (int current = 0; current < n; current++) {
				if ((visited & (1 << current)) == 0) continue;
				for (int next = 0; next < n; next++) {
					if ((visited & (1 << next)) != 0) continue;
					dp[next][visited | (1 << next)] = Math.min(
							dp[next][visited | (1 << next)],
							dp[current][visited] + dist[current][next]
					);
				}
			}
		}

		// 경로 역추적
		return reconstructPath(dp, dist, allPoints);
	}

	/**
	 * 최적 경로를 역추적하는 메서드
	 */
	private List<TripDto> reconstructPath(double[][] dp, double[][] dist, List<Integer> allPoints) throws Exception {
		int n = allPoints.size();
		List<Integer> path = new ArrayList<>();
		int finalState = (1 << n) - 1;
		int current = 0;

		path.add(allPoints.get(0)); // 시작 지점 추가

		while (finalState != 0) {
			int next = -1;
			for (int i = 0; i < n; i++) {
				if ((finalState & (1 << i)) == 0 || current == i) continue;
				if (next == -1 || dp[i][finalState] + dist[i][current] < dp[next][finalState] + dist[next][current]) {
					next = i;
				}
			}

			finalState &= ~(1 << next);
			current = next;
			if (!path.contains(allPoints.get(current))) {
				path.add(allPoints.get(current));
			}
		}

		// 경로 정제 및 반환
		List<TripDto> optimalPath = new ArrayList<>();
		for (int contentId : path) {
			TripDto attraction = tripMapper.selectTrip(contentId);
			if (attraction != null) {
				optimalPath.add(attraction);
			}
		}

		return optimalPath;
	}

	/**
	 * 하버사인 공식으로 두 지점 간 거리 계산
	 */
	private double haversine(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371; // 지구 반지름 (킬로미터)
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}

	@Override
	public List<SidosDto> getSidos() throws Exception {
		return tripMapper.getSidos();
	}

	@Override
	public List<GugunsDto> getGuguns(int sidoCode) throws Exception {
		return tripMapper.getGuguns(sidoCode);
	}

	@Override
	public List<ContentTypeDto> getContentTypes() throws Exception {
		return tripMapper.getContentTypes();
	}

	@Override
	public List<AttractionDTO> getAttractions(Integer sidoCode, Integer gugunCode, Integer contentTypeId, String keyword) throws Exception {
		return tripMapper.getAttractions(sidoCode, gugunCode, contentTypeId, keyword);
	}
}
