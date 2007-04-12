/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import java.awt.Image;

import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class DiagramDataSource extends CommonDataSource
{
	public DiagramDataSource(Project project)
	{
		super(project);
	}
	
	public Object getFieldValue(JRField field)
	{
		return "";
	}
	
	public Image getDiagramImage()
	{
		//FIXME: currently getDiagramComponent returns null if not in the diagram view
		if (EAM.mainWindow.getDiagramComponent()!=null)
			return EAM.mainWindow.getDiagramComponent().getImage();
		return null;
	}
} 
