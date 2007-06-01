/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.UiComboBox;

public class PanelComboBox extends UiComboBox
{
	public PanelComboBox()
	{
		setFont(getMainWindow().getUserDataPanelFont(getFont().getSize()));
	}

	public PanelComboBox(Object[] items)
	{
		super(items);
		setFont(getMainWindow().getUserDataPanelFont(getFont().getSize()));
	}
	
	public MainWindow getMainWindow()
	{
		return EAM.mainWindow;
	}
}
