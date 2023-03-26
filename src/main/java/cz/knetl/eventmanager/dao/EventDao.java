package cz.knetl.eventmanager.dao;

import cz.knetl.eventmanager.bo.Event;
import cz.knetl.eventmanager.exception.CRUDException;
import java.util.Date;

/**
 * CRUD operations for a Event
 */
public interface EventDao {


    /**
     *  Save an new Event.
     *
     * @Param newEvent - event to be stored
     * @return Unique ID of an new event
     * @throws CRUDException if saving new event failed
     *
     * */
    String create(final Event newEvent) throws CRUDException;


    /**
     *  Permanently delete an Event
     *
     * @Param rowKey - Unique event ID
     * @return Integer http status of operation
     * @throws CRUDException if removing event failed
     * */
    void permanentlyDelete(final String rowKey) throws CRUDException;


    /**
     *  Returns events by a date.
     *
     * @Param date - searching parameter
     * @return Collection of events start that date
     * @throws CRUDException if listing events failed
     * */
    Iterable<Event> findByDate(final Date date) throws CRUDException;


    /**
     *  Returns events by a month.
     *
     *  @Param date - searching parameter. Day is omitted.
     *  @return List of events
     *  @throws CRUDException if listing events failed
     * */
    Iterable<Event> findByMonth(final Date date) throws CRUDException;

}
