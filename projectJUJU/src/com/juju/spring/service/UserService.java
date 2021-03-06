package com.juju.spring.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.juju.spring.dao.UserDAO;
import com.juju.spring.dto.UserDTO;

@Service
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	public boolean checkID(String user_id) {
		String checking_id = userDAO.checkID(user_id);
		
		if(checking_id == null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void addUserInfo(UserDTO joinUserDTO) {
		userDAO.addUserInfo(joinUserDTO);
	}
	
	@Resource(name="loginUserDTO")
	private UserDTO loginUserDTO;
	
	public void getLoginUser(UserDTO tmpLoginUserDTO) {
		
		UserDTO fromDBUserDTO = userDAO.getLoginUser(tmpLoginUserDTO);
	
		if(fromDBUserDTO != null) {
			loginUserDTO.setUser_idx(fromDBUserDTO.getUser_idx());
			loginUserDTO.setUser_name(fromDBUserDTO.getUser_name());
			loginUserDTO.setUserLogin(true);
		}
	}
	
	public void getModifyUserDTO(UserDTO modifyUserDTO) {
		UserDTO fromDBModifyUserDTO = userDAO.getModifyUserDTO(loginUserDTO.getUser_idx());

		modifyUserDTO.setUser_id(fromDBModifyUserDTO.getUser_id());
		modifyUserDTO.setUser_name(fromDBModifyUserDTO.getUser_name());
		modifyUserDTO.setUser_idx(loginUserDTO.getUser_idx());
	}
	
	public void modifyUserInfo(UserDTO modifyUserDTO) {
	  	modifyUserDTO.setUser_idx(loginUserDTO.getUser_idx());
	  	userDAO.modifyUserInfo(modifyUserDTO);
	  }
}
