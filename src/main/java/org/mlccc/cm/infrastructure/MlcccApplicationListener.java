package org.mlccc.cm.infrastructure;

import org.mlccc.cm.infrastructure.security.MlcccCipher;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Properties;

@Component
public class MlcccApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event){
        ConfigurableEnvironment environment = event.getEnvironment();
        Properties props = new Properties();
        String dbPassword = environment.getProperty("spring.datasource.password");
        String dePassword;
        try {
            MlcccCipher.init(environment.getProperty("keyFilePath"),environment.getProperty("ivFilePath"));
            dePassword = MlcccCipher.decrypt(dbPassword);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        if(!StringUtils.isEmpty(dePassword)){
            props.put("spring.datasource.password", dePassword);
            environment.getPropertySources().addFirst(new PropertiesPropertySource("mlcccProps", props));
        }
    }
}
