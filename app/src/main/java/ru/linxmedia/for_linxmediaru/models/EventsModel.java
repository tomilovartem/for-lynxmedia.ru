package ru.linxmedia.for_linxmediaru.models;


public class EventsModel {

    private EventsItemModel[] events;

    public EventsModel(EventsItemModel[] events) {
        this.events = events;
    }

    public EventsItemModel[] getEvenst() {
        return events;
    }

    public void setEvenst(EventsItemModel[] events) {
        this.events = events;
    }
}
