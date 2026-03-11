package com.demo.reservations.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private String attendee;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    public ReservationEntity() {}

    public ReservationEntity(String roomId, String attendee,
                              LocalDateTime startTime, LocalDateTime endTime) {
        this.roomId    = roomId;
        this.attendee  = attendee;
        this.startTime = startTime;
        this.endTime   = endTime;
    }

    public Long getId()                  { return id; }
    public String getRoomId()            { return roomId; }
    public String getAttendee()          { return attendee; }
    public LocalDateTime getStartTime()  { return startTime; }
    public LocalDateTime getEndTime()    { return endTime; }

    public void setId(Long id)                      { this.id = id; }
    public void setRoomId(String roomId)            { this.roomId = roomId; }
    public void setAttendee(String attendee)        { this.attendee = attendee; }
    public void setStartTime(LocalDateTime start)   { this.startTime = start; }
    public void setEndTime(LocalDateTime end)       { this.endTime = end; }
}
