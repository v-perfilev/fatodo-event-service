package com.persoff68.fatodo.service;

import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.repository.EventUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SystemService {

    private final EventRepository eventRepository;
    private final EventUserRepository eventUserRepository;

    @Transactional
    public void deleteAccountPermanently(UUID userId) {
        eventRepository.deleteContactEventsByUserId(userId);
        eventUserRepository.deleteEventUsersByUserId(userId);
        eventRepository.deleteEmptyEvents();
    }

}
