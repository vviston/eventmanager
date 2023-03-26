package cz.knetl.eventmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *  The EventDto represents the Event model object in REST API
 * */
public class EventDto implements Comparable<EventDto> {


    private String rowKey;

    @NotBlank(message = "Name is mandatory")
    private String name;
    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    @NotNull
    private Date start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    @NotNull
    private Date end;

    public EventDto() {
    }

    public EventDto(String name, Date start, Date end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public EventDto(String name, String text, Date start, Date end) {
        this(name, start, end);
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    @Override
    public int compareTo(EventDto o) {
       return this.getStart().compareTo(o.getStart());
    }
}
