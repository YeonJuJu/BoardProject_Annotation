package com.juju.spring.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.juju.spring.dto.ContentDTO;

public interface BoardMapper {
	
	@Insert("INSERT INTO CONTENT_TABLE VALUES(CONTENT_SEQ.NEXTVAL, #{content_subject}, #{content_text}, #{content_file, jdbcType=VARCHAR}, #{content_writer_idx}, #{content_board_idx}, SYSDATE)")
	public void addContentInfo(ContentDTO writeContentDTO);

	@Select("SELECT BOARD_INFO_NAME FROM BOARD_INFO_TABLE WHERE BOARD_INFO_IDX=#{board_info_idx}")
	public String getBoardInfoName(int board_info_idx);
}
