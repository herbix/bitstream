/*
 * Copyright 2017, Chaofan. All rights reserved.
 * Use is subject to license terms.
 */
package io.chaofan.util.bitstream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class reads bits from an InputStream, in big endian order.
 * For example, if the InputStream provides 10110101, then 10010111.
 * reading 4 bits gets 1011, then 3 bits 010, then 7 bits 1100101,
 * then 2 bits 11.
 *
 * @author Chaofan
 */
public class BigEndianBitInputStream extends BitInputStream {

    private static final int[] MASKS = new int[] {
            0, 1, 3, 7, 0xf, 0x1f, 0x3f, 0x7f, 0xff
    };

    /**
     * Initializes a bit input stream from an InputStream.
     *
     * @param in the InputStream.
     */
    public BigEndianBitInputStream(InputStream in) {
        super(in);
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
                bufferBitCount = BITS_PER_BYTE;
            }

            if (bufferBitCount > numBits) {
                result = ((buffer >> bufferBitCount - numBits) & MASKS[numBits]) | result;
                bufferBitCount -= numBits;
                numBits = 0;
            } else {
                result = ((buffer & MASKS[bufferBitCount]) << numBits - bufferBitCount) | result;
                numBits -= bufferBitCount;
                bufferBitCount = 0;
            }

            resultContainData = true;
        }

        return result;
    }
}
