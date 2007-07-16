/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.treeViews;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.GenericTreeTableModel;

public class TaskTreeTableModel extends GenericTreeTableModel
{
	public TaskTreeTableModel(Object root)
	{
		super(root);
	}

	public int getColumnCount()
	{
		return 0;
	}

	public String getColumnName(int column)
	{
		return null;
	}

	static boolean isTreeStructureChangingCommand(CommandSetObjectData cmd)
	{
		int type = cmd.getObjectType();
		String tag = cmd.getFieldTag();
		if(type == ObjectType.STRATEGY && tag.equals(Strategy.TAG_ACTIVITY_IDS))
			return true;
		if(type == ObjectType.INDICATOR && tag.equals(Indicator.TAG_TASK_IDS))
			return true;
		if(type == ObjectType.TASK && tag.equals(Task.TAG_SUBTASK_IDS))
			return true;
		
		return false;
	}

}
