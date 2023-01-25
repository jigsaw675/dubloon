package doubloon;

import java.util.Properties;
public interface Producer {
    public static Properties createConfig(String servers){
        Properties props = new Properties();
        props.put("bootstrap.servers",servers);
        props.put("acks","all");
        props.put("retries",0);
        props.put("batch.size",1000);
        props.put("linger.ms",1);
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");

        return props;
    }
    public void produce(String message);
}
