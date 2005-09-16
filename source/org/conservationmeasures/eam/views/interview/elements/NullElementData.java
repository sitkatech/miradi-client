/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview.elements;


public class NullElementData extends ElementData
{
	public boolean isEmpty()
	{
		return true;
	}

	public void appendLine(String text)
	{
		throw new RuntimeException();
	}

	public String toString()
	{
		return null;
	}

	public void createComponent()
	{
		throw new RuntimeException();
	}
}