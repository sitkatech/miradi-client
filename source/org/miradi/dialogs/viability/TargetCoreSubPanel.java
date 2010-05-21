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
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.AbstractMiradiIcon;
import org.miradi.icons.HumanWelfareTargetIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.main.EAM;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.project.Project;
import org.miradi.questions.ViabilityModeQuestion;

public class TargetCoreSubPanel extends ObjectDataInputPanel
{
	public TargetCoreSubPanel(Project projectToUse, int targetType) throws Exception
	{
		super(projectToUse, targetType);
	
		ObjectDataInputField shortLabelField = createShortStringField(targetType, AbstractTarget.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(targetType, AbstractTarget.TAG_LABEL);
		addFieldsOnOneLine(getAbstractTargetLabel(targetType), getAbstractTargetIcon(targetType), new ObjectDataInputField[]{shortLabelField, labelField,});
		
		addField(createChoiceField(targetType, AbstractTarget.TAG_VIABILITY_MODE, new ViabilityModeQuestion()));
		
		updateFieldsFromProject();
	}

	private String getAbstractTargetLabel(int targetType)
	{
		if (HumanWelfareTarget.is(targetType))		
			return EAM.text("Human Welfare Target");	
		
		return EAM.text("Target");
	}

	private AbstractMiradiIcon getAbstractTargetIcon(int targetType)
	{
		if (HumanWelfareTarget.is(targetType))
			return new HumanWelfareTargetIcon();	
		
		return new TargetIcon();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Summary");
	}
}
