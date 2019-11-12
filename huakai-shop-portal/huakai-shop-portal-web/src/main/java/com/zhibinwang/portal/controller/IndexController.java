package com.zhibinwang.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	private static final String PO_INDEX="index";

	/**
	 * 跳转到首页
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public String index() {
		return PO_INDEX;
	}

	/**
	 * 跳转到首页
	 * 
	 * @return
	 */
	@RequestMapping("/index.html")
	public String indexHtml() {
		return index();
	}
}
