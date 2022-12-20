package ru.practicum.explorewithme.item.dto;

import ru.practicum.explorewithme.item.model.Comment;
import ru.practicum.explorewithme.item.model.Item;
import ru.practicum.explorewithme.user.model.User;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {

        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment fromCommentDto(CommentDto comment, User user, Item item) {

        return Comment.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(user)
                .item(item)
                .created(comment.getCreated())
                .build();
    }
}
