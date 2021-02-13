package io.github.si1kn.KangarooClient.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class KeyReleaseEvent {

    @Getter
    private final int key;

    @Getter
    private final boolean repeat;

}
