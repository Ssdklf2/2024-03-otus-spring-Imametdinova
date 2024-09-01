package ru.otus.hw.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.store.MessageGroup;
import org.springframework.integration.store.MessageGroupStore;
import org.springframework.integration.store.SimpleMessageStore;
import ru.otus.hw.domain.AbstractContainer;
import ru.otus.hw.domain.Garbage;
import ru.otus.hw.domain.GlassContainer;
import ru.otus.hw.domain.PaperContainer;
import ru.otus.hw.domain.PlasticContainer;
import ru.otus.hw.domain.Type;
import ru.otus.hw.services.GarbageSorterService;

import java.sql.Timestamp;
import java.util.List;

@Configuration
@Log4j2
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> garbageChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> containerChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public IntegrationFlow discardFlow() {
        return IntegrationFlow.from("discardChannel")
                .handle(message -> {
                    log.info(String.format("Garbage '%s' is too complex to be easily sorted.",
                            ((Garbage) message.getPayload()).getName()));
                })
                .get();
    }

    @Bean
    public IntegrationFlow sorterFlow(GarbageSorterService garbageSorterService) {
        return IntegrationFlow.from(garbageChannel())
                .split()
                .handle(garbageSorterService, "sort")
                .<Garbage>filter(garbage -> !garbage.getType().equals(Type.OTHER),
                        filter -> filter.discardChannel("discardChannel"))
                .aggregate(aggregatorSpec -> aggregatorSpec
                        .correlationStrategy(message -> ((Garbage) message.getPayload()).getType())
                        .releaseStrategy(group -> group.size() >= 5)
                        .outputProcessor(g -> {
                            AbstractContainer container = packingGarbage(g);
                            List<Garbage> garbageList = g.getMessages().stream()
                                    .map(message -> ((Garbage) message.getPayload()))
                                    .toList();
                            container.addAll(garbageList);
                            return container;
                        })
                        .messageStore(messageGroupStore())
                        .expireGroupsUponTimeout(true)
                )
                .channel(containerChannel())
                .get();
    }

    @Bean
    public MessageGroupStore messageGroupStore() {
        return new SimpleMessageStore();
    }

    private AbstractContainer packingGarbage(MessageGroup g) {
        Type type = (Type) g.getGroupId();
        AbstractContainer container;
        Timestamp creationTimestamp = new Timestamp(g.getTimestamp());
        switch (type) {
            case PLASTIC -> container = new PlasticContainer(creationTimestamp);
            case PAPER -> container = new PaperContainer(creationTimestamp);
            case GLASS -> container = new GlassContainer(creationTimestamp);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        return container;
    }
}