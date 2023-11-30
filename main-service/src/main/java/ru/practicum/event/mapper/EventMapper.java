package ru.practicum.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.EventUpdateRequestDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.State;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {
    public static EventFullDto toEventFullDto(Event event, Long confirmedRequests, Long views) {
        return EventFullDto
                .builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .id(event.getId())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toDtoShortUser(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto, User initiator, Category category, State state) {
        return Event
                .builder()
                .id(null)
                .eventDate(newEventDto.getEventDate())
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .initiator(initiator)
                .description(newEventDto.getDescription())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .state(state).location(newEventDto.getLocation())
                .publishedOn(null)
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    public static EventShortDto toEventDtoShort(Event event, Long confirmedRequests, Long views) {
        return EventShortDto
                .builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .title(event.getTitle())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toDtoShortUser(event.getInitiator()))
                .paid(event.getPaid())
                .views(views)
                .build();
    }

    public static Event forUpdateDto(EventUpdateRequestDto updateEventDto, Category newCategory, State newState, Event event) {
        if (updateEventDto.getEventDate() != null) {
            event.setEventDate(updateEventDto.getEventDate());
        }
        if (updateEventDto.getAnnotation() != null) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }
        if (newCategory != null) {
            event.setCategory(newCategory);
        }

        if (updateEventDto.getDescription() != null) {
            event.setDescription(updateEventDto.getDescription());
        }

        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }

        if (updateEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }
        event.setState(newState);

        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }

        if (updateEventDto.getTitle() != null) {
            event.setTitle(updateEventDto.getTitle());
        }
        return event;
    }
}