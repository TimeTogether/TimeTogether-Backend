package timetogether.ranktime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.when2meet.When2meet;

import java.util.List;

@Repository
public interface RankTimeRepository extends JpaRepository<RankTime, Long> {
    RankTime findByWhen2meet(When2meet when2meet);
}
