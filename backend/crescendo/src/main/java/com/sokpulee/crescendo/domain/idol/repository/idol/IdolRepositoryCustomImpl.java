package com.sokpulee.crescendo.domain.idol.repository.idol;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.entity.QIdol;
import com.sokpulee.crescendo.domain.idol.enums.Gender;
import jakarta.persistence.EntityManager;

import java.util.List;

public class IdolRepositoryCustomImpl implements IdolRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public IdolRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Idol> findAllIdolsByGender(Gender gender) {
        QIdol idol = QIdol.idol;
        BooleanExpression genderPredicate = idol.gender.eq(gender);

        return queryFactory.selectFrom(idol)
                .where(genderPredicate)
                .orderBy(idol.winNum.desc())
                .fetch();
    }
}
