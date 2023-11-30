package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.RequestStatus;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.requests.dto.EventParticipationRequestStatusUpdateRequestDto;
import ru.practicum.requests.dto.EventParticipationRequestStatusUpdateResponseDto;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.ParticipationRequestMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.repository.ParticipationRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.practicum.requests.mapper.ParticipationRequestMapper.toParticipationRequestDtoList;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId, LocalDateTime localDateTime) {
        Optional<ParticipationRequest> request = participationRequestRepository.findByEventIdAndRequesterId(eventId, userId);
        if (request.isPresent()) {
            throw new DataConflictException("Запрос на участие уже был отправлен");
        }

        User requester = getUser(userId);
        Event event = getEvent(eventId);

        if (userId.equals(event.getInitiator().getId())) {
            throw new DataConflictException("Запрос на участие уже отправлен");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new DataConflictException("Не далось опубликовать событие");
        }
        int limit = event.getParticipantLimit();
        if (limit != 0) {
            Long numberOfRequests = participationRequestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
            if (numberOfRequests >= limit) {
                throw new DataConflictException("Слишком много участников события");
            }
        }

        ParticipationRequest participationRequest = ParticipationRequest
                .builder()
                .created(localDateTime)
                .requester(requester)
                .event(event)
                .status(RequestStatus.PENDING)
                .build();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(RequestStatus.CONFIRMED);
        }
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId).orElseThrow(() -> new UserNotFoundException("Запрос не найден"));
        if (participationRequest.getRequester().getId().equals(userId)) {
            participationRequest.setStatus(RequestStatus.CANCELED);
            return ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
        }
        throw new DataConflictException("Чужой запрос нельзя отменить");

    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getParticipationRequest(Long userId) {
        log.info("Получена информация о заявках пользователя.");
        return toParticipationRequestDtoList(participationRequestRepository.findAllByRequesterId(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getAllUserEventRequests(Long eventId, Long userId) {
        getUser(userId);
        Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new DataConflictException("Пользователь не может просматривать запросы к событию, автором которого он не является");
        }
        log.info("Получена информация о запросах на участие в событии пользователя.");
        return toParticipationRequestDtoList(participationRequestRepository.findAllByEventId(eventId));
    }

    @Override
    @Transactional
    public EventParticipationRequestStatusUpdateResponseDto updateParticipationRequestsStatus(
            EventParticipationRequestStatusUpdateRequestDto updater,
            Long eventId,
            Long userId) {
        RequestStatus status = updater.getStatus();
        if (status == RequestStatus.CONFIRMED || status == RequestStatus.REJECTED) {
            getUser(userId);
            Event event = getEvent(eventId);
            if (!event.getInitiator().getId().equals(userId)) {
                throw new DataConflictException("Пользователь не может обновлять запросы к событию, автором которого он не является");
            }
            Integer participantLimit = event.getParticipantLimit();
            if (!event.getRequestModeration() || participantLimit == 0) {
                throw new DataConflictException("Событию не нужна модерация");
            }
            Long numberOfParticipants = participationRequestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
            if (numberOfParticipants >= participantLimit) {
                throw new DataConflictException("В событии уже максимальное кол-во участников");
            }
            List<ParticipationRequest> requests = participationRequestRepository.findAllByIdIn(updater.getRequestIds());
            RequestStatus newStatus = updater.getStatus();
            for (ParticipationRequest request : requests) {
                if (request.getEvent().getId().equals(eventId)) {
                    if (participantLimit > numberOfParticipants) {
                        if (newStatus == RequestStatus.CONFIRMED && request.getStatus() != RequestStatus.CONFIRMED) {
                            numberOfParticipants++;
                        }
                        request.setStatus(newStatus);
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                    }
                } else {
                    throw new DataConflictException("Запрос и событие не совпадают");
                }
            }
            requests = participationRequestRepository.saveAll(requests);
            List<ParticipationRequestDto> confirmedRequestDtos = toParticipationRequestDtoList(participationRequestRepository.findAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));

            List<ParticipationRequestDto> rejectedRequestDtos = toParticipationRequestDtoList(participationRequestRepository.findAllByEventIdAndStatus(eventId, RequestStatus.REJECTED));

            return new EventParticipationRequestStatusUpdateResponseDto(confirmedRequestDtos, rejectedRequestDtos);
        } else {
            throw new IllegalArgumentException("Доступны только статусы CONFIRMED или REJECTED");
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Неверный ID пользователя."));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new EntityNotFoundException("Неверный ID события."));
    }

}
