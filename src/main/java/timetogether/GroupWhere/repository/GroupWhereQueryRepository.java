package timetogether.GroupWhere.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import timetogether.GroupWhere.dto.GroupWhereViewResponseDto;

import java.util.List;

@Repository
public class GroupWhereQueryRepository {
  @PersistenceContext
  private EntityManager entityManager;

  public List<GroupWhereViewResponseDto> findAllByGroupIdAndGroupMeetingId(Long groupId, Long groupMeetingId) {
    String query = "SELECT new timetogether.GroupWhere.dto.GroupWhereViewResponseDto(" +
            "gw.id, " +
            "g.id, " +
            "gw.groupWhereName, " +
            "gw.groupWhereUrl, " +
            "gw.count, " +
            "m.id) " +
            "FROM GroupWhere gw " +
            "JOIN gw.group g " +
            "JOIN gw.groupMeeting m " +
            "WHERE g.id = :groupId AND m.id = :groupMeetingId";

    return entityManager.createQuery(query, GroupWhereViewResponseDto.class)
            .setParameter("groupId", groupId)
            .setParameter("groupMeetingId", groupMeetingId)
            .getResultList();
  }
}