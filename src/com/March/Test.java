package com.March;

import java.util.ArrayList;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Integer> list=new ArrayList<Integer>(); 
		list.add(93);
		list.add(102);
		list.add(79);
		list.add(60);
		list.add(38);
		list.add(43);
		list.add(62);
		list.add(60);
		list.add(113);
		list.add(130);
		list.add(118);
		list.add(116);
		list.add(96);
		String OutFile="D:\\test.txt";
		StatisticalWordFrequence swf=new StatisticalWordFrequence();
//		swf.ZeroFillModel(list);
		swf.OutTrainText(OutFile, list, list, list, list);
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
		}
		System.out.println(list.size());

	}
	
	

}
