/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import javax.swing.BorderFactory;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.layout.OneColumnGridLayout;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class TargetViabilityIndicatorPropertiesPanel extends ObjectDataInputPanel
{
	public TargetViabilityIndicatorPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, getInvalidTargetRef());			
		setLayout(new OneColumnGridLayout());
		
		addSubIndicatorPanel(new TargetViabilityIndicatorSubPanel(projectToUse, getInvalidTargetRef()));
		addSubIndicatorPanel(new IndicatorViabilityRatingsSubPanel(projectToUse, getInvalidTargetRef()));
		addSubIndicatorPanel(new IndicatorFutureStatusSubPanel(projectToUse, getInvalidTargetRef()));
		addSubIndicatorPanel(new IndicatorMonitoringPlanSubPanel(projectToUse, getInvalidTargetRef()));
		
		updateFieldsFromProject();
	}

	private void addSubIndicatorPanel(ObjectDataInputPanel subPanel)
	{
		subPanel.setBorder(BorderFactory.createTitledBorder(subPanel.getPanelDescription()));
		addSubPanel(subPanel);
		add(subPanel);
	}

	private static ORef getInvalidTargetRef()
	{
		return new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt()));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Indicator Properties");
	}
}
