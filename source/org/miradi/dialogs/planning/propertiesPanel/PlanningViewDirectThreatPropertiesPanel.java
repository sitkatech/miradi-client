/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.icons.DirectThreatIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Cause;
import org.miradi.project.Project;
import org.miradi.questions.ThreatClassificationQuestionV11;
import org.miradi.questions.ThreatClassificationQuestionV20;
import org.miradi.schemas.CauseSchema;

public class PlanningViewDirectThreatPropertiesPanel extends MinimalFactorPropertiesPanel
{
	public PlanningViewDirectThreatPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, projectToUse.getObjectManager().getSchemas().get(ObjectType.CAUSE));
		
		createAndAddFields(EAM.text("Threat"), new DirectThreatIcon());
	}
	
	@Override
	protected void addCustomFieldsStart()
	{
		super.addCustomFieldsStart();
		
		addField(createRadioButtonEditorFieldWithHierarchies(CauseSchema.getObjectType(), Cause.TAG_STANDARD_CLASSIFICATION_V11_CODE, new ThreatClassificationQuestionV11()));
		addField(createRadioButtonEditorFieldWithHierarchies(CauseSchema.getObjectType(), Cause.TAG_STANDARD_CLASSIFICATION_V20_CODE, new ThreatClassificationQuestionV20()));
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Direct Threat Properties");
	}

}
