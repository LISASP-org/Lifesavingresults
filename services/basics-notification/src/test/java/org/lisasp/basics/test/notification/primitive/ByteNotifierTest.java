package org.lisasp.basics.test.notification.primitive;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lisasp.basics.notification.primitive.ByteConsumer;
import org.lisasp.basics.notification.primitive.ByteNotifier;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class ByteNotifierTest {

    private ByteNotifier notifier;
    private ByteConsumer listener;

    private byte[] data;

    @BeforeEach
    void prepare() {
        data = new byte[256];
        for (int x = -128; x < 128; x++) {
            data[x + 128] = (byte) x;
        }

        listener = Mockito.mock(ByteConsumer.class);

        notifier = new ByteNotifier();
        notifier.register(listener);
    }

    @AfterEach
    void cleanup() {
        notifier = null;
        listener = null;
    }

    @Test
    void acceptEmpty() {
        verifyNoMoreInteractions(listener);
    }

    @Test
    void acceptOne() {
        notifier.accept((byte) 0x01);

        verify(listener, times(1)).accept((byte) 0x01);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void acceptTwo() {
        notifier.accept((byte) 0x01);
        notifier.accept((byte) 0x02);

        verify(listener, times(1)).accept((byte) 0x01);
        verify(listener, times(1)).accept((byte) 0x02);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void accept256() {
        for (byte b : data) {
            notifier.accept(b);
        }

        for (byte b : data) {
            verify(listener, times(1)).accept(b);
        }
        verifyNoMoreInteractions(listener);
    }
}
