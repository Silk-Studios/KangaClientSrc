package io.github.si1kn.KangarooClient.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class KeyPressEvent {

    @Getter
    private final int key;

    @Getter
    private final boolean repeat;

}

