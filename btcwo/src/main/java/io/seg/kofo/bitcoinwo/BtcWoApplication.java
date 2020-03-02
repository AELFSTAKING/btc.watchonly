package io.seg.kofo.bitcoinwo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import io.seg.frame.job.starter.annotation.EnableJob;
import io.seg.framework.sequence.sdk.annotation.EnableSequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author gin
 */
@SpringBootApplication
@EnableEurekaClient
@EnableApolloConfig
@EnableJob
@EnableSequence
@EnableFeignClients(basePackages = {"io.seg", "io.seg.framework.sequence.sdk.client"})
@Slf4j
public class BtcWoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtcWoApplication.class, args);
		log.info("{} is running...", BtcWoApplication.class.getName());
	}
}

