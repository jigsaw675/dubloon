package doubloon;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
public class Reader implements Consumer {
    private final KafkaConsumer<String,String> kafkaConsumer;
    private final String topic;
    public Reader(String servers, String groupId, String topic){
        this.kafkaConsumer = new KafkaConsumer<String, String>(Consumer.createConfig(servers, groupId));
        this.topic = topic;
    }
    @Override
    public ConsumerRecords<String,String> consume(){
        kafkaConsumer.subscribe(java.util.Arrays.asList(this.topic));
        ConsumerRecords<String,String> records = kafkaConsumer.poll(100);
        return records;
    }
}
