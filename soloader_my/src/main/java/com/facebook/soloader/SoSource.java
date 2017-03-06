//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.soloader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public abstract class SoSource {
    public static final int LOAD_RESULT_NOT_FOUND = 0;
    public static final int LOAD_RESULT_LOADED = 1;
    public static final int LOAD_RESULT_IMPLICITLY_PROVIDED = 2;
    public static final int LOAD_FLAG_ALLOW_IMPLICIT_PROVISION = 1;
    public static final int PREPARE_FLAG_ALLOW_ASYNC_INIT = 1;

    public SoSource() {
    }

    protected void prepare(int flags) throws IOException {
    }

    public abstract int loadLibrary(String var1, int var2) throws IOException;

    public abstract File unpackLibrary(String var1) throws IOException;

    public void addToLdLibraryPath(Collection<String> paths) {
    }
}
