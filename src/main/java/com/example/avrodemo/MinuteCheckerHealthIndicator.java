package com.example.avrodemo;
import org.slf4j.Logger;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
public class MinuteCheckerHealthIndicator implements HealthIndicator {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MinuteCheckerHealthIndicator.class);

    @Override
    public Health health() {
        logger.info("Health responds");
        GregorianCalendar gcalendar = new GregorianCalendar();
       if(gcalendar.get(Calendar.MINUTE) % 2 == 0){
           logger.info("Health up even");
           return Health.up().withDetail("even","true").build();
       }else{
           logger.info("Health up not even");
           return Health.up().withDetail("even","false").build();
       }
    }
}
