/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;

import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.utils.FastScrollPane;

public class StrategicPlanPanel extends DisposablePanel
{
	static public StrategicPlanPanel createForProject(MainWindow mainWindowToUse) throws Exception
	{
		return new StrategicPlanPanel(mainWindowToUse, StrategicPlanTreeTableModel.createForProject(mainWindowToUse.getProject()));
	}
	
	static public StrategicPlanPanel createForStrategy(MainWindow mainWindowToUse, Strategy intervention) throws Exception
	{
		return new StrategicPlanPanel(mainWindowToUse, StrategicPlanTreeTableModel.createForStrategy(mainWindowToUse.getProject(), intervention));
	}
	
	private StrategicPlanPanel(MainWindow mainWindowToUse, StrategicPlanTreeTableModel modelToUse) throws Exception
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		model = modelToUse;
		tree = new StrategicPlanTreeTable(mainWindow.getProject(), model);
		tree.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.getTree().setShowsRootHandles(true);
		add(new FastScrollPane(tree), BorderLayout.CENTER);
		tree.getTree().addSelectionRow(0);
		tree.restoreTreeState();
	}
	
	public void dispose()
	{
		tree.dispose();
		super.dispose();
	}
	
	public StrategicPlanTreeTableModel getModel()
	{
		return model;
	}
	
	MainWindow mainWindow;
	StrategicPlanTreeTable tree;
	StrategicPlanTreeTableModel model;
}

