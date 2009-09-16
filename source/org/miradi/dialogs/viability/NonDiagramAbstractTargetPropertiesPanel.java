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

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.main.EAM;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.project.Project;
import org.miradi.questions.StatusQuestion;

public class NonDiagramAbstractTargetPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public NonDiagramAbstractTargetPropertiesPanel(Project projectToUse, int targetTypeToUse)
	{
		super(projectToUse, targetTypeToUse);
		
		targetType = targetTypeToUse;
		
		TargetCoreSubPanel targetCoreSubPanel = new TargetCoreSubPanel(getProject(), targetType);
		addSubPanelWithTitledBorder(targetCoreSubPanel);
		
		createSingleSection(EAM.text("Status"));
		addFieldsOnOneLine("", new ObjectDataInputField[]{createReadOnlyStatusField()});
		
		ModelessTargetSubPanel modelessTargetSubPanel = new ModelessTargetSubPanel(getProject(), targetType);
		addSubPanelWithTitledBorder(modelessTargetSubPanel);
		
		updateFieldsFromProject();
	}
	
	private ObjectDataInputField createReadOnlyStatusField()
	{
		return createReadOnlyChoiceField(targetType, AbstractTarget.PSEUDO_TAG_TARGET_VIABILITY,getProject().getQuestion(StatusQuestion.class));
	} 
	
	@Override
	public String getPanelDescription()
	{
		if (HumanWelfareTarget.is(targetType))
			return EAM.text("Title|Human Welfare Target Properties");

		return EAM.text("Title|Target Properties");
	}
	
	private int targetType;
}
