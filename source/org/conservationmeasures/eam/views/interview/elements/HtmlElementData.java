/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview.elements;

public class HtmlElementData extends TextElementData
{
	public String toString()
	{
		return "<html>" + super.toString() + "</html>";
	}
}