package timetogether;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {
//    @Transactional
//    public void logout() {
//        KafkaProperties.Admin admin = (KafkaProperties.Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (redisTemplate.opsForValue().get("JWT_TOKEN:" + admin.getLoginId()) != null) {
//            redisTemplate.delete("JWT_TOKEN:" + admin.getLoginId()); //Token 삭제
//        }
//
//    }
}
