/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.project.Project;

public abstract class Command
{
	abstract public HashMap getLogData();
	abstract public String getCommandName();
	abstract public void execute(Project target) throws CommandFailedException;
	
	public boolean equals(Object other)
	{
		return toString().equals(other.toString());
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
	
	public void logData(Project target, HashMap dataPairs) throws CommandFailedException
	{
		//TODO: first phase check in , the following remains to be done
		// File logDirectory = EAM.mainWindow.getLogDirectory();  creation and replace of per project
		String logLine  = getCommandName() + ": ";
		HashMap logData = getLogData();
		if (logData!=null)
			logLine = logLine + processLogData(logData);
		//System.out.println("LOG ENTRY:  " + logLine);
	}
	
	private String processLogData(HashMap logData)
	{
		String logLine = "";
		Set keySet = logData.keySet();
		Arrays.sort(keySet.toArray());
		String[] keys = (String[])keySet.toArray(new String[0]);
		for (int i=0; i<keys.length; ++i) 
		{
			String name = keys[i];
			Object object = logData.get(name);
			logLine = logLine + processLogLine(name, object)+ ", " ;
		}
		return logLine;
	}
	
	private String processLogLine(String name, Object object)
	{
		if(name.equals(CreateObjectParameter.class.getSimpleName()))
		{
			return processCreateObjectParameter((CreateObjectParameter)object);
		}
		return name + "=" + object.toString();
	}
	
	private String processCreateObjectParameter(CreateObjectParameter parms)
	{
		if (parms==null) 
			return "";
		
		String logLine = parms.getClass().getSimpleName() + "=(";
		HashMap logData = parms.getLogData();
		Set keySet = logData.keySet();
		Arrays.sort(keySet.toArray());
		String[] keys = (String[])keySet.toArray(new String[0]);
		for (int i=0; i<keys.length; ++i) 
		{
			String name = keys[i];
			Object object = logData.get(name);
			logLine = logLine + name + "=" + object.toString() + ", " ;
		}
		return logLine + ")";
	}
}
