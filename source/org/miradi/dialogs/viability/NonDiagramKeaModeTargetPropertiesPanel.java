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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.StatusQuestion;

public class NonDiagramKeaModeTargetPropertiesPanel extends AbstractNonDiagramModelessTargetPropertiesPanel
{
	public NonDiagramKeaModeTargetPropertiesPanel(Project projectToUse)
	{
		super(projectToUse, Target.getObjectType());
	}

	protected ObjectDataInputField createStatusField()
	{
		return createReadOnlyChoiceField(ObjectType.TARGET, Target.PSEUDO_TAG_TARGET_VIABILITY, new StatusQuestion());
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Key Attribute Mode Target Properties");
	}
}
