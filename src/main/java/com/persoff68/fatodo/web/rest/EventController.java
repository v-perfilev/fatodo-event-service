package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.service.EventService;
import com.persoff68.fatodo.service.EventServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(EventController.ENDPOINT)
@RequiredArgsConstructor
public class EventController {
    static final String ENDPOINT = "/api/event";

    private final EventServiceFactory eventServiceFactory;

    @PostMapping
    public ResponseEntity<Void> addEvent(@Valid @RequestBody EventDTO eventDTO) {
        EventService eventService = eventServiceFactory.create(eventDTO.getType());
        eventService.addEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
