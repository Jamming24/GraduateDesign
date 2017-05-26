package com.winterholiday;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class GetRelevantMisblogNumUpdata {  
	public static PrintWriter pw2=null;
    public static void main(String[] args) {  
    	String AnswerPath="D:\\GraduationDesign\\24hour\\new_microblog-Highqrels .txt";
    	String querytweettime="D:\\GraduationDesign\\24hour\\total_querytweettime.txt";
    	String OutFile="D:\\GraduationDesign\\24hour\\24hourresult.txt";
		String TimetablePath="D:\\GraduationDesign\\microblog_time.txt";
		long InitTime=1295712000000L;
		long TimeInterval=24*60*60*1000;//����ע��Ҫ��
    	TreeMap<String,String> QuerlTime=ProcessDataTools.getInstance().getQueryTime(querytweettime);
    	Set<String> keys=QuerlTime.keySet();
    	GetRelevantMisblogNumUpdata t=new GetRelevantMisblogNumUpdata();
    	ArrayList<Long> IdList;
    	IdList=t.TimeStampAndIdMaping(TimetablePath, InitTime, TimeInterval);
    	
    	
    	
    	try {
    	pw2=new PrintWriter(new FileWriter(OutFile));
    		for(String i:keys){
    			System.out.println("��ѯ��"+i+" ���㿪ʼ");
    			ArrayList<String> answerlist=t.LoadAnswer(Integer.parseInt(i.substring(2, i.length())), AnswerPath);
    			t.ToCompareTimeTable(i,IdList, answerlist,OutFile);
    		}
    		} catch (IOException e) {
    		// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	pw2.close();
    } 
    
    private void ToCompareTimeTable(String i,ArrayList<Long> IdList,ArrayList<String> answerlist,String OutFile) throws IOException{
    	//��ѯ��ţ�ʱ���·�����𰸱�·����������·��
    	String result="����������";
    	pw2.println("��ѯ��"+i+" ��ʼ");
    	for(int index=0;index<IdList.size()-1;index++){
    		int Count=0;
    		for(String s:answerlist){
    			long key=Long.parseLong(s);
    			if(key>=IdList.get(index)&&key<=IdList.get(index+1)){
    				Count++;
    			}
    		}
    		result="Part"+index+"������������"+Count;
    		pw2.println(result);
    		System.out.println(result);
    	}
    }
    
    
	public ArrayList<Long> TimeStampAndIdMaping(String TimetablePath,long InitTime, long TimeInterval){
		//����ʱ�����΢��ID��ӳ���
		//ʱ�����ļ���ʱ����
//		String TimetablePath="//home//gaojiaming//ʵ������//microblog_time.txt";
		long currentTime=0L;
		long currentId=0L;
		int consult=0;
		ArrayList<Long> IdList=new ArrayList<Long>();
		try {
		Scanner sc=new Scanner(new FileReader(TimetablePath));
		while(sc.hasNext()){
			String line=sc.nextLine();
			currentTime=DateToStamp(line.split(",")[3]);
			currentId=Long.parseLong(line.split(",")[0]);
			if((currentTime-InitTime)%TimeInterval==0){
				if((currentTime-InitTime)/TimeInterval==consult){
					IdList.add(currentId);
					System.out.println(StampToDate(currentTime+"")+"   ID:"+currentId);
					consult++;
				}else{
					continue;
				}
			}
		}
		sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return IdList;
	}
	
	private long DateToStamp(String time) {
		//������ת��Ϊʱ���
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date;
		long ts=0;
		try {
			date = sdf.parse(time);
			ts=date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ts;
//		String res;
//		res=String.valueOf(ts);����������ת�����ַ���
	}
	
	
	private String StampToDate(String s){
		String res;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		long it=new Long(s);
		Date d=new Date(it);
		res=sdf.format(d);
		return res;
	}
	
	
    private ArrayList<String> LoadAnswer(int QueryNo,String AnswerPath){
		Scanner sc=null;
		String line=null;
		ArrayList<String> answerlist=new ArrayList<String>();
		try {
			sc = new Scanner(new FileReader(AnswerPath));
			while(sc.hasNext()){
				line=sc.nextLine();
				if(line.split(" ")[0].equals(QueryNo+"")){
					answerlist.add(line.split(" ")[2]);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.close();
		return answerlist;
	}

}  
	

