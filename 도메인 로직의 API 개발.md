- 도메인 모델 패턴ㅇ는 트랜잭션 스크립트처럼 작업 단위의 절차형 API를 만들기가 어렵다
- 도메인 로직의 명확한 작업 단위 API를 제공하는 애플리케이션 서비스가 필요
  - 도메인 모델 패턴 앞에 애플리케이션 서비스가 위치한다.
  - 전통적인 트랜잭션 스크립트 방식의 서비스는 작업 단위로 다 작성했다.
  - 애플리케이션 서비스는 어떤 입구 역할을 한다 그래서 Facade 패턴이라고도 부른다.
  - 애플리케이션 서비스는 도메인 모델의 기능을 조합하여 사용자나 외부 시스템의 '요청(작업)'을 처리하는 역할