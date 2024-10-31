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

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey, Role role, SocialType socialType) {
        super(authorities, attributes, nameAttributeKey);
        this.role = role;
        this.socialType = socialType;
    }
}
