package top.lmoon.test;

import org.springframework.stereotype.Component;

import top.lmoon.annotation.ExceptionHandle;
import top.lmoon.annotation.TimeStat;


@TimeStat
@Component
public class Test {
	
	@ExceptionHandle
	public String get(){
//		throw new RuntimeException("test exception！！！！！");
		int a = 1/0;
		return "success!";
	}

}
