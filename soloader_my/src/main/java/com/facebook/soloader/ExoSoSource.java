//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.soloader;

import android.content.Context;
import com.facebook.soloader.SysUtil;
import com.facebook.soloader.UnpackingSoSource;
import com.facebook.soloader.UnpackingSoSource.Dso;
import com.facebook.soloader.UnpackingSoSource.DsoManifest;
import com.facebook.soloader.UnpackingSoSource.InputDso;
import com.facebook.soloader.UnpackingSoSource.InputDsoIterator;
import com.facebook.soloader.UnpackingSoSource.Unpacker;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public final class ExoSoSource extends UnpackingSoSource {
    public ExoSoSource(Context context, String name) {
        super(context, name);
    }

    protected Unpacker makeUnpacker() throws IOException {
        return new ExoSoSource.ExoUnpacker();
    }

    private static final class FileDso extends Dso {
        final File backingFile;

        FileDso(String name, String hash, File backingFile) {
            super(name, hash);
            this.backingFile = backingFile;
        }
    }

    private final class ExoUnpacker extends Unpacker {
        private final ExoSoSource.FileDso[] mDsos;

        ExoUnpacker() throws IOException {
            Context context = ExoSoSource.this.mContext;
            File exoDir = new File("/data/local/tmp/exopackage/" + context.getPackageName() + "/native-libs/");
            ArrayList providedLibraries = new ArrayList();
            String[] arr$ = SysUtil.getSupportedAbis();
            int len$ = arr$.length;

            label303:
            for(int i$ = 0; i$ < len$; ++i$) {
                String abi = arr$[i$];
                File abiDir = new File(exoDir, abi);
                if(abiDir.isDirectory()) {
                    File metadataFileName = new File(abiDir, "metadata.txt");
                    if(metadataFileName.isFile()) {
                        FileReader fr = new FileReader(metadataFileName);
                        Throwable var12 = null;

                        try {
                            BufferedReader x2 = new BufferedReader(fr);
                            Throwable var14 = null;

                            try {
                                while(true) {
                                    String x21;
                                    do {
                                        if((x21 = x2.readLine()) == null) {
                                            continue label303;
                                        }
                                    } while(x21.length() == 0);

                                    int sep = x21.indexOf(32);
                                    if(sep == -1) {
                                        throw new RuntimeException("illegal line in exopackage metadata: [" + x21 + "]");
                                    }

                                    String soName = x21.substring(0, sep) + ".so";
                                    int nrAlreadyProvided = providedLibraries.size();
                                    boolean found = false;

                                    for(int backingFileBaseName = 0; backingFileBaseName < nrAlreadyProvided; ++backingFileBaseName) {
                                        if(((ExoSoSource.FileDso)providedLibraries.get(backingFileBaseName)).name.equals(soName)) {
                                            found = true;
                                            break;
                                        }
                                    }

                                    if(!found) {
                                        String var46 = x21.substring(sep + 1);
                                        providedLibraries.add(new ExoSoSource.FileDso(soName, var46, new File(abiDir, var46)));
                                    }
                                }
                            } catch (Throwable var42) {
                                var14 = var42;
                                throw var42;
                            } finally {
                                if(x2 != null) {
                                    if(var14 != null) {
                                        try {
                                            x2.close();
                                        } catch (Throwable var41) {
                                            var14.addSuppressed(var41);
                                        }
                                    } else {
                                        x2.close();
                                    }
                                }

                            }
                        } catch (Throwable var44) {
                            var12 = var44;
                            throw var44;
                        } finally {
                            if(fr != null) {
                                if(var12 != null) {
                                    try {
                                        fr.close();
                                    } catch (Throwable var40) {
                                        var12.addSuppressed(var40);
                                    }
                                } else {
                                    fr.close();
                                }
                            }

                        }
                    }
                }
            }

            this.mDsos = (ExoSoSource.FileDso[])providedLibraries.toArray(new ExoSoSource.FileDso[providedLibraries.size()]);
        }

        protected DsoManifest getDsoManifest() throws IOException {
            return new DsoManifest(this.mDsos);
        }

        protected InputDsoIterator openDsoIterator() throws IOException {
            return new ExoSoSource.ExoUnpacker.FileBackedInputDsoIterator();
        }

        private final class FileBackedInputDsoIterator extends InputDsoIterator {
            private int mCurrentDso;

            private FileBackedInputDsoIterator() {
            }

            public boolean hasNext() {
                return this.mCurrentDso < ExoUnpacker.this.mDsos.length;
            }

            public InputDso next() throws IOException {
                ExoSoSource.FileDso fileDso = ExoUnpacker.this.mDsos[this.mCurrentDso++];
                FileInputStream dsoFile = new FileInputStream(fileDso.backingFile);

                InputDso var4;
                try {
                    InputDso ret = new InputDso(fileDso, dsoFile);
                    dsoFile = null;
                    var4 = ret;
                } finally {
                    if(dsoFile != null) {
                        dsoFile.close();
                    }

                }

                return var4;
            }
        }
    }
}
