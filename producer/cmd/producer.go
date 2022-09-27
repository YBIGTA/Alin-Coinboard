package main

import (
	"fmt"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

// 카프카 클러스터 주소만 바꿔주면 됨.
const bootstrapServer = "localhost:9092"

func produceMessageToCluster(topic string, message string) {

	cfg := &kafka.ConfigMap{
		"bootstrap.servers": bootstrapServer,
	}

	p, err := kafka.NewProducer(cfg)
	if err != nil {
		panic(err)
	}

	defer p.Close()

	// Delivery report handler for produced messages
	go func() {
		for e := range p.Events() {
			switch ev := e.(type) {
			case *kafka.Message:
				if ev.TopicPartition.Error != nil {
					fmt.Printf("Delivery failed: %v\n", ev.TopicPartition)
				} else {
					fmt.Printf("Delivered message to %v\n", ev.TopicPartition)
				}
			}
		}
	}()

	p.Produce(&kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
		Value:          []byte(message),
	}, nil)

	// Wait for message deliveries before shutting down
	p.Flush(15 * 1000)
}