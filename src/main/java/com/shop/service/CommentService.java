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
}
