package main

import (
	"encoding/json"
	"fmt"
	"io"
	"os"
	"sync"

	"github.com/adshao/go-binance/v2"
	"github.com/gorilla/websocket"
)

func checkError(err error) {
  if err != nil {
    fmt.Println(err)
    os.Exit(1)
  }
}

func save_binance_kline(ticker string) {

	errHandler := func(err error) {
		fmt.Println(err)
	}
	wsKlineHandler := func(event *binance.WsKlineEvent) {
		f5, err := os.OpenFile("data/binance/" + ticker + ".txt", os.O_APPEND|os.O_WRONLY|os.O_CREATE, 0600)
		checkError(err)

		jsonEvent, err := json.Marshal(event)
		checkError(err)

		_, err = io.WriteString(f5, string(jsonEvent) + "\n")
		checkError(err)
	}
	doneC, _, err := binance.WsKlineServe(ticker, "1s", wsKlineHandler, errHandler)
	if err != nil {
			fmt.Println(err)
			return
	}
	<-doneC

}

const upbit_ws_url = "wss://api.upbit.com/websocket/v1"

func save_upbit_trade_data(ticker string) {

	conn, _, err := websocket.DefaultDialer.Dial(upbit_ws_url, nil)
	if err != nil {
			fmt.Println(err)
			return
	}
	defer conn.Close()

	subscribe_fmt := fmt.Sprintf(`[{"ticket":"test"},{"type":"ticker","codes":["%s"], "isOnlyRealtime" : "true"}]`, ticker) 

	conn.WriteMessage(websocket.TextMessage, []byte(subscribe_fmt))

	if err != nil {
			fmt.Println(err)
			return
	}


	for {
			_, message, err := conn.ReadMessage()
			if err != nil {
					fmt.Println(err)
					return
			}

			file, err := os.OpenFile("data/upbit/" + ticker + ".txt", os.O_APPEND|os.O_WRONLY|os.O_CREATE, 0600)
			if err != nil {
					fmt.Println(err)
					return
			}

			_, err = io.WriteString(file, string(message) + "\n")
			checkError(err)
	}

}




func main() {

	var wait sync.WaitGroup
	wait.Add(4)

	go save_binance_kline("btcusdt")
	go save_binance_kline("ethusdt")

	go save_upbit_trade_data("KRW-BTC")
	go save_upbit_trade_data("KRW-ETH")

	wait.Wait()

}
