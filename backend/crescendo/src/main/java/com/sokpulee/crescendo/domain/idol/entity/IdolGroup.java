package com.sokpulee.crescendo.domain.idol.entity;

import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdolGroup extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idol_group_id")
    private Long id;

    @Column(length = 50)
    private String name;

    private Integer peopleNum;

    @Column(length = 500)
    private String introduction;

    @Column(length = 500)
    private String profile;

    @Column(length = 500)
    private String banner;

    private Integer favoriteCnt;

    public void plusFavoriteCnt(){
        favoriteCnt++;
    }

    public void minusFavoriteCnt(){
        favoriteCnt--;
    }
}