package org.ncibi.chipenrich;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public final class ChipEnrichRServer
{
	private RConnection connection;

	public ChipEnrichRServer(String serverAddress, int serverPort)
	{
		connectToRserve(serverAddress, serverPort);
		loadChipEnrichRServerLibrary();
	}

	private void connectToRserve(String serverAddress, int serverPort)
	{
		try
		{
			connection = new RConnection(serverAddress, serverPort);
		}
		catch (RserveException e)
		{
			throw new IllegalStateException("Unable to connect to RServer: " + serverAddress + ":" + serverPort);
		}
	}

	private void loadChipEnrichRServerLibrary()
	{
		voidEvalRCommand("library(chipenrich)");
		// voidEvalRCommand("source('/usr/share/chipFileStore/Constant/ChipRVersion/main_chipoptimize.R')");
		
	}

	public void voidEvalRCommand(String command)
	{
		try
		{
			connection.voidEval(command);
		}
		catch (RserveException e)
		{
			e.printStackTrace();
			throw new IllegalStateException("Unable to run command on RServer: " + command);
		}
	}

	public REXP evalRCommand(String command)
	{
		try
		{
			return connection.eval(command);
		}
		catch (RserveException e)
		{
			e.printStackTrace();
			throw new IllegalStateException("Unable to run command on RServer: " + command);
		}
	}
	
	@SuppressWarnings("finally")
	public String parseAndEvalCommand(String command) throws REngineException, REXPMismatchException
	{
		String status = "";
		try
		{
			REXP r1=  connection.parseAndEval("try("+command+",silent=TRUE)");
			if (r1.inherits("try-error")) 
			{
			System.err.println("Error: "+r1.asString());
			status = "Error: "+r1.asString();
			}
		else { System.out.println("Hello");
			status = "Done";
		}
			
		}
		catch (RserveException e)
		{
			e.printStackTrace();
			throw new IllegalStateException("Unable to run command  parseAndEval on RServer: " + command);
		}
		finally
		{
		return status;
		}
	}

	public void assignRVariable(String rvar, double[] values)
	{
		try
		{
			connection.assign(rvar, values);
		}
		catch (REngineException e)
		{
			e.printStackTrace();
			throw new IllegalStateException("Unable to assign to assign variable on RServer: " + rvar);
		}
	}

	public void assignRVariable(String rvar, String[] values)
	{
		try
		{
			System.out.printf(rvar+ " = " +values.toString() + "\n");
			
			connection.assign(rvar, values);
		}
		catch (REngineException e)
		{
			e.printStackTrace();
			throw new IllegalStateException("Unable to assign to assign variable on RServer: " + rvar);
		}
	}

	public void assignRVariable(String rvar, int[] values)
	{
		try
		{
			connection.assign(rvar, values);
		}
		catch (REngineException e)
		{
			e.printStackTrace();
			throw new IllegalStateException("Unable to assign to assign variable on RServer: " + rvar);
		}
	}

	public void assignRVariable(String rvar, String arg)
	{
		try
		{
			System.out.printf(rvar+" = "+arg+ "\n");
			connection.assign(rvar, arg);
		}
		catch (REngineException e)
		{
			e.printStackTrace();
			throw new IllegalStateException("Unable to assign to assign variable on RServer: " + rvar);
		}
	}

	public void close()
	{
		connection.close();
	}
}
