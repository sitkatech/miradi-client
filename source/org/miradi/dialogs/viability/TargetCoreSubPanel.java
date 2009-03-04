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
import org.miradi.icons.TargetIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.ViabilityModeQuestion;

public class TargetCoreSubPanel extends ObjectDataInputPanel
{
	public TargetCoreSubPanel(Project projectToUse)
	{
		super(projectToUse, Target.getObjectType());
	
		ObjectDataInputField shortLabelField = createShortStringField(Target.getObjectType(), Target.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(Target.getObjectType(), Target.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Target"), new TargetIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});
		
		addField(createChoiceField(ObjectType.TARGET, Target.TAG_VIABILITY_MODE, new ViabilityModeQuestion()));
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Summary");
	}
}
