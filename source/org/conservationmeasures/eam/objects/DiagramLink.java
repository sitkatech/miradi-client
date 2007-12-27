/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.awt.Point;
import java.util.Set;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objectdata.BaseIdData;
import org.conservationmeasures.eam.objectdata.ORefListData;
import org.conservationmeasures.eam.objectdata.PointListData;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.utils.PointList;

public class DiagramLink extends BaseObject
{
	public DiagramLink(ObjectManager objectManager, BaseId idToUse, CreateDiagramFactorLinkParameter extraInfo) throws Exception
	{
		super(objectManager, new DiagramFactorLinkId(idToUse.asInt()));
		
		clear();
		setData(TAG_WRAPPED_ID, extraInfo.getFactorLinkId().toString());
		setData(TAG_FROM_DIAGRAM_FACTOR_ID, extraInfo.getFromFactorId().toString());
		setData(TAG_TO_DIAGRAM_FACTOR_ID, extraInfo.getToFactorId().toString());
	}
	
	public DiagramLink(BaseId idToUse, CreateDiagramFactorLinkParameter extraInfo) throws Exception
	{
		super(new DiagramFactorLinkId(idToUse.asInt()));
		
		clear();
		underlyingObjectId.setId(extraInfo.getFactorLinkId());
		fromId.setId(extraInfo.getFromFactorId());
		toId.setId(extraInfo.getToFactorId());
	}
	
	public DiagramLink(ObjectManager objectManager, int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new DiagramFactorLinkId(idToUse), json);
		
		underlyingObjectId.setId(json.getId(TAG_WRAPPED_ID));
		fromId.setId(json.getId(TAG_FROM_DIAGRAM_FACTOR_ID));
		toId.setId(json.getId(TAG_TO_DIAGRAM_FACTOR_ID));
	}
	
	public DiagramLink(int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(new DiagramFactorLinkId(idToUse), json);
		
		underlyingObjectId.setId(json.getId(TAG_WRAPPED_ID));
		fromId.setId(json.getId(TAG_FROM_DIAGRAM_FACTOR_ID));
		toId.setId(json.getId(TAG_TO_DIAGRAM_FACTOR_ID));
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();
		json.putId(TAG_WRAPPED_ID, underlyingObjectId.getId());
		json.putId(TAG_FROM_DIAGRAM_FACTOR_ID, fromId.getId());
		json.putId(TAG_TO_DIAGRAM_FACTOR_ID, toId.getId());
		
		return json;
	}

	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.DIAGRAM_LINK;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		switch(type)
		{
			case ObjectType.FACTOR_LINK: 
				return true;
			case ObjectType.DIAGRAM_FACTOR: 
				return true;
			default:
				return false;
		}
	}
	
	public Set<String> getReferencedObjectTags()
	{
		Set<String> set = super.getReferencedObjectTags();
		set.add(TAG_FROM_DIAGRAM_FACTOR_ID);
		set.add(TAG_TO_DIAGRAM_FACTOR_ID);
		set.add(TAG_WRAPPED_ID);
		set.add(TAG_GROUPED_DIAGRAM_LINK_REFS);
		
		return set;
	}
	
	public ORef getFromDiagramFactorRef()
	{
		return new ORef(DiagramFactor.getObjectType(), getFromDiagramFactorId());
	}
	
	public ORef getToDiagramFactorRef()
	{
		return new ORef(DiagramFactor.getObjectType(), getToDiagramFactorId());
	}
	
	public DiagramFactorId getFromDiagramFactorId()
	{
		return new DiagramFactorId(fromId.getId().asInt());
	}
	
	public DiagramFactorId getToDiagramFactorId()
	{
		return new DiagramFactorId(toId.getId().asInt());
	}

	public DiagramFactorLinkId getDiagramLinkageId()
	{
		return (DiagramFactorLinkId)getId(); 
	}
	
	public ORef getWrappedRef()
	{
		return getUnderlyingLink().getRef();
	}
	
	public FactorLinkId getWrappedId()
	{
		return new FactorLinkId(underlyingObjectId.getId().asInt());
	}
	
	public boolean isBidirectional()
	{
		return getUnderlyingLink().isBidirectional();
	}
	
	public boolean isTargetLink()
	{
		return getUnderlyingLink().isTargetLink();
	}
	
	public FactorLink getUnderlyingLink()
	{
		return (FactorLink)getProject().findObject(new ORef(FactorLink.getObjectType(), underlyingObjectId.getId()));
	}
	
	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_WRAPPED_ID))
			return underlyingObjectId.get();
		if(fieldTag.equals(TAG_FROM_DIAGRAM_FACTOR_ID))
			return fromId.get();
		if(fieldTag.equals(TAG_TO_DIAGRAM_FACTOR_ID))
			return toId.get();
		
		return super.getData(fieldTag);
	}

	public boolean bendPointAlreadyExists(Point location)
	{
		if (location == null)
			return false;
		
		return getBendPoints().contains(location);
	}

	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateDiagramFactorLinkParameter(
				new FactorLinkId(underlyingObjectId.getId().asInt()), 
				new DiagramFactorId(fromId.getId().asInt()), 
				new DiagramFactorId(toId.getId().asInt()));
	}
	
	public PointList getBendPoints()
	{
		return bendPoints.getPointList();
	}
	
	public static DiagramLink find(ObjectManager objectManager, ORef diagramLinkRef)
	{
		return (DiagramLink) objectManager.findObject(diagramLinkRef);
	}
	
	public static DiagramLink find(Project project, ORef diagramLinkRef)
	{
		return find(project.getObjectManager(), diagramLinkRef);
	}
		
	void clear()
	{
		super.clear();

		underlyingObjectId = new BaseIdData(FactorLink.getObjectType());
		fromId = new BaseIdData(DiagramFactor.getObjectType());
		toId = new BaseIdData(DiagramFactor.getObjectType());
		bendPoints = new PointListData();
		groupedDiagramLinkRefs = new ORefListData();
		
		addNoClearField(TAG_WRAPPED_ID, underlyingObjectId);
		addNoClearField(TAG_FROM_DIAGRAM_FACTOR_ID, fromId);
		addNoClearField(TAG_TO_DIAGRAM_FACTOR_ID, toId);
		addField(TAG_BEND_POINTS, bendPoints);
		addField(TAG_GROUPED_DIAGRAM_LINK_REFS, groupedDiagramLinkRefs);
	}
	
	public static final String TAG_WRAPPED_ID = "WrappedLinkId";
	public static final String TAG_FROM_DIAGRAM_FACTOR_ID = "FromDiagramFactorId";
	public static final String TAG_TO_DIAGRAM_FACTOR_ID = "ToDiagramFactorId";
	public static final String TAG_BEND_POINTS = "BendPoints";
	public static final String TAG_GROUPED_DIAGRAM_LINK_REFS = "GroupedDiagramLinkRefs";
	
	static final String OBJECT_NAME = "DiagramLink";
	
	private BaseIdData underlyingObjectId;
	private BaseIdData fromId;
	private BaseIdData toId;
	private PointListData bendPoints;
	private ORefListData groupedDiagramLinkRefs;
}
