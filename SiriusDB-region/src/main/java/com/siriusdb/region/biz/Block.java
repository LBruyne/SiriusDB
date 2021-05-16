package com.siriusdb.region.biz;

import java.util.Arrays;

/**
 * @Description: Block�ඨ��
 * @author: wangshaoxing
 * @date: 2021/05/16 3:36 ����
 */
public class Block {

    public static final int BLOCKSIZE = 4096;  //4KB for 1 block
    private int LRUCount = 0; //block replace strategy
    private int blockOffset = 0; //to check the block Ŀǰ�ڸ�blcok���ĸ�λ�� ��ʾ�������ļ��е�offset
    private boolean isDirty = false;  //true if the block has been modified
    private boolean isValid = false;  //true if the block is valid �ж�һ��block�Ƿ�д���ˣ�ûд������true��д������false��dirtyһ��valid,valid��һ��dirty������û���޸Ĺ���
    private boolean isLocked = false; //���������ܸ���
    private String filename;  //record where the block from ��block���䵽�ĸ��ļ���ȥ
    private byte[] blockData = new byte[BLOCKSIZE];  //allocate 4KB memory for 1 block

    public Block() {
        //do noting
    }

    public boolean write_data(int offset, byte[] data) {  //offset from 0 to 4096
        if (offset + data.length > BLOCKSIZE) return false;//����̫��Ͳ���д��
        for (int i = 0; i < data.length; i++)
            blockData[i + offset] = data[i];//һ���ֽ�һ���ֽڵ�д��
        isDirty = true;
        LRUCount++;//LRU�����滻���ȼ�����
        return true;
    }

    public void reset_modes() {
        isDirty = isLocked = isValid = false;  //reset all modes
        LRUCount = 0;  //reset LRU counter
    }//�൱�ڳ�ʼ��һ��block

    public int read_integer(int offset) {  //read integer from block data -- big-endian method ��˶�ȡ
        if (offset + 4 > BLOCKSIZE) return 0;//˵����ʣ��4���ֽڣ��޷��洢��һ�������ˣ�ֱ�ӷ���
        int b0 = blockData[offset] & 0xFF;
        int b1 = blockData[offset + 1] & 0xFF;
        int b2 = blockData[offset + 2] & 0xFF;
        int b3 = blockData[offset + 3] & 0xFF;
        LRUCount++;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }//��ȡ����

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

    public String read_string(int offset, int length) {//length���ַ����ĳ���
        byte[] buf = new byte[length];
        for (int i = 0; i < length && i < BLOCKSIZE - offset; i++)
            buf[i] = blockData[offset + i];
        LRUCount++;
        return new String(buf);
    }

    public boolean write_string(int offset, String str) {
        byte[] buf = str.getBytes();//���ַ���ת����byte����������
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
    }//�൱�ڸ�ֵ��

    public void set_block_data() {
        Arrays.fill(blockData, (byte) 0);  //memset block data to zero
    }//�ع���������ȫ����ֵΪ0

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
