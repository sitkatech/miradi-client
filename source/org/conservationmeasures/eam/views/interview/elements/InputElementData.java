/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview.elements;


import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.NotHugeTextArea;


public class InputElementData extends ElementData
{
	public InputElementData()
	{
		
	}
	
	public boolean isEmpty()
	{
		return false;
	}
	
	public boolean hasData()
	{
		return true;
	}
	
	public String toString()
	{
		return "<<input>>";
	}

	public void appendLine(String text)
	{
		fieldName = text;
	}
	
	public String getFieldName()
	{
		return fieldName;
	}
	
	public String getFieldData()
	{
		return fieldData;
	}
	
	public void setFieldData(String newData)
	{
		EAM.logDebug("InputElementData.setFieldData: " + newData);
		fieldData = newData;
	}

	public void copyDataFromComponent()
	{
		fieldData = ((NotHugeTextArea)getComponent()).getText();
		EAM.logDebug("copyDataFromComponent: " + fieldData);
	}

	public void copyDataToComponent()
	{
		EAM.logDebug("copyDataToComponent: " + fieldData);
		((NotHugeTextArea)getComponent()).setText(fieldData);
	}

	public void createComponent()
	{
		NotHugeTextArea field = new NotHugeTextArea();
		field.setLineWrap(true);
		field.setWrapStyleWord(true);
		component = field;
	}
	
	String fieldName;
	String fieldData;
}
