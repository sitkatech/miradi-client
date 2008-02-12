/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.goal;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.ids.GoalId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Goal;
import org.miradi.project.Project;

public class GoalPropertiesPanel extends ObjectDataInputPanel
{
	public GoalPropertiesPanel(Project projectToUse) throws Exception
	{
		this(projectToUse, new GoalId(BaseId.INVALID.asInt()));
	}
	
	public GoalPropertiesPanel(Project projectToUse, Goal goal) throws Exception
	{
		this(projectToUse, (GoalId)goal.getId());
	}
	
	public GoalPropertiesPanel(Project projectToUse, GoalId idToShow) throws Exception
	{
		super(projectToUse, ObjectType.GOAL, idToShow);
		
		
		addField(createShortStringField(Goal.TAG_SHORT_LABEL));
		addField(createStringField(Goal.TAG_LABEL));
		addField(createMultilineField(Goal.TAG_FULL_TEXT));
		addField(createReadonlyTextField(Goal.PSEUDO_TAG_FACTOR));
		addField(createReadonlyTextField(Goal.PSEUDO_TAG_STRATEGIES));
		addField(createReadonlyTextField(Goal.PSEUDO_TAG_DIRECT_THREATS));
		addField(createMultilineField(Goal.TAG_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Goal Properties");
	}

}
