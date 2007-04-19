package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;

import org.conservationmeasures.eam.project.Project;

public class BudgetPlanDataSource extends CommonDataSource
{
	public BudgetPlanDataSource(Project project)
	{
		super(project);
	}
	
	public JRDataSource getBudgetMonitoringDataSource() throws Exception
	{
		return new BudgetMonitoringDataSource(project);
	}
	
	public JRDataSource getBudgetStrategyDataSource() throws Exception
	{
		return new BudgetStrategyDataSource(project);
	}
} 
