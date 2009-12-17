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
package org.miradi.views.diagram;

import javax.swing.JPanel;

import org.miradi.actions.ActionInsertContributingFactor;
import org.miradi.actions.ActionInsertDirectThreat;
import org.miradi.actions.Actions;
import org.miradi.icons.StressIcon;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.MainWindow;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Stress;

public class ConceptualModelDiagramLegendPanel extends DiagramLegendPanel
{
	public ConceptualModelDiagramLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	@Override
	protected void createCustomLegendPanelSection(Actions actions, JPanel jpanel)
	{
		addButtonLineWithCheckBox(jpanel, Cause.getObjectType(), Cause.OBJECT_NAME_THREAT, actions.get(ActionInsertDirectThreat.class));
		addButtonLineWithCheckBox(jpanel, Cause.getObjectType(),  Cause.OBJECT_NAME_CONTRIBUTING_FACTOR, actions.get(ActionInsertContributingFactor.class));
	}
	
	@Override
	protected void addStressLine(TwoColumnPanel jpanel)
	{
		addIconLineWithCheckBox(jpanel, FactorLink.getObjectType(), Stress.OBJECT_NAME, new StressIcon());
	}
	
	@Override
	protected int getDiagramType()
	{
		return ConceptualModelDiagram.getObjectType();
	}
}
