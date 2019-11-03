package com.shifthunter.ppmtool;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;


@SpringBootApplication
@RestController
@EnableResourceServer
public class PpmToolSecureService {

	public static void main(String[] args) {
		SpringApplication.run(PpmToolSecureService.class, args);
	}
	
	
	// Added to DO Some Checks
	// Properties come from OAuth2
	@Autowired
	private ResourceServerProperties sso;

	//added
	// We are loading those props into CustomUserInfoTokenService to Inflate That token
	// The Basic token we doesn't recognise the OAuth2, because of this we have the CustomUserInfoToken Class
	// Now we can do PreAuthorize in "tollDAta"
	@Bean
	public ResourceServerTokenServices myUserInfoTokenServices() {
		return new CustomUserInfoTokenService(sso.getUserInfoUri(), sso.getClientId());
	}
	
	
	@RequestMapping("/tolldata")
	@PreAuthorize("#oauth2.hasScope('toll_read') and hasAuthority('ROLE_OPERATOR')")  //only 'omartini' can see this
	public ArrayList<TollUsage> getTollData() {
		
		TollUsage instance1 = new TollUsage("200", "station150", "B65GT1W", "2016-09-30T06:31:22");
		TollUsage instance2 = new TollUsage("201", "station119", "AHY673B", "2016-09-30T06:32:50");
		TollUsage instance3 = new TollUsage("202", "station150", "ZN2GP0", "2016-09-30T06:37:01");
		
		ArrayList<TollUsage> tolls = new ArrayList<TollUsage>();
		tolls.add(instance1);
		tolls.add(instance2);
		tolls.add(instance3);
		
		return tolls;
	}
		
	public class TollUsage {
			
			public String Id;
			public String stationId;
			public String licensePlate;
			public String timestamp;
			
			public TollUsage() {}
			
			public TollUsage(String id, String stationid, String licenseplate, String timestamp){
				this.Id = id;
				this.stationId = stationid;
				this.licensePlate = licenseplate;
				this.timestamp = timestamp;
			}
			
			
	}
}
