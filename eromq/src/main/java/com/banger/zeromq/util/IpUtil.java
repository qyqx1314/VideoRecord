package com.banger.zeromq.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IpUtil {
	private static String ip = "127.0.0.1";
	
	public static String getIp(){
		if("127.0.0.1".equals(ip)){
			try {
				if(SystemUtil.isWindows()){
					ip =  InetAddress.getLocalHost().getHostAddress();
					return ip;
				}else{
					Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
					while(allNetInterfaces.hasMoreElements()){
						NetworkInterface netInterface = (NetworkInterface)allNetInterfaces.nextElement();
						Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
						while(addresses.hasMoreElements()){
							InetAddress addr = (InetAddress)addresses.nextElement();
							if(addr!=null && addr instanceof Inet4Address && !"127.0.0.1".equals(addr.getHostAddress())){
								ip = addr.getHostAddress();
							}
						}
					}
					return ip;
				}
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ip;
	}
	
}
