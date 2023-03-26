package cz.knetl.eventmanager.bo;

import com.microsoft.azure.storage.table.TableServiceEntity;
import cz.knetl.eventmanager.util.UUIDGenerator;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 *  The Event business object represents a calendar event for Azure Table Storage
 *
 *  partitionKey - the first key is a String that is represent as a date in format MM-yyyy
 *  rowKey - the second key is a String that is represent as 21 digits unique number ID
 *
 * */
public class Event extends TableServiceEntity {

    public static final String TABLE_NAME = "EventTable";

    private String name;
    private String text;
    private Long startTimestamp;
    private Long endTimestamp;
    private String dayKey;

    public Event() {
    }

    public Event(String name, Long startTimestamp, Long endTimestamp) {
        this.name = name;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;

        this.partitionKey = new SimpleDateFormat("MM-yyyy").format(new Date(startTimestamp));
        this.dayKey = new SimpleDateFormat("dd").format(new Date(startTimestamp));
        this.rowKey = UUIDGenerator.getNext(); // more events may have the same start datetime and end datetime values

    }

    public Event(String name, String text, Long startTimestamp, Long endTimestamp) {
        this(name, startTimestamp, endTimestamp);
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

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getDayKey() {
        return dayKey;
    }

    public void setDayKey(String dayKey) {
        this.dayKey = dayKey;
    }
}
