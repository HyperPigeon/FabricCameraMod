package net.hyper_pigeon.camera;

import java.nio.ByteBuffer;

//https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java
public class ByteUtils {
    private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}