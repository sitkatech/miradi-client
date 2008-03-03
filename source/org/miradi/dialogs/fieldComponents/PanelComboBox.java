/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.fieldComponents;

import org.martus.swing.UiComboBox;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class PanelComboBox extends UiComboBox
{
	public PanelComboBox()
	{
		setFont(getMainWindow().getUserDataPanelFont());
	}

	public PanelComboBox(Object[] items)
	{
		super(items);
		setFont(getMainWindow().getUserDataPanelFont());
	}
	
	//TODO should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}
