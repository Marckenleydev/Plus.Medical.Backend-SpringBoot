package marc.dev.DoctorBooking_appointement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static marc.dev.DoctorBooking_appointement.constant.Constants.STRENGTH;


@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder(STRENGTH);
    }

}
