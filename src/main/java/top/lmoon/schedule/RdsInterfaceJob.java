package top.lmoon.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RdsInterfaceJob {
	
	private static final Logger logger = LoggerFactory.getLogger(RdsInterfaceJob.class);

	
//	@Scheduled(fixedRateString = "${SendCardData.fixedRate}")
//	public void executeCard()
//	{
//		logger.debug("--executeCard");
//	}

}
