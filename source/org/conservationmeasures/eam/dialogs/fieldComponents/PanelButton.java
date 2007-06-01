/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import javax.swing.Action;
import javax.swing.Icon;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.UiButton;

public class PanelButton extends UiButton
{
	public PanelButton(String text)
	{
		super(text);
		setFont(getMainWindow().getUserDataPanelFont(getFont().getSize()));
	}

	public PanelButton(Action action)
	{
		super(action);
		setFont(getMainWindow().getUserDataPanelFont(getFont().getSize()));
	}
	
	public PanelButton(Icon icon)
	{
		super(icon);
		setFont(getMainWindow().getUserDataPanelFont(getFont().getSize()));
	}
	
	public MainWindow getMainWindow()
	{
		return EAM.mainWindow;
	}
}