package debugworldgen.impl;

import net.minecraft.util.Identifier;

public class CurrentEntry {
    final Thread thread;
    final Identifier id;

    public CurrentEntry(Identifier id) {
        this.id = id;
        this.thread = Thread.currentThread();
    }

    public Thread getThread() {
        return thread;
    }

    public Identifier getId() {
        return id;
    }
}
