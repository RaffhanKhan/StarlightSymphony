package com.starlightsymphony.event.controller;

import com.starlightsymphony.event.model.Events;
import com.starlightsymphony.event.service.EventsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EventsController {

    private final EventsService eventsService;

    @PostMapping(value = "/createevent", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> createEvent(@RequestBody Events events){
        Map<String, Object> response = eventsService.createEvent(events);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/geteventbyid{id}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getEventById(@RequestParam long id){
        Map<String, Object> response = eventsService.getEventById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/geteventbyeventId", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getEventByEventId(@RequestBody Events events){
        Map<String, Object> response = eventsService.getEventByEventId(events);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/updateevent", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> updateEvent(@RequestBody Events events){
        Map<String, Object> response = eventsService.updateEvent(events);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/deleteevent", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> deleteEventByEventId(@RequestBody Events events){
        Map<String, Object> response = eventsService.deleteEventByEventId(events);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/getallevents", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getAllEvents(){
        Map<String, Object> response = eventsService.getAllEvents();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
