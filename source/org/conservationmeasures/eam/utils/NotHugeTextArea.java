/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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