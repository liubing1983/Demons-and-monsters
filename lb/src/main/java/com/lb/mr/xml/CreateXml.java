package com.lb.mr.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateXml {
	
	public static void main(String[] args){
		 File newFile=new File("/opt/1.xml");
	        FileWriter fileWriter=null;
	        PrintWriter printWriter=null;
	        try{
	            fileWriter=new FileWriter(newFile);
	            printWriter=new PrintWriter(fileWriter);
	            printWriter.print("<configuration>");
	            for(int i  = 0; i<=4000000; i++){
	            	printWriter.print(" <property> \r\n<name>dfs.replication</name> \r\n <value>"+i+"</value> \r\n </property> \r\n");
	            }
	            printWriter.print("</configuration>");
	        }catch(Exception e){
	            System.out.println("新建文件出错");
	            e.printStackTrace();
	        }finally{
	            try {
	                fileWriter.close();
	            } catch (IOException e) {
	                System.out.println("关闭流出错");
	            }
	        }
	}

}
