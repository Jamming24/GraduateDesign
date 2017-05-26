package com.March;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Counter {
	public static void main(String[] args) {
		String testfile="D:\\GraduationDesign\\test_ConverToValue.txt";
		String OutFile="D:\\GraduationDesign\\test_ConverToValueNoZero.txt";
		
		String predictfile="D:\\test\\svm_Part.predict";
		Counter test=new Counter();
		test.DeleteAllZero(testfile,OutFile);
//		test.Count(OutFile);
//		test.CalculateAccuracy(testfile, predictfile);
	}
	
	public void DeleteAllZero(String file,String OutFile){
		try {
			String line=null;
			int zNum=0;
			char sign;
			Scanner sc=new Scanner(new FileReader(file));
			PrintWriter pw=new PrintWriter(OutFile);
			while(sc.hasNext()){
				line=sc.nextLine();
				sign=line.charAt(0);
				if(sign!='0'){
					zNum++;
					pw.println(line);
				}
			}
			System.out.println("!0的数量："+zNum);
			sc.close();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void Count(String file){
		try {
		String line=null;
		int zNum=0;
		char sign;
		Scanner sc=new Scanner(new FileReader(file));
		while(sc.hasNext()){
			line=sc.nextLine();
			sign=line.charAt(0);
			if(sign=='1'){
				zNum++;
			}
		}
		System.out.println("0的数量："+zNum);
		sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void CalculateAccuracy(String testfile,String predictfile){
		try {
			String line=null;
			int index=0;
			int count=0;
			final double allNum=3504;
			char sign;
			ArrayList<Character> TestList=new ArrayList<Character>();
			Scanner scTest=new Scanner(new FileReader(testfile));
			Scanner scPredict=new Scanner(new FileReader(predictfile));
			while(scTest.hasNext()){
				line=scTest.nextLine();
				TestList.add(line.charAt(0));
			}
			while(scPredict.hasNext()){
				line=scPredict.nextLine();
				sign=line.charAt(0);
				if(sign==TestList.get(index)){
					count++;
				}
				index++;
			}
			
			System.out.println("正确数量："+count+"  正确率："+count/allNum);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
