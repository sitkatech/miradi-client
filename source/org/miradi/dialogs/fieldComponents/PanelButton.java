/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.fieldComponents;

import java.awt.Insets;

import javax.swing.Action;

import org.martus.swing.UiButton;
import org.miradi.actions.EAMAction;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class PanelButton extends UiButton
{
	public PanelButton(String text)
	{
		super(text);
		setFont(getMainWindow().getUserDataPanelFont());
	}
	
	public PanelButton(Action action)
	{
		super(action);
		setMargin(new Insets(2,2,2,2));
		setFont(getMainWindow().getUserDataPanelFont());
	}

	public PanelButton(EAMAction action)
	{
		this((Action)action);
		setToolTipText(action.getToolTipText());
	}
	
	//TODO should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}