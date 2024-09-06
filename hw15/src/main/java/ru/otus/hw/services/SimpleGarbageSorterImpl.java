package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.AbstractContainer;
import ru.otus.hw.domain.Garbage;
import ru.otus.hw.domain.GlassContainer;
import ru.otus.hw.domain.OtherContainer;
import ru.otus.hw.domain.PaperContainer;
import ru.otus.hw.domain.PlasticContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class SimpleGarbageSorterImpl implements GarbageSorter {

    public static final int MAX_NUM_OF_GARBAGE_BAG = 100;

    private final GarbageSorterGateway sorter;

    private String[] garbageList;

    @Override
    public void startSortGarbageLoop() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        garbageList = getGarbageArray();
        for (int i = 0; i < MAX_NUM_OF_GARBAGE_BAG; i++) {
            delay();
            int num = i + 1;
            pool.execute(() -> {
                Collection<Garbage> garbageBag = generateGarbageBag();
                log.info("{}, New garbageBag: {}", num,
                        garbageBag.stream().map(Garbage::getName)
                                .collect(Collectors.joining(",")));
                AbstractContainer container = sorter.process(garbageBag);
                if (container != null) {
                    log.info("Sorted: {}", container);
                }
            });
        }
    }

    private String[] getGarbageArray() {
        List<String> garbageList = new ArrayList<>(PlasticContainer.GARBAGE_LIST());
        garbageList.addAll(GlassContainer.GARBAGE_LIST());
        garbageList.addAll(OtherContainer.GARBAGE_LIST());
        garbageList.addAll(PaperContainer.GARBAGE_LIST());
        return garbageList.toArray(new String[0]);
    }

    private Collection<Garbage> generateGarbageBag() {
        List<Garbage> garbageBag = new ArrayList<>();
        for (int i = 0; i < RandomUtils.nextInt(1, 5); ++i) {
            garbageBag.add(generateOrderItem());
        }
        return garbageBag;
    }

    private Garbage generateOrderItem() {
        return new Garbage(garbageList[RandomUtils.nextInt(0, garbageList.length)]);
    }

    private void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
