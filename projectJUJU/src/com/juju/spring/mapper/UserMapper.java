package com.juju.spring.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.juju.spring.dto.UserDTO;

public interface UserMapper {
	
	@Select("SELECT USER_NAME FROM USER_TABLE WHERE USER_ID = #{user_id}")
	public String checkID(String user_id);

	@Insert("INSERT INTO USER_TABLE VALUES(USER_SEQ.NEXTVAL, #{user_name}, #{user_id}, #{user_pw})")
	public void addUserInfo(UserDTO joinUserDTO);

	@Select("SELECT USER_IDX, USER_NAME FROM USER_TABLE WHERE USER_ID=#{user_id} AND USER_PW=#{user_pw}")
	public UserDTO getLoginUser(UserDTO tmpLoginUserDTO);
}
