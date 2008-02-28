/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.miradi.utils;

import java.awt.Dimension;

import org.martus.swing.HyperlinkHandler;
import org.miradi.main.MainWindow;
import org.miradi.wizard.MiradiHtmlViewer;

public class FlexibleWidthHtmlViewer extends MiradiHtmlViewer
{
	public FlexibleWidthHtmlViewer(MainWindow mainWindow, String htmlText)
	{
		this(mainWindow, null, htmlText);
	}
	
	public FlexibleWidthHtmlViewer(MainWindow mainWindow, HyperlinkHandler hyperLinkHandler, String htmlText)
	{
		this(mainWindow, hyperLinkHandler);
		setText(htmlText);
	}
	
	public FlexibleWidthHtmlViewer(MainWindow mainWindow, HyperlinkHandler hyperLinkHandler)
	{
		super(mainWindow, hyperLinkHandler);
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		if(getParent() == null)
			return super.getPreferredSize();
		int width = getParent().getWidth();
		
		setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
		int height = super.getPreferredSize().height;
		setMaximumSize(null);
		return new Dimension(width, height);
	}
	
}