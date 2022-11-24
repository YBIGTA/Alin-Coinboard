FROM openjdk:11-jdk AS builder

WORKDIR /app

# install kafka
RUN wget https://downloads.apache.org/kafka/3.3.1/kafka_2.13-3.3.1.tgz
RUN tar -xzf kafka_2.13-3.3.1.tgz
RUN mv kafka_2.13-3.3.1 kafka

# install elastic sink connector
RUN wget https://d1i4a15mxbxib1.cloudfront.net/api/plugins/confluentinc/kafka-connect-elasticsearch/versions/14.0.2/confluentinc-kafka-connect-elasticsearch-14.0.2.zip
RUN unzip confluentinc-kafka-connect-elasticsearch-14.0.2.zip -d kafka/plugins
RUN mv kafka/plugins/confluentinc-kafka-connect-elasticsearch-14.0.2 kafka/plugins/kafka-connect-elasticsearch

COPY ./configs/elasticsearch-sink.properties kafka/config/elasticsearch-sink.properties
COPY ./configs/connect-distributed.properties kafka/config/connect-distributed.properties
COPY ./configs/connect-standalone.properties kafka/config/connect-standalone.properties

FROM openjdk:11-jdk AS runtime

WORKDIR /app

COPY --from=builder /app/kafka ./kafka 




