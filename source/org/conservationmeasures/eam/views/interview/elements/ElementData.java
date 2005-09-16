/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview.elements;

import javax.swing.JComponent;

public abstract class ElementData
{
	abstract public boolean isEmpty();
	abstract public void appendLine(String text);
	abstract public void createComponent();
	abstract public String toString();
	
	public boolean hasData()
	{
		return false;
	}
	
	public JComponent getComponent()
	{
		return component;
	}
	
	public void copyDataFromComponent()
	{
	}
	
	public void copyDataToComponent()
	{
	}
	
	public String getFieldName()
	{
		throw new RuntimeException("No field name in " + this);
	}

	public String getFieldData()
	{
		throw new RuntimeException("No field data in " + this);
	}
	
	public void setFieldData(String newData)
	{
		throw new RuntimeException("No field data in " + this);
	}
	
	JComponent component;
}