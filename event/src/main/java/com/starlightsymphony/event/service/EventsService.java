package com.starlightsymphony.event.service;

import com.starlightsymphony.event.model.Events;

import java.util.Map;

public interface EventsService {
    Map<String, Object> createEvent(Events events);

    Map<String, Object> getEventById(long id);

    Map<String, Object> getEventByEventId(Events events);

    Map<String, Object> updateEvent(Events events);

    Map<String, Object> deleteEventByEventId(Events events);
}
