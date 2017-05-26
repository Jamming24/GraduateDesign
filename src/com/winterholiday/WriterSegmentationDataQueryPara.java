package com.winterholiday;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WriterSegmentationDataQueryPara {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WriterSegmentationDataQueryPara wq=new WriterSegmentationDataQueryPara();
		String SegmentationDataFloder="/home/gaojiaming/实验数据/SegmentationDate";
		String IndexFolder="/home/gaojiaming/实验数据/Index";
		String RunQueryFolder="/home/gaojiaming/实验数据/RunQuery";
		String passTopicPath="/home/gaojiaming/实验数据/query/All_passedTopic.txt";
		String BatOutPath="/home/gaojiaming/实验数据/RunQuery/run_lm_drichlet.bat";
		TreeMap<String ,String > QueryContent;
		File SegmentationData=new File(SegmentationDataFloder);
		File[] files=SegmentationData.listFiles();
		wq.WriterRunQueryBatLM(BatOutPath, files);
		try {
			QueryContent=wq.MatchTopic(passTopicPath);
			String IndexPath=null;
			String OutPathString=null;
			for(File f:files){
				IndexPath=IndexFolder+"/twIndex"+f.getName();
				OutPathString=RunQueryFolder+"/para/"+f.getName()+".para";
				new File(RunQueryFolder+"/para").mkdirs();
				wq.writerQueryPara(OutPathString, IndexPath, QueryContent);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private   TreeMap<String ,String > MatchTopic(String passedQueryPath) throws IOException {
		//将文本内容载入到字符串里
		File f = new File(passedQueryPath);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String stemp = "";
		String content = "";
		while ((stemp = br.readLine()) != null) {
			content = content + stemp + " ";
		}
		fr.close();
		
		//通过正则表达式将查询编号和查询内容存入HashMap
		TreeMap<String ,String> QueryContent=new TreeMap<String,String>();
		Pattern pno = Pattern.compile("<DOC (.*?)>");
		Pattern ptext = Pattern.compile(">(.*?)</DOC>");
		Matcher mno = pno.matcher(content);
		Matcher mtext = ptext.matcher(content);
		while (mno.find() && mtext.find()) {
			 QueryContent.put(mno.group(1), mtext.group(1).trim());
		}
		System.out.println(QueryContent.size());
		return QueryContent;
	}

	private void writerQueryPara(String OutPathString, String IndexPath,TreeMap<String,String> QueryContent) throws IOException {
		//配置文件输出路径，索引文件的路径
		BufferedWriter bw = new BufferedWriter(new FileWriter(OutPathString));
		bw.write("<parameters>\n");
		bw.write("<memory>128m</memory>\n");
		bw.write("<index>" + IndexPath + "</index>\n");
		Set<String> keys=QueryContent.keySet();
		for(String key:keys){
			int no=Integer.parseInt(key.substring(2,key.length()));
			bw.write("<query>\n");
			bw.write("<type>indri</type>\n");
			bw.write("<number>" + no + "</number>\n");
			bw.write("<text>" + QueryContent.get(key) + "</text>\n");
			bw.write("</query>\n");
		}
		bw.write("</parameters>\n");

		bw.flush();
		bw.close();
	}
	
	private void WriterRunQueryBatLM(String BatOutPath,File[] ParameterFileName){
		PrintWriter pw=null;
		try {
			pw=new PrintWriter(new FileWriter(BatOutPath));
			for(File f:ParameterFileName){
				pw.println("IndriRunQuery.exe  para\\"+f.getName()+".para  -count=200  -trecFormat=true -runID=LM  -rule=method:dirichlet,mu:1500  -printDocuments=false -printQuery=false  >lm-drichlet-_score\\"+f.getName()+"_lm-drichlet.score");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pw.close();
		System.out.println("Bat文件写入成功");
	}
	


}
