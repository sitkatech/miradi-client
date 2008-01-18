/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;

import org.conservationmeasures.eam.project.Project;

public class MiradiDataSource extends CommonDataSource
{
	public MiradiDataSource(Project project)
	{
		super(project);
	}

	public JRDataSource getSummaryGeneranlDataSource()
	{
		return new SummaryGeneralDataSource(project);
	}
	
	public JRDataSource getDiagramDataSource()
	{
		return new DiagramDataSource(project);
	}
	
	public JRDataSource getThreatTableDataSource()
	{
		return new ThreatTableDataSource(project);
	}
	
	public JRDataSource getViabilityTargetsDataSource()
	{
		return new ViabilityTargetsDataSource(project);
	}
} 
