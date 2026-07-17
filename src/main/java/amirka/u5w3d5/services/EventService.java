package amirka.u5w3d5.services;

import amirka.u5w3d5.entities.Event;
import amirka.u5w3d5.entities.User;
import amirka.u5w3d5.enums.Role;
import amirka.u5w3d5.exceptions.ForbiddenEx;
import amirka.u5w3d5.exceptions.NotFoundEx;
import amirka.u5w3d5.payloads.EventDTO;
import amirka.u5w3d5.payloads.EventResponseDTO;
import amirka.u5w3d5.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(EventDTO dto, User organizer) {
        if (organizer.getRole() != Role.ORGANIZER) {
            throw new ForbiddenEx("Only organizers can create events");
        }

        Event event = new Event(
                dto.title(),
                dto.description(),
                dto.date(),
                dto.location(),
                dto.availableSpots(),
                organizer
        );

        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEventsByOrganizer(User organizer) {
        return eventRepository.findByOrganizer(organizer);
    }

    public Event getEventById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundEx("Event not found with id: " + id));
    }

    public Event updateEvent(UUID id, EventDTO dto, User currentUser) {
        Event event = getEventById(id);
        checkOwnership(event, currentUser);

        event.setTitle(dto.title());
        event.setDescription(dto.description());
        event.setDate(dto.date());
        event.setLocation(dto.location());
        event.setAvailableSpots(dto.availableSpots());

        return eventRepository.save(event);
    }

    public void deleteEvent(UUID id, User currentUser) {
        Event existing = getEventById(id);
        checkOwnership(existing, currentUser);
        eventRepository.delete(existing);
    }

    private void checkOwnership(Event event, User currentUser) {
        if (!event.getOrganizer()
                .getId()
                .equals(currentUser.getId())) {
            throw new ForbiddenEx("You can only manage your own events");
        }
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public EventResponseDTO toResponseDTO(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getLocation(),
                event.getAvailableSpots(),
                event.getOrganizer()
                        .getId(),
                event.getOrganizer()
                        .getUsername()
        );
    }

}
