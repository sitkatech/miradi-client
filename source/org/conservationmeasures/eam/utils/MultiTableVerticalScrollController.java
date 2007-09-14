/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Adjustable;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class MultiTableVerticalScrollController extends MultiTableScrollController
{
	public MultiTableVerticalScrollController()
	{
		super(Adjustable.VERTICAL);
	}
	
	protected JScrollBar getScrollBar(JScrollPane scrollPaneToAdd)
	{
		return scrollPaneToAdd.getVerticalScrollBar();
	}
}
