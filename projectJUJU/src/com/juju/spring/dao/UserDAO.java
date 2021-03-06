package com.juju.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.juju.spring.dto.UserDTO;
import com.juju.spring.mapper.UserMapper;

@Repository
public class UserDAO {
	
	@Autowired
	private UserMapper userMapper;
	
	public String checkID(String user_id) {
		
		String checking_id = userMapper.checkID(user_id);
		
		return checking_id;
	}
	
	public void addUserInfo(UserDTO joinUserDTO) {
		userMapper.addUserInfo(joinUserDTO);
	}
	
	public UserDTO getLoginUser(UserDTO tmpLoginUserDTO) {
		UserDTO fromDBUserDTO = userMapper.getLoginUser(tmpLoginUserDTO);
		return fromDBUserDTO;
	}
	
	public UserDTO getModifyUserDTO(int user_idx) {
		UserDTO fromDBModifyUserDTO = userMapper.getModifyUserDTO(user_idx);
		return fromDBModifyUserDTO;
	}
	
	public void modifyUserInfo(UserDTO modifyUserDTO) {
		userMapper.modifyUserInfo(modifyUserDTO);
	}
}
