//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.soloader;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtractFromZipSoSource extends UnpackingSoSource {
    protected final File mZipFileName;
    protected final String mZipSearchPattern;

    public ExtractFromZipSoSource(Context context, String name, File zipFileName, String zipSearchPattern) {
        super(context, name);
        this.mZipFileName = zipFileName;
        this.mZipSearchPattern = zipSearchPattern;
    }

    protected Unpacker makeUnpacker() throws IOException {
        return new ExtractFromZipSoSource.ZipUnpacker();
    }

    private static final class ZipDso extends Dso implements Comparable {
        final ZipEntry backingEntry;
        final int abiScore;

        ZipDso(String name, ZipEntry backingEntry, int abiScore) {
            super(name, makePseudoHash(backingEntry));
            this.backingEntry = backingEntry;
            this.abiScore = abiScore;
        }

        private static String makePseudoHash(ZipEntry ze) {
            return String.format("pseudo-zip-hash-1-%s-%s-%s-%s", new Object[]{ze.getName(), Long.valueOf(ze.getSize()), Long.valueOf(ze.getCompressedSize()), Long.valueOf(ze.getCrc())});
        }

        public int compareTo(Object other) {
            return this.name.compareTo(((ExtractFromZipSoSource.ZipDso)other).name);
        }
    }

    protected class ZipUnpacker extends Unpacker {
        private ExtractFromZipSoSource.ZipDso[] mDsos;
        private final ZipFile mZipFile;

        ZipUnpacker() throws IOException {
            this.mZipFile = new ZipFile(ExtractFromZipSoSource.this.mZipFileName);
        }

        final ExtractFromZipSoSource.ZipDso[] ensureDsos() {
            if(this.mDsos == null) {
                HashMap providedLibraries = new HashMap();
                Pattern zipSearchPattern = Pattern.compile(ExtractFromZipSoSource.this.mZipSearchPattern);
                String[] supportedAbis = SysUtil.getSupportedAbis();
                Enumeration entries = this.mZipFile.entries();

                while(true) {
                    ZipEntry dsos;
                    String i;
                    int j;
                    ExtractFromZipSoSource.ZipDso zd;
                    do {
                        do {
                            Matcher nrFilteredDsos;
                            do {
                                if(!entries.hasMoreElements()) {
                                    ExtractFromZipSoSource.ZipDso[] var11 = (ExtractFromZipSoSource.ZipDso[])providedLibraries.values().toArray(new ExtractFromZipSoSource.ZipDso[providedLibraries.size()]);
                                    Arrays.sort(var11);
                                    int var12 = 0;

                                    for(int var13 = 0; var13 < var11.length; ++var13) {
                                        ExtractFromZipSoSource.ZipDso var16 = var11[var13];
                                        if(this.shouldExtract(var16.backingEntry, var16.name)) {
                                            ++var12;
                                        } else {
                                            var11[var13] = null;
                                        }
                                    }

                                    ExtractFromZipSoSource.ZipDso[] var14 = new ExtractFromZipSoSource.ZipDso[var12];
                                    int var15 = 0;

                                    for(j = 0; var15 < var11.length; ++var15) {
                                        zd = var11[var15];
                                        if(zd != null) {
                                            var14[j++] = zd;
                                        }
                                    }

                                    this.mDsos = var14;
                                    return this.mDsos;
                                }

                                dsos = (ZipEntry)entries.nextElement();
                                nrFilteredDsos = zipSearchPattern.matcher(dsos.getName());
                            } while(!nrFilteredDsos.matches());

                            String filteredDsos = nrFilteredDsos.group(1);
                            i = nrFilteredDsos.group(2);
                            j = SysUtil.findAbiScore(supportedAbis, filteredDsos);
                        } while(j < 0);

                        zd = (ExtractFromZipSoSource.ZipDso)providedLibraries.get(i);
                    } while(zd != null && j >= zd.abiScore);

                    providedLibraries.put(i, new ExtractFromZipSoSource.ZipDso(i, dsos, j));
                }
            } else {
                return this.mDsos;
            }
        }

        protected boolean shouldExtract(ZipEntry ze, String soName) {
            return true;
        }

        public void close() throws IOException {
            this.mZipFile.close();
        }

        protected final DsoManifest getDsoManifest() throws IOException {
            return new DsoManifest(this.ensureDsos());
        }

        protected final InputDsoIterator openDsoIterator() throws IOException {
            return new ExtractFromZipSoSource.ZipUnpacker.ZipBackedInputDsoIterator();
        }

        private final class ZipBackedInputDsoIterator extends InputDsoIterator {
            private int mCurrentDso;

            private ZipBackedInputDsoIterator() {
            }

            public boolean hasNext() {
                ZipUnpacker.this.ensureDsos();
                return this.mCurrentDso < ZipUnpacker.this.mDsos.length;
            }

            public InputDso next() throws IOException {
                ZipUnpacker.this.ensureDsos();
                ExtractFromZipSoSource.ZipDso zipDso = ZipUnpacker.this.mDsos[this.mCurrentDso++];
                InputStream is = ZipUnpacker.this.mZipFile.getInputStream(zipDso.backingEntry);

                InputDso var4;
                try {
                    InputDso ret = new InputDso(zipDso, is);
                    is = null;
                    var4 = ret;
                } finally {
                    if(is != null) {
                        is.close();
                    }

                }

                return var4;
            }
        }
    }
}
