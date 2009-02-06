/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.summary;

import javax.swing.Icon;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitledBorder;
import org.miradi.forms.summary.PlanningTabForm;
import org.miradi.icons.PlanningIcon;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.rtf.RtfFormExporter;
import org.miradi.rtf.RtfWriter;

public class SummaryPlanningPanel extends ObjectDataInputPanel
{
	public SummaryPlanningPanel(MainWindow mainWindowToUse, ORef orefToUse)
	{
		super(mainWindowToUse.getProject(), orefToUse);
		setLayout(new OneColumnGridLayout());

		SummaryPlanningWorkPlanSubPanel workPlanSubPanel = new SummaryPlanningWorkPlanSubPanel(mainWindowToUse.getProject(), orefToUse);
		workPlanSubPanel.setBorder(new PanelTitledBorder(EAM.text("Workplan")));
		addSubPanel(workPlanSubPanel);
		add(workPlanSubPanel);
		
		SummaryPlanningFinancialSubPanel financialSubPanel = new SummaryPlanningFinancialSubPanel(mainWindowToUse);
		financialSubPanel.setBorder(new PanelTitledBorder(EAM.text("Financial")));
		addSubPanel(financialSubPanel);
		add(financialSubPanel);
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Planning");
	}
	
	@Override
	public Icon getIcon()
	{
		return new PlanningIcon();
	}
	
	@Override
	public boolean isRtfExportable()
	{
		return true;
	}
	
	@Override
	public void exportRtf(RtfWriter writer) throws Exception
	{
		RtfFormExporter rtfFormExporter = new RtfFormExporter(getProject(), writer, getSelectedRefs());
		rtfFormExporter.exportForm(new PlanningTabForm());
	}
}
