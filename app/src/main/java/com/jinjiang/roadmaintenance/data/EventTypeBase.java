package com.jinjiang.roadmaintenance.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wuwei on 2017/7/6.
 */

public class EventTypeBase implements Serializable {

    private EventType eventType;
    private ArrayList<EventAttr> eventAttrsList = new ArrayList<>();

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public ArrayList<EventAttr> getEventAttrsList() {
        return eventAttrsList;
    }

    public void setEventAttrsList(ArrayList<EventAttr> eventAttrsList) {
        this.eventAttrsList = eventAttrsList;
    }
}
