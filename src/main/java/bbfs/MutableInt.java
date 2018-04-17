package bbfs;

import java.util.concurrent.atomic.AtomicInteger;

public class MutableInt {

    private AtomicInteger value;

    public MutableInt(AtomicInteger value) {
        this.value = value;
    }

    Integer get() {
        return value.get();
    }

    void set(int value) {
        this.value.set(value);
    }
}
