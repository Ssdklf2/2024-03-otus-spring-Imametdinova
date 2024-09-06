package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Garbage;
import ru.otus.hw.domain.GlassContainer;
import ru.otus.hw.domain.PaperContainer;
import ru.otus.hw.domain.PlasticContainer;
import ru.otus.hw.domain.Type;

@Service
@Slf4j
public class GarbageSorterServiceImpl implements GarbageSorterService {

    @Override
    public Garbage sort(Garbage garbage) {
        garbage.setType(defineType(garbage));
        return garbage;
    }

    private Type defineType(Garbage garbage) {
        if (GlassContainer.GARBAGE_LIST().contains(garbage.getName())) {
            return Type.GLASS;
        } else if (PaperContainer.GARBAGE_LIST().contains(garbage.getName())) {
            return Type.PAPER;
        } else if (PlasticContainer.GARBAGE_LIST().contains(garbage.getName())) {
            return Type.PLASTIC;
        } else {
            return Type.OTHER;
        }
    }
}
