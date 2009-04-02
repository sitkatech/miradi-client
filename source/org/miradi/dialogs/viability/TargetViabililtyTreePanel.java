/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.viability;

import org.miradi.actions.ActionCloneIndicator;
import org.miradi.actions.ActionCreateIndicator;
import org.miradi.actions.ActionCreateIndicatorMeasurement;
import org.miradi.actions.ActionDeleteIndicator;
import org.miradi.actions.ActionDeleteIndicatorMeasurement;
import org.miradi.dialogs.diagram.FactorTreeTableNode;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.dialogs.viability.nodes.TargetViabilityRoot;
import org.miradi.dialogs.viability.nodes.ViabilityRoot;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

public class TargetViabililtyTreePanel extends TargetViabililtyTreeTablePanel
{
	public static TargetViabililtyTreePanel createTargetViabilityPanel(MainWindow mainWindowToUse, Project projectToUse, ORef targetRef) throws Exception
	{
		TargetViabilityTreeModel model = new TargetViabilityTreeModel(new TargetViabilityRoot(projectToUse, targetRef));
		return getTargetViabililtyTreePanel(mainWindowToUse, projectToUse, model);
	}

	public static TargetViabililtyTreePanel createTargetViabilityPoolPanel(MainWindow mainWindowToUse) throws Exception
	{
		ViabilityTreeModel model = new ViabilityTreeModel(new ViabilityRoot(mainWindowToUse.getProject()));
		return getTargetViabililtyTreePanel(mainWindowToUse, mainWindowToUse.getProject(), model);
	}

	public static TargetViabililtyTreePanel createFactorIndicatorPanel(MainWindow mainWindowToUse, ORef factorRef, Project projectToUse) throws Exception
	{
		IndicatorTreeModel model = new IndicatorTreeModel(new FactorTreeTableNode(projectToUse, factorRef));
		return getTargetViabililtyTreePanel(mainWindowToUse, projectToUse, model, buttonActions);
	}
	
	private static TargetViabililtyTreePanel getTargetViabililtyTreePanel(MainWindow mainWindowToUse, Project projectToUse, GenericViabilityTreeModel model) throws Exception
	{
		TargetViabilityTreeTable tree = new TargetViabilityTreeTable(mainWindowToUse, model);
		return new TargetViabililtyTreePanel(mainWindowToUse, projectToUse, tree, model);
	}
	
	private static TargetViabililtyTreePanel getTargetViabililtyTreePanel(MainWindow mainWindowToUse, Project projectToUse, GenericViabilityTreeModel model, Class[] buttonActionsToUse) throws Exception
	{
		TargetViabilityTreeTable tree = new TargetViabilityTreeTable(mainWindowToUse, model);
		return new TargetViabililtyTreePanel(mainWindowToUse, tree, model, buttonActionsToUse);
	}

	private TargetViabililtyTreePanel(MainWindow mainWindowToUse, Project projectToUse, TargetViabilityTreeTable treeToUse, GenericTreeTableModel modelToUse) throws Exception
	{
		super(mainWindowToUse, projectToUse, treeToUse);
		model = modelToUse;
	}
	
	private TargetViabililtyTreePanel(MainWindow mainWindowToUse, TargetViabilityTreeTable treeToUse, GenericTreeTableModel modelToUse, Class[] buttonActionsToUse) throws Exception
	{
		super(mainWindowToUse, treeToUse, buttonActionsToUse);
		model = modelToUse;
	}
	
	static final Class[] buttonActions = new Class[] {
		ActionCreateIndicator.class,
		ActionCreateIndicatorMeasurement.class,
		ActionCloneIndicator.class,
		ActionDeleteIndicator.class,
		ActionDeleteIndicatorMeasurement.class, 
		};
}