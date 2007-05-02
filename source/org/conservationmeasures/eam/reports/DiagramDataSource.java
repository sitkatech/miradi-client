/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import java.awt.Image;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMNormalObjectPool;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.DiagramImageCreator;

public class DiagramDataSource extends CommonDataSource
{
	public DiagramDataSource(Project project)
	{
		super(project);
	}

	public Image getDiagramImage()
	{
		EAMNormalObjectPool diagramContentsPool = (EAMNormalObjectPool) project.getPool(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
		ORef oref = diagramContentsPool.getORefList().get(0);
		DiagramObject diagramObject = (DiagramObject) project.findObject(oref);
		return DiagramImageCreator.getImage(EAM.mainWindow, diagramObject);
	}
} 
