/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.fieldComponents;

import org.martus.swing.UiCheckBox;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

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
	
	//TODO should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}
