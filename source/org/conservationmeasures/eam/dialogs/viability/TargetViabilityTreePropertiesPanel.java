/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.awt.CardLayout;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.DisposablePanelWithDescription;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanelSpecial;
import org.conservationmeasures.eam.dialogs.planning.MeasurementPropertiesPanel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.BlankPropertiesPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;

public class TargetViabilityTreePropertiesPanel extends ObjectDataInputPanelSpecial
{
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt())));		
				
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		
		blankPropertiesPanel = new BlankPropertiesPanel();
		targetViabilityKeaPropertiesPanel = new TargetViabilityKeaPropertiesPanel(projectToUse, actions);
		targetViabilityIndicatorPropertiesPanel = new TargetViabilityIndicatorPropertiesPanel(projectToUse, actions);
		targetViabilityMeasurementPropertiesPanel = new MeasurementPropertiesPanel(projectToUse);
		add(blankPropertiesPanel);
		add(targetViabilityKeaPropertiesPanel);
		add(targetViabilityIndicatorPropertiesPanel);
		add(targetViabilityMeasurementPropertiesPanel);

		updateFieldsFromProject();
	}
	
	public void dispose()
	{
		super.dispose();
		blankPropertiesPanel.dispose();
		targetViabilityKeaPropertiesPanel.dispose();
		targetViabilityIndicatorPropertiesPanel.dispose();
		targetViabilityMeasurementPropertiesPanel.dispose();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Target Viability Properties");
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		String panelDescription = findPanel(orefsToUse).getPanelDescription();
		System.out.println(panelDescription);
		cardLayout.show(this, panelDescription);

		targetViabilityKeaPropertiesPanel.setObjectRefs(orefsToUse);
		targetViabilityIndicatorPropertiesPanel.setObjectRefs(orefsToUse);
		targetViabilityMeasurementPropertiesPanel.setObjectRefs(orefsToUse);
	}
	
	private DisposablePanelWithDescription findPanel(ORef[] orefsToUse)
	{
		if(orefsToUse.length == 0)
			return blankPropertiesPanel;

		ORefList refs = new ORefList(orefsToUse);
		System.out.println(refs);
		int objectType = orefsToUse[0].getObjectType();
		if(objectType == KeyEcologicalAttribute.getObjectType())
			return targetViabilityKeaPropertiesPanel;
		if(objectType == Indicator.getObjectType())
			return targetViabilityIndicatorPropertiesPanel;
		if(objectType == Measurement.getObjectType())
			return targetViabilityMeasurementPropertiesPanel;

		return blankPropertiesPanel;
	}
	
	private void add(DisposablePanelWithDescription panelToAdd)
	{
		add(panelToAdd, panelToAdd.getPanelDescription());
	}
	
	private CardLayout cardLayout;
	private BlankPropertiesPanel blankPropertiesPanel;
	private TargetViabilityKeaPropertiesPanel targetViabilityKeaPropertiesPanel;
	private TargetViabilityIndicatorPropertiesPanel targetViabilityIndicatorPropertiesPanel;
	private MeasurementPropertiesPanel targetViabilityMeasurementPropertiesPanel;
}
