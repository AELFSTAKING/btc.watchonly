package io.seg.kofo.bitcoinwo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author gin
 */
@SpringBootApplication
@EnableEurekaClient
@EnableApolloConfig
@EnableFeignClients(basePackages = {"io.seg"})
@Slf4j
public class BtcWoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtcWoApplication.class, args);
		log.info("{} is running...", BtcWoApplication.class.getName());
	}
}

