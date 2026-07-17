package amirka.u5w3d5.controllers;

import amirka.u5w3d5.entities.Event;
import amirka.u5w3d5.entities.User;
import amirka.u5w3d5.exceptions.ValidationEx;
import amirka.u5w3d5.payloads.EventDTO;
import amirka.u5w3d5.payloads.EventResponseDTO;
import amirka.u5w3d5.services.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventResponseDTO> getAllEvents() {
        return eventService.getAllEvents()
                .stream()
                .map(eventService::toResponseDTO)
                .toList();
    }

    @GetMapping("/{eventId}")
    public EventResponseDTO getEventById(@PathVariable UUID eventId) {
        return eventService.toResponseDTO(eventService.getEventById(eventId));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public List<EventResponseDTO> getMyEvents(@AuthenticationPrincipal User currentUser) {
        return eventService.getEventsByOrganizer(currentUser)
                .stream()
                .map(eventService::toResponseDTO)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public EventResponseDTO createEvent(@RequestBody @Valid EventDTO dto,
                                        BindingResult validationResult,
                                        @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            throw new ValidationEx(validationResult.getFieldErrors()
                    .stream()
                    .map(fe -> fe.getDefaultMessage())
                    .toList());
        }
        Event event = eventService.createEvent(dto, currentUser);
        return eventService.toResponseDTO(event);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public EventResponseDTO updateEvent(@PathVariable UUID eventId,
                                        @RequestBody @Valid EventDTO dto,
                                        BindingResult validationResult,
                                        @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            throw new ValidationEx(validationResult.getFieldErrors()
                    .stream()
                    .map(fe -> fe.getDefaultMessage())
                    .toList());
        }
        Event updated = eventService.updateEvent(eventId, dto, currentUser);
        return eventService.toResponseDTO(updated);
    }


    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public void deleteEvent(@PathVariable UUID eventId, @AuthenticationPrincipal User currentUser) {
        eventService.deleteEvent(eventId, currentUser);
    }
}
