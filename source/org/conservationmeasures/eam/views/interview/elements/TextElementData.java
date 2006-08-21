/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview.elements;

import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;

class TextElementData extends ElementData implements HyperlinkHandler
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
		component = new HtmlViewer(toString(), this);
	}

	public void linkClicked(String linkDescription)
	{
	}

	public void valueChanged(String widget, String newValue)
	{
	}

	public void buttonPressed(String buttonName)
	{
	}

	private StringBuffer data;
}
