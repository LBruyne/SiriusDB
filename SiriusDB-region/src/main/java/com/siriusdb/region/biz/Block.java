package com.siriusdb.region.biz;

import java.util.Arrays;

/**
 * @Description: Block类定义
 * @author: wangshaoxing
 * @date: 2021/05/16 3:36 下午
 */
public class Block {

    public static final int BLOCKSIZE = 4096;  //4KB for 1 block
    private int LRUCount = 0; //block replace strategy
    private int blockOffset = 0; //to check the block 目前在该blcok的哪个位置 显示的是在文件中的offset
    private boolean isDirty = false;  //true if the block has been modified
    private boolean isValid = false;  //true if the block is valid 判断一个block是否被写过了，没写过就是true，写过就是false，dirty一定valid,valid不一定dirty（可能没被修改过）
    private boolean isLocked = false; //被锁定不能更改
    private String filename;  //record where the block from 该block分配到哪个文件中去
    private byte[] blockData = new byte[BLOCKSIZE];  //allocate 4KB memory for 1 block

    public Block() {
        //do noting
    }

    public boolean write_data(int offset, byte[] data) {  //offset from 0 to 4096
        if (offset + data.length > BLOCKSIZE) return false;//数据太大就不能写入
        for (int i = 0; i < data.length; i++)
            blockData[i + offset] = data[i];//一个字节一个字节的写入
        isDirty = true;
        LRUCount++;//LRU增大，替换优先级降低
        return true;
    }

    public void reset_modes() {
        isDirty = isLocked = isValid = false;  //reset all modes
        LRUCount = 0;  //reset LRU counter
    }//相当于初始化一个block

    public int read_integer(int offset) {  //read integer from block data -- big-endian method 大端读取
        if (offset + 4 > BLOCKSIZE) return 0;//说明不剩下4个字节，无法存储下一个整数了，直接返回
        int b0 = blockData[offset] & 0xFF;
        int b1 = blockData[offset + 1] & 0xFF;
        int b2 = blockData[offset + 2] & 0xFF;
        int b3 = blockData[offset + 3] & 0xFF;
        LRUCount++;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }//读取整数

    public boolean write_integer(int offset, int val) {
        if (offset + 4 > BLOCKSIZE) return false;
        blockData[offset] = (byte) (val >> 24 & 0xFF);
        blockData[offset + 1] = (byte) (val >> 16 & 0xFF);
        blockData[offset + 2] = (byte) (val >> 8 & 0xFF);
        blockData[offset + 3] = (byte) (val & 0xFF);
        LRUCount++;
        isDirty = true;
        return true;
    }

    public float read_float(int offset) {
        int dat = read_integer(offset);
        return Float.intBitsToFloat(dat);
    }

    public boolean write_float(int offset, float val) {
        int dat = Float.floatToIntBits(val);
        return write_integer(offset, dat);
    }

    public String read_string(int offset, int length) {//length是字符串的长度
        byte[] buf = new byte[length];
        for (int i = 0; i < length && i < BLOCKSIZE - offset; i++)
            buf[i] = blockData[offset + i];
        LRUCount++;
        return new String(buf);
    }

    public boolean write_string(int offset, String str) {
        byte[] buf = str.getBytes();//把字符串转换成byte存入数组里
        if (offset + buf.length > BLOCKSIZE) return false;
        for (int i = 0; i < buf.length; i++)
            blockData[offset + i] = buf[i];
        LRUCount++;
        isDirty = true;
        return true;
    }

    public boolean dirty() {
        return this.isDirty;
    }

    public boolean lock() {
        return this.isLocked;
    }

    public boolean valid() {
        return this.isValid;
    }

    public String get_filename() {
        return this.filename;
    }

    public int get_block_offset() {
        return this.blockOffset;
    }

    public int get_LRU() {
        return this.LRUCount;
    }

    public byte[] get_block_data() {
        return blockData;
    }

    public void set_block_data(byte[] data) {
        this.blockData = data;
    }//相当于赋值了

    public void set_block_data() {
        Arrays.fill(blockData, (byte) 0);  //memset block data to zero
    }//重构，把数据全部赋值为0

    public void dirty(boolean flag) {
        this.isDirty = flag;
    }

    public void lock(boolean flag) {
        this.isLocked = flag;
    }

    public void valid(boolean flag) {
        this.isValid = flag;
    }

    public void set_filename(String fname) {
        this.filename = fname;
    }

    public void set_block_offset(int ofs) {
        this.blockOffset = ofs;
    }


}
