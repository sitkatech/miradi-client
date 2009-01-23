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
package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.GroupBox;
import org.miradi.project.Project;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;

public class GroupBoxPropertiesPanel extends ObjectDataInputPanel
{
	public GroupBoxPropertiesPanel(Project projectToUse, DiagramFactor diagramFactor)
	{
		super(projectToUse, ObjectType.GROUP_BOX, diagramFactor.getWrappedId());

		setObjectRefs(new ORef[] {diagramFactor.getWrappedORef(), diagramFactor.getRef()});
		
		addField(createStringField(GroupBox.TAG_LABEL));
		addField(createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_BACKGROUND_COLOR, new DiagramFactorBackgroundQuestion()));
		addField(createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FONT_SIZE, new DiagramFactorFontSizeQuestion()));
		addField(createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FOREGROUND_COLOR, new DiagramFactorFontColorQuestion()));
		addField(createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FONT_STYLE, new DiagramFactorFontStyleQuestion()));

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Group Box Properties");
	}
}
