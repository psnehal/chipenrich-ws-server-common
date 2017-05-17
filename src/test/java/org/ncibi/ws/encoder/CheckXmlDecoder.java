package org.ncibi.ws.encoder;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import org.ncibi.chipenrich.ChipEnrichInputArguments;
import org.ncibi.lrpath.LRPathArguments;
import org.ncibi.chipenrich.ChipEnrichRServer;
import org.ncibi.lrpath.config.RServerConfiguration;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;

public class CheckXmlDecoder{

	public static void main(String[] args) throws Exception {
		
		FileInputStream fis = new FileInputStream("test.xml");
		BufferedInputStream bis = new BufferedInputStream(fis);
		ChipEnrichInputArguments o = new ChipEnrichInputArguments();
		XMLDecoder xmlDecoder = new XMLDecoder(bis);
		//Object deSerializedObject = xmlDecoder.readObject();
		o =(ChipEnrichInputArguments) xmlDecoder.readObject();	 
		xmlDecoder.close();
		
		
		 System.out.println("\nChecking For Values In De-Serialized Object"+o.getClass());
		 System.out.println(o);
		 ChipEnrichInputArguments data = new ChipEnrichInputArguments();
		 data.setUploadFile(o.getUploadFile());
			data.setOutPath(o.getOutPath());
			data.setOutName(o.getOutName());
			data.setEmail(o.getEmail());
			data.setSgList(o.getSgList());
			data.setLd(o.getLd()) ;
			data.setSgSetList(o.getSgSetList());
			data.setIsMappable(o.getIsMappable());
			data.setRc(o.getRc());
			data.setMethod(o.getMethod());
			data.setQc(o.getQc());
			data.setPeakThr("1");
		 
		 
	     System.out.println("...First Name: " + o.getSgSetList()[0]);
	     System.out.println("...First Name: " + o);
	      
	    
	      
	 System.out.println("1");
	 String test = "[biocarta_pathway";
	 System.out.println("2"+test.length());
	ChipEnrichRServer rserver = new ChipEnrichRServer(RServerConfiguration.rserverAddress(),
    RServerConfiguration.rserverPort());
  	System.out.println(RServerConfiguration.rserverAddress());
  	
  	//127.0.0.1
  	  String [] databases = o.getSgSetList();
  	  for(int i = 0; i<databases.length;i++)
  	  {
  		System.out.println(databases[i]);  
  	  }
      //rserver.assignRVariable("check", "to test if rserver is ruuning");
      rserver.assignRVariable("g", o.getSgSetList());
      
      
      //rserver.evalRCommand("str(g)").asString();
     
      String[] r = rserver.evalRCommand("g").asStrings();
      for(int i = 0; i<r.length;i++)
  	  {
  		System.out.println("\n1 "+"its the length of the each string"+r[i].length());  
  	  }
    
      
            rserver.assignRVariable("peaks", data.getUploadFile());
   			rserver.assignRVariable("outname", data.getOutName());
   			rserver.assignRVariable("outpath", data.getOutPath());
   			rserver.assignRVariable("genomedata", data.getSgList());
   		 	rserver.assignRVariable("genesetdata", data.getSgSetList());
   		 	rserver.assignRVariable("ld", data.getLd());
   		 	rserver.assignRVariable("methodata", data.getMethod());
   		 	
   		 	
   			String command = "";
   			command = "ChipEnrichResults <- chipenrich( " +
   			"peaks" +
   			", out_path = outpath" +
   			", genesets = genesetdata " +
   			//", locusdef = ld "+ 
   			", method = methodata"+
   			//", use_mappability = " + data.getIsMappable()+
   			//", read_length ="+ data.getRc()+
   			//", qc_plots =" + data.getQc()+
   			")"; 
   			
   			System.out.println ("command is \n"+ command);
   			
   			rserver.voidEvalRCommand(command);
      
	       
	
	
	}

}