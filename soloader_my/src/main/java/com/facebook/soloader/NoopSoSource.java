//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.soloader;

import java.io.File;

public class NoopSoSource extends SoSource {
    public NoopSoSource() {
    }

    public int loadLibrary(String soName, int loadFlags) {
        return 1;
    }

    public File unpackLibrary(String soName) {
        throw new UnsupportedOperationException("unpacking not supported in test mode");
    }
}
