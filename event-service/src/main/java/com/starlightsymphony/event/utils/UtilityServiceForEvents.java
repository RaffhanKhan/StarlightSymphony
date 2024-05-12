package com.starlightsymphony.event.utils;

import com.starlightsymphony.event.exceptions.CustomException;
import com.starlightsymphony.event.exceptions.ErrorResponse;
import com.starlightsymphony.event.model.Events;
import com.starlightsymphony.event.repository.EventsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilityServiceForEvents {
    public static final Logger LOGGER = LoggerFactory.getLogger(UtilityServiceForEvents.class);

    private final EventsRepository eventsRepository;


    public void saveEvent(Events events) {
        try {
            eventsRepository.save(events);
        } catch (Exception e){
            LOGGER.error("Failed saving events table-{}", e.getMessage());
            throw new CustomException(
                    new ErrorResponse(EventConstants.ERROR, "1002", "failed saving in db")
            );
        }
    }

    public void deleteEvent(Events events) {
        try {
            eventsRepository.delete(events);
        } catch (Exception e){
            LOGGER.error("Failed delete events table-{}", e.getMessage());
            throw new CustomException(
                    new ErrorResponse(EventConstants.ERROR, "1002", "failed delete in db")
            );
        }
    }

    public void noEventsCheck(Events events){
        if (null == events){
            throw new CustomException(
                    new ErrorResponse(EventConstants.ERROR, "1003", "No event found with this Event id")
            );
        }
    }

    public void mandatoryFieldsMissingError(Events events){
        if(null == events.getEventId()) {
            throw new CustomException(
                    new ErrorResponse(EventConstants.ERROR, "1001", "mandatory Fields missing"));
        }
    }

}
