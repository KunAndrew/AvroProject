package com.example.avrodemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;


@SpringBootApplication
@Configuration
@Controller
public class AvrodemoApplication{
    private MinuteCheckerHealthIndicator minuteCheckerHealthIndicator;

    @Autowired
    public AvrodemoApplication(MinuteCheckerHealthIndicator minuteCheckerHealthIndicator) {
        this.minuteCheckerHealthIndicator = minuteCheckerHealthIndicator;
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AvrodemoApplication.class);

    @ResponseBody
    @RequestMapping("/")
    public String entry(@RequestBody String requestString){
        logger.info("respond has started ");
        ObjectMapper objectMapper = new ObjectMapper();

        String requestJsonString=requestString;
        PutBankOrderRq putBankOrderRq = null;
        try {
            putBankOrderRq = objectMapper.readValue(requestJsonString, PutBankOrderRq.class);
            logger.info("RqUID --- {} ",putBankOrderRq.getRqUID());
        } catch (IOException e) {
            e.printStackTrace();
        }
        PutBankOrderRs putBankOrderRs = new PutBankOrderRs();
        putBankOrderRs.setRqUID(putBankOrderRq.getRqUID());
        putBankOrderRs.setRqTm(putBankOrderRq.getRqTm());
        putBankOrderRs.setSPName(putBankOrderRq.getSPName());
        putBankOrderRs.setSystemId("Идентификатор вызываемой системы (кого вызываем)");
        Status status =new Status();
        status.setDocRef(putBankOrderRq.getBankOrder().getDocHeader().getDocRef());
        status.setCode(1);
        status.setAnnotation("Описание ошибки в случае отказа");
        putBankOrderRs.setStatus(status);

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(putBankOrderRs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public static void main(String[] args)  {
        SpringApplication.run(AvrodemoApplication.class, args);
        logger.info("Application has started ");
    }
}
