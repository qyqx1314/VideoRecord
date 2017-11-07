package com.banger.zeromq.zmq;

import android.os.Handler;
import android.os.Message;

import com.banger.zeromq.util.IpUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.zeromq.ZContext;

public class FileUploadServer implements IFileUploadServer, IUploadConfig {
    private int threadCount = 1;
    private String serverAddress;
    private String localIp;
    private ZContext context;
    private Map<String, UploadFileState> uploadMap;
    private IUploadSpeedLimitService speedService;
    ExecutorService threadPool;
    static IFileUploadServer instance;

    public static IFileUploadServer newInstance(String serverAddr, int threadCount) {
        if (instance == null) {
            instance = new FileUploadServer(serverAddr, threadCount);
        }
        return instance;
    }

    private FileUploadServer(String serverAddress, int threadCount) {
        this.threadCount = threadCount;
        this.threadPool = Executors.newFixedThreadPool(this.threadCount);
        this.serverAddress = serverAddress;                    // http://127.0.0.1:8080/phone;
        this.localIp = IpUtil.getIp();
        this.context = new ZContext(1);
        this.speedService = new UploadSpeedLimitService();
        this.uploadMap = new HashMap<String, UploadFileState>();
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getLocalIp() {
        return localIp;
    }

    /**
     * IUploadCallBack callback
     */
    public void uploadFileTask(String filename, Handler handler,int link ,String bizID) {
        if (new File(filename).exists()) {
            if (this.uploadMap.containsKey(filename)) {
                Message message = Message.obtain();
                UploadFileState state = this.uploadMap.get(filename);
                if (state.getState() == UploadState.failed || state.getState() == UploadState.error) {
                    UploadFileTask task = new UploadFileTask(this.context, this, state, this.speedService, handler,link,bizID);
                    threadPool.submit(task);
                } else {
                    if (state.getState() == UploadState.waiting || state.getState() == UploadState.uploading) {
                        state.setMessage("文件正在排队上传中:" + filename);
                    }
                    message.arg1 = link;
                    message.obj = state;
                    handler.sendMessage(message);
                }
            } else {
                UploadFileState state = new UploadFileState(filename);
                this.uploadMap.put(filename, state);
                UploadFileTask task = new UploadFileTask(this.context, this, state, this.speedService, handler,link,bizID);
                threadPool.submit(task);
            }
        } else {
            Message message = Message.obtain();
            UploadFileState state = new UploadFileState(filename);
            state.setState(UploadState.failed);
            state.setMessage("上传文件不存在:" + filename);
//            callback.callback(state);
            message.arg1 = link;
            message.obj = state;
            handler.sendMessage(message);
        }
    }

    /**
     *
     */
    public IUploadFileState getUploadFileStatus(String filename) {
        if (this.uploadMap.containsKey(filename)) {
            return this.uploadMap.get(filename);
        }
        return null;

    }

}
