/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.slideshow;

import javax.swing.Box;

import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.main.MainWindow;

public class SlideShowDialog extends ModelessDialogWithClose
{

	public SlideShowDialog(MainWindow parent, SlideListManagementPanel panel, String headingText)
	{
		super(parent, panel, headingText);
		slideShowPanel = panel;
	}
	
	public void addAdditionalButtons(Box buttonBar)
	{
		createDirectionsButton(buttonBar);
	}

	SlideListManagementPanel slideShowPanel;
}

