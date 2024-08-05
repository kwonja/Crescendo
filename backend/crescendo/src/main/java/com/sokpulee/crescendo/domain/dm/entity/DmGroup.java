package com.sokpulee.crescendo.domain.dm.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.CreatedAtEntity;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class DmGroup extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dm_group_id")
    private Long id;

    @OneToMany(mappedBy = "dmGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DmParticipants> dmParticipantList = new ArrayList<>();

    @OneToMany(mappedBy = "dmGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DmMessage> dmMessageList = new ArrayList<>();

    public void addDmParticipant(DmParticipants dmParticipants) {
        this.dmParticipantList.add(dmParticipants);
        dmParticipants.changeDmGroup(this);
    }
}