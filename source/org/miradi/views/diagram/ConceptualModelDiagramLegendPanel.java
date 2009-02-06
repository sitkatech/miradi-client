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

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.miradi.actions.ActionInsertContributingFactor;
import org.miradi.actions.ActionInsertDirectThreat;
import org.miradi.actions.Actions;
import org.miradi.icons.StressIcon;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.MainWindow;
import org.miradi.objects.Cause;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Strategy;

public class ConceptualModelDiagramLegendPanel extends DiagramLegendPanel
{
	public ConceptualModelDiagramLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	protected void createCustomLegendPanelSection(Actions actions, JPanel jpanel)
	{
		addButtonLineWithCheckBox(jpanel, Cause.getObjectType(), Cause.OBJECT_NAME_THREAT, actions.get(ActionInsertDirectThreat.class));
		addButtonLineWithCheckBox(jpanel, Cause.getObjectType(),  Cause.OBJECT_NAME_CONTRIBUTING_FACTOR, actions.get(ActionInsertContributingFactor.class));
	}
	
	protected void setLegendVisibilityOfFactorCheckBoxes(LayerManager manager, String property)
	{
		JCheckBox checkBox = (JCheckBox)checkBoxes.get(property);
		
		if (property.equals(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR))
			manager.setContributingFactorsVisible(checkBox.isSelected());
		else if (property.equals(Cause.OBJECT_NAME_THREAT))
			manager.setDirectThreatsVisible(checkBox.isSelected());
		
		else if (property.equals(Strategy.OBJECT_NAME_DRAFT))
			manager.setDraftStrategiesVisible(checkBox.isSelected());
		
		super.setLegendVisibilityOfFactorCheckBoxes(manager, property);
	}
	
	public void updateCheckBox(LayerManager manager, String property)
	{
		super.updateCheckBox(manager, property);
		
		JCheckBox checkBox = (JCheckBox)checkBoxes.get(property);
		
		if (property.equals(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR))
			checkBox.setSelected(manager.areContributingFactorsVisible());
	
		else if (property.equals(Cause.OBJECT_NAME_THREAT))
			checkBox.setSelected(manager.areDirectThreatsVisible());
		
		if (property.equals(Strategy.OBJECT_NAME_DRAFT))
			checkBox.setSelected(manager.areDraftStrategiesVisible());
	}
	
	protected void addStressLine(TwoColumnPanel jpanel)
	{
		addIconLineWithCheckBox(jpanel, FactorLink.getObjectType(), FactorLink.OBJECT_NAME_STRESS, new StressIcon());
	}
}
