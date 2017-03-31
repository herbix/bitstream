/*
 * Copyright 2017, Chaofan. All rights reserved.
 * Use is subject to license terms.
 */
package io.chaofan.util.bitstream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Base class of bit input streams.
 *
 * @author Chaofan
 */
public abstract class BitInputStream extends BitStream {

    protected InputStream in;
    protected int buffer;
    protected int bufferBitCount;

    protected int markedBuffer = 0;
    protected int markedBufferBitCount = 0;

    /**
     * Initializes a bit input stream from an InputStream.
     *
     * @param in the InputStream.
     */
    protected BitInputStream(InputStream in) {
        this.in = in;
        this.bufferBitCount = 0;
    }

    /**
     * Reads some bits.
     * If available bits are not zero but less than numBits, this method will
     * return only available bits. No EOFException are thrown.
     *
     * @param numBits       bits count to be read. Must not larger than 32.
     * @return              an integer whose lower bits are bits read from stream.
     * @throws EOFException if no data available from the InputStream.
     * @throws IOException  if I/O error occurs.
     */
    public abstract int read(int numBits) throws IOException;

    /**
     * @return if this bit input stream supports mark and reset.
     */
    public boolean markSupported() {
        return in.markSupported();
    }

    /**
     * Mark current position of this bit input stream.
     *
     * @param readLimit     the maximum limit of bits that can read before the mark
     *                      position become invalid.
     * @see   java.io.InputStream#mark(int)
     */
    public void mark(int readLimit) {
        in.mark((readLimit + BITS_PER_BYTE - 1) / BITS_PER_BYTE);
        markedBuffer = buffer;
        markedBufferBitCount = bufferBitCount;
    }

    /**
     * Reset current position to marked position.
     * If this method is called without calling mark, an IOException is thrown or
     * current position is set to start of the stream, which depends on behaviors of
     * the InputStream.
     *
     * @throws IOException   if this stream has not been marked or if the
     *                       mark has been invalidated.
     * @see    java.io.InputStream#reset()
     */
    public void reset() throws IOException {
        in.reset();
        buffer = markedBuffer;
        bufferBitCount = markedBufferBitCount;
    }

    /**
     * @return available bits in the stream.
     * @throws IOException if I/O error occurs.
     */
    public int availableBits() throws IOException {
        return (in.available() * BITS_PER_BYTE) + bufferBitCount;
    }

    /**
     * Closes the InputStream.
     *
     * @throws IOException if I/O error occurs.
     */
    public void close() throws IOException {
        in.close();
    }
}
