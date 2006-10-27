/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;


import javax.swing.Icon;


public abstract class DesireIcon implements Icon
{
	public int getIconHeight()
	{
		return HEIGHT;
	}

	public int getIconWidth()
	{
		return WIDTH;
	}

	protected static final int WIDTH = 16;
	protected static final int HEIGHT = 16;
}
