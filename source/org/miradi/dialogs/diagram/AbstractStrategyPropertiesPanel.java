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

package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.progressReport.ProgressReportSubPanel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.views.umbrella.ObjectPicker;

abstract public class AbstractStrategyPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public AbstractStrategyPropertiesPanel(Project projectToUse, ObjectPicker picker) throws Exception
	{
		super(projectToUse, Strategy.getObjectType());
		
		setLayout(new OneColumnGridLayout());
		
		addSubPanelWithTitledBorder(new StrategyCoreSubpanel(getProject(), getMainWindow().getActions(), Strategy.getObjectType()));
		addSubPanelWithTitledBorder(new RelatedItemsSubpanel(getProject(), Strategy.getObjectType()));
		addSubPanelWithTitledBorder(new FactorSummaryCommentsPanel(getProject(), getMainWindow().getActions(), Strategy.getObjectType()));
		addSubPanelWithTitledBorder(new ProgressReportSubPanel(getMainWindow(), getPicker()));
		addBudgetSubPanels(picker);
		
		updateFieldsFromProject();
	}
	
	protected void addBudgetSubPanels(ObjectPicker picker) throws Exception
	{
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Strategy Properties");
	}
}
