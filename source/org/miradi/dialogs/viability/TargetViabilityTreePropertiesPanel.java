/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import java.awt.CardLayout;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.dialogs.base.ObjectDataInputPanelSpecial;
import org.miradi.dialogs.planning.MeasurementPropertiesPanel;
import org.miradi.dialogs.planning.propertiesPanel.BlankPropertiesPanel;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;

public class TargetViabilityTreePropertiesPanel extends ObjectDataInputPanelSpecial
{
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt())));		
				
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		
		blankPropertiesPanel = new BlankPropertiesPanel();
		targetViabilityKeaPropertiesPanel = new TargetViabilityKeaPropertiesPanel(projectToUse, actions);
		targetViabilityIndicatorPropertiesPanel = new IndicatorPropertiesPanel(projectToUse, actions);
		targetViabilityMeasurementPropertiesPanel = new MeasurementPropertiesPanel(projectToUse);
		futureStatusPropertiesPanel = new IndicatorFutureStatusSubPanel(projectToUse);
		add(blankPropertiesPanel);
		add(targetViabilityKeaPropertiesPanel);
		add(targetViabilityIndicatorPropertiesPanel);
		add(targetViabilityMeasurementPropertiesPanel);
		add(futureStatusPropertiesPanel);

		updateFieldsFromProject();
	}
	
	public void dispose()
	{
		super.dispose();
		blankPropertiesPanel.dispose();
		targetViabilityKeaPropertiesPanel.dispose();
		targetViabilityIndicatorPropertiesPanel.dispose();
		targetViabilityMeasurementPropertiesPanel.dispose();
		futureStatusPropertiesPanel.dispose();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Target Viability Properties");
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		String panelDescription = findPanel(orefsToUse).getPanelDescription();
		cardLayout.show(this, panelDescription);

		targetViabilityKeaPropertiesPanel.setObjectRefs(orefsToUse);
		targetViabilityIndicatorPropertiesPanel.setObjectRefs(orefsToUse);
		targetViabilityMeasurementPropertiesPanel.setObjectRefs(orefsToUse);
		futureStatusPropertiesPanel.setObjectRefs(orefsToUse);
	}
	
	private DisposablePanelWithDescription findPanel(ORef[] orefsToUse)
	{
		if(orefsToUse.length == 0)
			return blankPropertiesPanel;
		
		int objectType = orefsToUse[0].getObjectType();
		if(objectType == KeyEcologicalAttribute.getObjectType())
			return targetViabilityKeaPropertiesPanel;
		if(objectType == Indicator.getObjectType())
			return targetViabilityIndicatorPropertiesPanel;
		if(objectType == Measurement.getObjectType())
			return targetViabilityMeasurementPropertiesPanel;
		if(objectType == Goal.getObjectType())
			return futureStatusPropertiesPanel;

		return blankPropertiesPanel;
	}
	
	private void add(DisposablePanelWithDescription panelToAdd)
	{
		add(panelToAdd, panelToAdd.getPanelDescription());
	}
	
	private CardLayout cardLayout;
	private BlankPropertiesPanel blankPropertiesPanel;
	private TargetViabilityKeaPropertiesPanel targetViabilityKeaPropertiesPanel;
	private IndicatorPropertiesPanel targetViabilityIndicatorPropertiesPanel;
	private MeasurementPropertiesPanel targetViabilityMeasurementPropertiesPanel;
	private IndicatorFutureStatusSubPanel futureStatusPropertiesPanel;
}
