package timetogether.oauth2;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;
import timetogether.oauth2.entity.Role;
import timetogether.oauth2.entity.SocialType;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private Role role;
    private SocialType socialType;
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey, Role role, SocialType socialType) {
        super(authorities, attributes, nameAttributeKey);
        this.role = role;
        this.socialType = socialType;
    }
}
