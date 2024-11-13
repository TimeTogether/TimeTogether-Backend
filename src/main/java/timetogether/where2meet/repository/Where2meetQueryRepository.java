package timetogether.where2meet.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.where2meet.Where2meet;

@Repository
@RequiredArgsConstructor
public class Where2meetQueryRepository{
  @PersistenceContext
  private EntityManager entityManager;

  
}
