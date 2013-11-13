/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramLinkId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramLink;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.DiagramFactorSchema;

public class DiagramLinkPool extends BaseObjectPool
{
	public DiagramLinkPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.DIAGRAM_LINK);
	}
	
	public DiagramLink find(BaseId id)
	{
		return (DiagramLink)findObject(id);
	}

	@Override
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId) throws Exception
	{
		return new DiagramLink(objectManager, actualId);
	}
	
	public DiagramLinkId[] getallDiagramFactorLinkIds()
	{
		BaseId[] allBaseIds = getIds();
		DiagramLinkId[] allDiagramFactorLinkIds = new DiagramLinkId[allBaseIds.length];
		for (int i = 0; i < allBaseIds.length; i++)
		{
			allDiagramFactorLinkIds[i] = new DiagramLinkId(allBaseIds[i].asInt());
		}
		
		return allDiagramFactorLinkIds; 
	}
	
	//TODO this method needs to be more efficient.  Use link referrers and do an intersection
	public DiagramLink getDiagramLink(ORef fromDiagramFactorRef, ORef toDiagramFactorRef)
	{
		fromDiagramFactorRef.ensureExactType(DiagramFactorSchema.getObjectType());
		toDiagramFactorRef.ensureExactType(DiagramFactorSchema.getObjectType());
		
		ORefList diagramLinkRefs = getORefList();
		for(int i = 0; i < diagramLinkRefs.size(); ++i)
		{
			DiagramLink diagramLink = (DiagramLink) findObject(diagramLinkRefs.get(i));
			ORef thisFromRef = diagramLink.getFromDiagramFactorRef();
			ORef thisToRef = diagramLink.getToDiagramFactorRef();
			if(thisFromRef.equals(fromDiagramFactorRef) && thisToRef.equals(toDiagramFactorRef))
				return diagramLink;
			
			if(thisFromRef.equals(toDiagramFactorRef) && thisToRef.equals(fromDiagramFactorRef))
				return diagramLink;
		}
		return null;
	}		
	
	@Override
	public BaseObjectSchema createBaseObjectSchema(Project projectToUse)
	{
		return DiagramLink.createSchema();
	}
}
