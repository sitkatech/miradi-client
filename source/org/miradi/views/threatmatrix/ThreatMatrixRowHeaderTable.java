/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.event.ChangeEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import org.miradi.dialogs.fieldComponents.PanelTable;

public class ThreatMatrixRowHeaderTable extends PanelTable
{
	public ThreatMatrixRowHeaderTable(TableModel rowHeaderData, ThreatGridPanel panel)
	{
		super(rowHeaderData);
		getTableHeader().setResizingAllowed(true);
		getTableHeader().setReorderingAllowed(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		getColumnModel().getColumn(0).setPreferredWidth(ThreatGridPanel.LEFTMOST_COLUMN_WIDTH);
		setIntercellSpacing(new Dimension(0, 0));
		setRowHeight(ThreatGridPanel.ROW_HEIGHT);
		
		setDefaultRenderer(Object.class, new TableHeaderRenderer());
		getColumnModel().getColumn(0).setHeaderRenderer(new TableHeaderRenderer());
	
		JTableHeader rowHeader = getTableHeader();
		threatColumnSortListener = new ThreatNameColumnHandler(panel);
		rowHeader.addMouseListener(threatColumnSortListener);
		
		LookAndFeel.installColorsAndFont(this, "TableHeader.background",
				"TableHeader.foreground", "TableHeader.font");
	}
	
	public void columnMarginChanged(ChangeEvent e)    
	{
        super.columnMarginChanged(e);
		resetWidthToAllowResizeOfRowHeader();
    }

	private void resetWidthToAllowResizeOfRowHeader()
	{
		Dimension dimension =getPreferredScrollableViewportSize();
		dimension.width = getPreferredSize().width;
		setPreferredScrollableViewportSize(dimension);
	}
	
	public void sort(boolean sortOrder)
	{
		threatColumnSortListener.setToggle(sortOrder);
		threatColumnSortListener.sort(0);
	}
	
	private ThreatNameColumnHandler threatColumnSortListener;
}
