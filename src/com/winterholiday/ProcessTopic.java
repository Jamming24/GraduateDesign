package com.winterholiday;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessTopic {

	/**
	 * @param args
	 * @throws IOException 
	 * 
	 *����Topic�ļ� ���Ա����ѯʱ��  ������ ParseQuery.exe ���õĸ�ʽ
	 */
	private static int j=0;
	private static String[] temp=new String[6];
	public static void main(String[] args){
		// TODO Auto-generated method stub
		String inTopicPath="//home//gaojiaming//ʵ������//query//new_2012.topics.MB51-110.txt";
		String outTopicPath="//home//gaojiaming//ʵ������//query//trec-2012.topics.MB1-50.txt";
		String QueryTweetTimePath="//home//gaojiaming//ʵ������//twData//2012_querytweettime.txt";
		try {
			new ProcessTopic(inTopicPath, outTopicPath,QueryTweetTimePath);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public ProcessTopic(String inTopicPath,String outTopicPath,String QueryTweetTimePath) throws IOException, FileNotFoundException {
		// TODO Auto-generated constructor stub
		//��2011.topics.MB1-50.txt�ļ��г�С��
		String s="";
		int count=1;
		
		File topicpath =new File(inTopicPath);
		File saveTopicpath=new File(outTopicPath);
		InputStreamReader isr=new InputStreamReader(new FileInputStream(topicpath),"GB2312");
		OutputStreamWriter osw=new OutputStreamWriter(new FileOutputStream(saveTopicpath),"GB2312");
		BufferedReader br=new BufferedReader(isr);
		while((s=br.readLine())!=null){
			if(count%7!=0){
				temp[j++]=s;
			}
			else{
				temp[5]=s;
				j=0;
				String [] sTopic=saveTopic(temp,QueryTweetTimePath);//����saveTopic ����  ���������Ҫ�ĸ�ʽ
				for(int i=0;i<4;i++){
					osw.write(sTopic[i]+"\r\n");
				}
			}
			count++;
		}
		
		br.close();
		isr.close();
		osw.close();
	}

	public static int count=1;
	public String[] saveTopic(String[] topic,String QueryTweetTimePath) throws IOException{
		//���з��Ժ��С�鴦���trec-2011.topics.MB1-50.txt����Ҫ�ĸ�ʽ
//		System.out.println(">>"+count++);
		String[] sTopic=new String[4];
		Pattern p1=Pattern.compile("<num> Number: (.*?) </num>");
		Pattern p2=Pattern.compile("<title> (.*?) </title>");
		Matcher m=p1.matcher(topic[1]);
		Matcher m1=p2.matcher(topic[2]);
		String no=null;
		while(m.find()&&m1.find()){
			no=m.group(1);
			sTopic[0]="<DOC>";
			sTopic[1]="<DOCNO> "+no+" </DOCNO>";
			sTopic[2]="<TEXT> "+m1.group(1)+" </TEXT>";
			sTopic[3]="</DOC>";
		}
		querytime(no,topic[4],QueryTweetTimePath);

		return sTopic;
	}
	
	private void querytime(String QueryNum,String s,String QueryTweetTimePath ) throws IOException{
		//���س����Ͳ�ѯʱ��
		FileWriter fw=new FileWriter(QueryTweetTimePath,true);
		Long t = null;
		Pattern time50=Pattern.compile("<querytweettime> (.*?) </querytweettime>");
		Matcher m=time50.matcher(s);
		while(m.find()){
			t=new Long(m.group(1));
			fw.write(QueryNum+" "+m.group(1)+"\r\n");
		}
		fw.close();
		System.out.println(QueryNum+":"+t);
		
	}

}
