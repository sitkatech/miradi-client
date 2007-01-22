/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import javax.swing.JComponent;

import org.martus.swing.HyperlinkHandler;

public interface HtmlFormEventHandler extends HyperlinkHandler
{
	public void setComponent(String name, JComponent component);
}
