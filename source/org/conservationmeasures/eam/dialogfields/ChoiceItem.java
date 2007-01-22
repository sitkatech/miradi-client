/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.Color;

public class ChoiceItem
{
	public ChoiceItem(String codeToUse, String labelToUse)
	{
		this(codeToUse, labelToUse, null);
	}
	
	public ChoiceItem(String codeToUse, String labelToUse, Color colorToUse)
	{
		code = codeToUse;
		label = labelToUse;
		color = colorToUse;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public String toString()
	{
		return getLabel();
	}
	
	
	String code;
	String label;
	Color color;
}
