/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.diagram;


import org.miradi.dialogs.assignment.AssignmentsPropertiesSubPanel;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.output.StrategyOutputSubPanel;
import org.miradi.dialogs.progressReport.ProgressReportSubPanel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.schemas.StrategySchema;

public class StrategyPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public StrategyPropertiesPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow.getProject(), StrategySchema.getObjectType());

		setLayout(new OneColumnGridLayout());

		addSubPanelWithTitledBorder(new StrategyCoreSubPanel(getProject(), getMainWindow().getActions(), StrategySchema.getObjectType()));
		addSubPanelWithTitledBorder(new RelatedItemsSubpanel(getProject(), StrategySchema.getObjectType()));
		addSubPanelWithTitledBorder(new FactorSummaryCommentsPanel(getProject(), getProject().getObjectManager().getSchemas().get(ObjectType.STRATEGY)));
		addSubPanelWithTitledBorder(new ProgressReportSubPanel(getMainWindow()));
		addSubPanelWithTitledBorder(new TimeframePropertiesSubPanel(getProject(), ORef.createInvalidWithType(StrategySchema.getObjectType())));
		addSubPanelWithTitledBorder(new AssignmentsPropertiesSubPanel(getMainWindow(), StrategySchema.getObjectType(), getPicker()));
		addSubPanelWithTitledBorder(new StrategyOutputSubPanel(getMainWindow()));

		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Strategy Properties");
	}
}
