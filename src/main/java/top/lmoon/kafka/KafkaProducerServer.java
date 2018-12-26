package top.lmoon.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import com.alibaba.fastjson.JSON;

import top.lmoon.util.IdGenUtil;
import top.lmoon.util.SpringUtils;


/**
 * kafkaProducer模板
 *     使用此模板发送消息
 *
 */
public class KafkaProducerServer{
	
	private KafkaConfig kafkaConfig;

    private KafkaTemplate<String, String> kafkaTemplate;
    
//    <entry key="bootstrap.servers" value="localhost:7000" />
//    14                 <entry key="group.id" value="0" />
//    15                 <entry key="retries" value="1" />
//    16                 <entry key="batch.size" value="16384" />
//    17                 <entry key="linger.ms" value="1" />
//    18                 <entry key="buffer.memory" value="33554432" />
//    19                 <entry key="key.serializer"
//    20                 value="org.apache.kafka.common.serialization.StringSerializer" />
//    21                 <entry key="value.serializer"
//    22                 value="org.apache.kafka.common.serialization.StringSerializer" />
    public KafkaProducerServer(String ip,String port){  
    	kafkaConfig = SpringUtils.getBean(KafkaConfig.class);
		Map<String,Object> configs = new HashMap<>();
		configs.put("bootstrap.servers", ip+":"+port);
//		configs.put("group.id", kafkaConfig.getGroupId());
		configs.put("retries", kafkaConfig.getRetries());
		configs.put("batch.size", kafkaConfig.getBatchSize());
		configs.put("linger.ms", kafkaConfig.getLingerMs());
		configs.put("buffer.memory", kafkaConfig.getBufferMemory());
		configs.put("key.serializer", kafkaConfig.getKeySerializer());
		configs.put("value.serializer", kafkaConfig.getValueSerializer());

		ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configs);
		kafkaTemplate = new KafkaTemplate<String, String>(producerFactory,kafkaConfig.isAutoFlush());
		kafkaTemplate.setProducerListener(new KafkaProducerListener<String,String>());
    }
    
    public void sendMsgList(String topic, List<String> list){
    	for(String data:list){
    		String key = IdGenUtil.uuid();
    		sendMsg(topic,key, data);
    	}
    }
    
    public ListenableFuture<SendResult<String, String>> sendMsg(String topic, String data){
    	return kafkaTemplate.send(topic, data);
    }
    
    public ListenableFuture<SendResult<String, String>> sendMsg(String topic,String key, String data){
    	return kafkaTemplate.send(topic, key, data);
    }
    
    /**
     * kafka发送消息模板
     * @param topic 主题
     * @param value    messageValue
     * @param ifPartition 是否使用分区
     * @param partitionNum 分区数 如果是否使用分区为0,分区数必须大于0
     * @param role 角色:bbc app erp...
     */
    public Map<String,Object> sendMsg(String topic, Object value, boolean ifPartition, 
            Integer partitionNum, String role){
//        String key = role+"-"+value.hashCode();
    	String key = role;
        String valueString = JSON.toJSONString(value);
        if(ifPartition){
            //表示使用分区
            int partitionIndex = getPartitionIndex(key, partitionNum);
            ListenableFuture<SendResult<String, String>> result = kafkaTemplate.send(topic, partitionIndex, key, valueString);
            Map<String,Object> res = checkProRecord(result);
            return res;
        }else{
            ListenableFuture<SendResult<String, String>> result = kafkaTemplate.send(topic, key, valueString);
            Map<String,Object> res = checkProRecord(result);
            return res;
        }
    }

    /**
     * 根据key值获取分区索引
     * @param key
     * @param partitionNum
     * @return
     */
    private int getPartitionIndex(String key, int partitionNum){
        if (key == null) {
            Random random = new Random();
            return random.nextInt(partitionNum);
        }
        else {
            int result = Math.abs(key.hashCode())%partitionNum;
            return result;
        }
    }
    
    /**
     * 检查发送返回结果record
     * @param res
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Map<String,Object> checkProRecord(ListenableFuture<SendResult<String, String>> res){
        Map<String,Object> m = new HashMap<String,Object>();
        if(res!=null){
            try {
                SendResult r = res.get();//检查result结果集
                /*检查recordMetadata的offset数据，不检查producerRecord*/
                Long offsetIndex = r.getRecordMetadata().offset();
                if(offsetIndex!=null && offsetIndex>=0){
                    m.put("code", KafkaMsgConstant.SUCCESS_CODE);
                    m.put("message", KafkaMsgConstant.SUCCESS_MES);
                    return m;
                }else{
                    m.put("code", KafkaMsgConstant.KAFKA_NO_OFFSET_CODE);
                    m.put("message", KafkaMsgConstant.KAFKA_NO_OFFSET_MES);
                    return m;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                m.put("code", KafkaMsgConstant.KAFKA_SEND_ERROR_CODE);
                m.put("message", KafkaMsgConstant.KAFKA_SEND_ERROR_MES);
                return m;
            } catch (ExecutionException e) {
                e.printStackTrace();
                m.put("code", KafkaMsgConstant.KAFKA_SEND_ERROR_CODE);
                m.put("message", KafkaMsgConstant.KAFKA_SEND_ERROR_MES);
                return m;
            }
        }else{
            m.put("code", KafkaMsgConstant.KAFKA_NO_RESULT_CODE);
            m.put("message", KafkaMsgConstant.KAFKA_NO_RESULT_MES);
            return m;
        }
    }
    

}

