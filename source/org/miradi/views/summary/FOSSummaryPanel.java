/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.summary;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objects.FosProjectData;
import org.miradi.project.Project;
import org.miradi.questions.FosTrainingTypeQuestion;

public class FOSSummaryPanel extends ObjectDataInputPanel
{
	public FOSSummaryPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(FosProjectData.getObjectType()));
		
		addField(createChoiceField(FosProjectData.getObjectType(), FosProjectData.TAG_TRAINING_TYPE, new FosTrainingTypeQuestion()));
		addField(createStringField(FosProjectData.TAG_TRAINING_DATES));
		addField(createStringField(FosProjectData.TAG_TRAINERS));
		addField(createStringField(FosProjectData.TAG_COACHES));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Label|FOS");
	}
}
