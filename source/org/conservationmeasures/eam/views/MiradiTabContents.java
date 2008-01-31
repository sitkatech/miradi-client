/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import java.awt.Component;

import javax.swing.Icon;

public interface MiradiTabContents
{
	public String getTabName();
	public Icon getIcon();
	public Component getComponent();
}
