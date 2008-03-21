/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
