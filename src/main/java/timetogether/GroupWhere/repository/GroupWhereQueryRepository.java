package timetogether.GroupWhere.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import timetogether.GroupWhere.dto.GroupWhereChooseResponse;
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

  //장소 최종 결정 가져오는 쿼리
  public GroupWhereChooseResponse findByChosenOne(Long groupId, Long meetingId) {
    String query = "SELECT new timetogether.GroupWhere.dto.GroupWhereChooseResponse(" +
            "gw.id, " +            // groupWhereId
            "g.id, " +             // groupId
            "gw.groupWhereName, " +
            "gw.groupWhereUrl, " +
            "gm.groupMeetId, " +   // groupMeetingId
            "gw.chooseThis) " +    // groupWhereChooseThis
            "FROM GroupWhere gw " +
            "JOIN gw.group g " +
            "JOIN gw.groupMeeting gm " +
            "WHERE g.id = :groupId " +
            "AND gm.groupMeetId = :meetingId " +
            "AND gw.chooseThis = true";

    try {
      return entityManager.createQuery(query, GroupWhereChooseResponse.class)
              .setParameter("groupId", groupId)
              .setParameter("meetingId", meetingId)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}