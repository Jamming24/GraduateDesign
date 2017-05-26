package com.March;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lemurproject.lemur.Index;
import lemurproject.lemur.IndexManager;

public class StatisticalWordFrequence {

	/**
	 * @param args
	 */
	public static Index index = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String QueryLastSegmentationDataIndex = "D:\\GraduationDesign\\QueryLastSegmentationDataIndex";
		String IncludeAllSegmentationDataIndex = "D:\\GraduationDesign\\IncludeAllSegmentationDataIndex";
		String PassTopicPath = "D:\\GraduationDesign\\query\\All_passedTopic.txt";
		String SegmentationDataFloder = "D:\\GraduationDesign\\SegmentationData";
		String testRelevantAnswerPath="D:\\GraduationDesign\\test.txt";
		String trainRelevantAnswerPath="D:\\GraduationDesign\\train.txt";
//		String testOutFile="D:\\test.txt";
//		String trainOutFile="D:\\train.txt";
		String testOutFile="D:\\test_mt3.txt";
		String trainOutFile="D:\\train_mt3.txt";
		StatisticalWordFrequence swf = new StatisticalWordFrequence();
		ArrayList<String> PassedTopic = swf.LoadPassedTopic(PassTopicPath);
		TreeMap<String , ArrayList<ArrayList<Integer>>> AllQueryWordFrequenceMap=null;
		AllQueryWordFrequenceMap=swf.AllQueryWordFrequence(SegmentationDataFloder,IncludeAllSegmentationDataIndex,QueryLastSegmentationDataIndex, PassedTopic);
		System.out.println("AllQueryWordFrequenceMap�������");
		swf.GetQueryWordFrequenceText(testOutFile, testRelevantAnswerPath, AllQueryWordFrequenceMap);
		swf.GetQueryWordFrequenceText(trainOutFile, trainRelevantAnswerPath, AllQueryWordFrequenceMap);
		
	}
	

	public void GetQueryWordFrequenceText(String OutFile,String RelevantAnswerPath,TreeMap<String , ArrayList<ArrayList<Integer>>> AllQueryWordFrequenceMap) {
		ArrayList<Integer> RelevantAnswerList = new ArrayList<Integer>();

		String query = null;
		boolean flag = true;
		try {
			// int InitNum=0;
			// int trainNum=0;
			int AnswerNum = 0;
			String line = null;
			Scanner sc = new Scanner(new FileReader(RelevantAnswerPath));
			while (sc.hasNext()) {
				line = sc.nextLine();
				if (line.length() < 12) {
					if (flag == true) {
						query = line.substring(3, 8);
						flag = false;
					} else if (line.equals("EOF")) {
						System.out.println(query + "����:"
								+ RelevantAnswerList.size());
						OutTrainText(OutFile, RelevantAnswerList, AllQueryWordFrequenceMap.get(query).get(0), AllQueryWordFrequenceMap.get(query).get(1), AllQueryWordFrequenceMap.get(query).get(2));
						RelevantAnswerList.clear();
					} else {
						System.out.println(query + "����:"
								+ RelevantAnswerList.size());
						OutTrainText(OutFile, RelevantAnswerList, AllQueryWordFrequenceMap.get(query).get(0), AllQueryWordFrequenceMap.get(query).get(1), AllQueryWordFrequenceMap.get(query).get(2));
						RelevantAnswerList.clear();
						query = line.substring(3, 8);
					}

				} else {
					AnswerNum = Integer.parseInt(line.split("��")[1]);
					RelevantAnswerList.add(AnswerNum);
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void OutTrainText(String OutFile,ArrayList<Integer> data,ArrayList<Integer> MaxWordFrequenceList,ArrayList<Integer> MeanWordFrequenceList, ArrayList<Integer> MinWordFrequenceList){
		try {
			ZeroFillModel(MaxWordFrequenceList);
			ZeroFillModel(MeanWordFrequenceList);
			ZeroFillModel(MinWordFrequenceList);
			
			PrintWriter pw = new PrintWriter(new FileWriter(OutFile,true));
			int index=9;
			int local=0;
			for(int x=1;x<=data.size()-10;x++){
				int num=1;
				pw.print(data.get(index+1)+" ");
				for(int i=local;i<=index;i++){
//					pw.print(num+":"+data.get(i)+" ");
					pw.print(data.get(i)+" ");
					num++;
				}
//				for(int i=local;i<=index;i++ ){
////					pw.print(num+":"+MaxWordFrequenceList.get(i)+" ");
//					pw.print(MaxWordFrequenceList.get(i)+" ");
//					num++;
//				}
//				
//				for(int i=local;i<=index;i++){
////					pw.print(num+":"+MeanWordFrequenceList.get(i)+" ");
//					pw.print(MeanWordFrequenceList.get(i)+" ");
//					num++;
//					
//				}
//				
//				for(int i=local;i<=index;i++){
////					pw.print(num+":"+MinWordFrequenceList.get(i)+" ");
//					pw.print(MinWordFrequenceList.get(i)+" ");
//					num++;
//				}
				
				pw.println();
				index++;
				local++;
			}
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private TreeMap<String , ArrayList<ArrayList<Integer>>> AllQueryWordFrequence(String SegmentationDataFloder,String IncludeAllSegmentationDataIndex,String QueryLastSegmentationDataIndex, ArrayList<String> PassedTopic) {
		// ����:����ʱ��ε�����·�������һ��ʱ��ε�����·�������в�ѯ�ļ���
		TreeMap<String , ArrayList<ArrayList<Integer>>> AllQueryWordFrequenceMap=new TreeMap<String, ArrayList<ArrayList<Integer>>>();
		TreeMap<Integer, String> IncludeAllSegmentationDataMap = new TreeMap<Integer, String>();
		HashMap<String, String> QueryLastSegmentationDataIndexMap = new HashMap<String, String>();
		HashMap<String, String[]> QuerynameMappingNum=new HashMap<String, String[]>();
		String[] IndexPath = null;
		
		File IncludeAllSegmentationDataFloder = new File(
				IncludeAllSegmentationDataIndex);
		File[] IncludeAllSegmentationDataFiles = IncludeAllSegmentationDataFloder
				.listFiles();
		String IndexNum = null;
		for (File all : IncludeAllSegmentationDataFiles) {
			if (all.getName().length() >= 12 && all.getName().length() < 15) {
				IndexNum = all.getName().substring(11, all.getName().length());
				IncludeAllSegmentationDataMap.put(Integer.parseInt(IndexNum),
						all.getAbsoluteFile().toString());
				// System.out.println(Integer.parseInt(IndexNum)+">>   >>"+all.getAbsoluteFile().toString());
			}
		}
		System.out.println("IncludeAllSegmentationDataMap����:"
				+ IncludeAllSegmentationDataMap.size());

		File QueryLastSegmentationDataIndexFloder = new File(
				QueryLastSegmentationDataIndex);
		File[] QueryLastSegmentationDataIndexFiles = QueryLastSegmentationDataIndexFloder
				.listFiles();
		String queryName = null;
		for (File qf : QueryLastSegmentationDataIndexFiles) {
			if (qf.getName().length() == 10) {
				queryName = qf.getName().substring(0, 5);
				QueryLastSegmentationDataIndexMap.put(queryName,
						qf.getAbsolutePath());
				// System.out.println(queryName+"     "+qf.getAbsolutePath());
			}
		}
		System.out.println(QueryLastSegmentationDataIndexMap.size());

		
		File[] SegmentationDataFiles = new File(SegmentationDataFloder).listFiles();
		for (File QueryNameFloder : SegmentationDataFiles) {
			File[] QueryFiles = new File(QueryNameFloder.getAbsolutePath()).listFiles();
//			System.out.println(QueryFiles.length);
			IndexPath = CreateIndexPath(QueryFiles.length,IncludeAllSegmentationDataMap,QueryLastSegmentationDataIndexMap,QueryNameFloder.getName());
//			System.out.println(QueryNameFloder.getName());
			QuerynameMappingNum.put(QueryNameFloder.getName(), IndexPath);

		}
		
		for (String i : PassedTopic) {
			AllQueryWordFrequenceMap.put(i.split(">")[0], SimpleQueryWordFrequence(QuerynameMappingNum.get(i.split(">")[0]), i.split(" ")));
			System.out.println(i + ">>>>>���");
		}
		
		return AllQueryWordFrequenceMap;

	}

	private String[] CreateIndexPath(int num, TreeMap<Integer, String> IncludeAllSegmentationDataMap, HashMap<String, String> QueryLastSegmentationDataIndexMap, String QueryName) {
		String[] IndexPath = new String[num];
		for (int i = 0; i < num-1 ; i++) {
			IndexPath[i] = IncludeAllSegmentationDataMap.get(i);
		}
		IndexPath[num-1] = QueryLastSegmentationDataIndexMap.get(QueryName);
		return IndexPath;

	}

	private ArrayList<ArrayList<Integer>> SimpleQueryWordFrequence(String[] IndexPath, String[] Querys) {
		/*
		 * ���������ض��ڵ�����ѯ�ڲ�ͬʱ��εĴ�Ƶ�� ���صĽ����������ArrayList�� �ֱ���� ������ѯ��ͬʱ��ε����Ƶ�ʣ���СƵ��
		 * ��ƽ��Ƶ�� ��Ӧ��ArrayListΪ��MaxWordFrequenceList��MinWordFrequenceList��
		 * MeanWordFrequenceList
		 */
		ArrayList<ArrayList<Integer>> WordFrequenceList = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> MaxWordFrequenceList = new ArrayList<Integer>();
		ArrayList<Integer> MeanWordFrequenceList = new ArrayList<Integer>();
		ArrayList<Integer> MinWordFrequenceList = new ArrayList<Integer>();
		ArrayList<Integer> Value = new ArrayList<Integer>();
		int num = 0;
		int Maxnum = 0;
		int Meanum = 0;
		int Minum = 0;
		for (String i : IndexPath) {
			for (int qi = 1; qi < Querys.length; qi++) {// �����±��1��ʼ ��Ϊ����λ�ǲ�ѯ���
														// ��MB027> reduc energi
														// consumpt
				num = StatisticalWord(i, Querys[qi]);
				Value.add(num);
			}

			Maxnum = GetLimitValue(0, Value);// �õ����ֵ
			MaxWordFrequenceList.add(Maxnum);
			Minum = GetLimitValue(1, Value);// �õ���Сֵ
			MinWordFrequenceList.add(Minum);
			Meanum = GetLimitValue(2, Value);// �õ�ƽ��ֵ
			MeanWordFrequenceList.add(Meanum);
			Value.clear();

		}
		WordFrequenceList.add(MaxWordFrequenceList);
		WordFrequenceList.add(MeanWordFrequenceList);
		WordFrequenceList.add(MinWordFrequenceList);
		return WordFrequenceList;
	}

	private ArrayList<String> LoadPassedTopic(String PassTopicPath) {
		String Allcontent = null;
		ArrayList<String> QueryList = new ArrayList<String>();
		try {
			Scanner sc = new Scanner(new FileReader(PassTopicPath));
			while (sc.hasNext()) {
				Allcontent = Allcontent + sc.nextLine() + " ";
			}
			Pattern pattern = Pattern.compile("<DOC (.*?)</DOC>");
			Matcher matcher = pattern.matcher(Allcontent);
			while (matcher.find()) {
				QueryList.add(matcher.group(1));
				// System.out.println(matcher.group(1));
			}
			System.out.println("��ѯ����:" + QueryList.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return QueryList;
	}

	private int StatisticalWord(String IndexPath, String word) {
		int frequence = 0;
		try {
			index = IndexManager.openIndex(IndexPath);
			// System.out.println("�ĵ�����:" + index.docCount());
			// System.out.println("�ʵ�ID:" + index.term(word));
			// System.out.println("��Ƶ:" + index.termCount(index.term("staff"))
			// + "  �����Ǵʵ�ID");
			int id = index.term(word);
			frequence = index.termCount(id);
			index.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return frequence;

	}

	private int GetLimitValue(int flag, ArrayList<Integer> Value) {
		/*
		 * �������� ��ֵ������flag�����������С,ƽ������,ȡֵ��ΧΪ0,1,2�� ������ֵ������ �˺������Ի�ȡ���ֵ ƽ��ֵ ����Сֵ��
		 * �������ʾ���ֵ
		 */
		Collections.sort(Value);
		if (flag == 0) {
			// �������ֵ
			// System.out.println(Value.get(Value.size()-1));
			return Value.get(Value.size() - 1);
		} else if (flag == 1) {
			// ������Сֵ
			// System.out.println(Value.get(0));
			return Value.get(0);
		} else if (flag == 2) {
			// ����ƽ��ֵ
			double mean = 0;
			for (Integer t : Value) {
				mean += t;
			}
			return (int) (mean / Value.size());
		}

		return -1;

	}
	
	public void ZeroFillModel(ArrayList<Integer> list){
		/*
		 * �˺������� ����ArrayList��չΪ����204��С����ȱλ�ò��㡣
		 */
		for(int i=0;i<204;i++){
			if(i>list.size()-1){
				list.add(0);
			}
		}
		
	}

}
