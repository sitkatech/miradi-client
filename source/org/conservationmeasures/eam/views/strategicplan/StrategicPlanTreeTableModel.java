/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableNode;

public class StrategicPlanTreeTableModel extends GenericTreeTableModel
{
	static public StrategicPlanTreeTableModel createForProject(Project project)
	{
		return new StrategicPlanTreeTableModel(project, new StratPlanRoot(project));
	}
	
	static public StrategicPlanTreeTableModel createForStrategy(Project project, ConceptualModelIntervention strategy)
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
	
	public TreeTableNode getCurrentExpandedNode()
	{
		return null;
	}

	public static final int labelColumn = 0;
	public static final int resourcesColumn = 1;
	public static final int budgetColumn = 2;
	public static final int datesColumn = 3;
	
	static final String[] columnNames = {"Item", "Resources", "Budget", "Dates", };

	Project project;

	
}

