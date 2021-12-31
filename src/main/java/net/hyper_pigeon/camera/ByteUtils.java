package net.hyper_pigeon.camera;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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

    public static byte[] gzipCompress(byte[] uncompressedData) {
        byte[] result = new byte[]{};
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressedData.length);
             GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
            gzipOS.write(uncompressedData);
            // You need to close it before using bos
            gzipOS.close();
            result = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] gzipUncompress(byte[] compressedData) {
        byte[] result = new byte[]{};
        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPInputStream gzipIS = new GZIPInputStream(bis)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipIS.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            result = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

//    public static byte[][] divideArray(byte[] source, int chunksize) {
//        byte[][] ret = new byte[(int)Math.ceil(source.length / (double)chunksize)][chunksize];
//
//        int start = 0;
//        for(int i = 0; i < ret.length; i++) {
//            ret[i] = Arrays.copyOfRange(source,start, start + chunksize);
//            start += chunksize ;
//        }
//
//        return ret;
//    }

    public static byte[][] divideArray(byte[] source, int chunksize) {


        byte[][] ret = new byte[(int) Math.ceil((int)(source.length /chunksize))][chunksize];

        int start = 0;

        for(int i = 0; i < ret.length; i++) {
            if(start + chunksize > source.length) {
                //System.out.println("check");
                System.arraycopy(source, start, ret[i], 0, source.length - start);
            } else {
                System.arraycopy(source, start, ret[i], 0, chunksize);
            }
            start += chunksize ;
        }


        return ret;
    }
}