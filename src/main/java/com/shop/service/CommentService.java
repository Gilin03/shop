package com.shop.service;


import com.shop.dto.CommentDTO;
import com.shop.entity.CommentEntity;
import com.shop.entity.Item;
import com.shop.repository.CommentRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;

    public Long save(CommentDTO commentDTO) {
        Optional<Item> optionalItem = itemRepository.findById(commentDTO.getItemId());
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, item);
            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }
    }

    public List<CommentDTO> findAll(Long itemId) {
        Item item = itemRepository.findById(itemId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByItemOrderByIdDesc(item);
        /* EntityList -> DTOList */
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity, itemId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    public CommentDTO findById(Long commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        return CommentDTO.toCommentDTO(commentEntity, commentEntity.getItem().getId());
    }


    public List<CommentDTO> findByItemId(Long itemId) {
        List<CommentEntity> commentEntities = commentRepository.findByItemId(itemId);

        return commentEntities.stream()
                .map(commentEntity -> CommentDTO.toCommentDTO(commentEntity, itemId))
                .collect(Collectors.toList());
    }

    public CommentDTO update(Long id, CommentDTO commentDTO) throws Exception {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new Exception("Comment not found"));
        comment.update(commentDTO);
        commentRepository.save(comment);
        return CommentDTO.fromEntity(comment);
    }




}
