package timetogether.oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.oauth2.entity.SocialType;
import timetogether.oauth2.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findBySocialId(String socialId);
    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

//    UserProjection findUserNameBySocialId(String socialId);
//    Optional<User> findByUserName(String userName);

}
