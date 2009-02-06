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
package org.miradi.dialogs.progressReport;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objects.ProgressReport;
import org.miradi.project.Project;
import org.miradi.questions.ProgressReportStatusQuestion;

public class ProgressReportPropertiesPanel extends ObjectDataInputPanel
{
	public ProgressReportPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ProgressReport.getObjectType(), BaseId.INVALID);
			
		ProgressReportStatusQuestion progressReportStatusQuestion = new ProgressReportStatusQuestion();
		addField(createDateChooserField(ProgressReport.TAG_PROGRESS_DATE));
		addField(createChoiceField(ProgressReport.getObjectType(), ProgressReport.TAG_PROGRESS_STATUS, progressReportStatusQuestion));
		addField(createMultilineField(ProgressReport.TAG_DETAILS));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Progress Report Properties");
	}
}
