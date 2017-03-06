//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.soloader;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;

public class ApkSoSource extends ExtractFromZipSoSource {
    private static final String TAG = "ApkSoSource";
    public static final int PREFER_ANDROID_LIBS_DIRECTORY = 1;
    private static final byte APK_SIGNATURE_VERSION = 1;
    private final int mFlags;

    public ApkSoSource(Context context, String name, int flags) {
        super(context, name, new File(context.getApplicationInfo().sourceDir), "^lib/([^/]+)/([^/]+\\.so)$");
        this.mFlags = flags;
    }

    protected Unpacker makeUnpacker() throws IOException {
        return new ApkSoSource.ApkUnpacker();
    }

    protected byte[] getDepsBlock() throws IOException {
        return SysUtil.makeApkDepBlock(this.mZipFileName);
    }

    protected class ApkUnpacker extends ZipUnpacker {
        private File mLibDir;
        private final int mFlags;

        ApkUnpacker() throws IOException {
            super();
            this.mLibDir = new File(ApkSoSource.this.mContext.getApplicationInfo().nativeLibraryDir);
            this.mFlags = ApkSoSource.this.mFlags;
        }

        protected boolean shouldExtract(ZipEntry ze, String soName) {
            String zipPath = ze.getName();
            if((this.mFlags & 1) == 0) {
                Log.d("ApkSoSource", "allowing consideration of " + zipPath + ": self-extraction preferred");
                return true;
            } else {
                File sysLibFile = new File(this.mLibDir, soName);
                if(!sysLibFile.isFile()) {
                    Log.d("ApkSoSource", String.format("allowing considering of %s: %s not in system lib dir", new Object[]{zipPath, soName}));
                    return true;
                } else {
                    long sysLibLength = sysLibFile.length();
                    long apkLibLength = ze.getSize();
                    if(sysLibLength != apkLibLength) {
                        Log.d("ApkSoSource", String.format("allowing consideration of %s: sysdir file length is %s, but the file is %s bytes long in the APK", new Object[]{sysLibFile, Long.valueOf(sysLibLength), Long.valueOf(apkLibLength)}));
                        return true;
                    } else {
                        Log.d("ApkSoSource", "not allowing consideration of " + zipPath + ": deferring to libdir");
                        return false;
                    }
                }
            }
        }
    }
}
