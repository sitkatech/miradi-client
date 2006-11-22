/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.ObjectiveId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;

public class ObjectivePropertiesPanel extends ObjectDataInputPanel
{
	public ObjectivePropertiesPanel(Project projectToUse, Actions actions, Objective objective) throws Exception
	{
		this(projectToUse, actions, (ObjectiveId)objective.getId());
	}
	
	public ObjectivePropertiesPanel(Project projectToUse, Actions actions, ObjectiveId idToShow) throws Exception
	{
		super(projectToUse, ObjectType.OBJECTIVE, idToShow);
		
		addField(createStringField(Objective.PSEUDO_TAG_FACTOR));
		addField(createStringField(Objective.TAG_SHORT_LABEL));
		addField(createStringField(Objective.TAG_LABEL));
		addField(createMultilineField(Goal.TAG_FULL_TEXT));
		addField(createMultilineDisplayField(Objective.PSEUDO_TAG_STRATEGIES));
		addField(createMultilineDisplayField(Objective.PSEUDO_TAG_DIRECT_THREATS));
		addField(createMultilineDisplayField(Objective.PSEUDO_TAG_TARGETS));
				
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Objective Properties");
	}

}
