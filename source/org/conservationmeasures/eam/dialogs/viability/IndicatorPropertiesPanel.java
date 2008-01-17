/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.diagram.IndicatorSubPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.layout.OneColumnGridLayout;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class IndicatorPropertiesPanel extends ObjectDataInputPanel
{
	public IndicatorPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, getInvalidTargetRef());			
		setLayout(new OneColumnGridLayout());
		
		addSubPanelWithTitledBorder(new IndicatorSubPanel(projectToUse, getInvalidTargetRef()));
		addSubPanelWithTitledBorder(new IndicatorViabilityRatingsSubPanel(projectToUse, getInvalidTargetRef()));
		addSubPanelWithTitledBorder(new IndicatorFutureStatusSubPanel(projectToUse, getInvalidTargetRef()));
		addSubPanelWithTitledBorder(new IndicatorMonitoringPlanSubPanel(projectToUse, getInvalidTargetRef()));
		
		updateFieldsFromProject();
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
