<instruction>
{sido}에 위치한 관광지를 기반으로 여행 코스를 작성해줘.
0. 해당 지역의 관광지 정보를 10개 반환해줘. 반드시 10개를 제공해야 하며, 데이터가 부족한 경우에는 검색한 지역 인접 구역(예: 반경 10km)의 관광지 데이터를 포함해 총 10개를 만들어줘
1. 관광지 : name,
2. 관광타입 : contentTypeId로 하고 값의 경우 (12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점) 예시의 숫자로 해줘
3. 관련 이미지를 검색하여 대표 이미지의 실제 url도 넣어 주면 좋을거 같아 : imageUrl.
4. 주소 : address
5. 관광지 위치의 위도 : latitude, 경도 : longitude
6. 관광지의 중심좌표의 위도 : centerLatitude, 경도 : centerLongitude
7. 정확한 html 마크 업 언어로 답변 부탁해.
</instruction>
<example>
<div>
     <h2>여행 계획</h2>
     <div class="plan">
         <h3>중심 좌표</h3>
         <p><strong>위도</strong>: 35.2587</p>
         <p><strong>경도</strong>: 129.1614</p>

         <h3>명소</h3>
         <ul>
             <li>
                 <h4><strong>해운대 해수욕장</strong></h4>
                 <p><strong>컨텐츠 타입 ID:</strong> 12</p>
                 <p><strong>주소:</strong> 부산광역시 해운대구 해운대해변로 264</p>
                 <p><strong>좌표:</strong> (위도: 35.1587, 경도: 129.1604)</p>
                 <img src="https://example.com/image_haeundae.jpg" alt="해운대 해수욕장 이미지" width="200">
             </li>
             <li>
                 <h4><strong>해운대 미포철길</strong></h4>
                 <p><strong>컨텐츠 타입 ID:</strong> 25</p>
                 <p><strong>주소:</strong> 부산광역시 해운대구 중동 804</p>
                 <p><strong>좌표:</strong> (위도: 35.1614, 경도: 129.1707)</p>
                 <img src="https://example.com/image_mipo.jpg" alt="해운대 미포철길 이미지" width="200">
             </li>
             <li>
                 <h4><strong>동백섬 누리마루 APEC 하우스</strong></h4>
                 <p><strong>컨텐츠 타입 ID:</strong> 12</p>
                 <p><strong>주소:</strong> 부산광역시 해운대구 동백로 116</p>
                 <p><strong>좌표:</strong> (위도: 35.1532, 경도: 129.1451)</p>
                 <img src="https://example.com/image_apec.jpg" alt="동백섬 누리마루 APEC 하우스 이미지" width="200">
             </li>
             <li>
                 <h4><strong>해운대 암소갈비</strong></h4>
                 <p><strong>컨텐츠 타입 ID:</strong> 39</p>
                 <p><strong>주소:</strong> 부산광역시 해운대구 구남로 32</p>
                 <p><strong>좌표:</strong> (위도: 35.1636, 경도: 129.1628)</p>
                 <img src="https://example.com/image_amsogalbi.jpg" alt="해운대 암소갈비 이미지" width="200">
             </li>
             <li>
                 <h4><strong>파라다이스 호텔 부산</strong></h4>
                 <p><strong>컨텐츠 타입 ID:</strong> 32</p>
                 <p><strong>주소:</strong> 부산광역시 해운대구 해운대해변로 296</p>
                 <p><strong>좌표:</strong> (위도: 35.1595, 경도: 129.1631)</p>
                 <img src="https://example.com/image_paradise.jpg" alt="파라다이스 호텔 부산 이미지" width="200">
             </li>
         </ul>
     </div>
 </div>

</example>