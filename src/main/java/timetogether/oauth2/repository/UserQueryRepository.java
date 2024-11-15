package timetogether.oauth2.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import timetogether.group.Group;

import java.util.List;

@Repository
public class UserQueryRepository {
  @PersistenceContext
  EntityManager entityManager;


  public List<Group> findGroupsBySocialId(String socialId) {

    // User의 groupList에서 그룹 정보 조회
    String query = "SELECT DISTINCT g FROM User u " +
            "JOIN u.groupList g " +
            "WHERE u.socialId = :socialId";

    return entityManager.createQuery(query, Group.class)
            .setParameter("socialId", socialId)
            .getResultList();
  }

  @Transactional
  public void deleteWithGroup(Group foundGroup) {
    // 모든 User에서 groupList에 foundGroup이 포함된 경우, 해당 Group을 제거
    String query = "UPDATE User u " +
            "SET u.groupList = " +
            "(SELECT g FROM u.groupList g WHERE g != :foundGroup) " +
            "WHERE :foundGroup MEMBER OF u.groupList";

    // 모든 User에 대해 실행
    entityManager.createQuery(query)
            .setParameter("foundGroup", foundGroup)
            .executeUpdate();
  }

}
