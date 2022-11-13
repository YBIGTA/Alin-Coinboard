package tradingvolume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kimchipremium.Binance;
import kimchipremium.KimchiPremiumUtil;
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
    private static String BOOTSTRAP_SERVERS = "kafka:29092";
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

    private static String getTradingVolumeJson(String coin, String binanceString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Binance binance = objectMapper.readValue(binanceString, Binance.class);

        Float volume = getTradingVolume(binance);
        Long timestamp = binance.getEventTime();
        String date = KimchiPremiumUtil.changeTimestampToDateString(timestamp);

        TradingVolume tradingVolume = new TradingVolume(coin, volume, date);
        System.out.println("tradingVolume = " + tradingVolume);
        String jsonString = objectMapper.writeValueAsString(tradingVolume);
        return jsonString;
    }

    private static Float getTradingVolume(Binance binance){
        Binance.Kline kline = binance.getKline();
        Long n = kline.getNumberOfTrades();
        Float h = kline.getHighPrice();
        Float l = kline.getLowPrice();
        Float binancePrice = (h + l) / 2;

        Float tradingVolume = binancePrice * n;
        return tradingVolume;
    }

}
