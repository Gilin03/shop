package com.shop.dto;


import com.shop.entity.CommentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Long id;
    private String commentWriter;
    private String commentWriterEmail;
    private String commentContents;
    private Long itemId;
    private LocalDateTime commentCreatedTime;


    public static CommentDTO toCommentDTO(CommentEntity commentEntity, Long itemId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        commentDTO.setItemId(itemId);
        return commentDTO;
    }

    public static CommentDTO fromEntity(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentWriterEmail(commentEntity.getCommentWriterEmail());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        // 아래의 코드는 commentEntity에서 itemId를 얻을 수 있는 방법에 따라 수정이 필요할 수 있습니다.
        commentDTO.setItemId(commentEntity.getItem().getId());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        return commentDTO;
    }

    public String getCommentContents() {
        return commentContents;
    }
}
