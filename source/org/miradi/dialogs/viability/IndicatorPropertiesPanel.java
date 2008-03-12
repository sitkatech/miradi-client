/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.diagram.ForecastOverrideSubPanel;
import org.miradi.dialogs.diagram.IndicatorSubPanel;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.project.Project;

public class IndicatorPropertiesPanel extends ObjectDataInputPanel
{
	public IndicatorPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, getInvalidTargetRef());			
		setLayout(new OneColumnGridLayout());
		
		addSubPanelWithTitledBorder(new IndicatorSubPanel(projectToUse, getInvalidTargetRef()));
		
		viabilityRatingsSubPanel = new IndicatorViabilityRatingsSubPanel(projectToUse, getInvalidTargetRef());
		addSubPanelWithTitledBorder(viabilityRatingsSubPanel);
		addSubPanelWithTitledBorder(new IndicatorFutureStatusSubPanel(projectToUse, getInvalidTargetRef()));
		addSubPanelWithTitledBorder(new IndicatorMonitoringPlanSubPanel(projectToUse, getInvalidTargetRef()));
		addSubPanelWithTitledBorder(new ForecastOverrideSubPanel(getProject(), actions, new ORef(Indicator.getObjectType(), BaseId.INVALID)));
		
		updateFieldsFromProject();
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		
		viabilityRatingsSubPanel.setVisible(true);
		ORef foundRef = new ORefList(orefsToUse).getRefForType(KeyEcologicalAttribute.getObjectType());
		if (foundRef.isInvalid())
			viabilityRatingsSubPanel.setVisible(false);
	}
	
	private static ORef getInvalidTargetRef()
	{
		return new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt()));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Indicator Properties");
	}
	
	private IndicatorViabilityRatingsSubPanel viabilityRatingsSubPanel;
}
