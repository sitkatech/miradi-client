/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class ThreatMatrixTable extends JTable
{
	public ThreatMatrixTable(TableModel model, ThreatGridPanel panel)
	{
		super(model);
		setIntercellSpacing(new Dimension(0, 0));
		setRowHeight(ThreatGridPanel.ROW_HEIGHT);
		setRowSelectionAllowed(false);
		setColumnSelectionAllowed(true);
		setCellSelectionEnabled(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setColumnWidths();
		
		ListSelectionModel listSelectionModel = getSelectionModel();

		CellSelectionListener selectionListener = new CellSelectionListener(this, panel);
		listSelectionModel.addListSelectionListener(selectionListener);
		
		CustomTableCellRenderer customTableCellRenderer = new CustomTableCellRenderer(panel);
		setDefaultRenderer(Object.class, customTableCellRenderer);
	}

	public void columnMoved(TableColumnModelEvent event)
	{
		if(event.getToIndex() == getSummaryColumn())
			moveColumn(event.getToIndex(), event.getFromIndex());
		else
			super.columnMoved(event);
	}
	
	public int getSummaryColumn()
	{
		return getColumnCount() - 1;
	}

	private void setColumnWidths()
	{
		Graphics2D g2 = (Graphics2D)staticGraphics;
		Enumeration columns = getColumnModel().getColumns();
		while(columns.hasMoreElements())
		{
			TableColumn columnToAdjust = (TableColumn)columns.nextElement();
			String headerText = (String)columnToAdjust.getHeaderValue();
			int firstSpace = headerText.indexOf(' ');
			if(firstSpace >= 0)
				headerText = headerText.substring(0, firstSpace);
			
			if (headerText.length() == 0)
				headerText = "W";
			
			TextLayout textLayout = new TextLayout(headerText, g2.getFont(), g2.getFontRenderContext());
			int textWidth = textLayout.getBounds().getBounds().width;
			int width = Math.max(ThreatGridPanel.DEFAULT_COLUMN_WIDTH, textWidth + 20);
			columnToAdjust.setHeaderRenderer(new TableHeaderRenderer());
			columnToAdjust.setPreferredWidth(width);
			columnToAdjust.setWidth(width);
			columnToAdjust.setResizable(true);
		}
	}

	private final static Graphics staticGraphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();
}
