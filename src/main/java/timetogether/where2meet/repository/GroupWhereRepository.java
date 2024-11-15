package timetogether.where2meet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.GroupWhere.GroupWhere;

@Repository
public interface GroupWhereRepository extends JpaRepository<GroupWhere,Long> {
}
