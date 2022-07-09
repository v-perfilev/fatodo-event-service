package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventControllerIT {

    @Autowired
    EventRepository eventRepository;


    @Test
    void test() {
    }

}
