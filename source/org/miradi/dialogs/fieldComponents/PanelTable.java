/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.fieldComponents;

import javax.swing.table.TableModel;

import org.miradi.dialogs.tablerenderers.BasicTableCellRenderer;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.ExportableTable;
import org.miradi.utils.TableRowHeightSaver;

public class PanelTable extends ExportableTable
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
	
	//TODO should not use static ref here
	private MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}

	private static final int INTERCELL_LINE_SIZE = 3;
	private static final int VERTICAL_FONT_CUSHION = INTERCELL_LINE_SIZE + 
			TableRowHeightSaver.ROW_RESIZE_MARGIN + 2*BasicTableCellRenderer.CELL_MARGIN;
}
