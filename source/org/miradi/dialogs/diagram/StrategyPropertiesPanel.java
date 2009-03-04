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
import org.miradi.ids.BaseId;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;

public class StrategyPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public StrategyPropertiesPanel(MainWindow mainWindow)
	{
		super(mainWindow.getProject(), Strategy.getObjectType());
		setLayout(new OneColumnGridLayout());
		
		addSubPanelWithTitledBorder(new StrategyCoreSubpanel(getProject(), mainWindow.getActions(), Strategy.getObjectType()));
		
		ForecastSubPanel budgetSubPanel = new ForecastSubPanel(mainWindow, new ORef(Strategy.getObjectType(), BaseId.INVALID));
		addSubPanelWithTitledBorder(budgetSubPanel);

		addSubPanelWithTitledBorder(new RelatedItemsSubpanel(getProject(), Strategy.getObjectType()));
		
		addSubPanelWithTitledBorder(new FactorSummaryCommentsPanel(getProject(), mainWindow.getActions(), Strategy.getObjectType()));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Strategy Properties");
	}

}
