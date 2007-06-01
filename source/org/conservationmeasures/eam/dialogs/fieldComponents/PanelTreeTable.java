/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

import com.java.sun.jtreetable.JTreeTable;
import com.java.sun.jtreetable.TreeTableModel;

public class PanelTreeTable extends JTreeTable
{

	public PanelTreeTable(TreeTableModel treeTableModel)
	{
		super(treeTableModel);
		setFont(getMainWindow().getUserDataPanelFont());
		setRowHeight(getFontMetrics(getFont()).getHeight());
		getTableHeader().setFont(getMainWindow().getUserDataPanelFont());
	}
	
	//TODO: find a better way to get to main window
	public MainWindow getMainWindow()
	{
		return EAM.mainWindow;
	}
}
