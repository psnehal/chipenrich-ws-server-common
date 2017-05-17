package org.ncibi.ws.encoder;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.Test;
import org.ncibi.chipenrich.ChipEnrichInputArguments;
import org.rosuda.REngine.REXPMismatchException;

public class chipenrichResourseModuleEncoder {
	
	@Test
	public void testEncoder() throws REXPMismatchException, FileNotFoundException
	{
	
	ChipEnrichInputArguments data = new ChipEnrichInputArguments();
	
	data.setUploadFile("/home/snehal/workspace2/LRPathBranch/FileStore/nov30test2/E2A.txt");
	data.setUploadMappaFile("/home/snehal/workspace2/LRPathBranch/FileStore/nov30test2/E2A.txt");
	data.setOutPath("/home/snehal/workspace2/LRPathBranch/FileStore/nov30test2");
	
	data.setSgList("hg19");
	data.setSgSetList(new String[] { "biocarta_pathway","ehmn_pathway_gene"});
	data.setRc("24");
	data.setIsMappable("T");
	
	data.setEmail("snehal@med.umich.edu");
	data.setLd("1kb") ;
	data.setOutName("chip");
	data.setMethod("chipenrich");
	data.setQc("T");
	data.setFilter("2000");
	data.setPeakThr("2");

	
	
	FileOutputStream fos = new FileOutputStream("test.xml");
	BufferedOutputStream bos = new BufferedOutputStream(fos);
	XMLEncoder xmlEncoder = new XMLEncoder(bos);
	xmlEncoder.writeObject(data);
	xmlEncoder.close();

	try {
		
		System.out.println("Args: " + data);
	} catch (Throwable ignore) {}
	
	
	}
}
