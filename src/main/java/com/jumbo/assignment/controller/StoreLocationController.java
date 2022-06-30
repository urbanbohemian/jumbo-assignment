package com.jumbo.assignment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoreLocationController {

	@GetMapping("/stores")
	public String index() {
		return "storelocator";
	}

}