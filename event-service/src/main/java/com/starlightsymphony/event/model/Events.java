package com.starlightsymphony.event.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "events")
public class Events {
    @Id
    @SequenceGenerator(name = "EVENTS_SEQ_GEN", sequenceName = "EVENTS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENTS_SEQ_GEN")
    private long id;

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "location")
    private String location;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "ticket_price")
    private double ticketPrice;
}
