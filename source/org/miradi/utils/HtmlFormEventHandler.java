/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import javax.swing.JComponent;

import org.martus.swing.HyperlinkHandler;

public interface HtmlFormEventHandler extends HyperlinkHandler
{
	public void setComponent(String name, JComponent component);
}
