/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.awt.Dimension;

import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttributeIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttributeIndicator;
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
		super( mainWindowToUse, treeToUse, buttonActions);
	}



	public Dimension getPreferredSize()
	{
		return new Dimension(super.getPreferredSize().width, 100);
	}


	//FIXME: Kevin: This code needs to be analyzed
	// to see if it really needs to rebuld its tree under all these conditions
	// Also, taskTreeTablePanel and TargetViabilityTreeTablePanel should have very similar 
	// CommandExecuted methods with very similar structures
	public void commandExecuted(CommandExecutedEvent event)
	{
		GenericTreeTableModel treeTableModel = getModel();
		final boolean wereKEANodesAddedOrRemoved = 
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.TARGET, Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
		
		final boolean wereIndicatorNodesAddedOrRemoved = 
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttribute.TAG_INDICATOR_IDS);
		
		final boolean wereNodesAddedOrRemoved = wereKEANodesAddedOrRemoved || wereIndicatorNodesAddedOrRemoved;
		
		if( wereNodesAddedOrRemoved)
		{
			treeTableModel.rebuildEntireTree();
			restoreTreeExpansionState();
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
