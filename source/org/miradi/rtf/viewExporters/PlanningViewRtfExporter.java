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
package org.miradi.rtf.viewExporters;

import org.miradi.dialogs.accountingcode.AccountingCodePoolTableModel;
import org.miradi.dialogs.fundingsource.FundingSourcePoolTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.resource.ResourcePoolTableModel;
import org.miradi.main.MainWindow;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.AbstractTableExporter;

public class PlanningViewRtfExporter extends RtfViewExporter
{
	public PlanningViewRtfExporter(MainWindow mainWindow)
	{
		super(mainWindow);
	}
	
	@Override
	public void ExportView(RtfWriter writer) throws Exception
	{
		exportPlanningTab(writer);
		exportResourcesTab(writer);
		exportAccountingCodesTab(writer);
		exportFundingSourceTab(writer);
	}

	private void exportPlanningTab(RtfWriter writer) throws Exception
	{		
		PlanningTreeTablePanel panel = PlanningTreeTablePanel.createPlanningTreeTablePanelWithoutButtons(getMainWindow());
		AbstractTableExporter table = panel.getTableForExporting();
		panel.dispose();
		
		exportTable(writer, table);
	}

	private void exportResourcesTab(RtfWriter writer) throws Exception
	{
		exportObjectTableModel(writer, new ResourcePoolTableModel(getProject()));
	}

	private void exportAccountingCodesTab(RtfWriter writer) throws Exception
	{
		exportObjectTableModel(writer, new AccountingCodePoolTableModel(getProject()));
	}

	private void exportFundingSourceTab(RtfWriter writer) throws Exception
	{
		exportObjectTableModel(writer, new FundingSourcePoolTableModel(getProject()));
	}
}
