package com.sokpulee.crescendo.domain.dm.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DmParticipants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dm_participants_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dm_group_id")
    private DmGroup dmGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public DmParticipants(DmGroup dmGroup, User user) {
        this.dmGroup = dmGroup;
        this.user = user;
    }

    public void changeDmGroup(DmGroup dmGroup) {
        this.dmGroup = dmGroup;
    }
}
