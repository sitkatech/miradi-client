/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;

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
	
	public String getText()
	{
		String text = super.getText();
		try
		{
			double parsed = Double.parseDouble(text);
			return Double.toString(parsed);
		}
		catch (Exception e)
		{
			//Ignore. we dont care if it failed due to parsing a none double value
			return text;
		}
	}
}
