package timetogether.where2meet.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class Where2meetQueryRepository{
  @PersistenceContext
  private EntityManager entityManager;


  public Long deleteByLocationIdInMeetingId(Long meetingId) {
    String jpql = "SELECT m.where2meet.locationId FROM Meeting m WHERE m.meetId = :meetingId";
    Long locationId = entityManager.createQuery(jpql, Long.class)
            .setParameter("meetingId", meetingId)
            .getSingleResult();
    return locationId;
  }
}
