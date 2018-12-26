package top.lmoon.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import top.lmoon.schedule.RdsInterfaceJob;
import top.lmoon.test.Test;

@Controller
@RequestMapping(value = "/testapi")
public class TestController {
	
	@Autowired
	private Test test;
	
	@RequestMapping(value = "/test")
	@ResponseBody
	public String card() {
		String string = test.get();
		return string;
	}
	
	
	
}
