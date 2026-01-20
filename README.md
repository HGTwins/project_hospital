# 메디가이드 (MediGuide)
전국 병원 정보를 지도에서 한눈에 보고, 상세 정보와 실제 사용자 리뷰를 확인할 수 있는 통합 의료 정보 플랫폼

## 🚀 프로젝트 소개

- 개발 기간: 2025.12.17 ~ 2026.01.21
- 기획 배경: 병원 정보는 존재하지만, 사용자가 다양한 조건을 한눈에 비교하기에는 불편함이 있었음. <br/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 병원 정보를 한 화면에 제공해 탐색 과정의 비효율을 줄이고자 기획
- 활용 데이터: [전국의료기관표준데이터](https://www.data.go.kr/data/15096293/standard.do)

## 🧑 팀원 구성

| 이현지(Backend) | 김현지(Frontend) |
|:---:|:---:|
|<img src ='' width='150' height='150' /> <br /> <a href='https://github.com/maybecocheon'>@maybecocheon</a>|<img src='' width='150' height='150'/> <br /> <a href='https://github.com/guswlrla'>@guswlrla</a>|

## 🛠 기술 스택

- Frontend: Next.js, TypeScript, Tailwind CSS, ApexCharts, Kakao Maps SDK
- Backend: Spring Boot, Java, JWT(OAuth2), JPA, Bean Validation
- Database: MySQL

## 📁 프로젝트 구조

```
medical_FE/src/
├── app/                                
│   ├── layout.tsx                      
│   ├── page.tsx                        # 메인 페이지 (/)
│   ├── globals.css                     
│   │
│   ├── login/
│   │   └── page.tsx                    # 로그인 페이지 (/login)
│   │
│   ├── join/
│   │   └── page.tsx                    # 회원가입 페이지 (/join)
│   │
│   ├── medicalInfo/
│   │   └── page.tsx                    # 의료정보 검색 페이지 (/medicalInfo) - 지도, 필터, 통계, 상세정보 통합
│   │
│   ├── callback/
│   │   └── page.tsx                    # OAuth2 콜백 페이지 (/callback)
│   │
│   └── admin/
│       └── page.tsx                    # 관리자 페이지 (/admin)
│
├── components/                         
│   ├── Header.tsx                      
│   │
│   ├── SideBar.tsx                     
│   │                                   
│   ├── Dashboard.tsx                   
│   │                                   
│   ├── KakaoMap.tsx                    
│   │                                   
│   ├── MapLoading.tsx                  
│   │
│   ├── DetailContent.tsx               
│   │                                   
│   ├── ReviewSection.tsx               
│   │
│   ├── ReviewModal.tsx                 
│   │
│   ├── ScoreCard.tsx                   
│   │
│   ├── Modal.tsx                       
│   │                                   
│   ├── OverlayCard.tsx                 
│   │
│   ├── SelectBox.tsx                   
│   │
│   └── Pagenation.tsx                  
│                                        
│
└── types/                              
    ├── HospCategory.tsx                
    ├── HospDept.tsx                    
    ├── HospInfo.tsx                    
    └── HospLocation.tsx                
```

## 📝 API 명세

#### 사용자 리뷰

| URI | Method | 설명 | 화면 |
|-----|--------|------|------|
| `/api/review` | GET | 전체 리뷰 목록 조회(검색) | /medicalInfo |
| `/api/review` | POST | 새 리뷰 등록 | /medicalInfo |
| `/api/review/{seq}` | PUT | 기존 리뷰 수정 | /medicalInfo |
| `/api/review/{seq}` | DELETE | 리뷰 삭제 | /medicalInfo |

#### 병원 정보 조회

| URI | Method | 설명 | 화면 |
|-----|--------|------|------|
| `/api/sidoName` | GET | 시도명 목록 조회 | /medicalInfo |
| `/api/sigunguName` | GET | 시군구명 목록 조회 | /medicalInfo |
| `/api/medicalCountHospital` | GET | 병원 수 통계 (스코어카드) | /medicalInfo |
| `/api/medicalNight` | GET | 야간 진료 병원 조회 | /medicalInfo |
| `/api/medicalHoliday` | GET | 공휴일 의료 병원 조회 | /medicalInfo |
| `/api/medicalEssential` | GET | 필수 의료 정보 조회 | /medicalInfo |
| `/api/medicalType` | GET | 병원 유형별 통계 (차트용) | /medicalInfo |
| `/api/medicalDept` | GET | 진료 과목별 통계 (차트용) | /medicalInfo |
| `/api/medicalLocation` | GET | 위도/경도 기반 병원 조회 | /medicalInfo |
| `/api/medicalInfo` | GET | 시도/시군구 기반 병원 조회 | /medicalInfo |
| `/api/medicalid` | GET | 병원 ID로 단일 조회 (QueryParam) | /medicalInfo |
| `/api/medicalInfo/{hospitalid}` | GET | 병원 상세 페이지 정보 조회 | /medicalInfo |

#### 관리자 페이지

| URI | Method | 설명 | 화면 |
|-----|--------|------|------|
| `/api/admin/getMembers` | GET | [관리자] 전체 회원 목록 조회 | /admin |
| `/api/admin/deletMember/{username}` | DELETE | [관리자] 등록 회원 삭제 | /admin |
| `/api/getMember/{username}` | GET | 특정 회원 정보 조회 | /admin |
| `/api/review/memberid/{username}` | GET | 특정 사용자 아이디로 리뷰 조회 | /admin |
| `/api/review/hospitalid/{hospitalid}` | GET | 특정 병원 아이디로 리뷰 조회 | /admin |

#### 회원 가입 / 조회

| URI | Method | 설명 | 화면 |
|-----|--------|------|------|
| `/api/jwtcallback` | POST | OAuth2 로그인 시 토큰 전달 | /login |
| `/api/join` | POST | 회원 가입 | /join |
| `/api/check-duplicate` | GET | 아이디/이메일 중복 확인 | /join |

## 📌 주요 기능

- 일반 로그인 및 구글/네이버 OAuth2 소셜 로그인
- 회원 가입 기능 및 유효성 검사
- 지도 기반 병원 위치 조회
- 주제별 병원 리스트, 병원 상세정보 제공
- 병원 유형 및 진료 과목 차트 제공
- 로그인 사용자 대상 병원 리뷰 작성·수정·삭제 기능
- 관리자 전용 페이지(회원 관리 및 리뷰 모니터링)

## 🎬 동작 영상

https://github.com/user-attachments/assets/acecf341-558e-440c-8244-ce6e9a4b4617

## 💡 프로젝트 후기

- 
- 
