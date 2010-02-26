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

package org.miradi.dialogs.viability;

import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.diagram.IndicatorSubPanel;
import org.miradi.dialogs.progressReport.ProgressReportSubPanel;
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

abstract public class AbstractIndicatorPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public AbstractIndicatorPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, Indicator.getObjectType());
		
		setLayout(new OneColumnGridLayout());
		addSubPanelWithTitledBorder(new IndicatorSubPanel(getProject(), getInvalidTargetRef()));
		
		viabilityRatingsSubPanel = new IndicatorViabilityRatingsSubPanel(getProject(), getInvalidTargetRef());
		addSubPanelWithTitledBorder(viabilityRatingsSubPanel);
		addSubPanelWithTitledBorder(new IndicatorMonitoringPlanSubPanel(getProject(), getInvalidTargetRef()));
		addSubPanelWithTitledBorder(new ProgressReportSubPanel(getMainWindow(), getPicker()));
		addSubPanelWithTitledBorder(new IndicatorFutureStatusSubPanel(getProject(), getInvalidTargetRef()));
		addBudgetSubPanels();
		
		updateFieldsFromProject();
	}

	protected void addBudgetSubPanels() throws Exception
	{
	}

	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		
		ORef foundRef = new ORefList(orefsToUse).getRefForType(KeyEcologicalAttribute.getObjectType());
		setTabEnabled(viabilityRatingsSubPanel.getPanelDescription(), foundRef.isValid());
	}
	
	private static ORef getInvalidTargetRef()
	{
		return new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt()));
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Indicator Properties");
	}
	
	private IndicatorViabilityRatingsSubPanel viabilityRatingsSubPanel;
}
