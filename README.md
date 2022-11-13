# Alin-Coinboard

비트코인 거래량 / 김치 프리미엄 등 시각화 대시보드

## 팀원

- 20기 백승연
- 20기 서혁준
- 20기 이학민
- 20기 주현경

## 목표

- BTC, BNB, XRP, ETH, DOGE, TRX 코인에 대해서 현재가, 가격 변동 추이, 김치 프리미엄, 거래량 급등 정보를 보여주는 대시보드 구축

## 프로젝트 파이프라인

```mermaid
graph LR;

subgraph Kafka Cluster
	T_B[binance 토픽]
	T_U[upbit 토픽]
	T_EX[환율 토픽]
	T_Kim[김치 프리미엄 토픽]
	T_Vol[코인별 거래량 토픽]
end

subgraph ELK stack
	ES[ElasticSearch]
	KB[Kibana]
end

subgraph AWS
	S3[S3 버킷]
end

subgraph Kafka Streams
	S_Kim[김치 프리미엄]
	S_Vol[거래량]
end

%% API Input
B[Binance] --websocket--> T_B
U[Upbit] --websocket--> T_U
E[수출입은행 환율 API] --REST--> T_EX

%% 김치 프리미엄 계산
T_B --> S_Kim
T_U --> S_Kim
T_EX --> S_Kim
S_Kim --> T_Kim

%% 거래량 변화 계산
T_B --> S_Vol
S_Vol --> T_Vol

%% ELK 컨슈머
T_B --> ES
T_Kim --> ES
T_Vol --> ES
ES --> KB

%% S3 컨슈머
T_B --> S3
T_U --> S3
T_Kim --> S3
T_Vol --> S3
```

## 개발환경 셋업 방법

1. `docker-compose` 를 이용한 카프카 개발환경 실행

```bash

# 카프카 클러스터 실행
docker compose up -d

# binance 토픽 데이터 조회 (kafka-console-consumer)
docker compose logs binance-consumer

# upbit 토픽 데이터 조회 (kafka-console-consumer)
docker compose logs upbit-consumer

# kimchi premium 토픽 데이터 조회 (kafka-console-consumer)
docker compose logs kimchi-premium-consumer

# trading volume 토픽 데이터 조회 (kafka-console-consumer)
docker compose logs trading-volume-consumer

# 프로듀서 실행

# Apple Silicon
./producer/bin/start-producer-darwin-arm64

# Apple Intel
./producer/bin/start-producer-darwin-amd64

# Windows
./producer/bin/start-producer-windows-amd64.exe


```
