package kimchipremium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Properties;

public class Streams {
    private static String APPLICATION_NAME = "kimchi-premium-dev";
    private static String BOOTSTRAP_SERVERS = "kafka:29092";
    private static String BINANCE_SOURCE = "dev.alin.binance.json";
    private static String UPBIT_SOURCE = "dev.alin.upbit.json";
    private static String SINK = "dev.alin.kimchi_premium.json";
    private static final Float EXCHANGE_RATE = 1338.00f;

    public static void main(String[] args){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        KTable<String, String> binanceTable = builder.table(BINANCE_SOURCE);

        KStream<String, String> upbitTmp = builder.stream(UPBIT_SOURCE);
        KStream<String, String> upbitStream = upbitTmp.selectKey((k,v) -> k.substring(4)+"USDT");

        upbitStream.join(binanceTable,
                (upbit, binance) -> {
                    try {
                        return getKimchiPremiumJson(binance, upbit);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .to(SINK);
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

    }

    private static String getKimchiPremiumJson(String binanceString, String upbitString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Binance binance = objectMapper.readValue(binanceString, Binance.class);
        Upbit upbit = objectMapper.readValue(upbitString, Upbit.class);

        String coin = binance.getSymbol().substring(0, 3);
        Float premiumPrice = getPremiumPrice(binance, upbit);

        Long timestamp = binance.getEventTime();
        String date = KimchiPremiumUtil.changeTimestampToDateString(timestamp);

        KimchiPremium kimchiPremium = new KimchiPremium(coin, premiumPrice, date);
        System.out.println("kimchiPremium = " + kimchiPremium);
        String jsonString = objectMapper.writeValueAsString(kimchiPremium);
        return jsonString;
    }

    private static Float getPremiumPrice(Binance binance, Upbit upbit){
        Binance.Kline kline = binance.getKline();
        Float h = kline.getHighPrice();
        Float l = kline.getLowPrice();
        Float binancePrice = (h + l) / 2;

        Float upbitPrice = upbit.getTradePrice();

        Float premiumPrice = upbitPrice - binancePrice * EXCHANGE_RATE;
        return premiumPrice;
    }

}
