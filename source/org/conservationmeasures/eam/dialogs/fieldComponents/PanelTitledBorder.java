/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import javax.swing.border.TitledBorder;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class PanelTitledBorder extends TitledBorder
{
	public PanelTitledBorder(String title)
	{
		super(null, title, LEADING, TOP, getMainWindow().getUserDataPanelFont());
	}
	
	//TODO: Richard: should not use static ref here
	static private MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}
