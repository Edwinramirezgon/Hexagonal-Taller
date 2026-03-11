package com.demo.reservations.domain.model;

import java.time.LocalDateTime;

public class Reservation {

    private Long id;
    private String roomId;      
    private String attendee;    
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Reservation() {}


    public static Reservation create(String roomId, String attendee,
                                     LocalDateTime startTime, LocalDateTime endTime) {
        if (roomId == null || roomId.isBlank())
            throw new IllegalArgumentException("El identificador de la sala es obligatorio.");
        if (attendee == null || attendee.isBlank())
            throw new IllegalArgumentException("El nombre del asistente es obligatorio.");
        if (startTime == null)
            throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
        if (endTime == null)
            throw new IllegalArgumentException("La fecha de fin es obligatoria.");
        if (!endTime.isAfter(startTime))
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio.");

        Reservation reservation = new Reservation();
        reservation.roomId    = roomId;
        reservation.attendee  = attendee;
        reservation.startTime = startTime;
        reservation.endTime   = endTime;
        return reservation;
    }

  
    public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return this.startTime.isBefore(otherEnd) && otherStart.isBefore(this.endTime);
    }


    public Long getId()                  { return id; }
    public void setId(Long id)           { this.id = id; }

    public String getRoomId()            { return roomId; }
    public String getAttendee()          { return attendee; }
    public LocalDateTime getStartTime()  { return startTime; }
    public LocalDateTime getEndTime()    { return endTime; }

    public void setRoomId(String roomId)            { this.roomId = roomId; }
    public void setAttendee(String attendee)        { this.attendee = attendee; }
    public void setStartTime(LocalDateTime start)   { this.startTime = start; }
    public void setEndTime(LocalDateTime end)       { this.endTime = end; }
}
