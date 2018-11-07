package com.mvc.cryptovault.app.controller;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

public class ContiPerfTest {

	@Rule
	public ContiPerfRule i = new ContiPerfRule();
 
	@Test
	@PerfTest(duration = 10000, threads = 40)
	@Required(max = 1200, average = 250, totalTime = 60000)
	public void test1() throws Exception {



//		Thread.sleep(200);
	}
}