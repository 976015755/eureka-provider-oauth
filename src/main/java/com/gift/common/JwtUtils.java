package com.gift.common;



import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import com.gift.dao.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Jwt操作类
 * @author TOM
 */
public class JwtUtils {
	
	public static JwtUtils getInstance() {
		return new JwtUtils();
	}
	/**
	 * 生成JWT
	 * @param ttlMillis 过期时间（毫秒）
	 * @param user 用户对象
	 * @return token字符串
	 */
	public static String createJwt(long ttlMillis, User user) {
		//指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
		SignatureAlgorithm alg = SignatureAlgorithm.HS256;
		
		//生成jjwt的时间
		long nowMillis = System.currentTimeMillis();//当前时间戳（毫秒级）
		Date now = new Date();
		
		//密钥
		String secretKey = JwtUtils.getInstance().createKey();
		
		//自定义私有字段
		HashMap<String, Object> claimsHashMap = new HashMap<String, Object>();
		claimsHashMap.put("id", user.getUid());
		claimsHashMap.put("username", user.getUsername());
		claimsHashMap.put("level", user.getLevel());
		claimsHashMap.put("mobile", user.getMobile());
		
		
		//签发人
		String subject = user.getUsername();
		
		//生成jwt
		JwtBuilder jwt = Jwts.builder()
							//设置私有字段
							.setClaims(claimsHashMap)
							//设置签发人
							.setSubject(subject)
							//设置签名算法和密钥
							.signWith(alg, secretKey)
							//设置签发时间
							.setIssuedAt(now)
							//设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
			                .setId(UUID.randomUUID().toString())
			                ;
		if(ttlMillis >= 0) {
			jwt.setExpiration(new Date(ttlMillis + nowMillis));
		}
		return jwt.compact();
	}
	
	/**
	 * JWT解密-获取私有字段
	 * @param jwtString jwt字符串
	 * @param user 用户对象
	 * @return 解密后的json
	 */
	public static Claims parseToken(String token) {
		//密钥
		String secretKey = JwtUtils.getInstance().createKey();
		try {
			Claims claims = Jwts.parser()
							.setSigningKey(secretKey)
							.parseClaimsJws(token)
							.getBody();
			return claims;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			//throw e;
			//e.printStackTrace();
		}
		
	}
	
	/**
	 * 生成加密key的算法
	 * @return 加密key
	 */
	protected String createKey() {
		return "fadsfasd1212312";
	}
}
