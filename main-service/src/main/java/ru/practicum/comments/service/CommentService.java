package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto addComment(Long userId, Long eventId, NewCommentDto commentDto);

    void deleteCommentById(Long commentId, Long userId);

    List<CommentResponseDto> getAllCommentsByEventId(Long eventId, Integer from, Integer size);

    List<CommentResponseDto> get10LatestCommentsByEventId(Long eventId);

    CommentResponseDto updateComment(Long commentId, Long userId, NewCommentDto newCommentDto);
}
