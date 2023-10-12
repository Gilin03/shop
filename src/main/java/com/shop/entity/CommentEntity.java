package com.shop.entity;

import com.shop.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = true)
    private String commentWriter;

    @Column
    private String commentContents;

    /* Board:Comment = 1:N */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;



    public static CommentEntity toSaveEntity(CommentDTO commentDTO, Item item) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setItem(item);
        return commentEntity;
    }

    public void update(CommentDTO commentDTO) {
        // 필요한 필드를 업데이트하세요.
        this.commentContents = commentDTO.getCommentContents();
        // ... 다른 필드 업데이트 로직
    }



}
