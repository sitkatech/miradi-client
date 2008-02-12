/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

import java.util.HashMap;

import org.miradi.commands.Command;

public abstract class CreateObjectParameter
{
	abstract public String getFormatedDataString();
	
	protected String formatDataString(HashMap logData)
	{
		return "(" + Command.formatLogData(logData) + ")";
	}
}
