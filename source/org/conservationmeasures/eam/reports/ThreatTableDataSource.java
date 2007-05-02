package org.conservationmeasures.eam.reports;

import java.awt.Image;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.threatmatrix.MatrixTableImageCreator;

public class ThreatTableDataSource extends CommonDataSource
{
	public ThreatTableDataSource(Project project)
	{
		super(project);
	}

	public Image getThreatTableImage()
	{
		return MatrixTableImageCreator.getImage(project);
	}
} 
