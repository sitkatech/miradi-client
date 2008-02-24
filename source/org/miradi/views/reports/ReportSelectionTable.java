/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import org.miradi.main.AppPreferences;
import org.miradi.utils.TableWithColumnWidthSaver;

public class ReportSelectionTable extends TableWithColumnWidthSaver
{
	public ReportSelectionTable(TableModel model)
	{
		super(model);
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setBackground(AppPreferences.getControlPanelBackgroundColor());
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		Dimension size = super.getPreferredScrollableViewportSize();
		size.width = getPreferredSize().width;
		size.height = getPreferredSize().height;
		return size;
	}
	
	@Override
	public String getUniqueTableIdentifier()
	{
		return "ReportSelectionTable";
	}
}
