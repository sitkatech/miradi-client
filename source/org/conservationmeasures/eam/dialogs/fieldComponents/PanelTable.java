/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import javax.swing.table.TableModel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.TableWithHelperMethods;

public class PanelTable extends TableWithHelperMethods
{
	public PanelTable()
	{
		super();
		setFontData();
	}

	public PanelTable(TableModel model)
	{
		super(model);
		setFontData();
	}

	private void setFontData()
	{
		setFont(getMainWindow().getUserDataPanelFont());
		getTableHeader().setFont(getMainWindow().getUserDataPanelFont());
		setRowHeight(getFontMetrics(getFont()).getHeight() + VERTICAL_FONT_CUSHION);
	}
	
	//TODO: Richard: should not use static ref here
	private MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}

	private static final int VERTICAL_FONT_CUSHION = 10;


}
