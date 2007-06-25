/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionInsertContributingFactor;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;

public class ConceptualModelDiagramLegendPanel extends DiagramLegendPanel
{
	public ConceptualModelDiagramLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	protected void createCustomLegendPanelSection(Actions actions, JPanel jpanel)
	{
		addButtonLineWithCheckBox(jpanel, Cause.getObjectType(), Factor.OBJECT_NAME_THREAT, actions.get(ActionInsertDirectThreat.class));
		addButtonLineWithCheckBox(jpanel, Cause.getObjectType(),  Factor.OBJECT_NAME_CONTRIBUTING_FACTOR, actions.get(ActionInsertContributingFactor.class));
	}
	
	protected void setLegendVisibilityOfFacactorCheckBoxes(LayerManager manager, String property, JCheckBox checkBox)
	{
		if (property.equals(Factor.OBJECT_NAME_CONTRIBUTING_FACTOR))
			manager.setContributingFactorsVisible(checkBox.isSelected());
		else if (property.equals(Factor.OBJECT_NAME_THREAT))
			manager.setDirectThreatsVisible(checkBox.isSelected());
		
		super.setLegendVisibilityOfFacactorCheckBoxes(manager, property, checkBox);
	}
	
	public void updateCheckBoxes(LayerManager manager, String property, JCheckBox checkBox)
	{
		super.updateCheckBoxes(manager, property, checkBox);
		if (property.equals(Factor.OBJECT_NAME_CONTRIBUTING_FACTOR))
			checkBox.setSelected(manager.areContributingFactorsVisible());
	
		else if (property.equals(Factor.OBJECT_NAME_THREAT))
			checkBox.setSelected(manager.areDirectThreatsVisible());
	}
}
