//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.soloader;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

public final class FileLocker implements Closeable {
    private final FileOutputStream mLockFileOutputStream;
    private final FileLock mLock;

    public static FileLocker lock(File lockFile) throws IOException {
        return new FileLocker(lockFile);
    }

    private FileLocker(File lockFile) throws IOException {
        this.mLockFileOutputStream = new FileOutputStream(lockFile);
        FileLock lock = null;

        try {
            lock = this.mLockFileOutputStream.getChannel().lock();
        } finally {
            if(lock == null) {
                this.mLockFileOutputStream.close();
            }

        }

        this.mLock = lock;
    }

    public void close() throws IOException {
        try {
            this.mLock.release();
        } finally {
            this.mLockFileOutputStream.close();
        }

    }
}
