# 코인 정보 Kafka 프로듀서 코드

## 폴더 구조

```text
/bin : 빌드된 파일 저장된 폴더
/cmd : 메인 로직 폴더
|-- /main.go : 메인 프로듀서 로직
|-- /producer.go : 카프카 토픽으로 데이터 전송하는 함수
/pkg/upbit : 업비트 API 연동 패키지
build.sh : 빌드용 스크립트
go.mod
go.sum
```
