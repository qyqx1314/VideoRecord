/*
 * banger Inc.
 * Copyright (c) 2009-2012 All Rights Reserved.
 * ToDo       :This is Class Description...
 * Author     :yuanme
 * Create Date:2012-4-11
 */
package com.banger.zeromq.util;

/**
 * @author yuanme
 * @version SystemUtil.java,v 0.1 2012-4-11 下午2:18:30
 */
public class SystemUtil {
	
    public static boolean isWindows() {
        boolean result = false;
        if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            result = true;
        }
        return result;
    }
    
}
