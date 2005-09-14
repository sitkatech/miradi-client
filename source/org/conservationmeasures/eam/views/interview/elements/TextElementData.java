/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview.elements;

import javax.swing.JComponent;
import javax.swing.JLabel;

class TextElementData extends ElementData
{
	public TextElementData()
	{
		data = new StringBuffer("");
	}
	
	public boolean hasData()
	{
		return data.length() > 0;
	}
	
	public void appendLine(String text)
	{
		data.append(text);
		data.append("\n");
	}
	
	public String toString()
	{
		return data.toString();
	}
	
	public JComponent createComponent()
	{
		return new JLabel(toString());
	}

	private StringBuffer data;

}