/*
 * Copyright 2017, Chaofan. All rights reserved.
 * Use is subject to license terms.
 */
package io.chaofan.util.bitstream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class writes bits to an OutputStream, in big endian order.
 * For example, if 101 and 0101101 are written, 10101011 will be written to
 * the OutputStream, and 01 is stored in the buffer, waiting more input.
 *
 * @author Chaofan
 */
public class BigEndianBitOutputStream extends BitOutputStream {

    private static final int[] MASKS = new int[] {
            0, 1, 3, 7, 0xf, 0x1f, 0x3f, 0x7f, 0xff
    };

    /**
     * Initializes a bit output stream from an OutputStream.
     *
     * @param out the OutputStream.
     */
    public BigEndianBitOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * Writes some bits.
     *
     * @param data          bits to be written, which are stored in low bits.
     * @param numBits       bits count to be written.
     * @throws IOException  if an I/O error occurs.
     */
    public void write(int data, int numBits) throws IOException {
        while (numBits > 0) {
            int rest = 8 - bufferBitCount;

            if (rest > numBits) {
                buffer = ((data & MASKS[numBits]) << (rest - numBits)) | buffer;
                bufferBitCount += numBits;
                numBits = 0;
            } else {
                buffer = ((data >> (numBits - rest)) & MASKS[rest]) | buffer;
                out.write(buffer);
                numBits -= rest;
                bufferBitCount = 0;
                buffer = 0;
            }
        }
    }
}
