package com.winterholiday;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetRelevantMisblogNum {
	private static InputStreamReader input=null;
	private static BufferedReader br=null;
	private static String[] datablock=new String[4];
	private static int i=0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetRelevantMisblogNum g=new GetRelevantMisblogNum();
		int x=0;
		String AnswerPath="/home/gaojiaming/实验数据/trec_eval/new_microblog-Highqrels .txt";
		String querytweettime="/home/gaojiaming/实验数据/total_querytweettime.txt";
		String SegmentationDatePath="/home/gaojiaming/实验数据/SegmentationDate";
		String OutFile="/home/gaojiaming/实验数据/result.txt";
		TreeMap<String,String> QuerlTime=ProcessDataTools.getInstance().getQueryTime(querytweettime);
		try {
			PrintWriter pw=new PrintWriter(new FileWriter(OutFile));
			Set<String> keys=QuerlTime.keySet();
			for(String i:keys){
				ArrayList<String> Answerlist=g.LoadAnswer(Integer.parseInt(i.substring(2, i.length())), AnswerPath);
					for(int index=0;index<204;index++){
						x=g.CountNum(SegmentationDatePath+"/Part"+index, Answerlist);
						System.out.println("查询："+i+"  文件：Part"+index+"  数量："+x);
						pw.println("查询："+i+"  文件：Part"+index+"  数量："+x);
						pw.flush();
					}
			}
			
			pw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	
	public  int CountNum(String dataxml,ArrayList<String> keyIdList) throws IOException{
		//长整形查询时间，输出文件的路径，源数据文件路径
		int CountRelevant=0;
		long num=0;
		Pattern p=Pattern.compile("<DOCNO>(.*?)</DOCNO>");
		for(String keyId:keyIdList){
			try {
				int count=1;
				String s="";
				input =new InputStreamReader(new FileInputStream(dataxml), "UTF-8");
				br=new BufferedReader(input);
				while((s=br.readLine())!=null){
					//待处理  如何将文件中数据读取为数据块
					if(count%4!=0){
						datablock[i]=s;
						if(i==2){
							datablock[2]=s;
						}
						i++;
						count++;
					}
					else{
						datablock[3]=s;
						i=0;
						Matcher m=p.matcher(datablock[1]);
						if(m.find()){
							num=Long.parseLong(m.group(1));
						}
						if(num==Long.parseLong(keyId)){
							CountRelevant++;
						}
						count++;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				br.close();
				input.close();
				
			}
			
		}
		
		return CountRelevant;

	}

	
	

}
