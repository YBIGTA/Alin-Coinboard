package kimchipremium;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Upbit {

    @JsonProperty("ty")
    private String type;

    @JsonProperty("cd")
    private String code;

    @JsonProperty("op")
    private Float openingPrice;

    @JsonProperty("hp")
    private Float highPrice;

    @JsonProperty("lp")
    private Float lowPrice;

    @JsonProperty("tp")
    private Float tradePrice;

    @JsonProperty("pcp")
    private Float prevClosingPrice;

    @JsonProperty("c")
    private String change;

    @JsonProperty("cp")
    private Float changePrice;

    @JsonProperty("scp")
    private Float signedChangePrice;

    @JsonProperty("cr")
    private Float changeRate;

    @JsonProperty("scr")
    private Float signedChangeRate;

    @JsonProperty("tv")
    private Float tradeVolume;

    @JsonProperty("atv")
    private Float accTradeVolume;

    @JsonProperty("atv24h")
    private Float accTradeVolume24h;

    @JsonProperty("atp")
    private Float accTradePrice;

    @JsonProperty("atp24h")
    private Float accTradePrice24h;

    @JsonProperty("tdt")
    private String tradeDate;

    @JsonProperty("ttm")
    private String tradeTime;

    @JsonProperty("ttms")
    private Long tradeTimestamp;

    @JsonProperty("ab")
    private String askBid;

    @JsonProperty("aav")
    private Float accAskVolume;

    @JsonProperty("abv")
    private Float accBidVolume;

    @JsonProperty("h52wp")
    private Float highest52WeekPrice;

    @JsonProperty("h52wdt")
    private String highest52WeekDate;

    @JsonProperty("l52wp")
    private Float lowest52WeekPrice;

    @JsonProperty("l52wdt")
    private String lowest52WeekDate;

    @JsonProperty("ts")
    private String tradeStatus;

    @JsonProperty("ms")
    private String marketState;

    @JsonProperty("msfi")
    private String marketStateForIos;

    @JsonProperty("its")
    private Boolean isTradingSuspended;

    @JsonProperty("dd")
    private String delistingDate;

    @JsonProperty("mw")
    private String marketWarning;

    @JsonProperty("tms")
    private Long timestamp;

    @JsonProperty("st")
    private String streamType;

}


//{       "ty":"ticker",
//        "cd":"KRW-ETH",
//        "op":1748500,
//        "hp":1763500,
//        "lp":1742500,
//        "tp":1759000,
//        "pcp":1748500,
//        "c":"RISE",
//        "cp":10500,
//        "scp":10500,
//        "cr":0.0060051473,
//        "scr":0.0060051473,
//        "tv":0.1705,
//        "atv":3067.21338426,
//        "atv24h":55236.26799193,
//        "atp":5380594754.204535,
//        "atp24h":96739155059.4565,
//        "tdt":"20221113",
//        "ttm":"015026",
//        "ttms":1668304226319,
//        "ab":"ASK",
//        "aav":1315.05358601,
//        "abv":1752.15979825,
//        "h52wp":5900000,
//        "h52wdt":"2021-12-01",
//        "l52wp":1201500,
//        "l52wdt":"2022-06-18",
//        "ms":"ACTIVE",
//        "its":false,
//        "dd":"",
//        "mw":"NONE",
//        "ts":0,
//        "st":"REALTIME"}