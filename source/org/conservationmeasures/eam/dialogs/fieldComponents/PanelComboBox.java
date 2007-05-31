/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import java.awt.Font;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.UiComboBox;

public class PanelComboBox extends UiComboBox
{
	public PanelComboBox()
	{
		setFont(new Font(getMainWindow().getDataPanelFontFamily(),Font.PLAIN, getMainWindow().getDataPanelFontSize(this)));
	}

	public PanelComboBox(Object[] items)
	{
		super(items);
		setFont(new Font(getMainWindow().getDataPanelFontFamily(),Font.PLAIN, getMainWindow().getDataPanelFontSize(this)));
	}
	
	public MainWindow getMainWindow()
	{
		return EAM.mainWindow;
	}
}
