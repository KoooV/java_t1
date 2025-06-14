package ru.t1.java.demo.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.model.TimeLimitExceedLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;
import ru.t1.java.demo.repository.TimeLimitExceedLogRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;
    private final TimeLimitExceedLogRepository timeLimitExceedLogRepository;
    private final ObjectMapper objectMapper;

    /**
     * Обрабатывает сообщения из топиков metrics-topic и datasource-topic.
     * В зависимости от типа сообщения (errorType) выполняет соответствующую обработку:
     * - METRICS: Сохраняет информацию о превышении времени выполнения метода
     * - DATA_SOURCE: Сохраняет информацию об ошибке источника данных
     *
     * @param message Содержимое сообщения
     * @param errorType Тип сообщения (METRICS или DATA_SOURCE)
     */
    @KafkaListener(topics = {"metrics-topic", "datasource-topic"}, groupId = "${spring.kafka.consumer.group-id}")
    public void listen(@Payload String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            log.info("Received message from topic {}: {}", topic, message);

            switch (topic) {
                case "metrics-topic":
                    handleMetric(message);
                    break;
                case "datasource-topic":
                    handleDataSourceError(message);
                    break;
                default:
                    log.warn("Unknown topic: {}", topic);
            }
        } catch (Exception e) {
            log.error("Error processing message from topic {}: {}", topic, message, e);
        }
    }

    /**
     * Обрабатывает сообщение с метрикой времени выполнения метода.
     * Десериализует JSON в объект TimeLimitExceedLog и сохраняет его в базу данных.
     *
     * @param message JSON-сообщение с метрикой
     */
    private void handleMetric(String message) {
        try {
            TimeLimitExceedLog metricLog = objectMapper.readValue(message, TimeLimitExceedLog.class);
            timeLimitExceedLogRepository.save(metricLog);
            log.info("Metric saved: {}", metricLog);
        } catch (Exception e) {
            log.error("Error processing metric message: {}", message, e);
        }
    }

    /**
     * Обрабатывает сообщение об ошибке источника данных.
     * Десериализует JSON в объект DataSourceErrorLog и сохраняет его в базу данных.
     *
     * @param message JSON-сообщение с информацией об ошибке
     */
    private void handleDataSourceError(String message) {
        try {
            DataSourceErrorLog errorLog = objectMapper.readValue(message, DataSourceErrorLog.class);
            dataSourceErrorLogRepository.save(errorLog);
            log.info("Data source error saved: {}", errorLog);
        } catch (Exception e) {
            log.error("Error processing data source error message: {}", message, e);
        }
    }
} 