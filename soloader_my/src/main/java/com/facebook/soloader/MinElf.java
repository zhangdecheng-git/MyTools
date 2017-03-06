//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.facebook.soloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public final class MinElf {
    public static final int ELF_MAGIC = 1179403647;
    public static final int DT_NULL = 0;
    public static final int DT_NEEDED = 1;
    public static final int DT_STRTAB = 5;
    public static final int PT_LOAD = 1;
    public static final int PT_DYNAMIC = 2;
    public static final int PN_XNUM = 65535;

    public MinElf() {
    }

    public static String[] extract_DT_NEEDED(File elfFile) throws IOException {
        FileInputStream is = new FileInputStream(elfFile);

        String[] var2;
        try {
            var2 = extract_DT_NEEDED(is.getChannel());
        } finally {
            is.close();
        }

        return var2;
    }

    public static String[] extract_DT_NEEDED(FileChannel fc) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        if(getu32(fc, bb, 0L) != 1179403647L) {
            throw new MinElf.ElfError("file is not ELF");
        } else {
            boolean is32 = getu8(fc, bb, 4L) == 1;
            if(getu8(fc, bb, 5L) == 2) {
                bb.order(ByteOrder.BIG_ENDIAN);
            }

            long e_phoff = is32?getu32(fc, bb, 28L):get64(fc, bb, 32L);
            long e_phnum = is32?(long)getu16(fc, bb, 44L):(long)getu16(fc, bb, 56L);
            int e_phentsize = is32?getu16(fc, bb, 42L):getu16(fc, bb, 54L);
            long dynStart;
            long phdr;
            if(e_phnum == 65535L) {
                dynStart = is32?getu32(fc, bb, 32L):get64(fc, bb, 40L);
                phdr = is32?getu32(fc, bb, dynStart + 28L):getu32(fc, bb, dynStart + 44L);
                e_phnum = phdr;
            }

            dynStart = 0L;
            phdr = e_phoff;

            long d_tag;
            for(d_tag = 0L; d_tag < e_phnum; ++d_tag) {
                long nr_DT_NEEDED = is32?getu32(fc, bb, phdr + 0L):getu32(fc, bb, phdr + 0L);
                if(nr_DT_NEEDED == 2L) {
                    long p_offset = is32?getu32(fc, bb, phdr + 4L):get64(fc, bb, phdr + 8L);
                    dynStart = p_offset;
                    break;
                }

                phdr += (long)e_phentsize;
            }

            if(dynStart == 0L) {
                throw new MinElf.ElfError("ELF file does not contain dynamic linking information");
            } else {
                int var30 = 0;
                long dyn = dynStart;
                long ptr_DT_STRTAB = 0L;

                do {
                    d_tag = is32?getu32(fc, bb, dyn + 0L):get64(fc, bb, dyn + 0L);
                    if(d_tag == 1L) {
                        if(var30 == 2147483647) {
                            throw new MinElf.ElfError("malformed DT_NEEDED section");
                        }

                        ++var30;
                    } else if(d_tag == 5L) {
                        ptr_DT_STRTAB = is32?getu32(fc, bb, dyn + 4L):get64(fc, bb, dyn + 8L);
                    }

                    dyn += is32?8L:16L;
                } while(d_tag != 0L);

                if(ptr_DT_STRTAB == 0L) {
                    throw new MinElf.ElfError("Dynamic section string-table not found");
                } else {
                    long off_DT_STRTAB = 0L;
                    phdr = e_phoff;

                    long d_val;
                    for(int needed = 0; (long)needed < e_phnum; ++needed) {
                        d_val = is32?getu32(fc, bb, phdr + 0L):getu32(fc, bb, phdr + 0L);
                        if(d_val == 1L) {
                            long p_vaddr = is32?getu32(fc, bb, phdr + 8L):get64(fc, bb, phdr + 16L);
                            long p_memsz = is32?getu32(fc, bb, phdr + 20L):get64(fc, bb, phdr + 40L);
                            if(p_vaddr <= ptr_DT_STRTAB && ptr_DT_STRTAB < p_vaddr + p_memsz) {
                                long p_offset1 = is32?getu32(fc, bb, phdr + 4L):get64(fc, bb, phdr + 8L);
                                off_DT_STRTAB = p_offset1 + (ptr_DT_STRTAB - p_vaddr);
                                break;
                            }
                        }

                        phdr += (long)e_phentsize;
                    }

                    if(off_DT_STRTAB == 0L) {
                        throw new MinElf.ElfError("did not find file offset of DT_STRTAB table");
                    } else {
                        String[] var31 = new String[var30];
                        var30 = 0;
                        dyn = dynStart;

                        do {
                            d_tag = is32?getu32(fc, bb, dyn + 0L):get64(fc, bb, dyn + 0L);
                            if(d_tag == 1L) {
                                d_val = is32?getu32(fc, bb, dyn + 4L):get64(fc, bb, dyn + 8L);
                                var31[var30] = getSz(fc, bb, off_DT_STRTAB + d_val);
                                if(var30 == 2147483647) {
                                    throw new MinElf.ElfError("malformed DT_NEEDED section");
                                }

                                ++var30;
                            }

                            dyn += is32?8L:16L;
                        } while(d_tag != 0L);

                        if(var30 != var31.length) {
                            throw new MinElf.ElfError("malformed DT_NEEDED section");
                        } else {
                            return var31;
                        }
                    }
                }
            }
        }
    }

    private static String getSz(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        StringBuilder sb = new StringBuilder();

        short b;
        while((b = getu8(fc, bb, offset++)) != 0) {
            sb.append((char)b);
        }

        return sb.toString();
    }

    private static void read(FileChannel fc, ByteBuffer bb, int sz, long offset) throws IOException {
        bb.position(0);
        bb.limit(sz);

        while(bb.remaining() > 0) {
            int numBytesRead = fc.read(bb, offset);
            if(numBytesRead == -1) {
                break;
            }

            offset += (long)numBytesRead;
        }

        if(bb.remaining() > 0) {
            throw new MinElf.ElfError("ELF file truncated");
        } else {
            bb.position(0);
        }
    }

    private static long get64(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        read(fc, bb, 8, offset);
        return bb.getLong();
    }

    private static long getu32(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        read(fc, bb, 4, offset);
        return (long)bb.getInt() & 4294967295L;
    }

    private static int getu16(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        read(fc, bb, 2, offset);
        return bb.getShort() & '\uffff';
    }

    private static short getu8(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        read(fc, bb, 1, offset);
        return (short)(bb.get() & 255);
    }

    private static class ElfError extends RuntimeException {
        ElfError(String why) {
            super(why);
        }
    }
}
