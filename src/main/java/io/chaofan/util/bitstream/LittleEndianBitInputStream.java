/*
 * Copyright 2017, Chaofan. All rights reserved.
 * Use is subject to license terms.
 */
package io.chaofan.util.bitstream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class reads bits from an InputStream, in little endian order.
 * For example, if the InputStream provides 10110101, then 10010111.
 * reading 4 bits gets 0101, then 3 bits 011, then 7 bits 0101111,
 * then 2 bits 10.
 *
 * @author Chaofan
 */
public class LittleEndianBitInputStream {

    private static final int[] MASKS = new int[] {
            0, 1, 3, 7, 0xf, 0x1f, 0x3f, 0x7f
    };

    private InputStream in;
    private int buffer;
    private int bufferBitCount;

    /**
     * Initializes a bit input stream from an InputStream.
     *
     * @param in the InputStream.
     */
    public LittleEndianBitInputStream(InputStream in) {
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
    public int read(int numBits) throws IOException {
        int result = 0;
        int resultBits = 0;
        boolean resultContainData = false;

        while (numBits > 0) {
            if (bufferBitCount == 0) {
                buffer = in.read();
                if (buffer < 0) {
                    if (!resultContainData) {
                        throw new EOFException();
                    }
                    return result;
                }
                bufferBitCount = 8;
            }

            if (bufferBitCount > numBits) {
                result = ((buffer & MASKS[numBits]) << resultBits) | result;
                resultBits += numBits;
                bufferBitCount -= numBits;
                buffer >>= numBits;
                numBits = 0;
            } else {
                result = (buffer << resultBits) | result;
                resultBits += bufferBitCount;
                numBits -= bufferBitCount;
                bufferBitCount = 0;
            }

            resultContainData = true;
        }

        return result;
    }

    /**
     * @return available bits in the stream.
     * @throws IOException if I/O error occurs.
     */
    public int availableBits() throws IOException {
        return (in.available() << 3) + bufferBitCount;
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
