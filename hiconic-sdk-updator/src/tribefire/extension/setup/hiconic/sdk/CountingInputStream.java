package tribefire.extension.setup.hiconic.sdk;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends FilterInputStream {

    private long byteCount = 0;

    protected CountingInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        int byteRead = super.read();
        if (byteRead != -1) {
            byteCount++;
        }
        return byteRead;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = super.read(b, off, len);
        if (bytesRead != -1) {
            byteCount += bytesRead;
        }
        return bytesRead;
    }

    @Override
    public long skip(long n) throws IOException {
        long bytesSkipped = super.skip(n);
        byteCount += bytesSkipped;
        return bytesSkipped;
    }

    public long getByteCount() {
        return byteCount;
    }
}