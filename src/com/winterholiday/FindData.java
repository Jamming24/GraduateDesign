package com.winterholiday;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FindData {

	/**
	 * @param args
	 * ͨ��finddata���ҵ����ϲ�ѯʱ���΢��������д�뵽�Բ�ѯʱ���������ļ���
	 * �ļ���ĿӦ���Դ���ĿΪ��׼�������ǲ�ѯ��Ŀ
	 */
	private static InputStreamReader input=null;
	private static BufferedReader br=null;
	private static OutputStreamWriter output=null;
	private static String[] datablock=new String[4];
	private static TreeMap<String,String> querltime= new TreeMap<String,String>();
	private static int i=0;
	
	
	public static void main(String[] args) throws IOException {
		//΢��ʵ�������ļ���·��
		String dataxml="//home//gaojiaming//ʵ������//xml_ian_total.xml";
		//��ѯʱ��
		String QueryTimePath="//home//gaojiaming//ʵ������//twData//total_querytweettime.txt";
		//�зֺ��ʵ�����ݴ���ļ���
		String twDataPath="//home//gaojiaming//ʵ������//twData";
		FindData f=new FindData();
		f.SegmentationData(QueryTimePath, twDataPath,dataxml);
		
	}

	public void SegmentationData(String QueryTimePath,String twDataPath,String dataxml) throws IOException {
		//��ѯʱ���ļ�·����Ҫ������ļ��У�Դ����·��
		querltime=ProcessDataTools.getInstance().getQueryTime(QueryTimePath);
		Set<String> keys=querltime.keySet();
		for(String key:keys){
			long start=System.currentTimeMillis();
			long topicnum=new Long(querltime.get(key));
			new File(twDataPath+"//"+key).mkdir();
			String file=twDataPath+"//"+key+"//"+querltime.get(key)+".xml";
			findDatasimple(topicnum,file,dataxml);
			long end=System.currentTimeMillis();
			System.out.println(querltime.get(key)+"���ļ�������ɣ���ʱ"+(end-start)/1000+"��");
		}
	}


	public  void findDatasimple(Long qurel,String file,String dataxml) throws IOException{
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
//						datablock[2]=isORG(s);
//						System.out.println("΢��"+s);
					}
					i++;
					count++;
				}
				else{
					datablock[3]=s;
					i=0;
					if(isData(datablock,qurel.longValue())){
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

	public boolean isData(String[] tempdata,long topicnum){
		//		System.out.println(topicnum);
		//�ж����ݿ������ϲ�ѯҪ�����Ҫ�󷵻�true��
		Pattern p=Pattern.compile("<DOCNO>(.*?)</DOCNO>");
		Matcher m=p.matcher(tempdata[1]);
		if(m.find()){
			long num=Long.parseLong(m.group(1));
			//			System.out.println(num);
			if(num<=topicnum)
				return true;

		}
		return false;

	}
}