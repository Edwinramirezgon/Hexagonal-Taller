package com.demo.reservations.infrastructure.adapter.in.dto;

import java.time.LocalDateTime;

public class ReservationResponse {

    private Long id;
    private String roomId;
    private String attendee;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ReservationResponse() {}

    public ReservationResponse(Long id, String roomId, String attendee,
                                LocalDateTime startTime, LocalDateTime endTime) {
        this.id        = id;
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
}
