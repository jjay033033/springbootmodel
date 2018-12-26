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

public class Test {
	static KafkaTemplate kafkaTemplate;
	
	public static void main(String[] args) {
//		KafkaConfig kafkaConfig;
		Map<String,Object> configs = new HashMap<>();
//		configs.put("bootstrap.servers", "10.240.3.130:9092");
		configs.put("bootstrap.servers", "10.240.3.40:6667");
//		configs.put("group.id", kafkaConfig.getGroupId());
		configs.put("retries", 1);
		configs.put("batch.size", 16384);
		configs.put("linger.ms", 1);
		configs.put("buffer.memory", 33554432);
		configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configs);
		kafkaTemplate = new KafkaTemplate<String, String>(producerFactory,true);
		kafkaTemplate.setProducerListener(new KafkaProducerListener<String,String>());
		
		
//		KafkaProducerServer k = new KafkaProducerServer("10.240.3.130", "2181");
		ListenableFuture<SendResult<String, String>> result = sendMsg("test1", "abc");
		Map<String, Object> res = checkProRecord(result);
		System.out.println(res.toString());
	}
	
	public static void sendMsgList(String topic, List<String> list){
    	for(String data:list){
    		String key = IdGenUtil.uuid();
    		sendMsg(topic,key, data);
    	}
    }
    
    public static ListenableFuture<SendResult<String, String>> sendMsg(String topic, String data){
    	return kafkaTemplate.send(topic, data);
    }
    
    public static ListenableFuture<SendResult<String, String>> sendMsg(String topic,String key, String data){
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
    public static Map<String,Object> sendMsg(String topic, Object value, boolean ifPartition, 
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
    private static int getPartitionIndex(String key, int partitionNum){
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
    public static Map<String,Object> checkProRecord(ListenableFuture<SendResult<String, String>> res){
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
