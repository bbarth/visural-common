/*
 *  Copyright 2009 Richard Nichols.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.visural.common;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IO related utilities.
 *
 * @version $Id: IOUtil.java 94 2010-09-29 06:17:07Z tibes80@gmail.com $
 * @author Richard Nichols
 */
public class IOUtil {
    
    private static final Logger log = LoggerFactory.getLogger(IOUtil.class);   

    /**
     *
     */
    public enum IOMethod {

        NIO,
        StreamCopy;
    }

    /**
     * Return the given file into RAM (byte[]).
     *
     * Defaults to NIO method.
     *
     * @param f
     * @return
     * @throws IOException
     */
    public static byte[] read(File f) throws IOException {
        return read(f, IOMethod.NIO);
    }

    /**
     * Read the given file into RAM (byte[]) specifying the method used.
     *
     * @param inputFile
     * @param io
     * @return
     * @throws IOException
     */
    public static byte[] read(File inputFile, IOMethod io) throws IOException {
        if (!inputFile.exists()) {
            return null;
        }
        if (io.equals(IOMethod.StreamCopy)) {
            BufferedInputStream bis = null;
            try {
                ByteArrayOutputStream result = new ByteArrayOutputStream((int) inputFile.length());
                bis = new BufferedInputStream(new FileInputStream(inputFile));
                int read = 0;
                do {
                    read = bis.read();
                    if (read >= 0) {
                        result.write(read);
                    }
                } while (read >= 0);
                return result.toByteArray();
            } finally {
                silentClose(IOUtil.class, bis);
            }
        } else if (io.equals(IOMethod.NIO)) {
            FileChannel in = null;
            try {
                in = (new FileInputStream(inputFile)).getChannel();
                MappedByteBuffer b = in.map(FileChannel.MapMode.READ_ONLY, 0, inputFile.length());
                byte[] data = new byte[(int) inputFile.length()];
                b.get(data);
                return data;
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } else {
            throw new IllegalArgumentException("Unknown IO: " + io);
        }
    }

    /**
     * Write the given byte[] to a file, overwriting it if it exists.
     *
     * Defaults to NIO method.
     *
     * @param f
     * @param data
     * @throws IOException
     */
    public static void write(File f, byte[] data) throws IOException {
        write(f, data, IOMethod.NIO);
    }

    /**
     * Write the given byte[] to a file, overwriting it if it exists.
     *
     * @param f
     * @param data
     * @param io
     * @throws IOException
     */
    public static void write(File f, byte[] data, IOMethod io) throws IOException {
        if (io.equals(IOMethod.StreamCopy)) {
            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(f));
                os.write(data);
                os.flush();
            } finally {
                silentClose(IOUtil.class, os);
            }
        } else if (io.equals(IOMethod.NIO)) {
            FileChannel out = null;
            try {
                out = (new FileOutputStream(f)).getChannel();
                out.write(ByteBuffer.wrap(data));
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } else {
            throw new IllegalArgumentException("Unknown IO: " + io);
        }
    }

    /**
     * Copy the source file to the destination file using NIO channels.
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copy(File source, File dest) throws IOException {
        FileChannel in = (new FileInputStream(source)).getChannel();
        FileChannel out = (new FileOutputStream(dest)).getChannel();
        in.transferTo(0, source.length(), out);
        in.close();
        out.close();
    }

    /**
     * Return the SHA-1 hash of the inputstream bytes.
     *
     * @param is
     * @return
     */
    public static byte[] getSHA1(InputStream is) {
        return getDigest("SHA-1", is);
    }

    /**
     * Return the MD5 hash of the input stream bytes.
     *
     * @param is
     * @return
     */
    public static byte[] getMD5(InputStream is) {
        return getDigest("MD5", is);
    }

    /**
     * Return the SHA-1 hash of the byte array.
     *
     * @param data
     * @return
     */
    public static byte[] getSHA1(byte[] data) {
        return getDigest("SHA-1", data);
    }

    /**
     * Return the MD5 hash of the byte array.
     *
     * @param data
     * @return
     */
    public static byte[] getMD5(byte[] data) {
        return getDigest("MD5", data);
    }

    private static byte[] getDigest(String digest, byte[] data) throws IllegalStateException {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance(digest);
            md.update(data);
            return md.digest();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static byte[] getDigest(String digest, InputStream is) throws IllegalStateException {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance(digest);
            byte[] buf = new byte[8192];
            int r;
            while ((r = is.read(buf)) != -1) {
                md.update(buf, 0, r);
            }
            is.close();
            return md.digest();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Reads a Java Serialized object from the input stream and classloader
     * specified.
     *
     * @param <T>
     * @param clazz
     * @param cl
     * @param is
     * @return
     */
    public static <T extends Serializable> T readObject(Class<T> clazz, ClassLoader cl, InputStream is) {
        ObjectInputStream ois = null;
        try {
            ois = new ClassLoaderObjectInputStream(cl, is);
            T result = (T) ois.readObject();
            return result;
        } catch (Exception ex) {
            throw new IllegalStateException("An error occurred while reading a '" + clazz.getCanonicalName() + "' object.", ex);
        } finally {
            IOUtil.silentClose(IOUtil.class, ois);
            IOUtil.silentClose(IOUtil.class, is);
        }
    }

    /**
     * Writes a object to the given output stream using Java Serialization.
     *
     * @param <T>
     * @param os
     * @param obj
     */
    public static <T extends Serializable> void writeObject(OutputStream os, T obj) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(os);
            oos.writeObject(obj);
            oos.close();
        } catch (IOException ex) {
            throw new IllegalStateException("An error occurred while writing a '" + obj.getClass().getCanonicalName() + "' object.", ex);
        } finally {
            IOUtil.silentClose(IOUtil.class, oos);
            IOUtil.silentClose(IOUtil.class, os);
        }
    }
        
    public static void silentClose(Class myClass, InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException se) {
            log.error("Could not close InputStream in silentClose(...)", se);
        }
    }
    
    public static void silentClose(Class myClass, OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException se) {
            log.error("Could not close OutputStream in silentClose(...)", se);
        }
    }

    public static void silentClose(Class myClass, PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException se) {
            log.error("Could not close PreparedStatement in silentClose(...)", se);
        }
    }

    public static void silentClose(Class myClass, Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException se) {
            log.error("Could not close Connection in silentClose(...)", se);
        }
    }

    public static void silentClose(Class myClass, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException se) {
            log.error("Could not close ResultSet in silentClose(...)", se);
        }
    }

    /**
     * Reads an InputStream to a byte array and then closes the stream.
     *
     * @param os
     * @return
     * @throws IOException
     */
    public static byte[] readStream(InputStream is) throws IOException {
        return readStream(is, true);
    }

    /**
     * Reads an InputStream to a byte array. Optionally can close the stream
     * once all data has been read.
     *
     * If closing fails an error is logged.
     *
     * @param os
     * @param closeStream
     * @return
     * @throws IOException
     */
    public static byte[] readStream(InputStream is, boolean closeStream) throws IOException {
        if (is == null) {
            throw new IllegalArgumentException("Input stream can not be null");
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int r;
            while ((r = is.read()) != -1) {
                baos.write(r);
            }
            return baos.toByteArray();
        } finally {
            if (closeStream) {
                silentClose(IOUtil.class, is);
            }
        }
    }

    /**
     * Zips the contents of the given folder into the given zip file.
     *
     * @param destinationZipFile
     * @param folderToZip
     * @param recurseSubFolders
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void zipFolder(File destinationZipFile, File folderToZip, boolean recurseSubFolders) throws IOException, FileNotFoundException {
        // check input
        if (destinationZipFile.exists() && !destinationZipFile.delete()) {
            throw new IOException("ZIP file " + destinationZipFile + " already exists and can not be overwritten.");
        }
        if (!folderToZip.exists() || !folderToZip.isDirectory()) {
            throw new FileNotFoundException("Base folder does not exist.");
        }

        // gather file list
        List<ZipItem> alZI = new ArrayList<ZipItem>();
        zipFolderWorker(alZI, folderToZip, folderToZip.getPath(), recurseSubFolders);

        // create zip
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destinationZipFile));
        for (int n = 0; n < alZI.size(); n++) {
            byte[] ba = read(alZI.get(n).fileName);
            ZipEntry ze = new ZipEntry(alZI.get(n).zipName);
            ze.setTime(System.currentTimeMillis());
            zos.putNextEntry(ze);
            zos.write(ba);
            zos.closeEntry();
        }
        zos.close();
    }

    private static void zipFolderWorker(List<ZipItem> zipItems, File currentFolder, String baseFolder, boolean recurse) {
        for (File file : currentFolder.listFiles()) {
            if (file.isDirectory()) {
                if (recurse) {
                    zipFolderWorker(zipItems, file, baseFolder, true);
                }
            } else {
                String sZN = file.getPath();
                if (sZN.startsWith(baseFolder)) {
                    sZN = sZN.substring(baseFolder.length() + 1);
                }
                zipItems.add(new ZipItem(file, sZN));
            }
        }
    }

    private static class ZipItem {

        public final File fileName;
        public final String zipName;

        public ZipItem(File fileName, String zipName) {
            this.fileName = fileName;
            this.zipName = zipName;
        }
    }

    /**
     * Recursively deletes all files and folder under the path specified and
     * then removes the folder itself (use with care!).
     * 
     * Does not account for symbolic links etc. on unix-like platforms.
     *
     * @param folder
     * @throws IOException
     */
    public static void nukeFolder(File folder) throws IOException {
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    nukeFolder(file);
                } else {
                    if (!file.delete()) {
                        throw new IOException("Failed nuking " + folder + " - could not delete file '" + folder.getPath() + "'");
                    }
                }
            }
        }
        folder.delete();
    }
}
