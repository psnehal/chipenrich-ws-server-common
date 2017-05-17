package org.ncibi.lrpath;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public final class LRPathRServer
{
	private RConnection connection;

	public LRPathRServer(String serverAddress, int serverPort)
	{
		connectToRserve(serverAddress, serverPort);
		//loadLRPathLibrary();
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

	private void loadLRPathLibrary()
	{
		voidEvalRCommand("library(LRpath)");
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
