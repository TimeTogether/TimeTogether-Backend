package timetogether.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.group.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
