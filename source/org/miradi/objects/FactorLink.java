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

import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.objectdata.BooleanData;
import org.miradi.objectdata.ORefData;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;

public class FactorLink extends BaseObject
{
	public FactorLink(ObjectManager objectManager, FactorLinkId id, ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		super(objectManager, id);
		clear();
		setData(TAG_FROM_REF, fromFactorRef.toString());
		setData(TAG_TO_REF, toFactorRef.toString());
	}

	public FactorLink(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new FactorLinkId(idAsInt), jsonObject);
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
		ORef rawRef = fromRef.getRef();
		return new ORef(rawRef.getObjectType(), new FactorId(rawRef.getObjectId().asInt()));
	}
	
	public ORef getToFactorRef()
	{
		ORef rawRef = toRef.getRef();
		return new ORef(rawRef.getObjectType(), new FactorId(rawRef.getObjectId().asInt()));
	}
	
	@Override
	public CreateObjectParameter getCreationExtraInfo()
	{
		Factor fromFactor = (Factor) objectManager.findObject(getFromFactorRef());
		Factor toFactor = (Factor) objectManager.findObject(getToFactorRef());
		return new CreateFactorLinkParameter(fromFactor.getRef(), toFactor.getRef());
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
	
	@Override
	void clear()
	{
		super.clear();
		fromRef = new ORefData(TAG_FROM_REF);
		toRef = new ORefData(TAG_TO_REF);
		bidirectionalLink = new BooleanData(TAG_BIDIRECTIONAL_LINK);
		
		addNoClearField(TAG_FROM_REF, fromRef);
		addNoClearField(TAG_TO_REF, toRef);
		addField(TAG_BIDIRECTIONAL_LINK, bidirectionalLink);
	}
	
	public static final String TAG_FROM_REF = "FromRef";
	public static final String TAG_TO_REF = "ToRef";
	private static final String TAG_BIDIRECTIONAL_LINK = "BidirectionalLink";
	
	public static final String OBJECT_NAME = "Link";
	
	private ORefData fromRef;
	private ORefData toRef;
	private BooleanData bidirectionalLink;
}
