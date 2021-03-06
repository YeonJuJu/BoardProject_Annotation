package com.juju.spring.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import com.juju.spring.dto.ContentDTO;

public interface BoardMapper {
	
	@SelectKey(statement = "select content_seq.nextval from dual", keyProperty = "content_idx", before = true, resultType = int.class)
	
	@Insert("insert into content_table(content_idx, content_subject, content_text, " +
			"content_file, content_writer_idx, content_board_idx, content_date) " +
			"values (#{content_idx}, #{content_subject}, #{content_text}, #{content_file, jdbcType=VARCHAR}, " +
			"#{content_writer_idx}, #{content_board_idx}, sysdate)")
	public void addContentInfo(ContentDTO writeContentDTO);

	@Select("SELECT BOARD_INFO_NAME FROM BOARD_INFO_TABLE WHERE BOARD_INFO_IDX=#{board_info_idx}")
	public String getBoardInfoName(int board_info_idx);

	@Select("SELECT C.CONTENT_IDX, C.CONTENT_SUBJECT, U.USER_NAME CONTENT_WRITER_NAME, "
	        + "       TO_CHAR(C.CONTENT_DATE, 'YYYY-MM-DD') CONTENT_DATE "
	        + "  FROM CONTENT_TABLE C, USER_TABLE U "
	        + " WHERE C.CONTENT_WRITER_IDX = U.USER_IDX "
	        + "   AND C.CONTENT_BOARD_IDX = #{board_info_idx} "
	        + " ORDER BY C.CONTENT_IDX DESC")
	List<ContentDTO> getContentList(int board_info_idx, RowBounds rowBounds);

	@Select("SELECT U.USER_NAME CONTENT_WRITER_NAME, " 
			  + "TO_CHAR(C.CONTENT_DATE, 'YYYY-MM-DD HH24:MI:SS') CONTENT_DATE, " 
      + "C.CONTENT_SUBJECT, C.CONTENT_TEXT, C.CONTENT_FILE, C.CONTENT_WRITER_IDX "
      + "FROM CONTENT_TABLE C, USER_TABLE U "
      + "WHERE C.CONTENT_WRITER_IDX = U.USER_IDX "
      + "AND C.CONTENT_IDX = #{content_idx}")	
	public ContentDTO getContentInfo(int content_idx);
	
	@Update("UPDATE CONTENT_TABLE SET CONTENT_SUBJECT=#{content_subject}, "
	         +"CONTENT_TEXT=#{content_text}, "
	         +"CONTENT_FILE=#{content_file, jdbcType=VARCHAR} "
	         +"WHERE CONTENT_IDX=#{content_idx}")
	public void modifyContentInfo(ContentDTO modifyContentDTO);

	@Select("SELECT COUNT(*) FROM CONTENT_TABLE WHERE CONTENT_BOARD_IDX=#{content_board_idx}")
	public int getContentCnt(int content_board_idx);

	@Delete("DELETE FROM CONTENT_TABLE WHERE CONTENT_IDX=#{content_idx}")
	public void deleteContent(int content_idx);
}
