package org.ncibi.chipenrich;


import java.util.ArrayList;
import java.util.List;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;

public final class ChipEnrich
{
	private final ChipEnrichRServer rserver;

	public ChipEnrich(ChipEnrichRServer rserver)
	{
		this.rserver = rserver;
	}

	public List runAnalysis(ChipEnrichInputArguments data) 
	{

		setupCommonValues(data);
		List result;
	    String command = performAnalysis(data);
	   	result = processResultsOfAnalysis(command);
	   	result.add(data.getMethod());
	   	System.out.println("from ws-server-common and result is" + result.get(0) + "  "+result.size());
		return result;
	}

	private void setupCommonValues(ChipEnrichInputArguments data)
	{
	rserver.assignRVariable("peaks", data.getUploadFile());
	rserver.assignRVariable("outname", data.getOutName());
	rserver.assignRVariable("outpath", data.getOutPath());
	rserver.assignRVariable("genomedata", data.getSgList());
 	rserver.assignRVariable("genesetdata", data.getSgSetList());
 	rserver.assignRVariable("ld", data.getLd());
 	rserver.assignRVariable("methodata", data.getMethod());
 	rserver.assignRVariable("mappaFileLoc", data.getUploadMappaFile());
 	rserver.assignRVariable("mappaFileLoc", data.getUploadMappaFile());
	}
	
	

	private String performAnalysis(ChipEnrichInputArguments data) 
	{
		
		/*
		 * chipenrich(peaks, out_name = "chipenrich", out_path = getwd(), genome = "hg19",
	     genesets = c("GOBP","GOCC","GOMF"), locusdef = "nearest_tss", method = "chipenrich", fisher_alt = "two.sided", 
	     use_mappability = F, mappa_file = NULL, read_length = 36, 
	     qc_plots = T, max_geneset_size = 2000, num_peak_threshold = 1) */
		
		String command = "";
		String rc = data.getRc();
		
		// use_mappability = F,
		if(data.getIsMappable().equals("F"))
		{
			System.out.println("use_mappability = F,");
		  command = "  ChipEnrichResults <- chipenrich( " +
					"  peaks" +
					", out_name = outname "+ 
					", out_path = outpath" +
					", genome = genomedata "+
					", genesets = genesetdata " +
					", locusdef = ld "+ 
					", method = methodata"+
					", use_mappability = F, mappa_file = NULL, read_length = 24, "+
					", qc_plots = T"+
					", max_geneset_size = " + data.getFilter() +
					", num_peak_threshold = " + data.getPeakThr() +
					")";
		}
		else
		{
			
			if(data.getRc().equals("user"))
			{
				System.out.println("use_mappability = T with user mappability file,");
			  command = "  ChipEnrichResults <- chipenrich( " +
						"  peaks" +
						", out_name = outname "+ 
						", out_path = outpath" +
						", genome = genomedata "+
						", genesets = genesetdata " +
						", locusdef = ld "+ 
						", method = methodata"+
						", use_mappability = T" + 
						", mappa_file = mappaFileLoc "+
						", qc_plots = T"+
						", max_geneset_size = " + data.getFilter() +
						", num_peak_threshold = " + data.getPeakThr() +
						")";				
			}
			else
			{
				System.out.println("use_mappability = T with read length");
				
			  command = "  ChipEnrichResults <- chipenrich( " +
						"  peaks" +
						", out_name = outname "+ 
						", out_path = outpath" +
						", genome = genomedata "+
						", genesets = genesetdata " +
						", locusdef = ld "+ 
						", method = methodata"+
						", use_mappability = T"+
						", read_length ="+ data.getRc()+
						", qc_plots = T"+
						", max_geneset_size = " + data.getFilter() +
						", num_peak_threshold = " + data.getPeakThr() +
						")";
			}
			
			
			
		}
		
		System.out.println("command is  "+command);		
		return command;
		
	}

	
	private List processResultsOfAnalysis(String command)
	{
		List<String> status = new ArrayList();
		String result;
		try {
			result = rserver.parseAndEvalCommand(command);
			status.add(result);
		} catch (REngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Inside processResultsOfAnalysis");
		return status;
	}

	

}
