package com.banger.zeromq.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


public class HttpUtil {
	/**
     * ��ָ��URL����GET����������
     * 
     * @param url
     *            ���������URL
     * @param param
     *            ��������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return URL ����Զ����Դ����Ӧ���
     */
    public static String sendGet(String url, String param,int timeout) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // �򿪺�URL֮�������
            URLConnection connection = realUrl.openConnection();
            // ����ͨ�õ���������
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����ʵ�ʵ�����
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(30000);
            connection.connect();
            
            // ��ȡ������Ӧͷ�ֶ�
            Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("����GET��������쳣��" + e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ��ָ�� URL ����POST����������
     * 
     * @param url
     *            ��������� URL
     * @param param
     *            ��������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return ����Զ����Դ����Ӧ���
     */
    public static String sendPost(String url, byte[] data,int timeout) {
        String result = "";
        try {
            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            HttpURLConnection httpConn=(HttpURLConnection)realUrl.openConnection();
            httpConn.setConnectTimeout(timeout);
            
            httpConn.setDoOutput(true);//ʹ�� URL ���ӽ������
            httpConn.setDoInput(true);//ʹ�� URL ���ӽ�������
            httpConn.setUseCaches(false);//���Ի���
            httpConn.setRequestMethod("POST");//����URL���󷽷�
            
            // ����ͨ�õ���������
            httpConn.setRequestProperty("Content-length", ""+data.length);
            httpConn.setRequestProperty("Content-Type", "application/octet-stream");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// ά�ֳ�����
            httpConn.setRequestProperty("Charset", "UTF-8");
            
            //httpConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            //httpConn.setRequestProperty("Accept-Encoding","gzip, deflate, sdch");
            //httpConn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
            //httpConn.setRequestProperty("Host","66.0.43.145:7878");
            //httpConn.setRequestProperty("User-Agent","User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
            
            //text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
            
            //��ȡ����� 
            OutputStream os = httpConn.getOutputStream();  
            os.write(data);
            os.flush();
            
            //�����Ӧ״̬
            int responseCode = httpConn.getResponseCode();
            if(HttpURLConnection.HTTP_OK == responseCode){//���ӳɹ�
             
            //����ȷ��Ӧʱ�������
            StringBuffer sb = new StringBuffer();
            String readLine;
            
            //������Ӧ�����������������Ӧ������ı���һ��
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null){
             sb.append(readLine).append("\n");
            }
            responseReader.close();
            result = sb.toString();
            }
        }catch(Exception ex){
        	ex.printStackTrace();
        	result = "<message><result>fail</result><recordNo></recordNo><error>"+ex.getMessage()+"</error></message>";
        }
        return result;
    }
}
