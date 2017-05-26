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
	 * 通过finddata类找到符合查询时间的微博，并且写入到以查询时间命名的文件中
	 * 文件数目应该以答案数目为基准，而不是查询数目
	 */
	private static InputStreamReader input=null;
	private static BufferedReader br=null;
	private static OutputStreamWriter output=null;
	private static String[] datablock=new String[4];
	private static TreeMap<String,String> querltime= new TreeMap<String,String>();
	private static int i=0;
	
	
	public static void main(String[] args) throws IOException {
		//微博实验数据文件的路径
		String dataxml="//home//gaojiaming//实验数据//xml_ian_total.xml";
		//查询时间
		String QueryTimePath="//home//gaojiaming//实验数据//twData//total_querytweettime.txt";
		//切分后的实验数据存放文件夹
		String twDataPath="//home//gaojiaming//实验数据//twData";
		FindData f=new FindData();
		f.SegmentationData(QueryTimePath, twDataPath,dataxml);
		
	}

	public void SegmentationData(String QueryTimePath,String twDataPath,String dataxml) throws IOException {
		//查询时间文件路径，要保存的文件夹，源数据路径
		querltime=ProcessDataTools.getInstance().getQueryTime(QueryTimePath);
		Set<String> keys=querltime.keySet();
		for(String key:keys){
			long start=System.currentTimeMillis();
			long topicnum=new Long(querltime.get(key));
			new File(twDataPath+"//"+key).mkdir();
			String file=twDataPath+"//"+key+"//"+querltime.get(key)+".xml";
			findDatasimple(topicnum,file,dataxml);
			long end=System.currentTimeMillis();
			System.out.println(querltime.get(key)+"号文件查找完成！用时"+(end-start)/1000+"秒");
		}
	}


	public  void findDatasimple(Long qurel,String file,String dataxml) throws IOException{
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
//						datablock[2]=isORG(s);
//						System.out.println("微博"+s);
					}
					i++;
					count++;
				}
				else{
					datablock[3]=s;
					i=0;
					if(isData(datablock,qurel.longValue())){
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

	public boolean isData(String[] tempdata,long topicnum){
		//		System.out.println(topicnum);
		//判断数据块书否符合查询要求，如何要求返回true。
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