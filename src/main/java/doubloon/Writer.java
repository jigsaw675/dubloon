package doubloon;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
public class Writer implements Producer{
    private final KafkaProducer<String,String> kafkaProducer;
    private final String topic;
    public Writer(String servers, String topic){
        this.kafkaProducer = new KafkaProducer<String, String>(Producer.createConfig(servers));
        this.topic = topic;
    }
    @Override
    public void produce(String message){
        ProducerRecord<String,String> record=new ProducerRecord<>(topic, message);
        kafkaProducer.send(record);
    }
}
