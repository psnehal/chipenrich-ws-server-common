package org.ncibi.lrpath;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;

public final class LRPath
{
	private final LRPathRServer rserver;

	public LRPath(LRPathRServer rserver)
	{
		this.rserver = rserver;
	}

	public List<LRPathResult> runAnalysis(LRPathArguments data)
	{
		Map<String, String> conceptNameMap = null;
		List<LRPathResult> allResult = new LinkedList<LRPathResult>();
		setupCommonValues(data);
		String[] database = data.getDatabase().split(",");
		HashMap<String, Boolean> db = new DatasourceConfig(database, data.getSpecies()).getDbConfiguration();
		List<LRPathResult> result = null;
		for (String dbname : db.keySet())
		{
			if (db.get(dbname))
			{
//			    System.out.println("performAnalysisWithExternalDatabase = " + data);
//			    System.out.println(data.getIdentifiers()[0]);
				conceptNameMap = performAnalysisWithExternalDatabase(data, dbname);
				result = processResultsOfAnalysis(dbname, true, conceptNameMap);
			}
			else
			{
//			    System.out.println("performAnalysisWithInternalDatabase = " + data);
//			    System.out.println(data.getIdentifiers()[0]);
				performAnalysisWithInternalDatabase(data, dbname);
				result = processResultsOfAnalysis(dbname, false, null);
			}

			allResult.addAll(result);
		}

		return allResult;
	}

	private void setupCommonValues(LRPathArguments data)
	{

		if (data.getIdentifiers() != null)
		{
			rserver.assignRVariable("geneids", data.getIdentifiers());
		}
		else
		{
			rserver.assignRVariable("geneids", data.getGeneids());
		}

		rserver.assignRVariable("sigvals", data.getSigvals());

		rserver.assignRVariable("species", data.getSpecies());
	}

	private Map<String, String> performAnalysisWithExternalDatabase(LRPathArguments data, String database)
	{
		ConceptData conceptData = new ConceptData();
		try
		{
			Map<String, String> conceptNameMap = new HashMap<String, String>();

			String dictionaryId = conceptData.getDictionaryId(database);
			String taxid = Integer.toString(Species.toSpecies(data.getSpecies()).taxid());
			String conceptList = conceptData.getConceptListR(dictionaryId, taxid, data.getMing(), data.getMaxg());
			conceptNameMap = conceptData.getConceptName(dictionaryId);
			double[] nullsetList = conceptData.getDictionaryElementList(dictionaryId, taxid);

			String command = "";

			rserver.assignRVariable("nullsetList", nullsetList);
			rserver.voidEvalRCommand(conceptList);

			if (data.getDirection().length > 1)
			{
				rserver.assignRVariable("direction", data.getDirection());
				command = "LRResults <- LRpath(sigvals, geneids, species, direction, min.g=" + data.getMing() + ", max.g=" + data.getMaxg()
						+ ", sig.cutoff=" + data.getSigcutoff() + ", database=\"External\", conceptList, nullsetList,  odds.min.max=c("
						+ data.getOddsmin() + "," + data.getOddsmax() + "))";
			}
			else
			{
				command = "LRResults <- LRpath(sigvals, geneids, species, direction=NULL, min.g=" + data.getMing() + ", max.g="
						+ data.getMaxg() + ", sig.cutoff=" + data.getSigcutoff()
						+ ", database=\"External\", conceptList, nullsetList,  odds.min.max=c(" + data.getOddsmin() + ","
						+ data.getOddsmax() + "))";
			}

			rserver.voidEvalRCommand(command);
			return conceptNameMap;
		}
		catch (SQLException e)
		{
			throw new IllegalStateException("Failed SQL Command");
		}
	}

	private void performAnalysisWithInternalDatabase(LRPathArguments data, String database)
	{
//	    System.out.println("data.getIdentifiers().length = " + data.getIdentifiers().length);
//	    System.out.println("data.getDirection().length = " + data.getDirection().length);
//	    System.out.println("data.getSigvals().length = " + data.getSigvals().length);
		if (data.getDirection().length > 1)
		{
			rserver.assignRVariable("direction", data.getDirection());
			rserver.voidEvalRCommand("LRResults <- LRpath(sigvals, geneids, species, direction, min.g=" + data.getMing() + ", max.g="
					+ data.getMaxg() + ", sig.cutoff=" + data.getSigcutoff() + ", database=\"" + database + "\", odds.min.max=c("
					+ data.getOddsmin() + "," + data.getOddsmax() + "))");
		}
		else
		{
			rserver.voidEvalRCommand("LRResults <- LRpath(sigvals, geneids, species, direction=NULL, min.g=" + data.getMing() + ", max.g="
					+ data.getMaxg() + ", sig.cutoff=" + data.getSigcutoff() + ", database=\"" + database + "\", odds.min.max=c("
					+ data.getOddsmin() + "," + data.getOddsmax() + "))");
		}
	}

	private List<LRPathResult> processResultsOfAnalysis(String db, boolean isDbExternal, Map<String, String> conceptNameMap)
	{
		try
		{
			RList list = rserver.evalRCommand("LRResults").asList();
			

			String[] conceptId = ((REXP) list.get(0)).asStrings();
			String[] conceptName = ((REXP) list.get(1)).asStrings();
			String[] numUniqueGenes = ((REXP) list.get(2)).asStrings();
			String[] coeff = ((REXP) list.get(3)).asStrings();
			String[] oddsRatio = ((REXP) list.get(4)).asStrings();
			String[] pvalue = ((REXP) list.get(5)).asStrings();
			String[] fdr = ((REXP) list.get(6)).asStrings();
			String[] sigGenes = ((REXP) list.get(7)).asStrings();
			List<LRPathResult> resultData = new LinkedList<LRPathResult>();

			if (conceptId.length > 1)
			{
				for (int i = 0; i < conceptId.length; i++)
				{
					Vector<String> genes = new Vector<String>(Arrays.asList(sigGenes[i].split(",")));
					LRPathResult result = new LRPathResult();
					result.setConceptId(conceptId[i]);
					result.setConceptType(db);
					result.setNumUniqueGenes(Integer.parseInt(numUniqueGenes[i]));
					result.setCoeff(Double.parseDouble(coeff[i]));
					result.setOddsRatio(Double.parseDouble(oddsRatio[i]));
					result.setFdr(Double.parseDouble(fdr[i]));
					result.setPValue(Double.parseDouble(pvalue[i]));
					result.setSigGenes(genes);
					if (isDbExternal)
					{
						result.setConceptName(conceptNameMap.get(conceptName[i]));
					}
					else
					{
						result.setConceptName(conceptName[i]);
					}

					resultData.add(result);
				}
			}
			return resultData;
		}
		catch (REXPMismatchException e)
		{
			throw new IllegalArgumentException("Unable to convert objects.");
		}
	}

}
