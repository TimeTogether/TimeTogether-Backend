package timetogether.where2meet.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import timetogether.where2meet.Where2meet;

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

  public Where2meet findById(Long id) {
    String jpql = "SELECT w FROM Where2meet w WHERE w.locationId = :id";
    try {
      return entityManager.createQuery(jpql, Where2meet.class)
              .setParameter("id", id)
              .getSingleResult();
    } catch (NoResultException e) {
      return null; // 해당 ID의 엔티티가 없는 경우 null 반환
    }
  }
}
