package com.winterholiday;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;


public class ProcessDataTools {

	private ProcessDataTools (){}
	private static ProcessDataTools single=null;
	
	public static ProcessDataTools getInstance(){
		if (single == null) {    
            single = new ProcessDataTools();  
        }    
       return single;
	}
	
	public String CopyFileToAppointedPath(String FromPath,String AimPath){
		// �����ļ� �ļ�ʼ���أ��ļ�Ŀ�ĵ�
		FileInputStream fls=null;
		FileOutputStream fos=null;
		String infomation="��Ǹ,����ʧ��!!!";
		long s = System.currentTimeMillis();
		File file = new File(FromPath);
		File fileOut = new File(AimPath);
		try {
			fls = new FileInputStream(file);
			fos = new FileOutputStream(fileOut);
			byte[] buffer = new byte[1024 * 10];
			int b = fls.read(buffer);
			while (b != -1) {
				fos.write(buffer, 0, b);
				b = fls.read(buffer);
			}
			long endtime = System.currentTimeMillis();
			infomation="�������! ��ʱ:" + (endtime - s) / 1000 + "��";
			return infomation;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			infomation=e.toString();
			return infomation;
		}
		finally{
			try {
				fls.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				infomation=e.toString();
				return infomation;
			}

		}
	}
	
	public TreeMap<String,String> getQueryTime(String QueryFilePath){
		//�Ӳ�ѯ��text�ļ�����ȡ�� ��ѯ��ʱ���
		TreeMap<String,String> querytweettime=new TreeMap<String,String>();
		String[] sq=new String[2];
		Scanner s =null;
		try {
			s= new Scanner(new FileReader(QueryFilePath));
			while(s.hasNext()){
				sq=s.nextLine().split(" ");
				querytweettime.put(sq[0], sq[1]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.close();
		return querytweettime;
			
	}
}
