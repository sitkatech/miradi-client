/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview.elements;


import java.awt.Color;

import javax.swing.border.LineBorder;

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
		NotHugeTextArea field = getTextComponent();
		return (field).getText();
	}

	public void setFieldData(String newData)
	{
		NotHugeTextArea field = getTextComponent();
		(field).setText(newData);
	}

	private NotHugeTextArea getTextComponent()
	{
		return (NotHugeTextArea)getComponent();
	}
	
	public void createComponent()
	{
		NotHugeTextArea field = new NotHugeTextArea();
		field.setBorder(new LineBorder(Color.BLACK));
		field.setLineWrap(true);
		field.setWrapStyleWord(true);
		component = field;
	}
	
	String fieldName;
}
