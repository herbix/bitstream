/*
 * Copyright 2017, Chaofan. All rights reserved.
 * Use is subject to license terms.
 */
package io.chaofan.util.bitstream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class writes bits to an OutputStream, in little endian order.
 * For example, if 101 and 0101101 are written, 01101101 will be written to
 * the OutputStream, and 01 is stored in the buffer, waiting more input.
 *
 * @author Chaofan
 */
public class LittleEndianBitOutputStream extends BitOutputStream {

    private static final int[] MASKS = new int[] {
            0, 1, 3, 7, 0xf, 0x1f, 0x3f, 0x7f, 0xff
    };

    /**
     * Initializes a bit output stream from an OutputStream.
     *
     * @param out the OutputStream.
     */
    public LittleEndianBitOutputStream(OutputStream out) {
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
            int rest = BITS_PER_BYTE - bufferBitCount;

            if (rest > numBits) {
                buffer = ((data & MASKS[numBits]) << bufferBitCount) | buffer;
                bufferBitCount += numBits;
                numBits = 0;
            } else {
                buffer = ((data & MASKS[rest]) << bufferBitCount) | buffer;
                out.write(buffer);
                numBits -= rest;
                data >>>= rest;
                bufferBitCount = 0;
                buffer = 0;
            }
        }
    }
}
