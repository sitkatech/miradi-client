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

package org.miradi.dialogs.base;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.DiagramObjectDataInclusionQuestion;

public class CodeListPanelWithDiagramObjectDataInclusionDropDown extends CodeListEditorPanel
{
	public CodeListPanelWithDiagramObjectDataInclusionDropDown(Project projectToUse, ORef orefToUse, String tagToUse, ChoiceQuestion question, int columnCount)
	{
		super(projectToUse, orefToUse, tagToUse, question, columnCount);
	}
	
	@Override
	protected void addExtraField()
	{
		addField(createChoiceField(PlanningViewConfiguration.getObjectType(), PlanningViewConfiguration.TAG_DIAGRAM_DATA_INCLUSION, getProject().getQuestion(DiagramObjectDataInclusionQuestion.class)));
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Diagram Data Inclusio Chooser");
	}
}
