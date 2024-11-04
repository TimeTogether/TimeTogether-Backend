package timetogether.domain.where2meet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.domain.where2meet.Where2meet;

@Repository
public interface Where2meetRepository extends JpaRepository<Where2meet, Long>{
}
