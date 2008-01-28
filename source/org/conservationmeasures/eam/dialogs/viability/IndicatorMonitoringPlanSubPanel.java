/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.actions.ActionEditIndicatorProgressReports;
import org.conservationmeasures.eam.actions.ActionEditMethods;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;
import org.conservationmeasures.eam.utils.ObjectsActionButton;

public class IndicatorMonitoringPlanSubPanel extends ObjectDataInputPanel
{
	public IndicatorMonitoringPlanSubPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		addField(createReadonlyTextField(Indicator.getObjectType(), Indicator.PSEUDO_TAG_FACTOR));
		
		ObjectsAction editMethods = EAM.getMainWindow().getActions().getObjectsAction(ActionEditMethods.class);
		PanelButton editMethodsButton = new ObjectsActionButton(editMethods, getPicker());
		addFieldWithEditButton(EAM.text("Label|Methods"), createReadonlyTextField(Indicator.getObjectType(), Indicator.PSEUDO_TAG_METHODS), editMethodsButton);

		addField(createRatingChoiceField(Indicator.getObjectType(), Indicator.TAG_PRIORITY, new PriorityRatingQuestion(Indicator.TAG_PRIORITY)));
	
		
		ObjectsActionButton editProgressReportButton = createObjectsActionButton(EAM.getMainWindow().getActions().getObjectsAction(ActionEditIndicatorProgressReports.class), getPicker());
		ObjectDataInputField readOnlyProgressReportsList = createReadOnlyObjectList(Indicator.getObjectType(), Indicator.TAG_PROGRESS_REPORT_REFS);
		addFieldWithEditButton(EAM.text("Progress Reports"), readOnlyProgressReportsList, editProgressReportButton);
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Monitoring Plan");
	}
}
