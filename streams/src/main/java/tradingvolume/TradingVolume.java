package tradingvolume;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradingVolume {

    @JsonProperty
    private String coin;
    @JsonProperty
    private Float tradingVolume;
    @JsonProperty
    private String time;

    public TradingVolume(String coin, Float tradingVolume, String time){
        this.coin = coin;
        this.tradingVolume = tradingVolume;
        this.time = time;
    }
}