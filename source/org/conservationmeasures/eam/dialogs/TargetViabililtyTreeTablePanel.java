/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttribute;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;
import org.conservationmeasures.eam.views.treeViews.TreeTablePanel;

public class TargetViabililtyTreeTablePanel extends TreeTablePanel
{
	public TargetViabililtyTreeTablePanel(MainWindow mainWindowToUse, Project projectToUse, TreeTableWithStateSaving treeToUse)
	{
		super( mainWindowToUse, treeToUse, buttonActions, ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		TargetViabilityTreeModel treeTableModel = (TargetViabilityTreeModel)getModel();
		
		final boolean wereNodesAddedOrRemoved = isThisSeDataCommand(event, ObjectType.FACTOR, Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
		if( wereNodesAddedOrRemoved)
		{
			treeTableModel.rebuildEntreTree();
			restoreTreeExpansionState();
			IdList newIdList = extractNewlyAddedIds(event);
			for (int i=0; i<newIdList.size(); ++i)
			{
				selectObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, newIdList.get(i));
			}
		} 
		else if(isSetDataCommand(event))
		{
			CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
			final boolean isModifiedObjectMabeyInTree = cmd.getObjectType() == ObjectType.KEY_ECOLOGICAL_ATTRIBUTE;
			if (isModifiedObjectMabeyInTree)
			{
				treeTableModel.rebuildEntreTree();
				restoreTreeExpansionState();	
			}
		}
	}

	private IdList extractNewlyAddedIds(CommandExecutedEvent event)
	{
		try
		{
			CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
			IdList oldIdList = new IdList(cmd.getPreviousDataValue());
			IdList newIdList = new IdList(cmd.getDataValue());
			newIdList.subtract(oldIdList);
			return newIdList;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new IdList();
		}
	}

	
	static final Class[] buttonActions = new Class[] {
			ActionCreateKeyEcologicalAttribute.class, 
			ActionDeleteKeyEcologicalAttribute.class,};
}
