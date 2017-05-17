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

public class CopyOfCheckLRpathXmlDecoder{

	public static void main(String[] args) throws Exception {
		
		
		
		FileInputStream fis1 = new FileInputStream("test.xml");
		BufferedInputStream bis1 = new BufferedInputStream(fis1);
		Object o1 ;
		XMLDecoder xmlDecoder1 = new XMLDecoder(bis1);
		//Object deSerializedObject = xmlDecoder.readObject();
		o1 = xmlDecoder1.readObject();	 
		xmlDecoder1.close();
	
		 //System.out.println("\nChecking For Values In De-Serialized Object"+o1.getClass());
		 System.out.println(o1);
		
      
	       
	
	
	}

}