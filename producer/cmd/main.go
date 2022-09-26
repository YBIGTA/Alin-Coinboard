package main

import (
	"fmt"
	"io"
	"os"
	"sync"

	"github.com/adshao/go-binance/v2"
)

func checkError(err error) {
  if err != nil {
    fmt.Println(err)
    os.Exit(1)
  }
}

func save_binance_aggr_trade(ticker string) {

	wsAggTradeHandler := func(event *binance.WsAggTradeEvent) {
		fmt.Println(event)
		f5, err := os.OpenFile("data/" + ticker + ".txt", os.O_APPEND|os.O_WRONLY|os.O_CREATE, 0600)
		checkError(err)
		_, err = io.WriteString(f5, event.Symbol + " " + event.Price + " " + event.Quantity + "\n")
		checkError(err)
	}
	errHandler := func(err error) {
		fmt.Println(err)
	}
	doneC, _, err := binance.WsAggTradeServe(ticker, wsAggTradeHandler, errHandler)
	if err != nil {
		fmt.Println(err)
		return
	}
	<-doneC
}

func main() {

	var wait sync.WaitGroup
	wait.Add(2)

	go save_binance_aggr_trade("btcusdt")
	go save_binance_aggr_trade("ethusdt")

	wait.Wait()

}
