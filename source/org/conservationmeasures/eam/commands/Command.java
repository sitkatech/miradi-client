/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.project.Project;

public abstract class Command
{
	abstract public HashMap getLogData();
	abstract public String getCommandName();
	abstract public void execute(Project target) throws CommandFailedException;
	
	public boolean equals(Object other)
	{
		return toString().equals(getValue(other));
	}
	
	public boolean isBeginTransaction()
	{
		return false;
	}
	
	public boolean isEndTransaction()
	{
		return false;
	}
	
	public void executeAndLog(Project target) throws CommandFailedException
	{
		execute(target);
		HashMap dataPairs = getLogData();
		logData(target, dataPairs);
	}
	
	public Command getReverseCommand() throws CommandFailedException
	{
		throw new CommandFailedException("Unimplemented getReverseCommand: " + getClass());
	}
	
	private void logData(Project target, HashMap dataPairs) throws CommandFailedException
	{
		//TODO: error logs should be wrtten to this file as well, since it is project specific
		// If we do this then the wrtie(println) shuold be in synk method in main
		try
		{
			//FIXME: null check to handle test code
			if (EAM.mainWindow==null)
				return;
			PrintStream logPrintStream = EAM.mainWindow.getCommandLogFile();
			//TODO: need to handle no project calles
			if (logPrintStream==null)
				return;
			logPrintStream.println("LOG ENTRY:  " + processLogData(target));
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private String processLogData(Project target)
	{
		String logLine  = "{" + target.getFilename() + "} " + getCommandName() + ": ";
		HashMap logData = getLogData();
		if (logData!=null)
			logLine = logLine + processLogData(logData);
		return logLine;
	}
	
	private String processLogData(HashMap logData)
	{
		String logLine = "";
		String[] keys = getSortedKeys(logData);
		for (int i=0; i<keys.length; ++i) 
		{
			String name = keys[i];
			Object object = logData.get(name);
			logLine = logLine + processLogItem(name, object)+ ", " ;
		}
		return logLine;
	}

	private String processLogItem(String name, Object object)
	{
		if(name.equals(CreateObjectParameter.class.getSimpleName()))
		{
			return processCreateObjectParameter((CreateObjectParameter)object);
		}
		return name + "=" + getValue(object);
	}
	
	private String processCreateObjectParameter(CreateObjectParameter parms)
	{
		if (parms==null) 
			return "";
		
		String logLine = parms.getClass().getSimpleName() + "=(";
		HashMap logData = parms.getLogData();
		String[] keys = getSortedKeys(logData);
		for (int i=0; i<keys.length; ++i) 
		{
			String name = keys[i];
			Object object = logData.get(name);
			logLine = logLine + name + "=" + getValue(object) + ", " ;
		}
		return logLine + ")";
	}
	
	private String getValue(Object object)
	{
		if (object==null)
			return "NULL";
		return object.toString();
	}
	
	private String[] getSortedKeys(HashMap logData)
	{
		Set keySet = logData.keySet();
		String[] keys = (String[])keySet.toArray(new String[0]);
		Arrays.sort(keys);
		return keys;
	}
	
}
