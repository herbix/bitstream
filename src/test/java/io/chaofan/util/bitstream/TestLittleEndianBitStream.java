/*
 * Copyright 2017, Chaofan. All rights reserved.
 * Use is subject to license terms.
 */
package io.chaofan.util.bitstream;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * Test of little endian bit stream.
 */
public class TestLittleEndianBitStream {

    @Test
    public void testInput() throws IOException {
        byte[] b = new byte[]{0x12, 0x34, 0x56, 0x78, 0x32, (byte) 0xA8, 0x11};
        LittleEndianBitInputStream in = new LittleEndianBitInputStream(new ByteArrayInputStream(b));

        Assert.assertTrue(in.markSupported());

        Assert.assertEquals(56, in.availableBits());
        Assert.assertEquals(0, in.read(0));
        Assert.assertEquals(0x78563412, in.read(32));
        Assert.assertEquals(2, in.read(4));

        in.mark(200);

        Assert.assertEquals(3, in.read(3));
        Assert.assertEquals(0, in.read(1));
        Assert.assertEquals(0xA8, in.read(8));

        in.reset();

        Assert.assertEquals(3, in.read(4));
        Assert.assertEquals(0xA8, in.read(8));

        // Trying to read extra bits will only return available ones.
        Assert.assertEquals(8, in.availableBits());
        Assert.assertEquals(0x11, in.read(12));

        try {
            in.read(1);
            Assert.fail();
        } catch (EOFException ignored) {

        }

        in.close();
    }

    @Test
    public void testOutput() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        LittleEndianBitOutputStream out = new LittleEndianBitOutputStream(bout);

        out.write(0, 0);
        out.write(0x78563412, 32);
        out.write(2, 4);
        out.write(3, 3);
        out.write(0, 1);
        out.write(0xA8, 8);
        out.write(0x11, 6);

        out.close();

        byte[] b = new byte[]{0x12, 0x34, 0x56, 0x78, 0x32, (byte) 0xA8, 0x11};
        Assert.assertArrayEquals(b, bout.toByteArray());
    }
}
