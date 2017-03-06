package com.facebook.soloader;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by zhangdecheng on 2017/2/10.
 */
public class DirectorySoSource extends SoSource {
    public static final int RESOLVE_DEPENDENCIES = 1;
    public static final int ON_LD_LIBRARY_PATH = 2;
    protected final File soDirectory;
    protected final int flags;

    public DirectorySoSource(File soDirectory, int flags) {
        this.soDirectory = soDirectory;
        this.flags = flags;
    }

    public int loadLibrary(String soName, int loadFlags) throws IOException {
        return this.loadLibraryFrom(soName, loadFlags, this.soDirectory);
    }

    protected int loadLibraryFrom(String soName, int loadFlags, File libDir) throws IOException {
        Log.e("ggg", "loadLibraryFrom: soName::" +soName+",libDir:"+libDir.getAbsolutePath() );
        File soFile = new File(libDir, soName);
        if(!soFile.exists()) {
            return 0;
        } else if((loadFlags & 1) != 0 && (this.flags & 2) != 0) {
            return 2;
        } else {
            if((this.flags & 1) != 0) {
                String[] dependencies = getDependencies(soFile);

                for(int i = 0; i < dependencies.length; ++i) {
                    String dependency = dependencies[i];
                    if(!dependency.startsWith("/")) {
                        SoLoader.loadLibraryBySoName(dependency, loadFlags | 1);
                    }
                }
            }
            Log.e("ggg", "loadLibraryFrom: soFile.getAbsolutePath():::" +soFile.getAbsolutePath() );
            System.load(soFile.getAbsolutePath());
            return 1;
        }
    }

    private static String[] getDependencies(File soFile) throws IOException {
        try {
            String[] var1 = MinElf.extract_DT_NEEDED(soFile);
            return var1;
        } finally {
            ;
        }
    }

    public File unpackLibrary(String soName) throws IOException {
        File soFile = new File(this.soDirectory, soName);
        return soFile.exists()?soFile:null;
    }

    public void addToLdLibraryPath(Collection<String> paths) {
        paths.add(this.soDirectory.getAbsolutePath());
    }
}

