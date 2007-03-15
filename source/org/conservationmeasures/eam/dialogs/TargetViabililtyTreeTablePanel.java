/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttributeIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttributeIndicator;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
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
		GenericTreeTableModel treeTableModel = getModel();
		final boolean wereKEANodesAddedOrRemoved = 
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.FACTOR, Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
		
		final boolean wereIndicatorNodesAddedOrRemoved = 
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttribute.TAG_INDICATOR_IDS);
		
		final boolean wereNodesAddedOrRemoved = wereKEANodesAddedOrRemoved || wereIndicatorNodesAddedOrRemoved;
		
		if( wereNodesAddedOrRemoved)
		{
			treeTableModel.rebuildEntireTree();
			restoreTreeExpansionState();
			
			int annoationObjectType = ObjectType.KEY_ECOLOGICAL_ATTRIBUTE;
			if (wereIndicatorNodesAddedOrRemoved)
				annoationObjectType = ObjectType.INDICATOR;

			IdList newIdList = event.extractNewlyAddedIds();
			for (int i=0; i<newIdList.size(); ++i)
			{
				expandAndSelectObject(annoationObjectType, newIdList.get(i));
			}
			
			if (newIdList.size()==0)
			{
				//TODO: right now we only go to the root of the node being deleted....not the sibling of the delted node
				CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
				expandAndSelectObject(cmd.getObjectType(), cmd.getObjectId());
			}
			repaint();
		} 
		else if(event.isSetDataCommand())
		{
			treeTableModel.rebuildEntireTree();
			restoreTreeExpansionState();	
		}
	}

	static final Class[] buttonActions = new Class[] {
			ActionCreateKeyEcologicalAttribute.class, 
			ActionDeleteKeyEcologicalAttribute.class,
			ActionCreateKeyEcologicalAttributeIndicator.class,
			ActionDeleteKeyEcologicalAttributeIndicator.class,};
}
