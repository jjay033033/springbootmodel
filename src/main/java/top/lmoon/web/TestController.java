package top.lmoon.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import top.lmoon.test.Test;
import top.lmoon.test.Test2;

@Controller
@RequestMapping(value = "/testapi")
public class TestController {
	
	@Autowired
	private Test test;
	
	@RequestMapping(value = "/test")
	@ResponseBody
	public String test() {
		String string = test.get();
		return string;
	}
	
	@RequestMapping(value = "/test2")
	@ResponseBody
	public String test2() {
		Test2 t = new Test2();
		t.t();
		return "";
	}
	
}
