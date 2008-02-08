/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import org.conservationmeasures.eam.main.AppPreferences;
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
		setBackground(AppPreferences.getControlPanelBackgroundColor());
	}
	
	//TODO: Richard: should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}
