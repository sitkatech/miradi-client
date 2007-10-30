/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanelSpecial;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class TargetViabilityTreePropertiesPanel extends ObjectDataInputPanelSpecial
{
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt())));		
				
		JPanel mainPropertiesPanel = createGridLayoutPanel(4, 1);
		targetViabilityKeaPropertiesPanel = new TargetViabilityKeaPropertiesPanel(projectToUse, actions);
		targetViabilityIndicatorPropertiesPanel = new TargetViabilityIndicatorPropertiesPanel(projectToUse, actions);
		targetViabilityMeasurementPropertiesPanel = new TargetViabilityMeasurementPropertiesPanel(projectToUse, actions);
		targetViabilityFutureStatusPropertiesPanel = new TargetViabilityFutureStatusPropertiesPanel(projectToUse, actions);
		mainPropertiesPanel.add(targetViabilityKeaPropertiesPanel);
		mainPropertiesPanel.add(targetViabilityIndicatorPropertiesPanel);
		mainPropertiesPanel.add(targetViabilityMeasurementPropertiesPanel);
		mainPropertiesPanel.add(targetViabilityFutureStatusPropertiesPanel);

		addFieldComponent(mainPropertiesPanel);
		updateFieldsFromProject();
	}
	
	public void dispose()
	{
		super.dispose();
		targetViabilityKeaPropertiesPanel.dispose();
		targetViabilityIndicatorPropertiesPanel.dispose();
		targetViabilityMeasurementPropertiesPanel.dispose();
		targetViabilityFutureStatusPropertiesPanel.dispose();
	}
	
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		targetViabilityKeaPropertiesPanel.setObjectRefs(orefsToUse);
		targetViabilityIndicatorPropertiesPanel.setObjectRefs(orefsToUse);
		targetViabilityMeasurementPropertiesPanel.setObjectRefs(orefsToUse);
		targetViabilityFutureStatusPropertiesPanel.setObjectRefs(orefsToUse);
	}
	
	private TargetViabilityKeaPropertiesPanel targetViabilityKeaPropertiesPanel;
	private TargetViabilityIndicatorPropertiesPanel targetViabilityIndicatorPropertiesPanel;
	private TargetViabilityMeasurementPropertiesPanel targetViabilityMeasurementPropertiesPanel;
	private TargetViabilityFutureStatusPropertiesPanel targetViabilityFutureStatusPropertiesPanel;
}
