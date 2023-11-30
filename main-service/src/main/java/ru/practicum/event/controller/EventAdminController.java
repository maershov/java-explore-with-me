package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.State;
import ru.practicum.event.service.EventService;


import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEvent(@RequestParam(required = false) List<Long> users,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false) List<State> states,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                       @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                       @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {

        return eventService.getFullEventInfoByParam(users, categories, states, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventState(@PathVariable Long eventId, @RequestBody  UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateEvent(eventId, updateEventAdminRequest);
    }

}
