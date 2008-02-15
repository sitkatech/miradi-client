/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;

public class DiagramPropertiesPanel extends ObjectDataInputPanel
{
	public DiagramPropertiesPanel(Project projectToUse, ORef diagramObjectRef)
	{
		super(projectToUse, diagramObjectRef);
		
		ObjectDataInputField shortLabelField = createShortStringField(DiagramObject.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createMediumStringField(DiagramObject.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Page:"), new ObjectDataInputField[]{shortLabelField, labelField});
	
		addField(createMultilineField(DiagramObject.TAG_DETAIL));

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Diagram Properties");
	}
}
