package eu.mihalyi.java.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.io.InputStream;
import java.util.Iterator;

/**
 *
 * @author ondrej.mihalyi
 */
public abstract class LazyChunkInputStream extends InputStream {

    private InputStream entityStream;
    private Iterator dataIterator;

    public LazyChunkInputStream() {
    }

    public LazyChunkInputStream(List<Object> dataList) {
        this.dataIterator = dataList.iterator();
    }

    public String getLineFromEntity(Object entity) {
        return entity.toString();
    }

    @Override
    public int read() throws IOException {
        if (entityStream == null || (0 == entityStream.available())) {
            byte[] chunk = nextChunk();
            if (chunk != null) {
                entityStream = new ByteArrayInputStream(chunk);
            } else {
                return -1;
            }
        }
        return entityStream.read();
    }
    
    protected abstract byte[] nextChunk();

}
