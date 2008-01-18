/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionInsertIntermediateResult;
import org.conservationmeasures.eam.actions.ActionInsertThreatReductionResult;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.IntermediateResult;
import org.conservationmeasures.eam.objects.ThreatReductionResult;

public class ResultsChainDiagramLegendPanel extends DiagramLegendPanel
{
	public ResultsChainDiagramLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	protected void createCustomLegendPanelSection(Actions actions, JPanel jpanel)
	{
		addButtonLineWithCheckBox(jpanel, ThreatReductionResult.getObjectType(), ThreatReductionResult.OBJECT_NAME, actions.get(ActionInsertThreatReductionResult.class));
		addButtonLineWithCheckBox(jpanel, IntermediateResult.getObjectType(), IntermediateResult.OBJECT_NAME, actions.get(ActionInsertIntermediateResult.class));
	}
	
	protected void setLegendVisibilityOfFacactorCheckBoxes(LayerManager manager, String property)
	{		
		JCheckBox checkBox = (JCheckBox)checkBoxes.get(property);
		
		if (property.equals(IntermediateResult.OBJECT_NAME))
			manager.setIntermediateResultVisible(checkBox.isSelected());
		else if (property.equals(ThreatReductionResult.OBJECT_NAME))
			manager.setThreatReductionResultVisible(checkBox.isSelected());
		
		super.setLegendVisibilityOfFacactorCheckBoxes(manager, property);
	}
	
	public void updateCheckBox(LayerManager manager, String property)
	{
		super.updateCheckBox(manager, property);
		
		JCheckBox checkBox = (JCheckBox)checkBoxes.get(property);
		
		if (property.equals(IntermediateResult.OBJECT_NAME))
			checkBox.setSelected(manager.areIntermediateResultsVisible());
	
		else if (property.equals(ThreatReductionResult.OBJECT_NAME))
			checkBox.setSelected(manager.areThreatReductionResultsVisible());
	}

}
