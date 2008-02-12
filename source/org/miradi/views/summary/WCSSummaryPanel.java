/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objects.WcsProjectData;
import org.miradi.project.Project;

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
