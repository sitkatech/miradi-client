/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview.elements;

import javax.swing.JComponent;

public class NullElementData extends ElementData
{
	public boolean hasData()
	{
		return false;
	}

	public void appendLine(String text)
	{
		throw new RuntimeException();
	}

	public String toString()
	{
		return null;
	}

	public JComponent createComponent()
	{
		throw new RuntimeException();
	}
}