package tradingvolume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tradingvolume.TradingVolume;
import tradingvolume.TradingVolumeUtil;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Properties;

public class Streams {
    private static String APPLICATION_NAME = "trading-volume-dev";
    private static String BOOTSTRAP_SERVERS = "localhost:9092";
    private static String BINANCE_SOURCE = "dev.alin.binance.json";
    private static String SINK = "dev.alin.trading_volume.json";

    public static void main(String[] args){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> binanceStream = builder.stream(BINANCE_SOURCE);
        binanceStream.selectKey(
                (coin, v) -> {
                    try {
                        return getTradingVolumeJson(coin, v);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
        ).to(SINK);

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }

    private static String getTradingVolumeJson(String coin, String val) throws JsonProcessingException {
        Float volume = getTradingVolume(val);
        String timestamp = TradingVolumeUtil.getValue(val.split(",")[1], true);
        String date = TradingVolumeUtil.changeTimestampToDateString(timestamp);

        TradingVolume tradingVolume = new TradingVolume(coin, volume, date);
        System.out.println("tradingVolume = " + tradingVolume);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(tradingVolume);
        return jsonInString;
    }

    private static Float getTradingVolume(String val){
        Float binancePrice = 0f;
        String[] binanceSplit = val.split(",");
        String hTmp = binanceSplit[11];
        String lTmp = binanceSplit[12];
        String nTmp = binanceSplit[14];
        Float n = getPrice(nTmp, false);
        Float h = getPrice(hTmp, true);
        Float l = getPrice(lTmp, true);
        binancePrice = (h + l) / 2;

        Float tradingVolume = binancePrice * n;

        return tradingVolume;

    }

    private static Float getPrice(String str, Boolean isQuoted){
        return Float.valueOf(TradingVolumeUtil.getValue(str, isQuoted));
    }

}
