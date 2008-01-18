/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.actions.ActionCloneIndicator;
import org.conservationmeasures.eam.actions.ActionCreateIndicator;
import org.conservationmeasures.eam.actions.ActionCreateIndicatorMeasurement;
import org.conservationmeasures.eam.actions.ActionDeleteIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteIndicatorMeasurement;
import org.conservationmeasures.eam.dialogs.diagram.FactorTreeTableNode;
import org.conservationmeasures.eam.dialogs.treetables.GenericTreeTableModel;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;

public class TargetViabililtyTreePanel extends TargetViabililtyTreeTablePanel
{
	public static TargetViabililtyTreePanel createTargetViabilityPanel(MainWindow mainWindowToUse, Project projectToUse, FactorId targetId) throws Exception
	{
		TargetViabilityTreeModel model = new TargetViabilityTreeModel(new TargetViabilityRoot(projectToUse, targetId));
		return getTargetViabililtyTreePanel(mainWindowToUse, projectToUse, model);
	}

	public static TargetViabililtyTreePanel createTargetViabilityPoolPanel(MainWindow mainWindowToUse, Project projectToUse) throws Exception
	{
		ViabilityTreeModel model = new ViabilityTreeModel(new ViabilityRoot(projectToUse));
		return getTargetViabililtyTreePanel(mainWindowToUse, projectToUse, model);
	}

	public static TargetViabililtyTreePanel createFactorIndicatorPanel(MainWindow mainWindowToUse, ORef factorRef, Project projectToUse) throws Exception
	{
		IndicatorTreeModel model = new IndicatorTreeModel(new FactorTreeTableNode(projectToUse, factorRef));
		return getTargetViabililtyTreePanel(mainWindowToUse, projectToUse, model, buttonActions);
	}
	
	private static TargetViabililtyTreePanel getTargetViabililtyTreePanel(MainWindow mainWindowToUse, Project projectToUse, GenericViabilityTreeModel model) throws Exception
	{
		TargetViabilityTreeTable tree = new TargetViabilityTreeTable(projectToUse, model);
		return new TargetViabililtyTreePanel(mainWindowToUse, projectToUse, tree, model);
	}
	
	private static TargetViabililtyTreePanel getTargetViabililtyTreePanel(MainWindow mainWindowToUse, Project projectToUse, GenericViabilityTreeModel model, Class[] buttonActionsToUse) throws Exception
	{
		TargetViabilityTreeTable tree = new TargetViabilityTreeTable(projectToUse, model);
		return new TargetViabililtyTreePanel(mainWindowToUse, tree, model, buttonActionsToUse);
	}

	private TargetViabililtyTreePanel(MainWindow mainWindowToUse, Project projectToUse, TargetViabilityTreeTable treeToUse, GenericTreeTableModel modelToUse)
	{
		super(mainWindowToUse, projectToUse, treeToUse);
		model = modelToUse;
	}
	
	private TargetViabililtyTreePanel(MainWindow mainWindowToUse, TargetViabilityTreeTable treeToUse, GenericTreeTableModel modelToUse, Class[] buttonActionsToUse)
	{
		super(mainWindowToUse, treeToUse, buttonActionsToUse);
		model = modelToUse;
	}
	
	static final Class[] buttonActions = new Class[] {
		ActionCreateIndicator.class,
		ActionDeleteIndicator.class,
		ActionCloneIndicator.class,
		ActionCreateIndicatorMeasurement.class,
		ActionDeleteIndicatorMeasurement.class, 
		};
}