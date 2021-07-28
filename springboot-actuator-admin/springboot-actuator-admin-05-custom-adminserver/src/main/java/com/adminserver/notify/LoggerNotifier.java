package com.adminserver.notify;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LoggerNotifier extends AbstractEventNotifier {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public LoggerNotifier(InstanceRepository repository) {
        super(repository);
    }

    /**
     * 实现对指定实例的指定事件，进行自定义的告警通知。例如说，调用短信发送接口、钉钉消息发送接口等等
     * @param event
     * @param instance
     * @return
     */
    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                logger.info("Instance {} ({}) is {}", instance.getRegistration().getName(), event.getInstance(),
                        ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus());
            } else {
                logger.info("Instance {} ({}) {}", instance.getRegistration().getName(), event.getInstance(), event.getType());
            }
        });
    }

}