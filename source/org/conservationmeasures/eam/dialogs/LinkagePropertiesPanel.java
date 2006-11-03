/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.project.Project;

public class LinkagePropertiesPanel extends ObjectDataInputPanel
{
	public LinkagePropertiesPanel(Project projectToUse, ModelLinkageId objectIdToUse)
	{
		super(projectToUse, ObjectType.MODEL_LINKAGE, objectIdToUse);

		addField(createStringField(ConceptualModelLinkage.TAG_STRESS_LABEL));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Link Properties");
	}
}
