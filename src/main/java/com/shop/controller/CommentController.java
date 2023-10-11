package com.shop.controller;


import com.shop.dto.CommentDTO;
import com.shop.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // Correct import for Model
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")

public class CommentController {
    private final CommentService commentService;


    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO) {
        System.out.println("commentDTO = " + commentDTO);
        Long saveResult = commentService.save(commentDTO);
        if (saveResult != null) {
            List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getItemId());
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/edit/{commentId}")
    public String editComment(@PathVariable Long commentId, Model model) {
        CommentDTO commentDTO = commentService.findById(commentId);
        model.addAttribute("comment", commentDTO);

        Long itemId = commentDTO.getItemId();  // assuming CommentDTO has getItemId() method
        List<CommentDTO> comments = commentService.findByItemId(itemId);
        model.addAttribute("comments", comments);

        return "editComment";
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @ModelAttribute CommentDTO commentDTO) {
        try {
            CommentDTO updatedComment = commentService.update(id, commentDTO);
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Comment could not be updated", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
