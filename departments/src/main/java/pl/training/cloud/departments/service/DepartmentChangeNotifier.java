package pl.training.cloud.departments.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Aspect
@Service
@Log
@RequiredArgsConstructor
public class DepartmentChangeNotifier {

    @NonNull
    private Source source;

    @AfterReturning("@annotation(NotifyDepartmentChange)")
    public void sendNotification() {
        log.info("Sending change notification...");
        Message<String> message = MessageBuilder.withPayload("department-update").build();
        source.output().send(message);
    }

}
