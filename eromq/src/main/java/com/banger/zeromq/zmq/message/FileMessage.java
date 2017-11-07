package com.banger.zeromq.zmq.message;

import com.banger.zeromq.util.ByteUtil;
import com.banger.zeromq.util.FileUtil;
import com.banger.zeromq.util.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Date;

public class FileMessage extends AbstractMessage implements IBlobMessage {

    private final int midStart = 8;     //消息ID存放的超始位
    private final int midLen = 64;      //消息ID的长度
    private final int sizeStart = 72;   //文件大小
    private final int sizeLen = 8;      //存放文件大小的位数
    private final int psStart = 80;     //分包大小
    private final int psLen = 4;        //存放分包大小的位数
    private final int piStart = 84;     //分包索引
    private final int piLen = 4;        //存放分包索引的位数
    private final int fixStart = 88;    //文件后缀
    private final int fixLen = 4;       //文件后缀存放的位数
    private final int dataStart = 92;   //数据存放的起始位
    private int partCount = -1;			//分包数量
    private int batch = 1;				//一批几个包
    private File file;
    
    public FileMessage(String filename){
    	this.setMessageType(MessageType.File_Message);
    	this.setPartIndex(-1);
    	this.setPartSize(1024);
    	this.file = new File(filename);
    	if(!this.file.exists())throw new MessageException("文件不存在 "+filename);
    	else{
    		this.setMessageId(new Date().getTime()+"");
    		String fix = FileUtil.getFileFix(filename);
        	if(!StringUtil.isNullOrEmpty(fix)){
        		if(fix.charAt(0)=='.')this.setFix(fix.substring(1));
        		else this.setFix(fix);
        	}
    		this.setSize(this.file.length());
    	}
    	
    }

	public FileMessage(byte[] data){
		super(data);
	}
	
	/**
	 * 设置消息Id
	 * @param messageId
	 */
	public void setMessageId(String messageId){
		if(messageId!=null){
			if(messageId.length()>midLen){
				throw new MessageException("文件消息Id过长 "+messageId);
			}
			this.setData(messageId.getBytes(), midStart);
		}
	}
	
	/**
	 * 得到消息Id
	 * @return
	 */
	public String getMessageId(){
		byte[] data = this.getData(midStart, midLen);
		return new String(data).trim();
	}
	
	/**
	 * 文件类型
	 * @return
	 */
	public String getFix(){
		byte[] data = this.getData(fixStart, fixLen);
		return new String(ByteUtil.removeZeroBytes(data));
	}
	
	/**
	 * 文件类型
	 * @param fix
	 */
	public void setFix(String fix){
		if(fix!=null){
			if(fix.length()>fixLen){
				throw new MessageException("文件类型过长  "+fix);
			}
			this.setData(fix.getBytes(), fixStart);
		}
	}
	
	/** 
	 * 一批几个包
	 * @return
	 */
	public int getBatch() {
		return batch;
	}

	/**
	 * 设置一批几个包
	 * @param batch
	 */
	public void setBatch(int batch) {
		this.batch = batch;
	}

	/**
	 * 文件大小
	 * @return
	 */
	public long getSize(){
		byte[] data = this.getData(sizeStart, sizeLen);
		long size = ByteUtil.byteToLong(data);
		return size;
	}
	
	/**
	 * 设置文件大小
	 * @param size
	 * @return
	 */
	public void setSize(long size){
		byte[] data = ByteUtil.longToByte(size);
		this.setData(data, sizeStart);
	}
	
	/**
	 * 分包大小
	 * @return
	 */
	public int getPartSize(){
		byte[] data = this.getData(psStart, psLen);
		int size = ByteUtil.byteToInt(data);
		return size;
	}
	
	/**
	 * 分包大小
	 * @param size
	 */
	public void setPartSize(int size){
		byte[] data = ByteUtil.intToByte(size);
		this.setData(data, psStart);
	}
	
	/**
	 * 得到分包索引
	 * @return
	 */
	public int getPartIndex(){
		byte[] data = this.getData(piStart, piLen);
		int size = ByteUtil.byteToInt(data);
		return size;
	}
	
	/**
	 * 设置分包索引
	 */
	public void setPartIndex(int index){
		byte[] data = ByteUtil.intToByte(index);
		this.setData(data, piStart);
	}
	
	/**
	 * 分包数据
	 */
	public int getPartCount(){
		if(this.partCount<0){
			long s1 = this.getSize();
			long s2 = (long)this.getPartSize();
			long s3 = (s1%s2==0)?s1/s2:s1/s2+1;
			this.partCount = Integer.parseInt(String.valueOf(s3));
		}
		return this.partCount;
	}
	
	/**
	 * 得到分包数据
	 * @return
	 */
	public byte[] getPartData(){
		return this.getData(dataStart,this.data.length-dataStart);
	}
	
	/**
	 * 设置分包数据
	 */
	public void setPartData(byte[] data){
		this.setData(data, dataStart);
	}
	
	public boolean isLast(){
		boolean flag = this.getPartIndex()<this.getPartCount();
		return !flag;
	}
	
	/**
	 * 一批的最后一个包
	 * @return
	 */
	public boolean isBatchEnd(){
		int partIndex = this.getPartIndex();
		if(this.batch>1){
			int batchMod = (partIndex+1)%this.batch;
			int lastIndex = this.getPartCount()-1;
			if(batchMod==0){
				return true;
			}else if(partIndex == lastIndex){
	            return true;
	        }
			return false;
		}
        return true;
	}
	
	/**
	 * 读取下一个分包
	 * @return
	 */
	public boolean next(){
		int partIndex = this.getPartIndex();
		int size = this.getPartSize();
		if (partIndex < (this.getPartCount()-1)){
			partIndex++;
			this.setPartIndex(partIndex);
			byte[] data = this.readPartData();
			this.setPartData(data);
			if(data.length<size){
				this.resizeData(dataStart+data.length);				//最后一个包,重置大小
			}
            return true;
        }
        return false;
	}
	
	/**
	 * 是否有下一个包
	 * @return
	 */
	public boolean hasNext(){
		int partIndex = this.getPartIndex();
		int partCount = this.getPartCount();
		return partIndex < (partCount-1);
	}
	
	/**
	 * 读取分包数据
	 * @return
	 */
    private byte[] readPartData(){
        if(this.file.exists()){
        	int size = this.getPartSize();
            int index = this.getPartIndex();
            long pos = index*size;
	        byte[] data=new byte[size];
	        
			try {
				RandomAccessFile r = new RandomAccessFile(this.file,"r");//只读方式打开文件
				r.seek(pos);//指定下一次的开始位置
		        int len = r.read(data);
		        if(len<size){
		        	data = Arrays.copyOf(data, len);
		        }
		        r.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return data;
        }
        return null;
    }

}
