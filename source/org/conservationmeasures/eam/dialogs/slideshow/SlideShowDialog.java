/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.slideshow;

import javax.swing.Box;

import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.main.MainWindow;

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

