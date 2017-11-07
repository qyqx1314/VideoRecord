package com.banger.zeromq.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 鏂囦欢鎿嶄綔绫�
 */
public class FileUtil {
    
	/**
     * 
     * @param filename
     * @return
     */
    public static String getParentPath(String path){
    	File file = new File(path);
    	return file.getParent().replaceAll("\\\\","/");
    }
    
    /**
     * 寰楀埌鐩稿璺緞 ../
     * @param path
     * @return
     */
    public static String getCanonicalPath(String path){
    	File file = new File(path);
    	if(file.isAbsolute()){
    		String newPath = path.replaceAll("\\\\","/");
    		int n = newPath.indexOf("../");
	    	while(n>-1){
	    		int m = n>1?newPath.substring(0,n-1).lastIndexOf("/"):-1;
	    		if(m>-1){
	    			newPath = newPath.substring(0,m+1)+newPath.substring(n+3);
	    		}else{
	    			newPath = newPath.substring(n+3);
	    		}
	    		n = newPath.indexOf("../");
	    	}
	    	return newPath;
    	}else{
    		try {
    			return file.getCanonicalPath().replaceAll("\\\\","/");
    		} catch (IOException e) {
    			e.printStackTrace();
    			return path;
    		}
    	}
    }
    
    /**
     * 寰楀埌鏂囦欢鐨勭粷瀵硅矾寰�
     * @param path
     * @return
     */
    public static String getFileFullPath(String path){
    	String newPath = path.replaceAll("\\\\","/");
    	File file=new File(newPath);
    	return file.getAbsolutePath();
    }
    
    /**
     * 寰楀埌鏂囦欢鍚�
     * @param fullFileName
     * @return
     */
    public static String getFileName(String fullFileName){
        String path = fullFileName;
        int bit = 0;
        int n1 = path.lastIndexOf("/");
        if (n1 < 0) { n1 = path.lastIndexOf("\\"); }
        if (n1 > - 1) bit = 1;
        int s = n1 + bit;
        if(s>0){
        	return path.substring(s, path.length());
        }else{
        	return fullFileName;
        }
    }
    
    public static String getFileString(String fullFileName,String encoding){
    	String line;
    	StringBuilder sb=new StringBuilder();
    	try {
	    	File file=new File(fullFileName);
	    	FileInputStream stream=new FileInputStream(file);
	    	InputStreamReader inputReader = new InputStreamReader(stream,encoding);
	    	BufferedReader reader=new BufferedReader(inputReader);
			while ((line = reader.readLine()) != null) {
				sb.append(line+"\r\n");
			}
			stream.close();
			inputReader.close();
			reader.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		String fileString=sb.toString();
		return fileString;
    }
    
	/**
	 * 寰楀埌Xml鏂囦欢鍐呭
	 * @param fullFileName
	 * @param encoding
	 * @return
	 */
	public static String getXmlFileString(String fullFileName,String encoding)
    {
    	String line;
		StringBuilder sb=new StringBuilder();
    	try {
	    	File file=new File(fullFileName);
	    	FileInputStream stream=new FileInputStream(file);
	    	InputStreamReader inputReader = new InputStreamReader(stream,encoding);
	    	BufferedReader reader=new BufferedReader(inputReader);
			while ((line = reader.readLine()) != null) {
				sb.append(line+"");
			}
			stream.close();
			inputReader.close();
			reader.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		String fieString=sb.toString();
		return (fieString.length()>0 && (fieString.charAt(0)==65279))?fieString.substring(1,fieString.length()):fieString;
    }
	
	/**
	 * 鎷兼帴鏂囦欢璺緞
	 * @param path
	 * @param filename
	 * @return
	 */
    public static String contact(String path,String filename)
    {
    	char c = path.charAt(path.length() - 1);
        if (c == '/' || c == '\\')
            return StringUtil.format("{0}{1}", new Object[] { path, filename });
        else{
        	if(filename.length()>0 && filename.charAt(0)=='/'){
        		return StringUtil.format("{0}{1}", new Object[] { path, filename });
        	}else{
        		return StringUtil.format("{0}/{1}", new Object[] { path, filename });
        	}
        }
    }
    
    /**
     * 鍒ゆ柇鏂囦欢鏄惁瀛樺湪
     * @param path
     * @return
     */
    public static boolean isExistFile(String filename){
    	File f=new File(filename);
        return f.isFile() && f.exists();
    }
    
    /**
     * 鍒ゆ柇鏂囦欢璺緞鏄惁瀛樺湪
     * @param path
     * @return
     */
    public static boolean isExistFilePath(String path)
    {
        String filePath = getFileFullPath(path);
        File f=new File(filePath);
        return f.isDirectory() && f.exists();
    }
    
    /**
     * 鏍规嵁鏂囦欢璺緞寰楀埌鏂囦欢闆嗗悎
     * @param filePath 鏂囦欢璺緞
     * @param fix 鏂囦欢鍚庣紑
     * @return
     */
    public static String[] getFilesByPath(String filePath, String fix)
    {
    	String[] files = getFilesByPath(filePath);
        List<String> list = new ArrayList<String>();
        for(String file : files)
        {
        	String fileFix = getFileFix(file);
            if (fileFix.replace(".", "").equalsIgnoreCase(fix.replace(".", "")))
            {
                list.add(file);
            }
        }
        String[] cs = new String[list.size()];
        list.toArray(cs);
        return cs;
    }
    
    /**
     * 鏍规嵁鏂囦欢璺緞闆嗗悎寰楀埌鏂囦欢闆嗗悎
     * @param filePaths 鏂囦欢璺緞闆嗗悎
     * @param fix 鏂囦欢鍚庣紑
     * @return
     */
    public static String[] getFielsByPath(String[] filePaths, String fix)
    {
        List<String> list = new ArrayList<String>();
        for(String filePath : filePaths)
        {
        	String[] files = getFilesByPath(filePath);
            if (files.length > 0)
            {
                for(String file : files)
                {
                	String fileFix = getFileFix(file);
                    if (fileFix.equals(fix))
                    {
                        list.add(file);
                    }
                }
            }
        }
        String[] cs = new String[list.size()];
        list.toArray(cs);
        return cs;
    }

    
    /**
     * 鏍规嵁鏂囦欢鍚嶅緱鍒板悗缂�
     * @param filename 鏂囦欢鍚�
     * @return
     */
    public static String getFileFix(String filename)
    {
    	String name = getFileName(filename);
        int index = name.lastIndexOf('.');
        if (index < 0) return "";
        else
        {
            return name.substring(index+1, name.length());
        }
    }
    
    /**
     * 鏍规嵁鏂囦欢璺緞寰楀埌鏂囦欢闆嗗悎
     * @param filePath 鏂囦欢璺緞
     * @return
     */
    public static String[] getFilesByPath(String filePath){
        if (isExistFilePath(filePath)){
        	String path = getFileFullPath(filePath);
        	List<String> files = new ArrayList<String>();
        	addAllFilesByPath(path,files);
        	return files.toArray(new String[0]);
        }
        else return new String[0];
    }
    
    private static void addAllFilesByPath(String path,List<String> files){
    	File dir = new File(path);
    	if(dir.exists() && dir.isDirectory()){
    		for(String str : dir.list()){
    			String file = FileUtil.contact(dir.getAbsolutePath(),str);
    			File f = new File(file);
    			if(f.exists() && !f.isHidden()){
    				if(f.isFile())files.add(file);
    				else if(f.isDirectory()){
        				addAllFilesByPath(file,files);
        			}
    			}
    		}
    	}
    }
    
    /**
     * 鍒涘缓鏂囦欢璺緞
     * @param path
     */
    public static void createDirectory(String path)
    {
    	String fullPath = getFileFullPath(path);
    	if(!isExistFilePath(fullPath))
    	{
    		File f=new File(fullPath);
    		f.mkdirs();
    	}
    }
    
    /**
     * 鍐欏叆鏂囦欢鍐呭
     * @param fullFileName 鏂囦欢鍏ㄥ悕绉�
     * @param content 鏂囦欢鍐呭
     */
    public static void writeFile(String fullFileName, String content)
    {
        writeFile(fullFileName, content,"utf-8",true);
    }
    
    /**
     * 鍐欏叆鏂囦欢鍐呭
     * @param fullFileName 鏂囦欢鍏ㄥ悕绉�
     * @param content 鏂囦欢鍐呭
     * @param encoding	鏂囦欢缂栫爜
     */
    public static void writeFile(String fullFileName, String content,String encoding)
    {
        writeFile(fullFileName, content,encoding,true);
    }
    
    /**
     * 鍐欏叆鏂囦欢鍐呭
     * @param fullFileName 鏂囦欢鍏ㄥ悕绉�
     * @param content 鏂囦欢鍐呭
     * @param coverFlag 鏂囦欢澶嶇洊鏍囧織
     */
    public static void writeFile(String fullFileName, String content,boolean coverFlag)
    {
    	writeFile(fullFileName, content,"utf-8",coverFlag);
    }

    /**
     * 鍐欏叆鏂囦欢鍐呭
     * @param fullFileName 鏂囦欢鍏ㄥ悕绉�
     * @param content 鏂囦欢鍐呭
     * @param encoding 鏂囦欢缂栫爜
     * @param coverFlag 鏂囦欢澶嶇洊鏍囧織
     */
    public static void writeFile(String fullFileName, String content,String encoding,boolean coverFlag)
    {
    	String path = getParentPath(fullFileName);
        createDirectory(path);
        if (!isExistFile(fullFileName))
        {
        	File f=new File(fullFileName);
        	try {
				f.createNewFile();
				FileOutputStream fos = new FileOutputStream(fullFileName);
				OutputStreamWriter writer = new OutputStreamWriter(fos, encoding);
				writer.write(content);
				writer.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else
        {
            try {
            	File f=new File(fullFileName);
				FileOutputStream fos = new FileOutputStream(f,!coverFlag);
				OutputStreamWriter writer = new OutputStreamWriter(fos, encoding);
				writer.write(content);
				writer.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    public static void copyFile(String sourceFile, String newFile){
    	copyFile(new File(sourceFile),new File(newFile));
    }
    
    /**
     * 鎷疯礉鏂囦欢
     * @param sourceFile
     * @param newFile
     */
    public static void copyFile(File sourceFile, File newFile){
    	File nf=newFile;
    	File sf=sourceFile;
    	nf.mkdirs();
    	if(nf.exists())nf.delete();
        
		try {
			FileInputStream input = new FileInputStream(sf);
			BufferedInputStream inBuff=new BufferedInputStream(input); 
			 
	        FileOutputStream output = new FileOutputStream(nf); 
	        BufferedOutputStream outBuff=new BufferedOutputStream(output);
	        
	        byte[] b = new byte[1024 * 5]; 
	        int len; 
	        while ((len =inBuff.read(b)) != -1) { 
	            outBuff.write(b, 0, len); 
	        }
	        outBuff.flush(); 
	        inBuff.close(); 
	        outBuff.close(); 
	        output.close(); 
	        input.close(); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
    }
    
    /**
     * 鍒犻櫎鐩綍
     * @param path
     */
    public static boolean deleteDir(File dir){
    	if(dir.isDirectory()){
    		String[] children = dir.list();
    		
    		for(int i=0;i<children.length;i++){
    			boolean success = deleteDir(new File(dir,children[i]));
    			if(!success){
        			return false;
        		}
    		}
    	}
    	
    	return dir.delete();
    }
    
    /**
     * 寰楀埌鏂囦欢澶у皬
     * @param fullFilename 鏂囦欢涓嶅瓨鍦ㄨ繑鍥�1
     * @return
     */
    public static long getFilesize(String fullFilename){
    	File file = new File(fullFilename);
    	if(file.exists()){
    		return file.length();
    	}
    	return -1;
    }
    
    /**
     * 鏄惁涓虹粷瀵硅矾寰�
     * @param path
     * @return
     */
    public static boolean isAbsolutePath(String path)
    {
    	File file=new File(path);
    	return file.isAbsolute();
    }

	public static boolean isValidSystemFilePath(String path){
	  if(isWindows()){//鏄惁windows绯荤粺
            File file = new File(path);
            return file.isDirectory();
        }else{//鏄惁linux绯荤粺
            return path.matches("([\\\\/][\\w-]+)*$");
        }
    }

	public static boolean isWindows() {
	    boolean result = false;
	    if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
	        result = true;
	    }
	    return result;
	}
}
