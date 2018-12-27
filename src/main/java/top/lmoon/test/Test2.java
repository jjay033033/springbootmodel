package top.lmoon.test;

import top.lmoon.annotation.TimeStat;

@TimeStat
public class Test2 {

	@TimeStat
	public void t(){
		System.out.println("123");
	}

}
