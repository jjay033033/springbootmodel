package top.lmoon.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class KafkaConfig {
	
//	@Value("${kafka.group.id}")
//	private String groupId;
	
	@Value("${kafka.retries}")
	private int retries;
	
	@Value("${kafka.batch.size}")
	private int batchSize;
	
	@Value("${kafka.linger.ms}")
	private int lingerMs;
	
	@Value("${kafka.buffer.memory}")
	private int bufferMemory;
	
	@Value("${kafka.key.serializer}")
	private String keySerializer;
	
	@Value("${kafka.value.serializer}")
	private String valueSerializer;
	
	@Value("${kafka.autoFlush}")
	private boolean autoFlush;

//	public String getGroupId() {
//		return groupId;
//	}

	public int getRetries() {
		return retries;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public int getLingerMs() {
		return lingerMs;
	}

	public int getBufferMemory() {
		return bufferMemory;
	}

	public String getKeySerializer() {
		return keySerializer;
	}

	public String getValueSerializer() {
		return valueSerializer;
	}

	public boolean isAutoFlush() {
		return autoFlush;
	}

}
