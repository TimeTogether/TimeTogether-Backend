package timetogether.where2meet.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import timetogether.where2meet.dto.GroupWhereViewResponseDto;

import java.util.List;

@Repository
public class GroupWhereQueryRepository {
  @PersistenceContext
  private EntityManager entityManager;

  public List<GroupWhereViewResponseDto> findAllByGroupId(Long groupId) {
    String query = "SELECT new timetogether.where2meet.dto.GroupWhereViewResponseDto(" +
            "m.groupWhereName, " +
            "m.groupWhereUrl, " +
            "m.count) " +
            "FROM GroupWhere m " +
            "WHERE m.group.id = :groupId ";

    return entityManager.createQuery(query, GroupWhereViewResponseDto.class)
            .setParameter("groupId", groupId)
            .getResultList();
  }
}
