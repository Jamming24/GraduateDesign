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
		System.out.println("����������ɣ��ܼ���ʱ"+(end-start)/1000+"��");
		

	}
	public  void getParatemter(long InitTime,String QueryFilePath,String InDataPath,String OutDataPath,String TimetablePath,long TimeInterval){
		this.InDataPath=InDataPath;
		this.OutDataPath=OutDataPath;
		this.QuerlTime=ProcessDataTools.getInstance().getQueryTime(QueryFilePath);
		this.Idlist=TimeStampAndIdMaping(TimetablePath, InitTime, TimeInterval);
		System.out.println("ʱ����������");
		
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
					System.out.println("��ѯ "+key+" �з���ɣ���ʱ"+(end-start)/1000+"��");
				}
			});
		}
		ftp.shutdown();
	}
	
	public void SimpleSegmentationDate(ArrayList<Long> IdMap,String finalQueryId, String InDataPath,String OutDataPath){
		//����ʱ�������ֵĶ���΢��ID���б����з��ļ������һ��ID������·�������·��
		CompareTime fd=new CompareTime();
		int part=0;
		long fqi=Long.parseLong(finalQueryId);
		for(int index=0;index<IdMap.size()-1;index++){
			try {
				if(fqi>=IdMap.get(index)){
					//���з��ļ���IdҪ�ں����Id��Χ��
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
	
	class CompareTime {
		private  InputStreamReader input=null;
		private  BufferedReader br=null;
		private  OutputStreamWriter output=null;
		private  String[] datablock=new String[4];
		private  int i=0;
		public  void findDatasimple(long StartTime ,long EndTime,String file,String dataxml) throws IOException{
			//�����β�ѯʱ�䣬����ļ���·����Դ�����ļ�·��
			try {
				int count=1;
				String s="";
				input =new InputStreamReader(new FileInputStream(dataxml), "UTF-8");
				br=new BufferedReader(input);
				output=new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
				while((s=br.readLine())!=null){
					//������  ��ν��ļ������ݶ�ȡΪ���ݿ�
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
							//�ж����ݿ��Ƿ���ϲ�ѯ
							//����Ϊ���ݿ����飬����ֵΪ��������
							for(int j=0;j<4;j++){
								//����Ϊtrue,�����ݿ�д���ļ�
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
			//�ж����ݿ������ϲ�ѯҪ�����Ҫ�󷵻�true��
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
