/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.reports;

import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.main.AppPreferences;
import org.miradi.main.Miradi;
import org.miradi.utils.MiradiScrollPane;

public abstract class ReportsViewControlBar extends Box
{
	public ReportsViewControlBar(ReportSplitPane ownerToUse)
	{
		super(BoxLayout.PAGE_AXIS);
		
		owner = ownerToUse;
		setBackground(AppPreferences.getControlPanelBackgroundColor());
		add(createReportSelectionPanel(getReportSelectionModel()));
		add(Box.createVerticalGlue());
	}
	
	private MiradiScrollPane createReportSelectionPanel(ReportSelectionTableModel standardReportTableModel)
	{
		standardReportTable = new ReportSelectionTable(standardReportTableModel);
		standardReportTable.getSelectionModel().addListSelectionListener(new TableSelectionListener(standardReportTable));

		MiradiScrollPane scroller = new MiradiScrollPane(standardReportTable);
		scroller.setBackground(AppPreferences.getControlPanelBackgroundColor());
		scroller.getViewport().setBackground(AppPreferences.getControlPanelBackgroundColor());
		scroller.setAlignmentX(0.0f);
		return scroller;
	}
	
	public void clearSelection()
	{
		standardReportTable.clearSelection();
	}
	
	protected ReportSplitPane getOwner()
	{
		return owner;
	}

	public class TableSelectionListener implements ListSelectionListener
	{
		public TableSelectionListener(ReportSelectionTable tableToListenTo)
		{
			table = tableToListenTo;
		}
		
		public void valueChanged(ListSelectionEvent event)
		{
			int selectedRow = table.getSelectedRow();
			if(selectedRow < 0)
				return;
			
			String reportPath = table.getReportDirForRow(selectedRow);
			URL reportURL = Miradi.class.getResource(reportPath);
			owner.showReport(reportURL);
		}

		private ReportSelectionTable table;
	}
	
	abstract protected ReportSelectionTableModel getReportSelectionModel();

	private ReportSplitPane owner;
	private ReportSelectionTable standardReportTable;
	
	public static final String EXTERNAL_REPORTS_DIR_NAME = "ExternalReports";	
}
