package cz.knetl.eventmanager.service;

import cz.knetl.eventmanager.bo.Event;
import cz.knetl.eventmanager.exception.CRUDException;
import cz.knetl.eventmanager.mapper.EventMapper;
import cz.knetl.eventmanager.dao.EventDao;
import cz.knetl.eventmanager.dto.EventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * CRUD operations for a Event model object
 */
@Service
public class EventService {

    @Autowired
    private EventDao eventDao;

    private final static Logger logger = LoggerFactory.getLogger("EventService");

    /**
     * Save an new Event.
     *
     * @return Unique ID of an new event
     * @throws CRUDException if saving new event failed
     */
    public String create(final EventDto eventDto) {
        Event event = EventMapper.convert(eventDto);
        try {
            return eventDao.create(event);
        } catch (CRUDException e) {
            logger.error("Creating an event error. " + e.getMessage());
            return null;
        }

    }

    /**
     *  Find events by a date.
     *
     * @return Collection of result events. Null if listing events failed
     * */
    public List<EventDto> listEventsByDate(final Date date) {
        try {
            Iterable<Event> result = eventDao.findByDate(date);
            List<EventDto> events = new ArrayList<>();
            result.forEach(e -> events.add(EventMapper.convert(e)));
            Collections.sort(events);
            return events;
        } catch (CRUDException e) {
            logger.error("Listing events error. " + e.getMessage());
            return null;
        }

    }

    /**
     *  Find events by a month.
     *
     *  @return List of events. Null if listing events failed
     * */
    public List<EventDto> listEventsByMonth(final Date date) {
        try {
            Iterable<Event> result = eventDao.findByMonth(date);
            List<EventDto> events = new ArrayList<>();
            result.forEach(e -> events.add(EventMapper.convert(e)));
            Collections.sort(events);
            return events;
        } catch (CRUDException e) {
            logger.error("Listing events error. " + e.getMessage());
            return null;
        }
    }

    /**
     *  Permanently delete an Event
     *
     * @return Status - The event is removed when status is true.
     *
     * */
    public boolean permanentlyDelete(final String rowKey) {
        try {
             eventDao.permanentlyDelete(rowKey);
             return true;
        } catch (CRUDException e) {
            logger.error("Removing an event error. " + e.getMessage());
            return false;
        }
    }

}
