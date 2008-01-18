/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import java.awt.Image;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
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
		ORefList list = new ORefList();
		EAMNormalObjectPool conceptualPool = (EAMNormalObjectPool) project.getPool(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
		list.addAll(conceptualPool.getORefList());
		EAMNormalObjectPool resultchainPool = (EAMNormalObjectPool) project.getPool(ObjectType.RESULTS_CHAIN_DIAGRAM);
		list.addAll(resultchainPool.getORefList());
		setObjectList(list);
	}

	public Image getDiagramImage()
	{
		DiagramObject diagramObject = (DiagramObject)getCurrentObject();
		return DiagramImageCreator.getImage(EAM.getMainWindow(), diagramObject);
	}
	
} 
