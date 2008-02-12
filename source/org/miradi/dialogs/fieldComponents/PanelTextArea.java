/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.fieldComponents;

import org.martus.swing.UiTextArea;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class PanelTextArea extends UiTextArea
{
	public PanelTextArea(int rows, int cols)
	{
		super(rows, cols);
		setFont(getMainWindow().getUserDataPanelFont());
	}

	public PanelTextArea(String text)
	{
		super(text);
		setFont(getMainWindow().getUserDataPanelFont());
	}
	
	//TODO: Richard: should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}