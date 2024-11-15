package timetogether.oauth2.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import timetogether.group.Group;
import timetogether.oauth2.entity.User;

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

  public void deleteGroupInUserEntity(List<User> groupUserList,Group foundGroup) {
    for (User user : groupUserList) {
      user.getGroupList().remove(foundGroup);
    }
  }
}
