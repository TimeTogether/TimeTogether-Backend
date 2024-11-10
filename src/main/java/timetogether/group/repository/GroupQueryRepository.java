package timetogether.group.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import timetogether.group.Group;

import java.util.Optional;

@Repository
public class GroupQueryRepository {
  @PersistenceContext
  EntityManager entityManager;
  public Optional<Group> findByGroupNameAndIsMgr(String socialId, String groupName) {
    String query = "SELECT m FROM Group m " +
            "where m.groupName =: groupName " +
            "AND m.groupMgrId =: socialId";
    try{
      Group result = entityManager.createQuery(query, Group.class)
              .setParameter("groupName" , groupName)
              .setParameter("socialId", socialId)
              .getSingleResult();
      return Optional.ofNullable(result);
    }catch(NoResultException e){
      return Optional.empty();
    }
  }
}
