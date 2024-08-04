package com.sokpulee.crescendo.domain.quiz.repository.quiz;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.quiz.dto.response.QuizListResponse;
import com.sokpulee.crescendo.domain.quiz.entity.QQuiz;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class QuizRepositoryImpl implements CustomQuizRepository {

    private final JPAQueryFactory queryFactory;

    public QuizRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<QuizListResponse> findQuizzes(String title, Pageable pageable) {
        QQuiz quiz = QQuiz.quiz;

        BooleanExpression titleCondition = title != null ? quiz.title.containsIgnoreCase(title) : null;

        List<QuizListResponse> quizzes = queryFactory
                .select(Projections.constructor(QuizListResponse.class,
                        quiz.thumbnailPath,
                        quiz.title,
                        quiz.content))
                .from(quiz)
                .where(titleCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable))
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                .select(quiz.count())
                .from(quiz)
                .where(titleCondition)
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(quizzes, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
        QQuiz quiz = QQuiz.quiz;
        if (pageable.getSort().isUnsorted()) {
            return quiz.createdAt.desc(); // 기본값을 최신순으로 설정
        }

        return pageable.getSort().stream()
                .map(order -> {
                    switch (order.getProperty()) {
                        case "createdAt":
                            return order.isAscending() ? quiz.createdAt.asc() : quiz.createdAt.desc();
                        case "hit":
                            return order.isAscending() ? quiz.hit.asc() : quiz.hit.desc();
                        default:
                            throw new IllegalArgumentException("Unsupported sort property: " + order.getProperty());
                    }
                })
                .findFirst()
                .orElse(quiz.createdAt.desc()); // 기본값을 최신순으로 설정
    }
}
