package com.juju.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.juju.spring.dto.ContentDTO;
import com.juju.spring.mapper.BoardMapper;

@Repository
public class BoardDAO {

	@Autowired
	private BoardMapper boardMapper;
	
	public void addContentInfo(ContentDTO writeContentDTO) {
		boardMapper.addContentInfo(writeContentDTO);
	}
	
	public String getBoardInfoName(int board_info_idx) {
		String board_info_name = boardMapper.getBoardInfoName(board_info_idx);
		return board_info_name;
	}
}
