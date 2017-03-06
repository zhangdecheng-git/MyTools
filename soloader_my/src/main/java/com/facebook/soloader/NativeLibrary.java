//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.soloader;

import android.util.Log;

import java.util.Iterator;
import java.util.List;

public abstract class NativeLibrary {
    private static final String TAG = NativeLibrary.class.getName();
    private final Object mLock = new Object();
    private List<String> mLibraryNames;
    private Boolean mLoadLibraries = Boolean.valueOf(true);
    private boolean mLibrariesLoaded = false;
    private volatile UnsatisfiedLinkError mLinkError = null;

    protected NativeLibrary(List<String> libraryNames) {
        this.mLibraryNames = libraryNames;
    }

    public boolean loadLibraries() {
        Object var1 = this.mLock;
        synchronized(this.mLock) {
            if(!this.mLoadLibraries.booleanValue()) {
                return this.mLibrariesLoaded;
            } else {
                try {
                    Iterator error = this.mLibraryNames.iterator();

                    while(error.hasNext()) {
                        String name = (String)error.next();
                        SoLoader.loadLibrary(name);
                    }

                    this.initialNativeCheck();
                    this.mLibrariesLoaded = true;
                    this.mLibraryNames = null;
                } catch (UnsatisfiedLinkError var5) {
                    Log.e(TAG, "Failed to load native lib: ", var5);
                    this.mLinkError = var5;
                    this.mLibrariesLoaded = false;
                }

                this.mLoadLibraries = Boolean.valueOf(false);
                return this.mLibrariesLoaded;
            }
        }
    }

    public void ensureLoaded() throws UnsatisfiedLinkError {
        if(!this.loadLibraries()) {
            throw this.mLinkError;
        }
    }

    protected void initialNativeCheck() throws UnsatisfiedLinkError {
    }

    public UnsatisfiedLinkError getError() {
        return this.mLinkError;
    }
}
