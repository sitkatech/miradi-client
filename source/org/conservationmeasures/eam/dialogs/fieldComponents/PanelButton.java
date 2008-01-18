/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import java.awt.Insets;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.UiButton;

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
		String name = (String)action.getValue(AbstractAction.NAME);
		if(name == null || name.length() == 0)
			setMargin(new Insets(1,1,1,1));

		setFont(getMainWindow().getUserDataPanelFont());
	}
	
	//TODO: Richard: should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}