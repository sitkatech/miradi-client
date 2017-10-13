/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.dialogs.strategicPlan;

import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.diagram.IndicatorSubPanel;
import org.miradi.dialogs.progressReport.ProgressReportSubPanel;
import org.miradi.dialogs.viability.IndicatorMonitoringPlanSubPanel;
import org.miradi.dialogs.viability.IndicatorViabilityRatingsSubPanel;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.Project;
import org.miradi.schemas.IndicatorSchema;

public class IndicatorPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public IndicatorPropertiesPanel(Project projectToUse, MainWindow mainWindow) throws Exception
	{
		super(projectToUse, IndicatorSchema.getObjectType());

		setLayout(new OneColumnGridLayout());
		addSubPanelWithTitledBorder(new IndicatorSubPanel(getProject(), getInvalidTargetRef(), mainWindow.getActions()));

		viabilityRatingsSubPanel = new IndicatorViabilityRatingsSubPanel(getProject(), getInvalidTargetRef());
		addSubPanelWithTitledBorder(viabilityRatingsSubPanel);
		addSubPanelWithTitledBorder(new IndicatorMonitoringPlanSubPanel(getProject(), getInvalidTargetRef()));
		addSubPanelWithTitledBorder(new ProgressReportSubPanel(getMainWindow()));

		updateFieldsFromProject();
	}

	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		setTabEnabled(viabilityRatingsSubPanel.getPanelDescription(), viabilityRatingsSubPanel.shouldBeEnabled());
	}

	private static ORef getInvalidTargetRef()
	{
		return new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt()));
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Indicator Properties");
	}

	private IndicatorViabilityRatingsSubPanel viabilityRatingsSubPanel;
}
