package doubloon;

import java.io.IOException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
public class Validator implements Producer {
    private final KafkaProducer<String,String> kafkaProducer;
    private final String goodTopic;
    private final String badTopic;
    protected static final ObjectMapper MAPPER=new ObjectMapper();
    public Validator(String servers, String goodTopic, String badTopic){
        this.kafkaProducer = new KafkaProducer<String, String>(Producer.createConfig(servers));
        this.goodTopic=goodTopic;
        this.badTopic=badTopic;
    }

    private String validate(JsonNode root, String path){
        JsonNode node = root.at(path);
        if(node.isMissingNode()){
            return path.concat(" is missing. ");
        }
        return "";
    }

    @Override
    public void produce(String message){
        ProducerRecord<String,String> pr=null;
        try{
            JsonNode root=MAPPER.readTree(message);
            String error="";
            error = error.concat(validate(root,"/event"));
            error = error.concat(validate(root,"/customer"));
            error = error.concat(validate(root,"/currency"));
            error = error.concat(validate(root,"/timestamp"));
            error = error.concat(validate(root,"/customer/id"));
            error = error.concat(validate(root,"/customer/name"));
            error = error.concat(validate(root,"/customer/ipAddress"));
            error = error.concat(validate(root,"/currency/name"));
            error = error.concat(validate(root,"/currency/price"));

            if(error.length()>0){
                String value = "{\"error\":\""+error+"\"}";
                pr = new ProducerRecord<String,String>(this.badTopic, value);
            }else{
                pr = new ProducerRecord<String,String>(this.goodTopic,MAPPER.writeValueAsString(message));
            }
        }catch(IOException e){
            String value = "\"error\":\""+e.getClass().getSimpleName()+":"+e.getMessage()+"\"}";
            pr = new ProducerRecord<String,String>(this.badTopic, value);
        }finally{
            if(pr != null){
                kafkaProducer.send(pr);
            }
        }

    }

}
