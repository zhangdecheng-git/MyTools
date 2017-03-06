//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.soloader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SoLoader {
    static final String TAG = "SoLoader";
    static final boolean DEBUG = false;
    static final boolean SYSTRACE_LIBRARY_LOADING = false;
    private static SoSource[] sSoSources = null;
    private static final Set<String> sLoadedLibraries = new HashSet();
    private static ThreadPolicy sOldPolicy = null;
    private static String SO_STORE_NAME_MAIN = "lib-main";
    public static final int SOLOADER_ENABLE_EXOPACKAGE = 1;
    public static final int SOLOADER_ALLOW_ASYNC_INIT = 2;
    private static int sFlags;

    public SoLoader() {
    }

    public static void init(Context context, int flags) throws IOException {
        ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();

        try {
            initImpl(context, flags);
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }

    }

    public static void init(Context context, boolean nativeExopackage) {
        try {
            init(context, nativeExopackage?1:0);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    private static synchronized void initImpl(Context context, int flags) throws IOException {
        if(sSoSources == null) {
            sFlags = flags;
            ArrayList soSources = new ArrayList();
            String LD_LIBRARY_PATH = System.getenv("LD_LIBRARY_PATH");
            if(LD_LIBRARY_PATH == null) {
                LD_LIBRARY_PATH = "/vendor/lib:/system/lib";
            }

            String[] systemLibraryDirectories = LD_LIBRARY_PATH.split(":");

            for(int finalSoSources = 0; finalSoSources < systemLibraryDirectories.length; ++finalSoSources) {
                File prepareFlags = new File(systemLibraryDirectories[finalSoSources]);
                soSources.add(new DirectorySoSource(prepareFlags, 2));
            }

            if(context != null) {
                if((flags & 1) != 0) {
                    soSources.add(0, new ExoSoSource(context, SO_STORE_NAME_MAIN));
                } else {
                    ApplicationInfo var11 = context.getApplicationInfo();
                    boolean var13 = (var11.flags & 1) != 0 && (var11.flags & 128) == 0;
                    byte i;
                    if(var13) {
                        i = 0;
                    } else {
                        i = 1;
                        int ourSoSourceFlags = 0;
                        if(VERSION.SDK_INT <= 17) {
                            ourSoSourceFlags |= 1;
                        }

                        DirectorySoSource ourSoSource = new DirectorySoSource(new File(var11.nativeLibraryDir), ourSoSourceFlags);
                        soSources.add(0, ourSoSource);
                    }

                    soSources.add(0, new ApkSoSource(context, SO_STORE_NAME_MAIN, i));
                }
            }

            SoSource[] var10 = (SoSource[])soSources.toArray(new SoSource[soSources.size()]);
            int var12 = makePrepareFlags();
            int var14 = var10.length;

            while(var14-- > 0) {
                var10[var14].prepare(var12);
            }

            sSoSources = var10;
        }

    }

    private static int makePrepareFlags() {
        int prepareFlags = 0;
        if((sFlags & 2) != 0) {
            prepareFlags |= 1;
        }

        return prepareFlags;
    }

    public static void setInTestMode() {
        sSoSources = new SoSource[]{new NoopSoSource()};
    }

    public static synchronized void loadLibrary(String shortName) throws UnsatisfiedLinkError {
        if(sSoSources == null) {
            if(!"http://www.android.com/".equals(System.getProperty("java.vendor.url"))) {
                System.loadLibrary(shortName);
                return;
            }

            assertInitialized();
        }

        try {
            loadLibraryBySoName(System.mapLibraryName(shortName), 0);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        } catch (UnsatisfiedLinkError var4) {
            String message = var4.getMessage();
            if(message != null && message.contains("unexpected e_machine:")) {
                throw new SoLoader.WrongAbiError(var4);
            } else {
                throw var4;
            }
        }
    }

    public static File unpackLibraryAndDependencies(String shortName) throws UnsatisfiedLinkError {
        assertInitialized();

        try {
            return unpackLibraryBySoName(System.mapLibraryName(shortName));
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static void loadLibraryBySoName(String soName, int loadFlags) throws IOException {
        int result = sLoadedLibraries.contains(soName)?1:0;
        if(result == 0) {
            boolean restoreOldPolicy = false;
            if(sOldPolicy == null) {
                sOldPolicy = StrictMode.allowThreadDiskReads();
                restoreOldPolicy = true;
            }

            try {
                for(int i = 0; result == 0 && i < sSoSources.length; ++i) {
                    result = sSoSources[i].loadLibrary(soName, loadFlags);
                }
            } finally {
                if(restoreOldPolicy) {
                    StrictMode.setThreadPolicy(sOldPolicy);
                    sOldPolicy = null;
                }

            }
        }

        if(result == 0) {
            throw new UnsatisfiedLinkError("couldn\'t find DSO to load: " + soName);
        } else {
            if(result == 1) {
                sLoadedLibraries.add(soName);
            }

        }
    }

    static File unpackLibraryBySoName(String soName) throws IOException {
        for(int i = 0; i < sSoSources.length; ++i) {
            File unpacked = sSoSources[i].unpackLibrary(soName);
            if(unpacked != null) {
                return unpacked;
            }
        }

        throw new FileNotFoundException(soName);
    }

    private static void assertInitialized() {
        if(sSoSources == null) {
            throw new RuntimeException("SoLoader.init() not yet called");
        }
    }

    public static synchronized void prependSoSource(SoSource extraSoSource) throws IOException {
        assertInitialized();
        extraSoSource.prepare(makePrepareFlags());
        SoSource[] newSoSources = new SoSource[sSoSources.length + 1];
        newSoSources[0] = extraSoSource;
        System.arraycopy(sSoSources, 0, newSoSources, 1, sSoSources.length);
        sSoSources = newSoSources;
    }

    public static synchronized String makeLdLibraryPath() {
        assertInitialized();
        ArrayList pathElements = new ArrayList();
        SoSource[] soSources = sSoSources;

        for(int i = 0; i < soSources.length; ++i) {
            soSources[i].addToLdLibraryPath(pathElements);
        }

        return TextUtils.join(":", pathElements);
    }

    public static final class WrongAbiError extends UnsatisfiedLinkError {
        WrongAbiError(Throwable cause) {
            super("APK was built for a different platform");
            this.initCause(cause);
        }
    }
}
