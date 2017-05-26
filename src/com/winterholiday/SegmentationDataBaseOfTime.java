package com.winterholiday;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SegmentationDataBaseOfTime {
	
	private String InDataPath;
	private String OutDataPath;
	private TreeMap<String,String> QuerlTime;
	private ArrayList<Long> Idlist;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start=System.currentTimeMillis();
		String QueryFilePath="D:\\GraduationDesign\\twData\\total_querytweettime.txt";
		String InDataPath="D:\\GraduationDesign\\twData";
		String OutDataPath="D:\\GraduationDesign\\SegmentationData";
		String TimetablePath="D:\\GraduationDesign\\microblog_time.txt";
		long InitTime=1295712000000L;
		long TimeInterval=2*60*60*1000;
		SegmentationDataBaseOfTime sdbt=new SegmentationDataBaseOfTime();
		sdbt.getParatemter(InitTime,QueryFilePath, InDataPath, OutDataPath, TimetablePath, TimeInterval);
		sdbt.SegmentationDate(6);
		long end=System.currentTimeMillis();
		System.out.println("程序运行完成！总计用时"+(end-start)/1000+"秒");
		

	}
	public  void getParatemter(long InitTime,String QueryFilePath,String InDataPath,String OutDataPath,String TimetablePath,long TimeInterval){
		this.InDataPath=InDataPath;
		this.OutDataPath=OutDataPath;
		this.QuerlTime=ProcessDataTools.getInstance().getQueryTime(QueryFilePath);
		this.Idlist=TimeStampAndIdMaping(TimetablePath, InitTime, TimeInterval);
		System.out.println("时间表载入完成");
		
	}
	
	public void SegmentationDate(int MaxThreadNum){
		ExecutorService ftp= Executors.newFixedThreadPool(MaxThreadNum);
		Set<String> keys=QuerlTime.keySet();
		for(final String key:keys){
			String SimpleOutDataPath=OutDataPath+"//"+key;
			new File(SimpleOutDataPath).mkdir();
			ftp.execute(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					long start=System.currentTimeMillis();
					SimpleSegmentationDate(Idlist,QuerlTime.get(key), InDataPath+"//"+key+"//"+QuerlTime.get(key)+".xml", OutDataPath+"//"+key);
					long end=System.currentTimeMillis();
					System.out.println("查询 "+key+" 切分完成！用时"+(end-start)/1000+"秒");
				}
			});
		}
		ftp.shutdown();
	}
	
	public void SimpleSegmentationDate(ArrayList<Long> IdMap,String finalQueryId, String InDataPath,String OutDataPath){
		//按照时间间隔划分的对于微博ID的列表，待切分文件的最后一个ID，输入路径，输出路径
		CompareTime fd=new CompareTime();
		int part=0;
		long fqi=Long.parseLong(finalQueryId);
		for(int index=0;index<IdMap.size()-1;index++){
			try {
				if(fqi>=IdMap.get(index)){
					//待切分文件的Id要在合理的Id范围内
					fd.findDatasimple(IdMap.get(index), IdMap.get(index+1),OutDataPath+"//Part"+part, InDataPath);
					part++;
				}else{
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
		
	}
	
	
	public ArrayList<Long> TimeStampAndIdMaping(String TimetablePath,long InitTime, long TimeInterval){
		//生成时间戳和微博ID的映射表
		//时间表的文件，时间间隔
//		String TimetablePath="//home//gaojiaming//实验数据//microblog_time.txt";
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
		//将日期转换为时间戳
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
//		res=String.valueOf(ts);将长整形数转换成字符串
	}
	
	private String StampToDate(String s){
		String res;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		long it=new Long(s);
		Date d=new Date(it);
		res=sdf.format(d);
		return res;
	}
	
	class CompareTime {
		private  InputStreamReader input=null;
		private  BufferedReader br=null;
		private  OutputStreamWriter output=null;
		private  String[] datablock=new String[4];
		private  int i=0;
		public  void findDatasimple(long StartTime ,long EndTime,String file,String dataxml) throws IOException{
			//长整形查询时间，输出文件的路径，源数据文件路径
			try {
				int count=1;
				String s="";
				input =new InputStreamReader(new FileInputStream(dataxml), "UTF-8");
				br=new BufferedReader(input);
				output=new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
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
						if(isData(datablock[1],StartTime,EndTime)){
							//判断数据块是否符合查询
							//参数为数据块数组，返回值为布尔类型
							for(int j=0;j<4;j++){
								//若果为true,将数据块写入文件
								output.write(datablock[j]+"\n");
							}
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
				output.close();

			}

		}

		public boolean isData(String temps,long StartTime,long EndTime){
			//		System.out.println(topicnum);
			//判断数据块书否符合查询要求，如何要求返回true。
			Pattern p=Pattern.compile("<DOCNO>(.*?)</DOCNO>");
			Matcher m=p.matcher(temps);
			if(m.find()){
				long num=Long.parseLong(m.group(1));
				if(num>=StartTime&&num<EndTime)
					return true;
			}
			return false;
		}
	}


}
