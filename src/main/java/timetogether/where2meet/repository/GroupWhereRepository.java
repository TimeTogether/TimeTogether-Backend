package timetogether.where2meet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.where2meet.GroupWhere;

@Repository
public interface GroupWhereRepository extends JpaRepository<GroupWhere,Long> {
}
