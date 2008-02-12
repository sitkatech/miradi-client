/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.awt.Adjustable;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class MultiTableHorizontalScrollController extends MultiTableScrollController
{
	public MultiTableHorizontalScrollController()
	{
		super(Adjustable.HORIZONTAL);
	}
	
	protected JScrollBar getScrollBar(JScrollPane scrollPaneToAdd)
	{
		return scrollPaneToAdd.getHorizontalScrollBar();
	}
}
