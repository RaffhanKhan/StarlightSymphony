package com.starlightsymphony.event.repository;

import com.starlightsymphony.event.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsRepository extends JpaRepository<Events, Long> {

    Events getEventsById(long id);
    boolean existsByEventId(String id);

    Events getEventsByEventId(String eventId);
}
