/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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
		setFont(getMainWindow().getUserDataPanelFont());
	}

	public PanelComboBox(Object[] items)
	{
		super(items);
		setFont(getMainWindow().getUserDataPanelFont());
	}
	
	//TODO: Richard: should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}
