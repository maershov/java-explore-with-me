package ru.practicum.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.event.model.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class EventParticipationRequestStatusUpdateRequestDto {
    @NotNull
    private final RequestStatus status;
    private final List<Long> requestIds;
}
