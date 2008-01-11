/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.project.Project;

public class DiagramPropertiesPanel extends ObjectDataInputPanel
{
	public DiagramPropertiesPanel(Project projectToUse, ORef diagramObjectRef)
	{
		super(projectToUse, diagramObjectRef);
		
		ObjectDataInputField shortLabelField = createShortStringField(DiagramObject.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createStringField(DiagramObject.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Page:"), new ObjectDataInputField[]{shortLabelField, labelField});
	
		addField(createMultilineField(DiagramObject.TAG_DETAIL));

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Diagram Properties");
	}
}
