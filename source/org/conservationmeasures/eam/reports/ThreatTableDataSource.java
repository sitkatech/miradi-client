package org.conservationmeasures.eam.reports;

import java.awt.Image;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class ThreatTableDataSource extends CommonDataSource
{
	public ThreatTableDataSource(Project project)
	{
		super(project);
	}

	public Image getThreatTableImage()
	{
		return EAM.mainWindow.getThreatView().getImage();
	}
} 
