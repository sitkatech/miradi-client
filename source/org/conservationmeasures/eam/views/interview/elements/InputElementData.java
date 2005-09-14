/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview.elements;

import javax.swing.JComponent;

import org.conservationmeasures.eam.utils.NotHugeTextArea;


public class InputElementData extends ElementData
{
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
		throw new RuntimeException();
	}

	public JComponent createComponent()
	{
		NotHugeTextArea field = new NotHugeTextArea();
		field.setLineWrap(true);
		field.setWrapStyleWord(true);
		return field;
	}
}