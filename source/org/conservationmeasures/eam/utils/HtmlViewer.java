/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.Color;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HtmlViewer extends JEditorPane implements HyperlinkListener
{
	public HtmlViewer(String htmlSource, HyperlinkHandler hyperLinkHandler)
	{
		linkHandler = hyperLinkHandler;
		
		setContentType("text/html");
		setEditable(false);
		setText(htmlSource);
		setBackground(Color.LIGHT_GRAY);
		addHyperlinkListener(this);
	}

	public void hyperlinkUpdate(HyperlinkEvent e)
	{
		if(e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
		{
			String clicked = e.getDescription();
			linkHandler.clicked(clicked);
		}

	}
	
	HyperlinkHandler linkHandler;
}