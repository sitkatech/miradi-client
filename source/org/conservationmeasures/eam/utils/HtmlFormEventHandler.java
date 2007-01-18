/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import javax.swing.JComponent;

import org.martus.swing.HyperlinkHandler;

public interface HtmlFormEventHandler extends HyperlinkHandler
{
	public void setComponent(String name, JComponent component);
}
