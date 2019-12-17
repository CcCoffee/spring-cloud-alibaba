package ribbonconfiguration;

import com.study.contentcenter.configuration.NacosSameZoneRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonGlobalConfiguration {

    @Bean
    public NacosSameZoneRule nacosSameZoneRule(){
        return new NacosSameZoneRule();
    }
}
