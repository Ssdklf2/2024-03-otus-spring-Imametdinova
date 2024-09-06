package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.AbstractContainer;
import ru.otus.hw.domain.Garbage;

import java.util.Collection;

@MessagingGateway
public interface GarbageSorterGateway {
    @Gateway(requestChannel = "garbageChannel", replyChannel = "containerChannel")
    AbstractContainer process(Collection<Garbage> garbageBag);
}
