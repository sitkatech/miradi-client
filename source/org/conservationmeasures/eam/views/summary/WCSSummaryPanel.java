/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.WcsProjectData;
import org.conservationmeasures.eam.project.Project;

public class WCSSummaryPanel extends ObjectDataInputPanel
{

	public WCSSummaryPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(WcsProjectData.getObjectType()));

		addField(createStringField(WcsProjectData.TAG_ORGANIZATIONAL_FOCUS));
		addField(createStringField(WcsProjectData.TAG_ORGANIZATIONAL_LEVEL));
		
		ObjectDataInputField swotCompletedField = createCheckBoxField(WcsProjectData.TAG_SWOT_COMPLETED);
		ObjectDataInputField swotUrlField = createStringField(WcsProjectData.TAG_SWOT_URL);
		addFieldsOnOneLine(EAM.text("SWOT"), new ObjectDataInputField[]{swotCompletedField, swotUrlField});
		
		ObjectDataInputField stepCompletedField = createCheckBoxField(WcsProjectData.TAG_STEP_COMPLETED);
		ObjectDataInputField stepUrlField = createStringField(WcsProjectData.TAG_STEP_URL);
		addFieldsOnOneLine(EAM.text("STEP"), new ObjectDataInputField[]{stepCompletedField, stepUrlField});
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Label|WCS");
	}

}
