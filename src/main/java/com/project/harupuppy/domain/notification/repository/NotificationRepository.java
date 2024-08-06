package com.project.harupuppy.domain.notification.repository;

import com.project.harupuppy.domain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n where n.receiver.userId = :userId")
    List<Notification> findByUserId(@Param("userId") Long userId);
}