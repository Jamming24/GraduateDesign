package com.winterholiday;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

public class WriterSegmentationDataIndexPara {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WriterSegmentationDataIndexPara wsdip = new WriterSegmentationDataIndexPara();
		String StopWordListPath = "D:\\GraduationDesign\\stoplist.dft";
		String AllIndexFilePath = "D:\\GraduationDesign\\IncludeAllSegmentationData";
		String AllIndexPath = "D:\\GraduationDesign\\IncludeAllSegmentationDataIndex";
		// String
		// QueryFilePath="//home//gaojiaming//实验数据//twData//total_querytweettime.txt";
		// String IndexExePath="//home//gaojiaming//实验数据//IndriBuildIndex.exe";
		// wsdip.WriteAllPara(QueryFilePath, AllIndexPath, AllIndexFilePath,
		// StopWordListPath,IndexExePath);

		HashSet<String> StopWordList = wsdip.LoadStopList(StopWordListPath);
		File SegmentationData = new File(AllIndexFilePath);
		File[] files = SegmentationData.listFiles();
		String IndexOutPath = null;
		String IndexPath = null;
		for (File f : files) {
			IndexOutPath = AllIndexPath + "\\para\\" + f.getName() + ".para";
			IndexPath = AllIndexPath + "\\twIndex" + f.getName();
			wsdip.WriteSimpleIndexPara(IndexOutPath, IndexPath,
			f.getAbsolutePath(), StopWordList);

		}
		wsdip.WriterIndexBat(AllIndexPath + "\\RunIndex.bat", files);

	}

	// private void WriteAllPara(String QueryFilePath,String AllIndexPath,String
	// AllIndexFilePath,String StopWordListPath,String IndexExePath){
	// HashSet<String> StopWordList=LoadStopList(StopWordListPath);
	// TreeMap<String,String>
	// QuerlTime=ProcessDateTools.getInstance().getQueryTime(QueryFilePath);
	// Set<String> keys=QuerlTime.keySet();
	// System.out.println(keys.size());
	// for(String key:keys){
	// File SimpleSegmentationDataFolder=new File(AllIndexFilePath+"//"+key);
	// File[] files=SimpleSegmentationDataFolder.listFiles();
	// for(File f:files){
	// String IndexPath=new
	// File(AllIndexPath).getAbsolutePath()+"/"+key+"/twIndex"+f.getName();
	// String IndexOutPath=AllIndexPath+"//"+key+"//para//"+f.getName()+".para";
	// new File(AllIndexPath+"//"+key+"//para").mkdirs();
	// try {
	// WriteSimpleIndexPara(IndexOutPath, IndexPath, f.getAbsolutePath(),
	// StopWordList);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// ProcessDateTools.getInstance().CopyFileToAppointedPath(IndexExePath,
	// AllIndexPath+"//"+key+"//IndriBuildIndex.exe");
	// WriterIndexBat(AllIndexPath+"//"+key+"//"+key+".bat", files);
	// System.out.println(key+"配置文件写入完成！！！");
	// }
	//
	// }

	private void WriterIndexBat(String BatOutPath, File[] ParameterFileName) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(BatOutPath));
			for (File f : ParameterFileName) {
				pw.println("IndriBuildIndex.exe para\\" + f.getName() + ".para");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pw.close();
		System.out.println("Bat文件写入成功");
	}

	public void WriteSimpleIndexPara(String IndexOutPath, String IndexPath,
			String IndexFilePath, HashSet<String> stoplist) {
		// 配置文件输出路径,索引输出路径,需要建立索引文件的路径,停用词表集合
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(IndexOutPath));
			pw.println("<parameters>");
			pw.println("<memory>512M</memory>");
			pw.println("<metadata>");
			pw.println("<forward>docno</forward>");
			pw.println("<backward>docno</backward>");
			pw.println("</metadata>");
			pw.println("<field>");
			pw.println("<name>TEXT</name>");
			pw.println("</field>");
			pw.println("<stemmer>");
			pw.println("<name>porter</name>");
			pw.println("</stemmer>");
			pw.println("<index>" + IndexPath + "</index>");
			pw.println("<corpus>");
			pw.println("<path>" + IndexFilePath + "</path>");
			pw.println("<class>trectext</class>");
			pw.println("</corpus>");
			pw.println("<stopper>");
			for (String stopword : stoplist) {
				pw.println("<word>" + stopword + "</word>");
			}
			pw.println("</stopper>");
			pw.println("</parameters>");
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HashSet<String> LoadStopList(String stoplistpath) {
		HashSet<String> stoplist = new HashSet<String>();
		Scanner s = null;
		try {
			s = new Scanner(new FileReader(stoplistpath));
			while (s.hasNext()) {
				stoplist.add(s.nextLine());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.close();
		return stoplist;
	}

}
