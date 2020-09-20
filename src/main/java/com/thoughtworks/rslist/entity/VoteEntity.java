package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "vote")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteEntity {

    @Id
    @GeneratedValue
    private Integer id;

    private int num;
    private Timestamp time;

    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventEntity rsEvents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
