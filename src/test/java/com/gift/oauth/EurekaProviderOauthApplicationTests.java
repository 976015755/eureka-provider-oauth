package com.gift.oauth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gift.common.JwtUtils;
import com.gift.dao.User;

import io.jsonwebtoken.Claims;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EurekaProviderOauthApplicationTests {

	@Test
	public void contextLoads() {
		User user = new User();
		user.setLevel(1);
		user.setUsername("admin");
		user.setUid(2);
		user.setPassword("123456");
		String string = JwtUtils.createJwt(new Long(10000000L), user);
		System.out.println(string);
	}

	@Test
	public void jwtParse() {
		User user = new User();
		user.setLevel(1);
		user.setUsername("admin");
		user.setUid(2);
		user.setPassword("123456");
		String tokenString = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImxldmVsIjoxLCJpZCI6MiwiZXhwIjoxNTcxNzQ5MDU5LCJpYXQiOjE1NzE3MzkwNTksImp0aSI6IjNlMGYwYjk4LWNjYjQtNGVmN"
				+ "i05OGFkLTM1ZGE3ZTU0NTUwZSIsInVzZXJuYW1lIjoiYWRtaW4ifQ.1yNs6bflnNulTvB7qVxQwElnJKkLpTeb-gP4dLY_xcI";
		Claims claims = JwtUtils.parseToken(tokenString);
		System.out.println(claims.toString());
		
	}
}
