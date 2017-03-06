//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.soloader;

import android.content.Context;
import android.os.Parcel;
import android.util.Log;

import java.io.Closeable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

public abstract class UnpackingSoSource extends DirectorySoSource {
    private static final String TAG = "fb-UnpackingSoSource";
    private static final String STATE_FILE_NAME = "dso_state";
    private static final String LOCK_FILE_NAME = "dso_lock";
    private static final String DEPS_FILE_NAME = "dso_deps";
    private static final String MANIFEST_FILE_NAME = "dso_manifest";
    private static final byte STATE_DIRTY = 0;
    private static final byte STATE_CLEAN = 1;
    private static final byte MANIFEST_VERSION = 1;
    protected final Context mContext;

    protected UnpackingSoSource(Context context, String name) {
        super(getSoStorePath(context, name), 1);
        this.mContext = context;
    }

    public static File getSoStorePath(Context context, String name) {
        return new File(context.getApplicationInfo().dataDir + "/" + name);
    }

    protected abstract UnpackingSoSource.Unpacker makeUnpacker() throws IOException;

    private static void writeState(File stateFileName, byte state) throws IOException {
        RandomAccessFile stateFile = new RandomAccessFile(stateFileName, "rw");
        Throwable var3 = null;

        try {
            stateFile.seek(0L);
            stateFile.write(state);
            stateFile.setLength(stateFile.getFilePointer());
            stateFile.getFD().sync();
        } catch (Throwable var12) {
            var3 = var12;
            throw var12;
        } finally {
            if(stateFile != null) {
                if(var3 != null) {
                    try {
                        stateFile.close();
                    } catch (Throwable var11) {
//                        var3.addSuppressed(var11);
                    }
                } else {
                    stateFile.close();
                }
            }

        }

    }

    private void deleteUnmentionedFiles(UnpackingSoSource.Dso[] dsos) throws IOException {
        String[] existingFiles = this.soDirectory.list();
        if(existingFiles == null) {
            throw new IOException("unable to list directory " + this.soDirectory);
        } else {
            for(int i = 0; i < existingFiles.length; ++i) {
                String fileName = existingFiles[i];
                if(!fileName.equals("dso_state") && !fileName.equals("dso_lock") && !fileName.equals("dso_deps") && !fileName.equals("dso_manifest")) {
                    boolean found = false;

                    for(int fileNameToDelete = 0; !found && fileNameToDelete < dsos.length; ++fileNameToDelete) {
                        if(dsos[fileNameToDelete].name.equals(fileName)) {
                            found = true;
                        }
                    }

                    if(!found) {
                        File var7 = new File(this.soDirectory, fileName);
                        Log.v("fb-UnpackingSoSource", "deleting unaccounted-for file " + var7);
                        SysUtil.dumbDeleteRecursive(var7);
                    }
                }
            }

        }
    }

    private void extractDso(UnpackingSoSource.InputDso iDso, byte[] ioBuffer) throws IOException {
        Log.i("fb-UnpackingSoSource", "extracting DSO " + iDso.dso.name);
        File dsoFileName = new File(this.soDirectory, iDso.dso.name);
        RandomAccessFile dsoFile = null;

        try {
            dsoFile = new RandomAccessFile(dsoFileName, "rw");
        } catch (IOException var10) {
            Log.w("fb-UnpackingSoSource", "error overwriting " + dsoFileName + " trying to delete and start over", var10);
            dsoFileName.delete();
            dsoFile = new RandomAccessFile(dsoFileName, "rw");
        }

        try {
            InputStream dsoContent = iDso.content;
            int sizeHint = dsoContent.available();
            if(sizeHint > 1) {
                SysUtil.fallocateIfSupported(dsoFile.getFD(), (long)sizeHint);
            }

            SysUtil.copyBytes(dsoFile, iDso.content, 2147483647, ioBuffer);
            dsoFile.setLength(dsoFile.getFilePointer());
            if(!dsoFileName.setExecutable(true, false)) {
                throw new IOException("cannot make file executable: " + dsoFileName);
            }
        } finally {
            dsoFile.close();
        }

    }

    private void regenerate(byte state, UnpackingSoSource.DsoManifest desiredManifest, UnpackingSoSource.InputDsoIterator dsoIterator) throws IOException {
        Log.v("fb-UnpackingSoSource", "regenerating DSO store " + this.getClass().getName());
        File manifestFileName = new File(this.soDirectory, "dso_manifest");
        RandomAccessFile manifestFile = new RandomAccessFile(manifestFileName, "rw");
        Throwable var6 = null;

        try {
            UnpackingSoSource.DsoManifest x2 = null;
            if(state == 1) {
                try {
                    x2 = UnpackingSoSource.DsoManifest.read(manifestFile);
                } catch (Exception var36) {
                    Log.i("fb-UnpackingSoSource", "error reading existing DSO manifest", var36);
                }
            }

            if(x2 == null) {
                x2 = new UnpackingSoSource.DsoManifest(new UnpackingSoSource.Dso[0]);
            }

            this.deleteUnmentionedFiles(desiredManifest.dsos);
            byte[] ioBuffer = new byte['耀'];

            while(dsoIterator.hasNext()) {
                UnpackingSoSource.InputDso iDso = dsoIterator.next();
                Throwable var10 = null;

                try {
                    boolean x21 = true;

                    for(int i = 0; x21 && i < x2.dsos.length; ++i) {
                        if(x2.dsos[i].name.equals(iDso.dso.name) && x2.dsos[i].hash.equals(iDso.dso.hash)) {
                            x21 = false;
                        }
                    }

                    if(x21) {
                        this.extractDso(iDso, ioBuffer);
                    }
                } catch (Throwable var37) {
                    var10 = var37;
                    throw var37;
                } finally {
                    if(iDso != null) {
                        if(var10 != null) {
                            try {
                                iDso.close();
                            } catch (Throwable var35) {
                                //var10.addSuppressed(var35);
                            }
                        } else {
                            iDso.close();
                        }
                    }

                }
            }
        } catch (Throwable var39) {
            var6 = var39;
            throw var39;
        } finally {
            if(manifestFile != null) {
                if(var6 != null) {
                    try {
                        manifestFile.close();
                    } catch (Throwable var34) {
//                        var6.addSuppressed(var34);
                    }
                } else {
                    manifestFile.close();
                }
            }

        }

    }

    private boolean refreshLocked(final FileLocker lock, int flags, final byte[] deps) throws IOException {
        final File stateFileName = new File(this.soDirectory, "dso_state");
        RandomAccessFile depsFileName = new RandomAccessFile(stateFileName, "rw");
        Throwable desiredManifest = null;

        byte state;
        try {
            try {
                state = depsFileName.readByte();
                if(state != 1) {
                    Log.v("fb-UnpackingSoSource", "dso store " + this.soDirectory + " regeneration interrupted: wiping clean");
                    state = 0;
                }
            } catch (EOFException var89) {
                state = 0;
            }
        } catch (Throwable var90) {
            desiredManifest = var90;
            throw var90;
        } finally {
            if(depsFileName != null) {
                if(desiredManifest != null) {
                    try {
                        depsFileName.close();
                    } catch (Throwable var87) {
//                        desiredManifest.addSuppressed(var87);
                    }
                } else {
                    depsFileName.close();
                }
            }

        }

        final File depsFileName1 = new File(this.soDirectory, "dso_deps");
        UnpackingSoSource.DsoManifest desiredManifest1 = null;
        RandomAccessFile manifest = new RandomAccessFile(depsFileName1, "rw");
        Throwable syncer = null;

        try {
            byte[] x2 = new byte[(int)manifest.length()];
            if(manifest.read(x2) != x2.length) {
                Log.v("fb-UnpackingSoSource", "short read of so store deps file: marking unclean");
                state = 0;
            }

            if(!Arrays.equals(x2, deps)) {
                Log.v("fb-UnpackingSoSource", "deps mismatch on deps store: regenerating");
                state = 0;
            }

            if(state == 0) {
                Log.v("fb-UnpackingSoSource", "so store dirty: regenerating");
                writeState(stateFileName, (byte)0);
                UnpackingSoSource.Unpacker u = this.makeUnpacker();
                Throwable var12 = null;

                try {
                    desiredManifest1 = u.getDsoManifest();
                    UnpackingSoSource.InputDsoIterator x21 = u.openDsoIterator();
                    Throwable var14 = null;

                    try {
                        this.regenerate(state, desiredManifest1, x21);
                    } catch (Throwable var88) {
                        var14 = var88;
                        throw var88;
                    } finally {
                        if(x21 != null) {
                            if(var14 != null) {
                                try {
                                    x21.close();
                                } catch (Throwable var86) {
//                                    var14.addSuppressed(var86);
                                }
                            } else {
                                x21.close();
                            }
                        }

                    }
                } catch (Throwable var92) {
                    var12 = var92;
                    throw var92;
                } finally {
                    if(u != null) {
                        if(var12 != null) {
                            try {
                                u.close();
                            } catch (Throwable var85) {
//                                var12.addSuppressed(var85);
                            }
                        } else {
                            u.close();
                        }
                    }

                }
            }
        } catch (Throwable var95) {
            syncer = var95;
            throw var95;
        } finally {
            if(manifest != null) {
                if(syncer != null) {
                    try {
                        manifest.close();
                    } catch (Throwable var84) {
//                        syncer.addSuppressed(var84);
                    }
                } else {
                    manifest.close();
                }
            }

        }

        if(desiredManifest1 == null) {
            return false;
        } else {
            final UnpackingSoSource.DsoManifest desiredManifest11 = desiredManifest1;
            Runnable syncer1 = new Runnable() {
                public void run() {
                    try {
                        try {
                            Log.v("fb-UnpackingSoSource", "starting syncer worker");
                            RandomAccessFile ex = new RandomAccessFile(depsFileName1, "rw");
                            Throwable manifestFile = null;

                            try {
                                ex.write(deps);
                                ex.setLength(ex.getFilePointer());
                            } catch (Throwable var41) {
                                manifestFile = var41;
                                throw var41;
                            } finally {
                                if(ex != null) {
                                    if(manifestFile != null) {
                                        try {
                                            ex.close();
                                        } catch (Throwable var39) {
//                                            manifestFile.addSuppressed(var39);
                                        }
                                    } else {
                                        ex.close();
                                    }
                                }

                            }

                            File ex1 = new File(UnpackingSoSource.this.soDirectory, "dso_manifest");
                            RandomAccessFile manifestFile1 = new RandomAccessFile(ex1, "rw");
                            Throwable x2 = null;

                            try {
                                desiredManifest11.write(manifestFile1);
                            } catch (Throwable var40) {
                                x2 = var40;
                                throw var40;
                            } finally {
                                if(manifestFile1 != null) {
                                    if(x2 != null) {
                                        try {
                                            manifestFile1.close();
                                        } catch (Throwable var38) {
//                                            x2.addSuppressed(var38);
                                        }
                                    } else {
                                        manifestFile1.close();
                                    }
                                }

                            }

                            SysUtil.fsyncRecursive(UnpackingSoSource.this.soDirectory);
                            UnpackingSoSource.writeState(stateFileName, (byte)1);
                        } finally {
                            Log.v("fb-UnpackingSoSource", "releasing dso store lock for " + UnpackingSoSource.this.soDirectory + " (from syncer thread)");
                            lock.close();
                        }

                    } catch (IOException var45) {
                        throw new RuntimeException(var45);
                    }
                }
            };
            if((flags & 1) != 0) {
                (new Thread(syncer1, "SoSync:" + this.soDirectory.getName())).start();
            } else {
                syncer1.run();
            }

            return true;
        }
    }

    protected byte[] getDepsBlock() throws IOException {
        Parcel parcel = Parcel.obtain();
        UnpackingSoSource.Unpacker depsBlock = this.makeUnpacker();
        Throwable var3 = null;

        try {
            UnpackingSoSource.Dso[] x2 = depsBlock.getDsoManifest().dsos;
            parcel.writeByte((byte)1);
            parcel.writeInt(x2.length);

            for(int i = 0; i < x2.length; ++i) {
                parcel.writeString(x2[i].name);
                parcel.writeString(x2[i].hash);
            }
        } catch (Throwable var13) {
            var3 = var13;
            throw var13;
        } finally {
            if(depsBlock != null) {
                if(var3 != null) {
                    try {
                        depsBlock.close();
                    } catch (Throwable var12) {
//                        var3.addSuppressed(var12);
                    }
                } else {
                    depsBlock.close();
                }
            }

        }

        byte[] var15 = parcel.marshall();
        parcel.recycle();
        return var15;
    }

    protected void prepare(int flags) throws IOException {
        SysUtil.mkdirOrThrow(this.soDirectory);
        File lockFileName = new File(this.soDirectory, "dso_lock");
        FileLocker lock = FileLocker.lock(lockFileName);

        try {
            Log.v("fb-UnpackingSoSource", "locked dso store " + this.soDirectory);
            if(this.refreshLocked(lock, flags, this.getDepsBlock())) {
                lock = null;
            } else {
                Log.i("fb-UnpackingSoSource", "dso store is up-to-date: " + this.soDirectory);
            }
        } finally {
            if(lock != null) {
                Log.v("fb-UnpackingSoSource", "releasing dso store lock for " + this.soDirectory);
                lock.close();
            } else {
                Log.v("fb-UnpackingSoSource", "not releasing dso store lock for " + this.soDirectory + " (syncer thread started)");
            }

        }

    }

    protected abstract static class Unpacker implements Closeable {
        protected Unpacker() {
        }

        protected abstract UnpackingSoSource.DsoManifest getDsoManifest() throws IOException;

        protected abstract UnpackingSoSource.InputDsoIterator openDsoIterator() throws IOException;

        public void close() throws IOException {
        }
    }

    protected abstract static class InputDsoIterator implements Closeable {
        protected InputDsoIterator() {
        }

        public abstract boolean hasNext();

        public abstract UnpackingSoSource.InputDso next() throws IOException;

        public void close() throws IOException {
        }
    }

    protected static final class InputDso implements Closeable {
        public final UnpackingSoSource.Dso dso;
        public final InputStream content;

        public InputDso(UnpackingSoSource.Dso dso, InputStream content) {
            this.dso = dso;
            this.content = content;
        }

        public void close() throws IOException {
            this.content.close();
        }
    }

    public static final class DsoManifest {
        public final UnpackingSoSource.Dso[] dsos;

        public DsoManifest(UnpackingSoSource.Dso[] dsos) {
            this.dsos = dsos;
        }

        static final UnpackingSoSource.DsoManifest read(DataInput xdi) throws IOException {
            byte version = xdi.readByte();
            if(version != 1) {
                throw new RuntimeException("wrong dso manifest version");
            } else {
                int nrDso = xdi.readInt();
                if(nrDso < 0) {
                    throw new RuntimeException("illegal number of shared libraries");
                } else {
                    UnpackingSoSource.Dso[] dsos = new UnpackingSoSource.Dso[nrDso];

                    for(int i = 0; i < nrDso; ++i) {
                        dsos[i] = new UnpackingSoSource.Dso(xdi.readUTF(), xdi.readUTF());
                    }

                    return new UnpackingSoSource.DsoManifest(dsos);
                }
            }
        }

        public final void write(DataOutput xdo) throws IOException {
            xdo.writeByte(1);
            xdo.writeInt(this.dsos.length);

            for(int i = 0; i < this.dsos.length; ++i) {
                xdo.writeUTF(this.dsos[i].name);
                xdo.writeUTF(this.dsos[i].hash);
            }

        }
    }

    public static class Dso {
        public final String name;
        public final String hash;

        public Dso(String name, String hash) {
            this.name = name;
            this.hash = hash;
        }
    }
}
