/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import java.awt.Dimension;

import org.miradi.actions.ActionCreateKeyEcologicalAttribute;
import org.miradi.actions.ActionCreateKeyEcologicalAttributeIndicator;
import org.miradi.actions.ActionCreateKeyEcologicalAttributeMeasurement;
import org.miradi.actions.ActionDeleteKeyEcologicalAttribute;
import org.miradi.actions.ActionDeleteKeyEcologicalAttributeIndicator;
import org.miradi.actions.ActionDeleteKeyEcologicalAttributeMeasurement;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.dialogs.treetables.TreeTablePanel;
import org.miradi.dialogs.treetables.TreeTableWithStateSaving;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Target;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;

public class TargetViabililtyTreeTablePanel extends TreeTablePanel
{
	public TargetViabililtyTreeTablePanel(MainWindow mainWindowToUse, Project projectToUse, TreeTableWithStateSaving treeToUse)
	{
		this( mainWindowToUse, treeToUse, buttonActions);
	}

	public TargetViabililtyTreeTablePanel(MainWindow mainWindowToUse, TreeTableWithStateSaving treeToUse, Class[] buttonActionToUse)
	{
		super(mainWindowToUse, treeToUse, buttonActionToUse);
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

		final boolean wereMeasuremetNodesAddedOrRemoved = event.isSetDataCommandWithThisTypeAndTag(Indicator.getObjectType(), Indicator.TAG_MEASUREMENT_REFS);
		
		final boolean wereFactorIndicatorNodesAddedOrRemoved = event.isFactorSetDataCommandWithThisTypeAndTag(Factor.TAG_INDICATOR_IDS);
		
		final boolean wereNodesAddedOrRemoved = wereKEANodesAddedOrRemoved || 
												wereIndicatorNodesAddedOrRemoved ||
												wereMeasuremetNodesAddedOrRemoved ||
												wereFactorIndicatorNodesAddedOrRemoved;
		if( wereNodesAddedOrRemoved)
		{
			treeTableModel.rebuildEntireTree();
			restoreTreeExpansionState();
		} 
		else if(event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_CURRENT_EXPANSION_LIST))
		{
			restoreTreeExpansionState();	
		}
		
		validateModifiedObject(event, KeyEcologicalAttribute.getObjectType());
		validateModifiedObject(event, Target.getObjectType());
		validateModifiedObject(event, Indicator.getObjectType());
		validateModifiedObject(event, Measurement.getObjectType());
		
		repaintToGrowIfTreeIsTaller();	
	}
	
	private void validateModifiedObject(CommandExecutedEvent event, int type)
	{
		if(isSelectedObjectModification(event, type))
		{
			validate();
		}
	}

	static final Class[] buttonActions = new Class[] {
			ActionCreateKeyEcologicalAttribute.class, 
			ActionDeleteKeyEcologicalAttribute.class,
			ActionCreateKeyEcologicalAttributeIndicator.class,
			ActionDeleteKeyEcologicalAttributeIndicator.class,
			ActionCreateKeyEcologicalAttributeMeasurement.class,
			ActionDeleteKeyEcologicalAttributeMeasurement.class, 
			};
}
