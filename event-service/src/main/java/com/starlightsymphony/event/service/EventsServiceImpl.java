package com.starlightsymphony.event.service;

import com.starlightsymphony.event.exceptions.CustomException;
import com.starlightsymphony.event.exceptions.ErrorResponse;
import com.starlightsymphony.event.model.Events;
import com.starlightsymphony.event.repository.EventsRepository;
import com.starlightsymphony.event.utils.EventConstants;
import com.starlightsymphony.event.utils.HelperUtil;
import com.starlightsymphony.event.utils.UtilityServiceForEvents;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService{

    public static final Logger LOGGER = LoggerFactory.getLogger(EventsServiceImpl.class);

    private final UtilityServiceForEvents utilityServiceForEvents;
    private final EventsRepository eventsRepository;


    @Override
    public Map<String, Object> createEvent(Events events) {
        LOGGER.debug("ENTER-createEvent-{}", events.toString());
        Map<String, Object> response = new HashMap<>();
        List<String> missingFields = validateMandatoryFields(events);
        if(!missingFields.isEmpty()){
            throw  new CustomException(
                    new ErrorResponse(EventConstants.ERROR, "1001", "mandatory Fields missing"));
        }
        events.setEventId(generateUniqueEventId(events.getLocation()));
        utilityServiceForEvents.saveEvent(events);


        response.put(EventConstants.STATUS, EventConstants.SUCCESS);
        response.put(EventConstants.PAYLOAD, new ErrorResponse(EventConstants.SUCCESS, "2001", "Created Event"));
        response.put(EventConstants.RESPONSE_BODY, events);
        return response;
    }

    @Override
    public Map<String, Object> getEventById(long id) {
        LOGGER.debug("ENTER-getEventById-{}", id);

        Map<String, Object> response = new HashMap<>();
        Events events = eventsRepository.getEventsById(id);
        if (null == events){
            throw new CustomException(
                    new ErrorResponse(EventConstants.ERROR, "1003", "No event found with this id")
            );
        }
        response.put(EventConstants.STATUS, EventConstants.SUCCESS);
        response.put(EventConstants.PAYLOAD, new ErrorResponse(EventConstants.SUCCESS, "2002", "Found event"));
        response.put("eventDetails", events);
        LOGGER.debug("EXIT-getEventById-{}", response);

        return response;
    }

    @Override
    public Map<String, Object> getEventByEventId(Events request) {
        LOGGER.debug("ENTER-getEventByEventId-{}", request.getEventId());

        Map<String, Object> response = new HashMap<>();
        Events events = eventsRepository.getEventsByEventId(request.getEventId());

        utilityServiceForEvents.noEventsCheck(events);

        response.put(EventConstants.STATUS, EventConstants.SUCCESS);
        response.put(EventConstants.PAYLOAD, new ErrorResponse(EventConstants.SUCCESS, "2003", "Found event"));
        response.put("eventDetails", events);
        LOGGER.debug("EXIT-getEventByEventId-{}", response);

        return response;
    }

    @Override
    @Transactional
    public Map<String, Object> updateEvent(Events request) {
        LOGGER.debug("ENTER-updateEvent-{}", request);

        utilityServiceForEvents.mandatoryFieldsMissingError(request);
        Events events = eventsRepository.getEventsByEventId(request.getEventId());
        utilityServiceForEvents.noEventsCheck(events);

        updateFields(request, events);

        Map<String, Object> response = new HashMap<>();
        utilityServiceForEvents.saveEvent(events);
        response.put(EventConstants.STATUS, EventConstants.SUCCESS);
        response.put(EventConstants.PAYLOAD, new ErrorResponse(EventConstants.SUCCESS, "2004", "Event Updated"));
        response.put("eventDetails", events);
        LOGGER.debug("EXIT-updateEvent-{}", response);

        return response;
    }

    @Override
    public Map<String, Object> deleteEventByEventId(Events request) {
        LOGGER.debug("ENTER-deleteEventByEventId-{}", request.getEventId());

        Map<String, Object> response = new HashMap<>();
        Events events = eventsRepository.getEventsByEventId(request.getEventId());

        utilityServiceForEvents.noEventsCheck(events);
        
        utilityServiceForEvents.deleteEvent(events);
        response.put(EventConstants.STATUS, EventConstants.SUCCESS);
        response.put(EventConstants.PAYLOAD, new ErrorResponse(EventConstants.SUCCESS, "2005", "Deleted event successfully"));
        LOGGER.debug("EXIT-deleteEventByEventId-{}", response);

        return response;
    }

    @Override
    public Map<String, Object> getAllEvents() {
        LOGGER.debug("ENTER-getAllEvents");

        List<Events> eventsList = eventsRepository.findAll();
        eventsList.sort(Comparator.comparing(Events::getStartDate));
        Map<String, Object> response = new HashMap<>();
        response.put("eventsList", eventsList);
        response.put(EventConstants.STATUS, EventConstants.STATUS);
        return response;
    }

    private List<String> validateMandatoryFields(Events events) {
        List<String> missingFields = new ArrayList<>();
        if(HelperUtil.checkNull(events.getEventName())) {
            missingFields.add("Event Name");
        }
        if(HelperUtil.checkNull(events.getLocation())) {
            missingFields.add("Event Description");
        }
        if(events.getCapacity() ==0) {
            missingFields.add("Capacity");
        }
        if(events.getTicketPrice() ==0) {
            missingFields.add("Ticket Price");
        }
        if(HelperUtil.checkNull(String.valueOf(events.getStartDate()))){
            missingFields.add("Start Date");
        }
        if(HelperUtil.checkNull(String.valueOf(events.getEndDate()))){
            missingFields.add("End Date");
        }
        if(HelperUtil.checkNull(String.valueOf(events.getEventId()))){
            missingFields.add("Event Id");
        }

        return missingFields;
    }

    public String generateUniqueEventId(String location) {
        String eventId;
        do {
            // Generate a candidate event ID
            eventId = generateEventId(location);
        } while (eventsRepository.existsByEventId(eventId)); // Check if the generated ID already exists
        return eventId;
    }
    public static String generateEventId(String location) {
        // Extract the first 3 words of the location
        StringBuilder eventIdBuilder = new StringBuilder();
        String[] words = location.split("\\s+");
        for (String word : words) {
            if (!word.isEmpty()) {
                eventIdBuilder.append(word.substring(0, Math.min(word.length(), 3)).toUpperCase());
            }
        }

        // Append the current month and year
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("MMyy"));
        eventIdBuilder.append(formattedDate);

        // Generate a 5-digit random number
        Random random = new Random();
        int randomNumber = random.nextInt(90000) + 10000; // Generates a random number between 10000 and 99999
        eventIdBuilder.append(randomNumber);

        return eventIdBuilder.toString();
    }

    private void updateFields(Events request, Events existingEvent) {
        // Changing only fields which are updated
        if (!HelperUtil.isNullOrEmpty(request.getEventName())) {
            existingEvent.setEventName(request.getEventName());
        }
        if (!HelperUtil.isNullOrEmpty(request.getDescription())) {
            existingEvent.setDescription(request.getDescription());
        }
        if (request.getStartDate() != null) {
            existingEvent.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            existingEvent.setEndDate(request.getEndDate());
        }
        if (!HelperUtil.isNullOrEmpty(request.getLocation())) {
            existingEvent.setLocation(request.getLocation());
        }
        if (HelperUtil.isNotDefaultInt(request.getCapacity())) {
            existingEvent.setCapacity(request.getCapacity());
        }
        if (HelperUtil.isNotDefaultDouble(request.getTicketPrice())) {
            existingEvent.setTicketPrice(request.getTicketPrice());
        }
    }


}
