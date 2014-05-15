package sk.mihalyi.java.utils.test;

import eu.mihalyi.java.utils.LazyChunkInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author ondrej.mihalyi
 */
public class LazyStreamTest {

    public LazyStreamTest() {
    }

    @Test
    public void lazyInputStreamShouldProcessOnlyTwoObjects() throws IOException {
        final List<String> objectList = mock(List.class);
        final Integer[] range = new Integer[]{0, 1, 2, 3};
        int desiredBytesToRead = 0;
        StringBuilder sb = new StringBuilder();
        for (Integer i : range) {
            final String value = "item" + i + "\n";
            when(objectList.get(i)).thenReturn(value);
            
            if (i < 2) {
                sb.append(value);
                desiredBytesToRead += value.length();
            }
        }
        String expectedDataRead = sb.toString();
        when(objectList.size()).thenReturn(range.length);
        InputStream lazyStream = new LazyChunkInputStreamFromStringArray(objectList);
        Reader reader = new InputStreamReader(lazyStream);
        final char[] actualDataRead = new char[desiredBytesToRead];
        reader.read(actualDataRead);
        assertEquals(expectedDataRead, new String(actualDataRead));
    }

    private static class LazyChunkInputStreamFromStringArray extends LazyChunkInputStream {

        private final List<String> objectList;

        public LazyChunkInputStreamFromStringArray(List<String> objectList) {
            this.objectList = objectList;
        }

        private int chunkIndex = 0;

        @Override
        protected byte[] nextChunk() {
            if (chunkIndex >= objectList.size()) {
                return null;
            } else {
                return objectList.get(chunkIndex++).getBytes();
            }
        }
    }
}
