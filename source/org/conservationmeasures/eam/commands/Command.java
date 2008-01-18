/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
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
	
	public boolean isDoNothingCommand(Project project) throws CommandFailedException
	{
		return false;
	}
	
	public void executeAndLog(Project target) throws CommandFailedException
	{
		execute(target);
		logData(target, getLogData());
	}
	
	public Command getReverseCommand() throws CommandFailedException
	{
		throw new CommandFailedException("Unimplemented getReverseCommand: " + getClass());
	}
	
	private void logData(Project target, HashMap dataPairs) throws CommandFailedException
	{
		try
		{
			target.writeLogLine(logDataAsString(target));
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private String logDataAsString(Project target)
	{
		return getCommandName() + ": " + formatLogData(getLogData());
	}
	
	public static String formatLogData(HashMap logData)
	{
		String logLine = "";
		String[] keys = getSortedKeys(logData);
		for (int i=0; i<keys.length; ++i) 
		{
			String name = keys[i];
			Object object = logData.get(name);
			logLine = logLine + logItemAsString(name, object)+ ", " ;
		}
		return logLine;
	}
	
	public static String logItemAsString(String name, Object object)
	{
		String value = (object==null)? "NULL" : object.toString();
		return name + "=" + value;
	}
	
	public static String[] getSortedKeys(HashMap logData)
	{
		Set keySet = logData.keySet();
		String[] keys = (String[])keySet.toArray(new String[0]);
		Arrays.sort(keys);
		return keys;
	}
	
}
