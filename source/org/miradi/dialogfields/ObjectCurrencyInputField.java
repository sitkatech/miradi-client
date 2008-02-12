/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import org.miradi.ids.BaseId;
import org.miradi.project.Project;

public class ObjectCurrencyInputField extends ObjectStringInputField
{
	public ObjectCurrencyInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, int columnsToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, columnsToUse);
	}
	
	public void setText(String newValue)
	{
		try
		{
			double valueToFormat = Double.parseDouble(newValue);
			newValue = project.getCurrencyFormatter().format(valueToFormat);
		}
		catch (Exception e) 
		{
			//Ignore. we dont care if it failed due to parsing a none double value
		}
		super.setText(newValue);
	}
}
