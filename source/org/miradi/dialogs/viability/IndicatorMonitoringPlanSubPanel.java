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

import org.miradi.actions.ActionEditIndicatorProgressReports;
import org.miradi.actions.ActionEditMethods;
import org.miradi.actions.ObjectsAction;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.utils.ObjectsActionButton;

public class IndicatorMonitoringPlanSubPanel extends ObjectDataInputPanel
{
	public IndicatorMonitoringPlanSubPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		addField(createReadonlyTextField(Indicator.getObjectType(), Indicator.PSEUDO_TAG_FACTOR));
		
		ObjectsAction editMethods = getMainWindow().getActions().getObjectsAction(ActionEditMethods.class);
		editMethodsButton = new ObjectsActionButton(editMethods, getPicker());
		addFieldWithEditButton(EAM.text("Label|Methods"), createReadonlyTextField(Indicator.getObjectType(), Indicator.PSEUDO_TAG_METHODS), editMethodsButton);

		addField(createRatingChoiceField(Indicator.getObjectType(), Indicator.TAG_PRIORITY, new PriorityRatingQuestion()));
	
		
		ObjectsAction editProgressAction = getMainWindow().getActions().getObjectsAction(ActionEditIndicatorProgressReports.class);
		editProgressReportButton = createObjectsActionButton(editProgressAction, getPicker());
		ObjectDataInputField readOnlyProgressReportsList = createReadOnlyObjectList(Indicator.getObjectType(), Indicator.TAG_PROGRESS_REPORT_REFS);
		addFieldWithEditButton(EAM.text("Progress Reports"), readOnlyProgressReportsList, editProgressReportButton);
		
		updateFieldsFromProject();
	}
	
	@Override
	public void dispose()
	{
		editMethodsButton.dispose();
		editProgressReportButton.dispose();
		super.dispose();
	}

	public String getPanelDescription()
	{
		return EAM.text("Monitoring Plan");
	}
	
	private ObjectsActionButton editMethodsButton;
	private ObjectsActionButton editProgressReportButton;
}
