/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.progressReport;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProgressReport;
import org.conservationmeasures.eam.project.Project;

public class ProgressReportPropertiesPanel extends ObjectDataInputPanel
{
	public ProgressReportPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ProgressReport.getObjectType(), BaseId.INVALID);
			
		addField(createMultilineField(ProgressReport.TAG_LABEL));
		//FIXME add prgress question
		//addField(createChoiceField(ProgressReport.TAG_PROGRESS_STATUS, question));
		addField(createDateChooserField(ProgressReport.TAG_PROGRESS_DATE));
		addField(createMultilineField(ProgressReport.TAG_COMMENTS));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Progress Report Properties");
	}
}
