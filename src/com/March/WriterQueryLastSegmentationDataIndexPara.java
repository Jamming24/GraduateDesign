package com.March;

import java.io.File;
import java.util.HashSet;

import com.winterholiday.ProcessDataTools;
import com.winterholiday.WriterSegmentationDataIndexPara;

public class WriterQueryLastSegmentationDataIndexPara {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WriterQueryLastSegmentationDataIndexPara t=new WriterQueryLastSegmentationDataIndexPara();
//		String infloder="D:\\GraduationDesign\\SegmentationData";
//		String OutFloder="D:\\GraduationDesign\\QueryLastSegmentationData";
		String infloder="D:\\GraduationDesign\\QueryLastSegmentationData";
		String OutFloder="D:\\GraduationDesign\\QueryLastSegmentationDataIndex\\para";
		String IndexPathFloder="D:\\GraduationDesign\\QueryLastSegmentationDataIndex";
//		t.copyFile(infloder, OutFloder);
		t.WriterQueryIndexPara(infloder, OutFloder,IndexPathFloder);
		

	}
	
	public void WriterQueryIndexPara(final String Rootfloder,final String OutFloder,final String IndexPathFloder){
		File file=new File(Rootfloder);
		File[] files=file.listFiles();
		String StopWordListPath = "D:\\GraduationDesign\\stoplist.dft";
		WriterSegmentationDataIndexPara wsdip=new WriterSegmentationDataIndexPara();
		HashSet<String> StopWordList=new HashSet<String>();
		StopWordList=wsdip.LoadStopList(StopWordListPath);
		for(File sf:files){
			File[] sfs=sf.listFiles();
			for(File f:sfs){
				wsdip.WriteSimpleIndexPara(IndexPathFloder+"\\para\\"+sf.getName()+".para", IndexPathFloder+"\\"+sf.getName()+"Index", f.getAbsolutePath(), StopWordList);
				System.out.println("IndriBuildIndex.exe para\\"+sf.getName()+".para");
			}
		}
	}
	
	public void copyFile(String infloder,String OutFloder){
		File f=new File(infloder);
		File[] fs=f.listFiles();
		String FromPath=null;
		String AimPath=null;
		for(File sf:fs){
			File[] sfs=sf.listFiles();
			FromPath=sf.getAbsolutePath()+"\\Part"+(sfs.length-1);
			AimPath=OutFloder+"\\"+sf.getName()+"\\Part"+(sfs.length-1);
			new File(OutFloder+"\\"+sf.getName()).mkdir();
			System.out.println(FromPath);
			System.out.println(AimPath);
			ProcessDataTools.getInstance().CopyFileToAppointedPath(FromPath, AimPath);
		}
	}

}
