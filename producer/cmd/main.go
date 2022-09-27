package main

import (
	"alin-coinboard/producer/pkg/upbit"
	"encoding/json"
	"fmt"
	"io"
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

// 주어진 symbol들에 대한 Binance API Kline 데이터를 웹소켓으로 받아와 /data/binance.txt에 저장
// https://binance-docs.github.io/apidocs/spot/en/#kline-candlestick-streams
func save_binance_kline(tickers []string, interval string) {
	errHandler := func(err error) {
		fmt.Println(err)
	}
	wsKlineHandler := func(event *binance.WsKlineEvent) {
		file, err := os.OpenFile("../data/binance.txt", os.O_APPEND|os.O_WRONLY|os.O_CREATE, 0600)
		checkError(err)

		jsonEvent, err := json.Marshal(event)
		checkError(err)

		_, err = io.WriteString(file, string(jsonEvent) + "\n")
		checkError(err)

		// produceMessageToCluster(binanceTopic, string(jsonEvent))
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

// 주어진 symbol들에 대한 Upbit API Ticker 데이터를 웹소켓으로 받아와 /data/upbit.txt에 저장
// https://docs.upbit.com/docs/upbit-quotation-websocket#현재가ticker-응답
func save_upbit_ticker(tickers []string) {
	errHandler := func (err error)  {
		fmt.Println(err)	
	}
	wsTickerHandler := func(event *upbit.WsSimpleTicker) {
			file, err := os.OpenFile("../data/upbit.txt", os.O_APPEND|os.O_WRONLY|os.O_CREATE, 0600)
			if err != nil {
					fmt.Println(err)
					return
			}
			jsonEvent, err := json.Marshal(event)
			checkError(err)
			_, err = io.WriteString(file, string(jsonEvent) + "\n")
			checkError(err)

			// produceMessageToCluster(upbitTopic, string(jsonEvent))
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

	go save_binance_kline([]string{"btcusdt", "ethusdt"}, "1s")
	go save_upbit_ticker([]string{"KRW-BTC", "KRW-ETH"})

	wait.Wait()

}
