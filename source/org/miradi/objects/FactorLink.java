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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.FactorLinkSchema;
import org.miradi.utils.EnhancedJsonObject;

public class FactorLink extends BaseObject
{
	public FactorLink(ObjectManager objectManager, BaseId id) throws Exception
	{
		super(objectManager, id, new FactorLinkSchema());
		clear();
	}

	public FactorLink(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject, new FactorLinkSchema());
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.FACTOR_LINK;
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			ConceptualModelDiagram.getObjectType(), 
			ResultsChainDiagram.getObjectType()
			};
	}
	
	public ORef getFromFactorRef()
	{
		return getRefData(TAG_FROM_REF);
	}
	
	public ORef getToFactorRef()
	{
		return getRefData(TAG_TO_REF);
	}
	
	public ORef getFactorRef(int direction)
	{
		if(DiagramLink.isFrom(direction))
			return getFromFactorRef();
		if(DiagramLink.isTo(direction))
			return getToFactorRef();
		throw new RuntimeException("Link: Unknown direction " + direction);
	}

	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static FactorLink find(Project project, ORef factorLinkRef)
	{
		return (FactorLink) project.getObjectManager().findObject(factorLinkRef);
	}
	
	public static final String TAG_FROM_REF = "FromRef";
	public static final String TAG_TO_REF = "ToRef";
	public static final String TAG_BIDIRECTIONAL_LINK = "BidirectionalLink";
	
	public static final String OBJECT_NAME = "Link";
}
