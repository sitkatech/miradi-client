/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.UiCheckBox;

public class PanelCheckBox extends UiCheckBox
{
	public PanelCheckBox()
	{
		this(null);
	}

	public PanelCheckBox(String fieldLabel, boolean selected)
	{
		this(fieldLabel);
		setSelected(selected);
	}
	
	public PanelCheckBox(String fieldLabel)
	{
		super(fieldLabel);
		setFont(getMainWindow().getUserDataPanelFont());
	}
	
	//TODO: Richard: should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.mainWindow;
	}
}
