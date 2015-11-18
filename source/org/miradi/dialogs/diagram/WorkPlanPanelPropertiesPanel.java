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

package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.utils.FillerLabel;

public class WorkPlanPanelPropertiesPanel extends ObjectDataInputPanel
{
	public WorkPlanPanelPropertiesPanel(Project projectToUse, ORef orefToUse) throws Exception
	{
		super(projectToUse, orefToUse);
		
		add(new PanelTitleLabel(EAM.text("Who")));
		addFieldWithoutLabel(createWhoEditorField(orefToUse));

		add(new FillerLabel());
		add(new FillerLabel());
		addField(createLeaderDropDownField(orefToUse.getObjectType(), BaseObject.TAG_PLANNED_LEADER_RESOURCE));
		addField(createReadonlyTextField(BaseObject.PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));
		
		updateFieldsFromProject();
	}

	@Override
	protected boolean doesSectionContainFieldWithTag(String tag)
	{
		if (tag.equals(CustomPlanningColumnsQuestion.META_ASSIGNED_WHO_TOTAL))
			return true;

		return super.doesSectionContainFieldWithTag(tag);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Who, When Plan");
	}
}
