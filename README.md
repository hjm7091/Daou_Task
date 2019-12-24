### 1. 과제 소개
    
    로그 파일 분석하여 단위 시간 별 통계 파일 생성

![image](https://user-images.githubusercontent.com/28583661/71387359-d4e3a680-2636-11ea-9d1a-98ebb3434e64.png)

### 2. 요구사항

    1) 최초 실행 시에는 가장 오래된 날짜 로그 파일부터 '현재-1분'까지의 모든 로그를 처리하여 통계 파일을 생성
    2) access_날짜.txt -> 분석 -> yyyyMMddHHmm 파일 생성 -> 취합 -> yyyyMMddHH 파일 생성 -> 취합 -> stat_yyyy-MM-dd 파일 생성
    3) yyyyMMddHHmm 파일은 최근 60분 파일만 유지, yyyyMMddHH 파일은 최근 24시간 파일만 유지, stat_yyyy-MM-dd 파일은 모두 유지
    4) 모든 통계 파일은 로그 빈도수에 따라 내림차순으로 정렬을 수행하여 저장
    5) 실시간으로 처리될 수 있도록 구현
    
### 3. 추가 요구사항

    1) IP / EMAIL / METHOD / URL 속성을 갖는 모델 객체 활용
    2) equals(), hashCode() 재정의하여 통계 산출하는데 사용
    3) access_날짜.txt 파일을 하나의 맵 또는 리스트에 담는 것 제거
    4) 프로그램을 재시작 했을 때 이미 처리한 로그는 중복 처리 하지 않도록 구현

### 4. 클래스 다이어그램

    1) 파일을 관리하기 위한 객체
![image](https://user-images.githubusercontent.com/28583661/71387723-9353fb00-2638-11ea-9f2d-65d5c11ac741.png)
![image](https://user-images.githubusercontent.com/28583661/71387737-a9fa5200-2638-11ea-9535-e828081540ea.png)
![image](https://user-images.githubusercontent.com/28583661/71387745-b383ba00-2638-11ea-9bc2-4a24a55320e0.png)

    2) 다른 객체를 사용하는 서비스 객체
![image](https://user-images.githubusercontent.com/28583661/71387917-710ead00-2639-11ea-8f7b-6a053dfc71dc.png)
![image](https://user-images.githubusercontent.com/28583661/71387919-73710700-2639-11ea-80f5-29eaafcfda88.png)
![image](https://user-images.githubusercontent.com/28583661/71387923-75d36100-2639-11ea-8699-247e958c69c2.png)

    3) 읽은 라인을 가공하는 서비스 객체
![image](https://user-images.githubusercontent.com/28583661/71387857-30169880-2639-11ea-90c4-1c7de348a013.png)
![image](https://user-images.githubusercontent.com/28583661/71387859-33118900-2639-11ea-94a6-f3f434132562.png)
![image](https://user-images.githubusercontent.com/28583661/71387862-36a51000-2639-11ea-828d-11450cff970b.png)

    4) 최종 클래스 다이어그램
![image](https://user-images.githubusercontent.com/28583661/71387970-ab784a00-2639-11ea-807b-5f436ecb7157.png)

### 5. 핵심 코드

![image](https://user-images.githubusercontent.com/28583661/71388015-e5e1e700-2639-11ea-8576-bc24d4cff157.png)
      
    라인을 읽어 나가면서 분 단위의 시간이 변했을 때 임시 저장소에 저장되어 있는 이전 라인들을 로컬에 저장하는 방법을 사용. 
    이렇게 하면 메모리에 하나의 파일 단위가 아닌 분 단위의 라인들이 들어갈 것이기 때문에 메모리를 효율적으로 사용 가능
    


    
