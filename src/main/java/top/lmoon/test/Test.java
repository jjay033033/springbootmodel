package top.lmoon.test;

import org.springframework.stereotype.Component;

import top.lmoon.annotation.TimeStat;

@TimeStat
@Component
public class Test {
	
	public String get(){
		return "success!";
	}

}
