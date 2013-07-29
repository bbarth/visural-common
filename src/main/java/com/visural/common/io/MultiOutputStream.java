package com.visural.common.io;

import static com.google.common.collect.Lists.newArrayList;
import java.io.IOException;
import java.io.OutputStream;
import static java.util.Arrays.asList;
import java.util.Collection;

/**
 * An outputstream that duplicates writes to multiple outputstreams.
 * 
 * (Similar to TeeOutputStream in commons-io)
 * 
 * @author Richard Nichols
 */
public class MultiOutputStream extends OutputStream {

    private final Collection<OutputStream> outputStreams;

    public MultiOutputStream(OutputStream... outputStreams) {
        this(asList(outputStreams));
    }

    public MultiOutputStream(Iterable<OutputStream> outputStreams) {
        this.outputStreams = newArrayList(outputStreams);
    }

    @Override
    public void write(int b) throws IOException {
        for (OutputStream outputStream : outputStreams) {
            outputStream.write(b);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        for (OutputStream outputStream : outputStreams) {
            outputStream.write(b);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        for (OutputStream outputStream : outputStreams) {
            outputStream.write(b, off, len);
        }
    }

    @Override
    public void close() throws IOException {
        IOException firstException = null;
        for (OutputStream outputStream : outputStreams) {
            try {
                outputStream.close();
            } catch (IOException e) {
                if (firstException == null) {
                    firstException = e;
                }
            }
        }
        if (firstException != null) {
            throw firstException;
        }
    }
}
