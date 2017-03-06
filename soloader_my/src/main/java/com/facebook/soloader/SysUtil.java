//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.soloader;

import android.os.Build;
import android.os.Parcel;
import android.os.Build.VERSION;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public final class SysUtil {
    private static final byte APK_SIGNATURE_VERSION = 1;

    public SysUtil() {
    }

    public static int findAbiScore(String[] supportedAbis, String abi) {
        for(int i = 0; i < supportedAbis.length; ++i) {
            if(supportedAbis[i] != null && abi.equals(supportedAbis[i])) {
                return i;
            }
        }

        return -1;
    }

    public static void deleteOrThrow(File file) throws IOException {
        if(!file.delete()) {
            throw new IOException("could not delete file " + file);
        }
    }

    public static String[] getSupportedAbis() {
        return VERSION.SDK_INT < 21?new String[]{Build.CPU_ABI, Build.CPU_ABI2}:SysUtil.LollipopSysdeps.getSupportedAbis();
    }

    public static void fallocateIfSupported(FileDescriptor fd, long length) throws IOException {
        if(VERSION.SDK_INT >= 21) {
            SysUtil.LollipopSysdeps.fallocateIfSupported(fd, length);
        }

    }

    public static void dumbDeleteRecursive(File file) throws IOException {
        if(file.isDirectory()) {
            File[] fileList = file.listFiles();
            if(fileList == null) {
                return;
            }

            File[] arr$ = fileList;
            int len$ = fileList.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                File entry = arr$[i$];
                dumbDeleteRecursive(entry);
            }
        }

        if(!file.delete() && file.exists()) {
            throw new IOException("could not delete: " + file);
        }
    }

    static void mkdirOrThrow(File dir) throws IOException {
        if(!dir.mkdirs() && !dir.isDirectory()) {
            throw new IOException("cannot mkdir: " + dir);
        }
    }

    static int copyBytes(RandomAccessFile os, InputStream is, int byteLimit, byte[] buffer) throws IOException {
        int bytesCopied;
        int nrRead;
        for(bytesCopied = 0; bytesCopied < byteLimit && (nrRead = is.read(buffer, 0, Math.min(buffer.length, byteLimit - bytesCopied))) != -1; bytesCopied += nrRead) {
            os.write(buffer, 0, nrRead);
        }

        return bytesCopied;
    }

    static void fsyncRecursive(File fileName) throws IOException {
        if(fileName.isDirectory()) {
            File[] file = fileName.listFiles();
            if(file == null) {
                throw new IOException("cannot list directory " + fileName);
            }

            for(int i = 0; i < file.length; ++i) {
                fsyncRecursive(file[i]);
            }
        } else if(!fileName.getPath().endsWith("_lock")) {
            RandomAccessFile var13 = new RandomAccessFile(fileName, "r");
            Throwable var14 = null;

            try {
                var13.getFD().sync();
            } catch (Throwable var11) {
                var14 = var11;
                throw var11;
            } finally {
                if(var13 != null) {
                    if(var14 != null) {
                        try {
                            var13.close();
                        } catch (Throwable var10) {
                            var14.addSuppressed(var10);
                        }
                    } else {
                        var13.close();
                    }
                }

            }
        }

    }

    public static byte[] makeApkDepBlock(File apkFile) {
        Parcel parcel = Parcel.obtain();
        parcel.writeByte((byte)1);
        parcel.writeString(apkFile.getPath());
        parcel.writeLong(apkFile.lastModified());
        byte[] depsBlock = parcel.marshall();
        parcel.recycle();
        return depsBlock;
    }

    private static final class LollipopSysdeps {
        private LollipopSysdeps() {
        }

        public static String[] getSupportedAbis() {
            return Build.SUPPORTED_32_BIT_ABIS;
        }

        public static void fallocateIfSupported(FileDescriptor fd, long length) throws IOException {
            try {
                Os.posix_fallocate(fd, 0L, length);
            } catch (ErrnoException var4) {
                if(var4.errno != OsConstants.EOPNOTSUPP && var4.errno != OsConstants.ENOSYS && var4.errno != OsConstants.EINVAL) {
                    throw new IOException(var4.toString(), var4);
                }
            }

        }
    }
}
