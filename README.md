# 제공 API  

## 1. Keyword 블로그 조회  
- 키워드를 통해서 블로그를 검색하여 제공하는 API   
   
## Request 

### URL : /blogs?keyword=&sort=&page=&size=
| 파라미터명 | 필수여부 | 기본값 | 설명 | 
|---|---|---|---| 
| keyword | Y |  | 검색 키워드. ex) 블로그 검색 |
| sort | N | ACCURACY | 정렬 방식. ACCURACY(정확도순), RECENCY(최신순) |
| page | N | 1 | 요청 페이지. 1 ~ 50 사이만 가능 |
| size | N | 10 | 페이지 당 블로그 수. 1 ~ 50 사이만 가능 |   

## Response  

### 200 OK 
| 키 | 타입 | 설명 | 
|---|---|---|
| blogs | Array | 블로그 목록 |
| has_next | Boolean | 다음 페이지 존재 여부 | 

#### blogs 상세
| 키 | 타입 | 설명 | 
|---|---|---|
| title | String | 블로그 제목 | 
| contents | String | 블로그 내용 | 
| url | String | 블로그 주소 | 
| blogname | String | 블로그명 |
| thumbnail | String | 블로그 썸네일 주소 | 
| datetime | String | 블로그 작성 일시 |  
   
### 400 Bad Request, 500 Internal Server Error   
| 키 | 타입 | 설명 | 
|---|---|---|
| timestamp | String | 응답 일시 | 
| message | String | 오류 메세지 | 
| details | Array | 오류 상세 메세지 |

## 2. 블로그 API 제공기관 조회    
- 블로그 API를 제공하고 있는 기관을 조회하는 API     

## Request  

### URL : /blogs/provider

## Response  

### 200 OK    
| 키 | 타입 | 설명 | 
|---|---|---|
| current_provider | String | API 제공자 ex) KAKAO, NAVER |
| timestamp | String | 응답 일시 | 

## 3. 인기 Keyword 조회
- 검색에 사용된 인기 키워드를 제공(최대 10개)    

## Request  

### URL : /keywoads

## Response   

| 키 | 타입 | 설명 | 
|---|---|---|
| keywords | Array | 키워드 정보 목록 |
| timestamp | String | 응답 일시 | 

#### keywords 상세   
| 키 | 타입 | 설명 | 
|---|---|---|
| keyword | String | 키워드 | 
| search_count | Number | 검색 요청 건수 | 
