/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public abstract class Command
{
	abstract public HashMap<String, Comparable> getLogData();
	abstract public String getCommandName();
	abstract public void execute(Project project) throws CommandFailedException;

	@Override
	public abstract String toString();
	
	@Override
	public boolean equals(Object other)
	{
		return toString().equals(other.toString());
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
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
	
	public void executeAndLog(Project project) throws CommandFailedException
	{
		execute(project);
		logData(project, getLogData());
	}
	
	public Command getReverseCommand() throws CommandFailedException
	{
		throw new CommandFailedException("Unimplemented getReverseCommand: " + getClass());
	}
	
	private void logData(Project project, HashMap<String, Comparable> dataPairs) throws CommandFailedException
	{
		try
		{
			EAM.logVerbose("Command Executed: " +logDataAsString());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public String logDataAsString()
	{
		return getCommandName() + ": " + formatLogData(getLogData());
	}
	
	public static String formatLogData(HashMap<String, Comparable> logData)
	{
		String logLine = "";
		String[] keys = getSortedKeys(logData);
		for (int i=0; i<keys.length; ++i) 
		{
			String name = keys[i];
			Object object = logData.get(name);
			logLine = logLine + logItemAsString(name, object)+ "\n" ;
		}
		return logLine;
	}
	
	public static String logItemAsString(String name, Object object)
	{
		String value = (object==null)? "NULL" : object.toString();
		return name + "=" + value;
	}
	
	public static String[] getSortedKeys(HashMap<String, Comparable> logData)
	{
		Set<String> keySet = logData.keySet();
		String[] keys = keySet.toArray(new String[0]);
		Arrays.sort(keys);
		return keys;
	}
	
}
