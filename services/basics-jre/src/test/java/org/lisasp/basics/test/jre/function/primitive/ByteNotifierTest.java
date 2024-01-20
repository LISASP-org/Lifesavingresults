package org.lisasp.basics.test.jre.function.primitive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.basics.jre.function.primitive.ByteConsumer;
import org.lisasp.basics.jre.function.primitive.ByteNotifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ByteNotifierTest {

    private static class TestByteConsumer implements ByteConsumer {

        private byte[] data;
        private int pos = 0;

        public void setData(byte... data) {
            pos = 0;
            this.data = data;
        }

        @Override
        public void accept(byte value) {
            assertEquals(data[pos], value);
            pos++;
        }
    }

    private ByteNotifier notifier;
    private TestByteConsumer listener;

    private byte[] data;

    @BeforeEach
    void prepare() {
        data = new byte[256];
        for (int x = -128; x < 128; x++) {
            data[x + 128] = (byte) x;
        }

        listener = new TestByteConsumer();

        notifier = new ByteNotifier();
        notifier.register(listener);
    }

    @Test
    void acceptOne() {
        listener.setData((byte) 0x01);

        notifier.accept((byte) 0x01);
    }

    @Test
    void acceptTwo() {
        listener.setData((byte) 0x01, (byte) 0x02);

        notifier.accept((byte) 0x01);
        notifier.accept((byte) 0x02);
    }

    @Test
    void accept256() {
        listener.setData(data);

        for (byte b : data) {
            notifier.accept(b);
        }
    }
}
