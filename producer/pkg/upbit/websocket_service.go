package upbit

import (
	"encoding/json"
	"fmt"
	"strings"
	"time"
)

// Endpoints

const (
	baseWsMainURL = "wss://api.upbit.com/websocket/v1"
)

var (
	// WebsocketTimeout is an interval for sending ping/pong messages if WebsocketKeepalive is enabled
	WebsocketTimeout = time.Second * 60
	// WebsocketKeepalive enables sending ping/pong messages to check the connection stability
	WebsocketKeepalive = false
)

type WsSimpleTicker struct {
	Type string `json:"ty"`
	Symbol string `json:"cd"`
	OpeningPrice float64 `json:"op"`
	HighPrice float64 `json:"hp"`
	LowPrice float64 `json:"lp"`
	TradePrice float64 `json:"tp"`
	PrevClosingPrice float64 `json:"pcp"`
	Change string `json:"c"`
	ChangePrice float64 `json:"cp"`
	SignedChangePrice float64 `json:"scp"`
	ChangeRate float64 `json:"cr"`
	SignedChangeRate float64 `json:"scr"`
	TradeVolume float64 `json:"tv"`
	AccTradeVolume float64 `json:"atv"`
	AccTradeVolume24H float64 `json:"atv24h"`
	AccTradePrice float64 `json:"atp"`
	ACCTradePrice24H float64 `json:"atp24h"`
	TradeDate string `json:"tdt"`
	TradeTime string `json:"ttm"`
	TradeTimestamp int64 `json:"ttms"`
	AskBid string `json:"ab"`
	AccAskVolume float64 `json:"aav"`
	AccBidVolume float64 `json:"abv"`
	Highest52WeekPrice float64 `json:"h52wp"`
	Highest52WeekDate string `json:"h52wdt"`
	Lowest52WeekPrice float64 `json:"l52wp"`
	Lowest52WeekDate string `json:"l52wdt"`
	MarketState string `json:"ms"`
	IsTradingSuspended bool `json:"its"`
	DelistingDate string `json:"dd"`
	MarketWarning string `json:"mw"`
	TimeStamp int64 `json:"ts"`
	StreamType string `json:"st"`
}

type WsSimpleTickerHandler func(ticker *WsSimpleTicker)

func WsCombinedTickerServe(symbols []string, handler WsSimpleTickerHandler, errHandler ErrHandler ) (doneC, stopC chan struct{}, err error) {

	subscribe_fmt := fmt.Sprintf(`[{"ticket":"test"},{"type":"ticker","codes":["%s"], "isOnlyRealtime" : "true"}, {"format": "SIMPLE"}]`, strings.Join(symbols, `","`))

	cfg := newWsConfig(baseWsMainURL, subscribe_fmt)
	wsHandler := func(message []byte) {
		event := new(WsSimpleTicker)
		err := json.Unmarshal(message, event)
		if err != nil {
			errHandler(err)
			return
		}
		handler(event)
	}

	return wsServe(cfg, wsHandler, errHandler)

}
