/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.Dimension;

import org.martus.swing.UiTextArea;

public class NotHugeTextArea extends UiTextArea
{
	public NotHugeTextArea()
	{
		super(1, 80);
	}
	
	public Dimension getMaximumSize()
	{
		return getPreferredSize();
	}
}