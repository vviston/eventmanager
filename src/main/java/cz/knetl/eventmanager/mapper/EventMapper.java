package cz.knetl.eventmanager.mapper;

import cz.knetl.eventmanager.bo.Event;
import cz.knetl.eventmanager.dto.EventDto;
import org.springframework.util.StringUtils;
import java.util.Date;

/**
 *  Map a Event model object to EventDto API representation and vice versa
 * */
public class EventMapper {

    public static Event convert(final EventDto eventDto){
        Event event = new Event(
                eventDto.getName(),
                eventDto.getText(),
                eventDto.getStart().getTime(),
                eventDto.getEnd().getTime()
                );
        if(!StringUtils.isEmpty(eventDto.getRowKey())){
            event.setRowKey(eventDto.getRowKey());
        }
        return event;
    }

    public static EventDto convert(final Event event){
        EventDto eventDto = new EventDto(
                event.getName(),
                event.getText(),
                new Date(event.getStartTimestamp()),
                new Date(event.getEndTimestamp())
        );
        eventDto.setRowKey(event.getRowKey());
        return eventDto;
    }
}
