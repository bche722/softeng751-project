

package bbfs;


public class MutableInt {
    private java.util.concurrent.atomic.AtomicInteger value;

    public MutableInt(java.util.concurrent.atomic.AtomicInteger value) {
        bbfs.MutableInt.this.value = value;
    }

    java.lang.Integer get() {
        return value.get();
    }

    void set(int value) {
        bbfs.MutableInt.this.value.set(value);
    }
}

