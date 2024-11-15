package timetogether.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.group.Group;
import timetogether.group.dto.GroupCreateResponseDto;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    GroupProjection findGroupNameById(Long groupId);
    GroupProjection findGroupTimesById(Long groupId);
}
