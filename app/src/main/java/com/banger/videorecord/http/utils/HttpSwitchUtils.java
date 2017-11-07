package com.banger.videorecord.http.utils;

/**
 * Created by zhusiliang on 16/6/20.
 */
public class HttpSwitchUtils {
    /**
     * @param deviceId     设备ID
     * @param recordNo     流水号，这里就时文件名（不带后缀
     * @param fileId       文件名（不带后缀）
     * @param ip           本地IP
     * @param beginTime    录像开始时间 自己取 格式 2012-12-12 12:12:12
     * @param endTime      录像结束时间 自己取 格式 2012-12-12 12:12:12
     * @param fileName     文件名
     * @param fileSize     文件大小
     * @param recTime      时长
     * @param account      账号，没有取员工号
     * @param businessNo   自己取 业务流水号
     * @param videoProcess 录像环节
     * @return
     */
    public static String StringToXml(String deviceId, String recordNo, String fileId, String ip, String beginTime,
                                     String endTime, String fileName, String fileSize, String recTime, String account,
                                     String businessNo, String videoProcess) {
        StringBuffer sb = new StringBuffer();
        sb.append("<video>").append("/n")
                .append("<deviceId>").append(deviceId).append("</deviceId>").append("/n")
                .append("<recordNo>").append(recordNo).append("</recordNo>").append("/n")
                .append("<fileId>").append(fileId).append("</fileId>").append("/n")
                .append("<transit>").append("2").append("</transit>").append("/n")
                .append("<ip>").append(ip).append("</ip>").append("/n")
                .append("<beginTime>").append(beginTime).append("</beginTime>").append("/n")
                .append("<endTime>").append(endTime).append("</endTime>").append("/n")
                .append("<fileName>").append(fileName).append("</fileName>").append("/n")
                .append("<fileSize>").append(fileSize).append("</fileSize>").append("/n")
                .append("<recTime>").append(recTime).append("</recTime>").append("/n")
                .append("<account>").append(account).append("</account>").append("/n")
                .append("<schema>").append("Android").append("</schema>").append("/n")
                .append("<businessNo>").append(businessNo).append("</businessNo>").append("/n")
                .append("<videoProcess>").append(videoProcess).append("</videoProcess>").append("/n")
                .append("</video>");

        return sb.toString();
    }
}
