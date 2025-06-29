package ru.job4j.bmb.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
    List<MoodLog> findAll();

    List<MoodLog> findByUserId(Long userId);

    Stream<MoodLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query(value = "SELECT * FROM mb_user u WHERE u.id NOT IN (SELECT user_id FROM mb_mood_log WHERE created_at BETWEEN :startOfDay AND :endOfDay)",
            nativeQuery = true)
    List<User> findUsersWhoDidNotVoteToday(long startOfDay, long endOfDay);

    @Query("SELECT m FROM MoodLog m WHERE m.user.id = :userId AND m.createdAt >= :weekStart")
    List<MoodLog> findMoodLogsForWeek(@Param("userId") Long userId,
                                      @Param("weekStart") long weekStart);

    @Query("SELECT m FROM MoodLog m WHERE m.user.id = :userId AND m.createdAt >= :monthStart")
    List<MoodLog> findMoodLogsForMonth(@Param("userId") Long userId,
                                       @Param("monthStart") long monthStart);

}