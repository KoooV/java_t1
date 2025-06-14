package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String METRIC_TOPIC = "metrics-topic";
    private static final String DATASOURCE_TOPIC = "datasource-topic";

    /**
     * Отправляет метрику в топик metrics-topic.
     * Метрика содержит информацию о времени выполнения метода.
     *
     * @param metricMessage JSON-сообщение с метрикой
     */
    public CompletableFuture<SendResult<String, String>> sendMetric(String metricMessage) {
        return sendMessage(METRIC_TOPIC, metricMessage, "METRICS");
    }

    /**
     * Отправляет информацию об ошибке источника данных в топик datasource-topic.
     * Содержит детали ошибки, возникшей при работе с базой данных.
     *
     * @param errorMessage JSON-сообщение с информацией об ошибке
     */
    public CompletableFuture<SendResult<String, String>> sendDataSourceError(String errorMessage) {
        return sendMessage(DATASOURCE_TOPIC, errorMessage, "DATA_SOURCE");
    }

    /**
     * Вспомогательный метод для отправки сообщений в Kafka.
     * Создает сообщение с указанным содержимым и топиком.
     *
     * @param topic Топик для отправки
     * @param message Содержимое сообщения
     * @param errorType Тип ошибки
     */
    private CompletableFuture<SendResult<String, String>> sendMessage(String topic, String message, String errorType) {
        log.info("Sending message to topic {}: {}", topic, message);
        
        Message<String> kafkaMessage = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader("errorType", errorType)
                .build();

        return kafkaTemplate.send(kafkaMessage)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message sent successfully to topic {} with errorType={}", topic, errorType);
                    } else {
                        log.error("Unable to send message to topic {}: {}", topic, message, ex);
                    }
                });
    }
} 