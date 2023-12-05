package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.service.CommentService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
public class EventCommentController {

    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getAllCommentsByEventId(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getAllCommentsByEventId(eventId, from, size);
    }

    @GetMapping("/last10")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> get10LatestCommentsByEventId(@PathVariable Long eventId) {
        return commentService.get10LatestCommentsByEventId(eventId);
    }
}

