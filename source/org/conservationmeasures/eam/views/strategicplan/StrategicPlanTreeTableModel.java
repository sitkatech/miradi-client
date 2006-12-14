/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableNode;

public class StrategicPlanTreeTableModel extends GenericTreeTableModel
{
	static public StrategicPlanTreeTableModel createForProject(Project project) throws Exception
	{
		return new StrategicPlanTreeTableModel(project, new StratPlanRoot(project));
	}
	
	static public StrategicPlanTreeTableModel createForStrategy(Project project, Strategy strategy)
	{
		return new StrategicPlanTreeTableModel(project, new StratPlanStrategy(project, strategy));
	}
	
	
	private StrategicPlanTreeTableModel(Project projectToUse, TreeTableNode root)
	{
		super(root);
		project = projectToUse;
	}
	
	public int getColumnCount()
	{
		return columnNames.length;
	}

	public String getColumnName(int column)
	{
		return columnNames[column];
	}
	
	public static final int labelColumn = 0;
	public static final int budgetColumn = 1;
	public static final int datesColumn = 2;
	
	static final String[] columnNames = {"Item", };

	Project project;

	
}

