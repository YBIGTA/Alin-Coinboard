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
    private static String BOOTSTRAP_SERVERS = "localhost:9092";
    private static String BINANCE_SOURCE = "dev.alin.binance.json";
    private static String UPBIT_SOURCE = "dev.alin.upbit.json";
    private static String SINK = "dev.alin.upbit-copy.json";

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

    private static String getKimchiPremiumJson(String binance, String upbit) throws JsonProcessingException {
        String coin = KimchiPremiumUtil.getValue(binance.split(",")[2], true);
        coin = coin.substring(0, 3);
        Float premiumPrice = getPremiumPrice(binance, upbit);
        String timestamp = KimchiPremiumUtil.getValue(binance.split(",")[1], false);
        String date = KimchiPremiumUtil.changeTimestampToDateString(timestamp);

        KimchiPremium kimchiPremium = new KimchiPremium(coin, premiumPrice, date);
        System.out.println("kimchiPremium = " + kimchiPremium);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(kimchiPremium);
        return jsonInString;
    }

    private static Float getPremiumPrice(String binance, String upbit){
        Float upbitPrice = 0f;
        String tpTmp = upbit.split(",")[5];
        upbitPrice = getPrice(tpTmp, false);

        Float binancePrice = 0f;
        String[] binanceSplit = binance.split(",");
        String hTmp = binanceSplit[11];
        Float h = getPrice(hTmp, true);
        String lTmp = binanceSplit[12];
        Float l = getPrice(lTmp, true);
        binancePrice = (h+l) / 2;

        Float premiumPrice = upbitPrice - binancePrice * 1450;
        return premiumPrice;
    }

    private static Float getPrice(String str, Boolean isQuoted){
        return Float.valueOf(KimchiPremiumUtil.getValue(str, isQuoted));
    }
}
