/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.fieldComponents;

import org.martus.swing.UiTextField;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class PanelTextField extends UiTextField
{
	public PanelTextField()
	{
		super();
		setFont(getMainWindow().getUserDataPanelFont());
	}
	
	public PanelTextField(String text)
	{
		super(text);
		setFont(getMainWindow().getUserDataPanelFont());
	}
	
	//TODO should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}
