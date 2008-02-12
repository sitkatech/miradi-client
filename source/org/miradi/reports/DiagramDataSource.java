/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.reports;

import java.awt.Image;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMNormalObjectPool;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;
import org.miradi.views.diagram.DiagramImageCreator;

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
