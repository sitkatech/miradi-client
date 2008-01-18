/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
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
