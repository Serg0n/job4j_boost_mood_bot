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

    @Query("SELECT u FROM User u WHERE u.id NOT IN ("
            + "SELECT m.user.id FROM MoodLog m WHERE m.createdAt BETWEEN :startOfDay AND :endOfDay)")
    List<User> findUsersWhoDidNotVoteToday(@Param("startOfDay") long startOfDay,
                                           @Param("endOfDay") long endOfDay);

    @Query("SELECT m FROM MoodLog m WHERE m.user.id = :userId AND m.createdAt >= :weekStart")
    List<MoodLog> findMoodLogsForWeek(@Param("userId") Long userId,
                                      @Param("weekStart") long weekStart);

    @Query("SELECT m FROM MoodLog m WHERE m.user.id = :userId AND m.createdAt >= :monthStart")
    List<MoodLog> findMoodLogsForMonth(@Param("userId") Long userId,
                                       @Param("monthStart") long monthStart);

}