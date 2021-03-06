package com.juju.spring.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.juju.spring.dto.ContentDTO;
import com.juju.spring.dto.PageDTO;
import com.juju.spring.dto.UserDTO;
import com.juju.spring.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@Resource(name = "loginUserDTO")
	private UserDTO loginUserDTO;
	
	@GetMapping("/main")
	public String main(@RequestParam("board_info_idx") int board_info_idx, @RequestParam(value="page", defaultValue="1") int page ,Model model) {
		
		model.addAttribute("board_info_idx", board_info_idx);
		
		String board_info_name = boardService.getBoardInfoName(board_info_idx);
		model.addAttribute("board_info_name", board_info_name);
		
		List<ContentDTO> contentList = boardService.getContentList(board_info_idx, page);
		model.addAttribute("contentList", contentList);
		
		PageDTO pageDTO = boardService.getContentCnt(board_info_idx, page);
		model.addAttribute("pageDTO", pageDTO);
		
		model.addAttribute("page", page);
		
		return "board/main";
	}
	
	@GetMapping("/read")
	public String read(@RequestParam("board_info_idx") int board_info_idx, @RequestParam("content_idx") int content_idx, @RequestParam("page") int page, Model model) {
		
		model.addAttribute("board_info_idx", board_info_idx);
		model.addAttribute("content_idx", content_idx);
		
		model.addAttribute("loginUserDTO", loginUserDTO);
		
		ContentDTO readContentDTO = boardService.getContentInfo(content_idx);
		model.addAttribute("readContentDTO", readContentDTO);
		
		model.addAttribute("page", page);
		
		return "board/read";
	}
	
	@GetMapping("/write")
	public String write(@ModelAttribute("writeContentDTO") ContentDTO writeContentDTO, @RequestParam("board_info_idx") int board_info_idx) {
		
		writeContentDTO.setContent_board_idx(board_info_idx);
		
		return "board/write";
	}
	
	@PostMapping("/write_proc")
	public String writeProc(@Valid @ModelAttribute("writeContentDTO") ContentDTO writeContentDTO, BindingResult result) {
	
		if(result.hasErrors()) {
			return "board/write";
		}
		
		boardService.addContentInfo(writeContentDTO);
		
		return "board/write_success";
	}
	
	@GetMapping("/modify")
	public String modify(@RequestParam("board_info_idx") int board_info_idx, @RequestParam("content_idx") int content_idx, @ModelAttribute("modifyContentDTO") ContentDTO modifyContentDTO, @RequestParam("page") int page, Model model) {
		
		model.addAttribute("board_info_idx", board_info_idx);
		model.addAttribute("content_idx", content_idx);
		model.addAttribute("page", page);
		
		ContentDTO fromDBContentDTO = boardService.getContentInfo(content_idx);
		
		modifyContentDTO.setContent_board_idx(board_info_idx);
		modifyContentDTO.setContent_idx(content_idx);
		modifyContentDTO.setContent_writer_idx(fromDBContentDTO.getContent_writer_idx());
		modifyContentDTO.setContent_writer_name(fromDBContentDTO.getContent_writer_name());
		modifyContentDTO.setContent_subject(fromDBContentDTO.getContent_subject());
		modifyContentDTO.setContent_text(fromDBContentDTO.getContent_text());
		modifyContentDTO.setContent_date(fromDBContentDTO.getContent_date());
		modifyContentDTO.setContent_file(fromDBContentDTO.getContent_file());
		
		return "board/modify";
	}
	
	@PostMapping("/modify_proc")
	public String modifyProc(@Valid @ModelAttribute("modifyContentDTO") ContentDTO modifyContentDTO, @RequestParam("page") int page, BindingResult result, Model model) {
		
		model.addAttribute("page", page);
		
		if(result.hasErrors()) {
			System.out.println(result.getFieldErrors());
			return "board/modify";
		}
		
		boardService.modifyContentInfo(modifyContentDTO);
		
		return "board/modify_success";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("board_info_idx") int board_info_idx, @RequestParam("content_idx") int content_idx, Model model) {
		
		model.addAttribute("board_info_idx", board_info_idx);
		model.addAttribute("content_idx", content_idx);
		
		boardService.deleteContent(content_idx);
		
		return "board/delete";
	}
	
	@GetMapping("/not_writer")
	public String notWriter() {
		return "/board/not_writer";
	}
	
}
