/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import java.awt.Component;

import javax.swing.Icon;

import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitledBorder;
import org.miradi.icons.PlanningIcon;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class SummaryPlanningPanel extends AbstractObjectDataInputPanel
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

	public void addFieldComponent(Component component)
	{
	}
}
