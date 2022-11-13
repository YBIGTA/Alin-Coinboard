package main

import (
	// "alin-coinboard/producer/pkg/upbit"
	"alin-coinboard/producer/pkg/upbit"
	"encoding/json"
	"fmt"
	"os"
	"sync"

	"github.com/adshao/go-binance/v2"
)

const (
	binanceTopic = "dev.alin.binance.json"
	upbitTopic = "dev.alin.upbit.json"
)


func checkError(err error) {
  if err != nil {
    fmt.Println(err)
    os.Exit(1)
  }
}

// 주어진 symbol들에 대한 Binance API Kline 데이터를 웹소켓으로 받아와 카프카 프로듀서로 전송
// https://binance-docs.github.io/apidocs/spot/en/#kline-candlestick-streams
func produce_binance_kline(tickers []string, interval string) {
	errHandler := func(err error) {
		fmt.Println(err)
	}
	wsKlineHandler := func(event *binance.WsKlineEvent) {

		jsonEvent, err := json.Marshal(event)
		checkError(err)

		produceMessageToCluster(binanceTopic, event.Symbol, string(jsonEvent))
	}

	symbolIntervalPair := make(map[string]string)
	for _, ticker := range tickers {
		symbolIntervalPair[ticker] = interval
	}

	doneC, _, err := binance.WsCombinedKlineServe(symbolIntervalPair, wsKlineHandler, errHandler)
	if err != nil {
			fmt.Println(err)
			return
	}
	<-doneC
}

// 주어진 symbol들에 대한 Upbit API Ticker 데이터를 웹소켓으로 받아와 카프카 프로듀서로 전송
// https://docs.upbit.com/docs/upbit-quotation-websocket#현재가ticker-응답
func produce_upbit_ticker(tickers []string) {
	errHandler := func (err error)  {
		fmt.Println(err)	
	}
	wsTickerHandler := func(event *upbit.WsSimpleTicker) {

			jsonEvent, err := json.Marshal(event)
			checkError(err)

			produceMessageToCluster(upbitTopic, event.Symbol, string(jsonEvent))
	}
	doneC, _, err := upbit.WsCombinedTickerServe(tickers, wsTickerHandler, errHandler)
	if err != nil {
			fmt.Println(err)
			return
	}
	<-doneC

}


func main() {

	var wait sync.WaitGroup
	wait.Add(2)

	go produce_binance_kline([]string{"btcusdt", "ethusdt"}, "1s")
	go produce_upbit_ticker([]string{"KRW-BTC", "KRW-ETH"})

	wait.Wait()

}
