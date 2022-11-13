package kimchipremium;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Binance {

    @JsonProperty("e")
    private String eventType;

    @JsonProperty("E")
    private Long eventTime;

    @JsonProperty("s")
    private String symbol;

    @JsonProperty("k")
    private Kline kline;

    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Getter
    public
    class Kline {

        @JsonProperty("t")
        private Long startTime;

        @JsonProperty("T")
        private Long closeTime;

        @JsonProperty("s")
        private String symbol;

        @JsonProperty("i")
        private String interval;

        @JsonProperty("f")
        private Long firstTrade;

        @JsonProperty("L")
        private Long lastTrade;

        @JsonProperty("o")
        private Float openPrice;

        @JsonProperty("c")
        private Float closePrice;

        @JsonProperty("h")
        private Float highPrice;

        @JsonProperty("l")
        private Float lowPrice;

        @JsonProperty("v")
        private Float baseAssetVolume;

        @JsonProperty("n")
        private Long numberOfTrades;

        @JsonProperty("x")
        private Boolean isKlineClosed;

        @JsonProperty("q")
        private Float quoteAssetVolume;

        @JsonProperty("V")
        private Float takerBuyBaseAssetVolume;

        @JsonProperty("Q")
        private Float takerBuyQuoteAssetVolume;
    }

}


//{       "e":"kline",
//        "E":1668304129001,
//        "s":"ETHUSDT",
//        "k":{"t":1668304128000,
//        "T":1668304128999,
//        "s":"ETHUSDT",
//        "i":"1s",
//        "f":1020803598,
//        "L":1020803601,
//        "o":"1263.16000000",
//        "c":"1263.17000000",
//        "h":"1263.17000000",
//        "l":"1263.16000000",
//        "v":"3.25520000",
//        "n":4,
//        "x":true,
//        "q":"4111.87090200",
//        "V":"3.24700000",
//        "Q":"4101.51299000"}}