package com.gift.oauth.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gift.dao.User;
import com.gift.repository.UserRepository;

@Service
public class UserdetailServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("查找用户：" + username);
		//TODO: 通过feign调用用户服务获取用户信息
		User user = userRepository.findFirstByUsername(username);
		if(user != null) {
			return new UserdetailsImpl(user);
		} else {
			System.out.println("没有该用户");
			throw new UsernameNotFoundException("没有该用户");
		}
	}
}
