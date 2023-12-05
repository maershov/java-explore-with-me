package ru.practicum.comments.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public static Comment toComment(NewCommentDto newCommentDto, User user, Event event) {
        return Comment
                .builder()
                .id(null)
                .author(user)
                .text(newCommentDto.getText())
                .event(event)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto
                .builder()
                .author(comment.getAuthor().getId())
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .id(comment.getId())
                .event(comment.getEvent().getId())
                .build();
    }

    public static List<CommentResponseDto> toListOfCommentResponseDto(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentResponseDto).collect(Collectors.toList());
    }
}
