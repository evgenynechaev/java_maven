package ru.rtk.core;

import java.util.List;

@FunctionalInterface
public interface Command {
    void execute(State ctx, List<String> args);
}
