<div align="center">
  
<!-- [![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fharu-puppy2024&count_bg=%23404040&title_bg=%23D77B3C&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com) -->

# **하루강아지** 🐶🗓️

</div>

<!--**[배포 URL]**

- URL: -->

<!-- 이미지 -->

[![하루강아지 메인 이미지](https://github.com/user-attachments/assets/2467e516-5525-4089-99dc-b55ac2605e6e)](https://haru-puppy-frontend.vercel.app)

## 1. 소개 🐶

- 온 가족이 함께 살피는 강아지의 하루하루🐾

- 강아지 케어를 할 때 생길 수 있는 소통의 비효율을 줄이는 것이 목표입니다. 

- 가족 구성원들이 날짜, 시간, 담당자, 메모 등을 포함한 강아지 스케줄을 캘린더에서 확인할 수 있습니다.

- 홈에서 주간 산책 횟수, 오늘 배변 횟수, 마지막 목욕 날짜, 마지막 건강검진 날짜 등의 레포트를 확인할 수 있습니다.

- 등록한 스케줄 설정에 따라 가족 구성원 모두에게 알림을 보내 확인할 수 있습니다.

## 하루강아지 팀원 👥

|   **FE 준미**   |   **FE 황혜명**   |  **BE 안세준**  |
| :-----: | :-------: |  :----: |
| <img width="180" alt="junmieee_profile_img" src="https://github.com/haru-puppy2024/.github/assets/87015026/c8d29dd3-1e40-499e-9089-396a47606d3f">  | <img width="180" alt="hyemyoung_hwang_profile_img" src="https://github.com/haru-puppy2024/.github/assets/87015026/9ab2c575-5328-4f71-ae6e-0515da170486">  | <img width="180" alt="asjjun_img" src="https://github.com/haru-puppy2024/.github/assets/87015026/d4e8a9c3-0120-4845-a9a0-932630ba88aa"> |
**GitHub**: [junmieee](https://github.com/junmieee)  <br>   |   **GitHub**: [CosmicLatte009](https://github.com/CosmicLatte009) <br>**blog**: [깃헙 블로그](https://cosmiclatte009.github.io/blog/) | **GitHub**: [asjjun](https://github.com/asjjun) <br>   |  

## 2. 개발 환경 및 배포 URL 🔗

**[개발 환경]**

- Front-End: Next.js, React-query, Recoil, Styled-components
- Back-End: Java 17, SpringBoot, Spring Security, Spring Data Jpa, Spring Scheduler, MariaDB, Redis

**[배포 URL]**

- URL: https://haru-puppy-frontend.vercel.app

## 3. 개발일정 🗓

#### 기간 : 2024.04.15(월) ~ 2024.08

- 프로젝트 회의 : 04.15(월) ~ 04.25(목)
  - 🔗[API 문서](https://www.notion.so/API-1db717659bc54f4ebceb410edbd9df79), 기술스택 논의

- 기능구현 : 04.26(금) ~ 08.18(일)
  - 🔗[스프린트](https://www.notion.so/Sprint-43750bc47db846898d2e6c53eb8dbde5)

<!-- ## 4. 프로젝트 구조 🗂 -->

<!-- 폴더 구조를 좀 정리해서 마지막에 싹 넣으면 좋을 것 같습니다. -->


## 5. 역할 분담 👨‍👩‍👧‍👧

### [👩🏻‍💻준미](https://github.com/junmieee)

- 스플래쉬, 로그인
- 스케줄 메인 페이지, 스케줄 체크 카드
- 홈페이지 레포트, 산책 횟수 랭킹

### [🧑🏻‍💻황혜명](https://github.com/CosmicLatte009)

- 스케줄 생성, 수정, 삭제 
- 회원가입, 초대 유저 회원가입, 내 프로필 수정
- 메이트 초대 기능

### [👩🏻‍💻안세준](https://github.com/asjjun)

- 백엔드 서버 개발 및 배포

## 6. 구현 기능 🛠

### 1) 로그인 및 회원가입

| 로그인 | 내 프로필 | 강아지 프로필 | 로그아웃
| :-------------: | :----------------: | :---------------: | :---------------: |
| ![로그인](https://github.com/user-attachments/assets/77ef85f9-e8d6-41ca-9b98-6597ac635895) | ![내 프로필](https://github.com/user-attachments/assets/ebf3cd8a-9004-4a83-9619-855d7dffeeb2) | ![강아지 프로필](https://github.com/user-attachments/assets/56441bf3-1303-484c-8cd1-fdaf906c915c) | ![로그아웃](https://github.com/user-attachments/assets/d271fae1-e76d-4bc6-ae16-325ad5d25877)
| 서비스 접속시 <br>처음에 보이는 화면입니다. |   카카오 OAuth로 로그인 및 회원가입할 수 있습니다. <br> 초대 받은 유저의 경우 내 프로필만 입력하면 바로 회원가입됩니다.  | 유저 프로필에서 이미지, 닉네임, 역할을 입력할 수 있습니다. | 강아지 프로필에서 이미지, 강아지 이름, 생일, 체중을 입력할 수 있습니다. <br> 모두 입력하면 회원가입이 완료됩니다.  | 설정 페이지에서 로그아웃탭을 클릭하면 바로 로그아웃 후 로그인 페이지로 되돌아갑니다. 




###  2) 홈페이지, 스케줄 메인 페이지

| 홈페이지| 스케줄 캘린더| 스케줄 TODO| 스케줄 상세 조회 |
| :-------------: | :----------------: | :---------------: | :---------------: |
|  ![홈페이지](https://github.com/user-attachments/assets/b4e99672-bc36-42e2-9dda-8f9244ded32c)  | ![스케줄 캘린더](https://github.com/user-attachments/assets/174003bb-d31e-4536-a996-34fbf3bc38c6)| ![스케줄 TODO](https://github.com/user-attachments/assets/bf479e0e-6376-45b8-a4cb-602458b2684b) | ![스케줄 단일 조회](https://github.com/user-attachments/assets/a428f8c6-418e-4438-bb24-cbde29eae19b)  |
|  강아지 정보, 메이트 정보, <br> 레포트, 주간 산책 랭킹을 확인할 수 있습니다. <br> 오늘의 배변횟수를 기록할 수 있습니다. |  캘린더를 월별, 주별로 볼 수 있고 <br> 스케줄 유무를 확인할 수 있습니다. | 스케줄 TODO 카드에서 해야할 활동, 담당자, <br> 시간 등을 확인하고 수정할 수 있습니다. | 스케줄 TODO 카드를 클릭하면 해당 스케줄을 상세 조회할 수 있습니다. |

### 3) 스케줄 생성, 수정, 삭제

| 새 스케줄 생성| 단일 스케줄 수정| 반복 스케줄 수정 | 스케줄 삭제 |
| :-------------: | :----------------: | :---------------: | :---------------: |
| ![새 스케줄 생성](https://github.com/user-attachments/assets/49e22e57-49dc-491c-ae80-cda3963522a7) | ![단일 스케줄 수정](https://github.com/user-attachments/assets/525af6a8-87d0-47b9-9702-5ca93382bdaa) | ![반복 스케줄 수정](https://github.com/user-attachments/assets/483d3abe-3d29-490a-9a15-432536195d38) | ![스케줄 삭제](https://github.com/user-attachments/assets/4557e29d-b8d6-45ee-b27c-a9bfc2299c69) |
| 할 일, 담당자, 날짜, 시간, 반복, 알림, 메모 등을 <br> 입력하여 새 스케줄을 생성할 수 있습니다. | 반복되지 않는 단일 스케줄의 경우에는  <br> 할 일, 날짜, 시간, 반복, 메모 항목을 변경할 수 있습니다. | 반복 스케줄의 경우에는 이후 스케줄도 변경할지 혹은 해당 스케줄만 변경할지 선택할 수 있습니다. | 스케줄 삭제가 가능합니다. 반복 스케줄의 경우에는 <br> 이후 스케줄도 삭제할지 혹은 해당 스케줄만 삭제할지 선택할 수 있습니다. |


### 4) 설정 페이지 기타 기능

| 설정 페이지 | 내 프로필 수정 | 강아지 프로필 수정 | 메이트 초대 기능 |
| :-------------: | :----------------: | :---------------: | :---------------: |
| ![설정 페이지](https://github.com/user-attachments/assets/d3b62fb1-a3e7-4032-b940-8dd6241f2b47) | ![내 프로필 수정](https://github.com/user-attachments/assets/be4b459a-14a1-4254-a988-d52c4195c086) | ![강아지 프로필 수정](https://github.com/user-attachments/assets/397edce4-8026-4e64-a4fe-10323b6059aa)  |  ![메이트 초대하기](https://github.com/user-attachments/assets/a14fd2aa-f35e-4fdc-94ca-b8d873d5a3c5) 
| 설정 페이지에서 알림 토글, 로그아웃, 회원탈퇴 등의 <br> 기능을 확인할 수 있습니다. | 내 프로필 수정을 할 수 있습니다. | 강아지 프로필 수정을 할 수 있습니다.  | 강아지를 같이 케어할 메이트를 초대할 수 있습니다.  |



<!--
## 8. 남은 TODO

- [ ] 회원탈퇴 에러
- [ ] 알림 설정 토글 기능
- [ ] 알림 페이지 데이터 페칭
- [ ] 폼 처리 파트 리팩토링(react-hook-form, valibot 적용)
- [ ] Ariakit UI 라이브러리 도입하여 웹 접근성 향상
-->

## 7. 레슨런 및 스페셜 포인트

### 1) 기술적 측면

- SSR 서버사이드 렌더링과 CSR 구분하여 적용, 동적 라우팅
- 파괴적 변경을 방지하기 위한 헤드리스 컴포넌트 설계의 중요성
- 폼 처리 코드 효율적으로 개선

### 2) 팀원 간 커뮤니케이션 

- 🔗[노션 페이지](https://www.notion.so/4ecd16d0c33d4bed9a06e3ba2d478406)
- 이슈와 풀리퀘에 필요한 경우 상세한 설명 달기.
- 구글 Meet로 주마다 정기적 회의


<!-- ## 6. 개발 이슈 💡 -->

<!-- 프로젝트하며 겪었던 이슈중에 남기고싶은 이슈 -->
<!-- 코드 방향성에 대해 고민했던 이슈 -->

<!-- 프로젝트 회고 -->
