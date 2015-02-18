/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.dialogs.base;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class WorkPlanProjectResourceFilterEditorPanel extends ObjectDataInputPanel
{
	public WorkPlanProjectResourceFilterEditorPanel(Project projectToUse, ORef orefToUse, String tagToUse, ChoiceQuestion question)
	{
		super(projectToUse, orefToUse);
		
		addHtmlWrappedLabel("");
		addHtmlWrappedLabel(EAM.text("Selecting one or more resources below<br>" +
				"will filter the display to only include <br>" +
				"work unit and budget total values for <br>" +
				"items that are assigned to those resources."));
		addFieldWithCustomLabel(createStringMapProjectResourceFilterCodeListEditor(orefToUse.getObjectType(), tagToUse, question), EAM.text("Resources:"));
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Project Resource Filter Editor");
	}
}
