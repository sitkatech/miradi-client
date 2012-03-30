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
package org.miradi.dialogs.diagram;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;
import org.miradi.schemas.ConceptualModelDiagramSchema;

public class ConceptualModelPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public ConceptualModelPropertiesPanel(Project projectToUse, ORef diagramObjectRef) throws Exception
	{
		super(projectToUse, diagramObjectRef);
		createSingleSection(EAM.text("CM Page"));
		
		ObjectDataInputField shortLabelField = createShortStringField(ConceptualModelDiagramSchema.getObjectType(), DiagramObject.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createMediumStringField(ConceptualModelDiagramSchema.getObjectType(), DiagramObject.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Page:"), new ObjectDataInputField[]{shortLabelField, labelField});
	
		addField(createMultilineField(ConceptualModelDiagramSchema.getObjectType(), DiagramObject.TAG_DETAIL));
		addField(createReadOnlyObjectList(ConceptualModelDiagramSchema.getObjectType(), ConceptualModelDiagram.PSEUDO_DRAFT_STRATEGY_REFS));

		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Diagram Properties");
	}
}
