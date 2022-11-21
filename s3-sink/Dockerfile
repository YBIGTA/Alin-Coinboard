FROM openjdk:11-jdk AS builder

WORKDIR /app

# install kafka
RUN wget https://downloads.apache.org/kafka/3.3.1/kafka_2.13-3.3.1.tgz
RUN tar -xzf kafka_2.13-3.3.1.tgz
RUN mv kafka_2.13-3.3.1 kafka

# install s3 sink
RUN wget https://d1i4a15mxbxib1.cloudfront.net/api/plugins/confluentinc/kafka-connect-s3/versions/10.3.0/confluentinc-kafka-connect-s3-10.3.0.zip
RUN unzip confluentinc-kafka-connect-s3-10.3.0.zip -d kafka/plugins
RUN mv kafka/plugins/confluentinc-kafka-connect-s3-10.3.0 kafka/plugins/kafka-connect-s3

COPY ./configs/s3-sink.properties kafka/config/s3-sink.properties
COPY ./configs/connect-distributed.properties kafka/config/connect-distributed.properties
COPY ./configs/connect-standalone.properties kafka/config/connect-standalone.properties

FROM openjdk:11-jdk AS runtime

WORKDIR /app

COPY --from=builder /app/kafka ./kafka 

# install aws cli
RUN apt-get update && apt-get install -y awscli



