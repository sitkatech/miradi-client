/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview.elements;

import org.conservationmeasures.eam.utils.HtmlViewer;

class TextElementData extends ElementData
{
	public TextElementData()
	{
		data = new StringBuffer("");
	}
	
	public boolean isEmpty()
	{
		return data.length() == 0;
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
	
	public void createComponent()
	{
		component = new HtmlViewer(toString(), null);
	}

	private StringBuffer data;

}