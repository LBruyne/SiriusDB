package com.siriusdb.region.biz;

import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * @Description: BufferManager管理逻辑
 * @author: wangshaoxing
 * @date: 2021/05/16 3:45 下午
 */
@Slf4j
public class BufferManager {
    private static final int MAXBLOCKNUM = 50;  //设置最大的内存中的block数量
    private static final int EOF = -1; //none-exist num
    public static Block[] buffer = new Block[MAXBLOCKNUM];  //buffer的大小为50*4kb也就是200kb

    public BufferManager() {
        //do nothing
    }//构造函数

    public static void initial_buffer() {
        for (int i = 0; i < MAXBLOCKNUM; i++)
            buffer[i] = new Block();
    }//buffer里每个块都清空且初始化

    public static void test_interface() {
        Block b = new Block();
        b.write_integer(1200, 2245);
        b.write_float(76, (float) 2232.14);
        b.write_string(492, "test");
        b.set_filename("testname");
        b.set_block_offset(15);
        buffer[1] = b;
        write_block_to_disk(1);
    }

    public static void destruct_buffer_manager() {
        for (int i = 0; i < MAXBLOCKNUM; i++)
            if (buffer[i].valid()) write_block_to_disk(i); //write back to disk if it's valid
    }//把整个buffer写回硬盘，如果block是valid就写回硬盘，否则不写回

    public static void make_invalid(String filename) {
        for (int i = 0; i < MAXBLOCKNUM; i++)
            if (buffer[i].get_filename() != null && buffer[i].get_filename().equals(filename))//存在这个文件，且文件的的名字相匹配
                buffer[i].valid(false);
    }

    //if the block exist and it's valid, return this block else return a empty block
    public static int read_block_from_disk(String filename, int ofs) {
        int i, j;
        for (i = 0; i < MAXBLOCKNUM; i++)  //find the target block
            if (buffer[i].valid() && buffer[i].get_filename().equals(filename)
                    && buffer[i].get_block_offset() == ofs) return i;
        File file = new File(filename); //block does not found
        int bid = get_free_block_id();
        try {
            if (bid == EOF) return EOF; //there are no free blocks
            if (!file.exists()) file.createNewFile();  //if not exists such file
            if (!read_block_from_disk(filename, ofs, bid)) return EOF;
        } catch (Exception e) {
            return EOF;
        }
        return bid;
    }//从磁盘读取到块里

    //if the block exist and it's valid, return this block else return a empty block
    public static Block read_block_from_disk_quote(String filename, int ofs) {
        int i, j;
        for (i = 0; i < MAXBLOCKNUM; i++)  //find the target block
            if (buffer[i].valid() && buffer[i].get_filename().equals(filename)
                    && buffer[i].get_block_offset() == ofs) break;
        if (i < MAXBLOCKNUM) {  //there exist a block
            return buffer[i];
        } else { //block does not found
            File file = new File(filename);
            int bid = get_free_block_id();
            if (bid == EOF || !file.exists()) return null; //there are no free blocks
            if (!read_block_from_disk(filename, ofs, bid)) return null;
            return buffer[bid];
        }
    }

    //把文件里的数据读入内存
    private static boolean read_block_from_disk(String filename, int ofs, int bid) {
        boolean flag = false;  //check whether operation is success
        byte[] data = new byte[Block.BLOCKSIZE];  //temporary data一个空的block临时存储数据
        RandomAccessFile raf = null;  //to seek the position for data to write
        try {
            File in = new File(filename);
            raf = new RandomAccessFile(in, "rw");//可写可读
            if ((ofs + 1) * Block.BLOCKSIZE <= raf.length()) {  //if the block is in valid position //如果超出了就说明数据溢出，无法读取
                raf.seek(ofs * Block.BLOCKSIZE);
                raf.read(data, 0, Block.BLOCKSIZE);//光标已经移到ofs了，所以0就ok了
            } else {  //when overflow it returns an empty block
                Arrays.fill(data, (byte) 0);
            }
            flag = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (raf != null) raf.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (flag) { //must reset all modes and data after successfully handle the file
            buffer[bid].reset_modes();
            buffer[bid].set_block_data(data);
            buffer[bid].set_filename(filename);
            buffer[bid].set_block_offset(ofs);
            buffer[bid].valid(true);  //make it valid
        }
        return flag;
    }

    //把内存里的数据存入文件里
    private static boolean write_block_to_disk(int bid) {
        if (!buffer[bid].dirty()) {  //block is valid but does not be modified
            buffer[bid].valid(false);  //only to make it invalid
            return true;
        }//

        RandomAccessFile raf = null;  //to seek the position for data to write
        try {
            File out = new File(buffer[bid].get_filename());
            raf = new RandomAccessFile(out, "rw");
            if (!out.exists()) out.createNewFile();  //if file does not exist
            raf.seek(buffer[bid].get_block_offset() * Block.BLOCKSIZE);
            raf.write(buffer[bid].get_block_data());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            try {
                if (raf != null) raf.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        buffer[bid].valid(false);  //make it invalid
        return true;
    }

    private static int get_free_block_id() {
        int i;
        int index = EOF;  //-1 for none free block exist
        int mincount = 0x7FFFFFFF;  //initialize with maximum integer
        for (i = 0; i < MAXBLOCKNUM; i++) {
            if (!buffer[i].lock() && buffer[i].get_LRU() < mincount) {
                index = i;
                mincount = buffer[i].get_LRU();
            }
        }
        if (index != EOF && buffer[index].dirty())  //if the block is dirty
            write_block_to_disk(index);
        return index;
    }//找一个空的block，从前往后找，如果是空的，或者LRU可以替换的话，就替换
}
