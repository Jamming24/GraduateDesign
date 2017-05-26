package com.winterholiday;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FilterAnswer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FilterAnswer fa=new FilterAnswer();
		String AnswerPath="//home//gaojiaming//实验数据//trec_eval//microblog11-Highqrels.txt";
		String OutFilePath="//home//gaojiaming//实验数据//trec_eval//new_microblog11-Highqrels.txt";
		fa.CalculatorAnswerNum(AnswerPath,OutFilePath);
		System.out.println("处理完成！！！");

	}
	
	private void CalculatorAnswerNum(String AnswerPath,String OutFilePath){
		int AnswerNum=50;
		ArrayList<String> TempAnswerList=new ArrayList<String>();
		String[] temp=new String[4]; 
		int signLast=1;//这里很重要  一定要是第一行的 行号
		int signNext;
		File a=new File(AnswerPath);
		try {
			Scanner sc=new Scanner(a);
			while(sc.hasNext()){
				String answer=sc.nextLine();
				temp=answer.split(" ");
				signNext=Integer.parseInt(temp[0]);
				if(signNext==signLast){
					TempAnswerList.add(answer);
				}else{
					if(TempAnswerList.size()>=AnswerNum){
						//写入文件
						WriterNewAnswerFile(OutFilePath, TempAnswerList);
					}else{
						System.out.println("去掉查询编号："+signLast+"   答案数量："+TempAnswerList.size());
					}
					TempAnswerList.clear();
					signLast=signNext;
					TempAnswerList.add(answer);
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void WriterNewAnswerFile(String OutFilePath,ArrayList<String> list){
		try {
			FileWriter fw=new FileWriter(OutFilePath,true);
			for(String i:list){
				fw.write(i+"\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
