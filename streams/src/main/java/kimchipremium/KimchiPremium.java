package kimchipremium;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KimchiPremium {

    @JsonProperty
    private String coin;
    @JsonProperty
    private Float premiumPrice;
    @JsonProperty
    private String time;

    public KimchiPremium(String coin, Float premiumPrice, String time){
        this.coin = coin;
        this.premiumPrice = premiumPrice;
        this.time = time;
    }
}
