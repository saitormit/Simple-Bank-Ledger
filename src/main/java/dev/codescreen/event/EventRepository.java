package dev.codescreen.event;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//EventRepository stores transaction events for each user
@Repository
public class EventRepository {
    private final Map<String, List<Event>> eventRepository;

    public EventRepository() {
        this.eventRepository = new HashMap<>();
    }

    public Map<String, List<Event>> getEventRepository() {
        return eventRepository;
    }

    public void addEventToRepository(String userId, Event event) {
        List<Event> eventList = eventRepository.getOrDefault(userId, new ArrayList<>());
        eventList.add(event);
        eventRepository.put(userId, eventList);
    }
}