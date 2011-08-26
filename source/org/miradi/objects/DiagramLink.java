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

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.DiagramLinkId;
import org.miradi.ids.FactorLinkId;
import org.miradi.objectdata.BaseIdData;
import org.miradi.objectdata.BooleanData;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.ORefListData;
import org.miradi.objectdata.ObjectData;
import org.miradi.objectdata.PointListData;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.DiagramLinkColorQuestion;
import org.miradi.utils.CommandVector;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.PointList;

public class DiagramLink extends BaseObject
{
	public DiagramLink(ObjectManager objectManager, BaseId idToUse, CreateDiagramFactorLinkParameter extraInfo) throws Exception
	{
		super(objectManager, new DiagramLinkId(idToUse.asInt()));
		
		setData(TAG_WRAPPED_ID, extraInfo.getFactorLinkId().toString());
		setData(TAG_FROM_DIAGRAM_FACTOR_ID, extraInfo.getFromFactorId().toString());
		setData(TAG_TO_DIAGRAM_FACTOR_ID, extraInfo.getToFactorId().toString());
	}
	
	public DiagramLink(ObjectManager objectManager, int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new DiagramLinkId(idToUse), json);
		
		underlyingObjectId.set(json.getId(TAG_WRAPPED_ID).toString());
		fromId.set(json.getId(TAG_FROM_DIAGRAM_FACTOR_ID).toString());
		toId.set(json.getId(TAG_TO_DIAGRAM_FACTOR_ID).toString());
	}
	
	@Override
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();
		json.putId(TAG_WRAPPED_ID, underlyingObjectId.getId());
		json.putId(TAG_FROM_DIAGRAM_FACTOR_ID, fromId.getId());
		json.putId(TAG_TO_DIAGRAM_FACTOR_ID, toId.getId());
		
		return json;
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
		return ObjectType.DIAGRAM_LINK;
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			ConceptualModelDiagram.getObjectType(), 
			ResultsChainDiagram.getObjectType()
			};
	}
	
	@Override
	public ORefList getAllObjectsToDeepCopy(ORefList deepCopiedFactorRefs)
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy(deepCopiedFactorRefs);
		deepObjectRefsToCopy.addAll(getGroupedDiagramLinkRefs());
		
		return deepObjectRefsToCopy;
	}
	
	public ORef getDiagramFactorRef(int direction)
	{
		if(direction == DiagramLink.FROM)
			return getFromDiagramFactorRef();
		if(direction == DiagramLink.TO)
			return getToDiagramFactorRef();
		throw new RuntimeException("Unknown direction: " + direction);
	}
	
	public ORef getOppositeEndRef(ORef diagramFactorRef)
	{
		if (getFromDiagramFactorRef().equals(diagramFactorRef))
			return getToDiagramFactorRef();
		
		if (getToDiagramFactorRef().equals(diagramFactorRef))
			return getFromDiagramFactorRef();
		
		return ORef.INVALID;
	}
	
	public ORef getOppositeDiagramFactorRef(int direction)
	{
		if(direction == DiagramLink.FROM)
			return getToDiagramFactorRef();
		if(direction == DiagramLink.TO)
			return getFromDiagramFactorRef();
		throw new RuntimeException("Unknown direction: " + direction);
	}
	
	public DiagramFactor getDiagramFactor(int direction)
	{
		return DiagramFactor.find(objectManager, getDiagramFactorRef(direction));
	}
	
	public DiagramFactor getOppositeDiagramFactor(int direction)
	{
		return DiagramFactor.find(objectManager, getOppositeDiagramFactorRef(direction));
	}
	
	public ORef getFromWrappedRef()
	{
		return getFromDiagramFactor().getWrappedORef();
	}
	
	public ORef getFromDiagramFactorRef()
	{
		return new ORef(DiagramFactor.getObjectType(), getFromDiagramFactorId());
	}
	
	public DiagramFactor getFromDiagramFactor()
	{
		return DiagramFactor.find(getProject(), getFromDiagramFactorRef());
	}
	
	public ORef getToWrappedRef()
	{
		return getToDiagramFactor().getWrappedORef();
	}
	
	public ORef getToDiagramFactorRef()
	{
		return new ORef(DiagramFactor.getObjectType(), getToDiagramFactorId());
	}
	
	public DiagramFactor getToDiagramFactor()
	{
		return DiagramFactor.find(getProject(), getToDiagramFactorRef());
	}
	
	public DiagramFactorId getFromDiagramFactorId()
	{
		return new DiagramFactorId(fromId.getId().asInt());
	}
	
	public DiagramFactorId getToDiagramFactorId()
	{
		return new DiagramFactorId(toId.getId().asInt());
	}

	public DiagramLinkId getDiagramLinkId()
	{
		return (DiagramLinkId)getId(); 
	}
	
	public ORefList getGroupedDiagramLinkRefs()
	{
		return groupedDiagramLinkRefs.getRefList();
	}
	
	public ORef getWrappedRef()
	{
		return new ORef(FactorLink.getObjectType(), underlyingObjectId.getId());
	}
	
	public FactorLinkId getWrappedId()
	{
		return new FactorLinkId(underlyingObjectId.getId().asInt());
	}
	
	public DiagramObject getDiagramObject()
	{
		ORefList cmPageRefs = findObjectsThatReferToUs(ConceptualModelDiagram.getObjectType());
		if(cmPageRefs.size() > 0)
			return ConceptualModelDiagram.find(objectManager, cmPageRefs.get(0));

		ORefList rcRefs = findObjectsThatReferToUs(ResultsChainDiagram.getObjectType());
		if(rcRefs.size() > 0)
			return ResultsChainDiagram.find(objectManager, rcRefs.get(0));
		
		return null;
	}
	
	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_GROUPED_DIAGRAM_LINK_REFS))
			return DiagramLink.getObjectType();
		
		return super.getAnnotationType(tag);
	}
	
	@Override
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_GROUPED_DIAGRAM_LINK_REFS))
			return true;
		
		return super.isRefList(tag);
	}
	
	public boolean alsoLinksOurFromOrTo(DiagramLink otherDiagramLink)
	{
		if (isToOrFrom(otherDiagramLink.getFromDiagramFactorRef()))
			return true;
		
		if (isToOrFrom(otherDiagramLink.getToDiagramFactorRef()))
			return true;
		
		return false;
	}
	
	public boolean isToOrFrom(DiagramFactor diagramFactor)
	{
		return isToOrFrom(diagramFactor.getRef());
	}
	
	public boolean isToOrFrom(ORef diagramFactorRef)
	{
		if (getToDiagramFactorRef().equals(diagramFactorRef))
			return true;
		
		if (getFromDiagramFactorRef().equals(diagramFactorRef))
			return true;
		
		return false;
	}
	
	public boolean isBidirectional()
	{
		return isBidirectionalLink.get().equals(BIDIRECTIONAL_LINK);
	}
	
	public FactorLink getWrappedFactorLink()
	{
		return FactorLink.find(getProject(), getWrappedRef());
	}
	
	@Override
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

	public ORefList getSelfOrChildren()
	{
		if (isGroupBoxLink())
			return getGroupedDiagramLinkRefs();
		
		return new ORefList(getRef());
	}
	
	public boolean isGroupBoxLink()
	{
		if (DiagramFactor.find(getProject(), getFromDiagramFactorRef()).isGroupBoxFactor())
			return true;
		
		if (DiagramFactor.find(getProject(), getToDiagramFactorRef()).isGroupBoxFactor())
			return true;
		
		return false;
	}
	
	public String getToolTipString() 
	{
		DiagramFactor fromDiagramFactor = DiagramFactor.find(getProject(), getFromDiagramFactorRef());
		DiagramFactor toDiagramFactor = DiagramFactor.find(getProject(), getToDiagramFactorRef());
		Factor fromFactor = Factor.findFactor(getProject(), fromDiagramFactor.getWrappedORef());
		Factor toFactor = Factor.findFactor(getProject(), toDiagramFactor.getWrappedORef());
		String toolTipText = "<html><b>From : " + fromFactor.getLabel() + "</b><BR>" +
				           		   "<b>To : " + toFactor.getLabel() + "</b>";
		
		return toolTipText;
	}
	
	@Override
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
	
	public Rectangle getBendPointBounds()
	{
		return getBendPoints().getBounds();
	}
	
	public Color getColor()
	{
		return getColorChoiceItem().getColor();
	}

	public ChoiceItem getColorChoiceItem()
	{
		ChoiceQuestion question = getProject().getQuestion(DiagramLinkColorQuestion.class);
		return question.findChoiceByCode(color.get());
	}
	
	public boolean isCoveredByGroupBoxLink()
	{
		ORefList groupBoxLinks = findObjectsThatReferToUs(DiagramLink.getObjectType());
		return (groupBoxLinks.size() > 0);
	}
	
	public CommandVector createCommandsToEnableBidirectionalFlag()
	{
		return createCommandsToSetBidirectionalFlag(true);
	}
	
	public CommandVector createCommandsToSetBidirectionalFlag(boolean shouldBeBidirectional)
	{
		CommandVector commands = new CommandVector();
		String newBidirectionalValue = BooleanData.toString(shouldBeBidirectional);
		commands.add(new CommandSetObjectData(getRef(), DiagramLink.TAG_IS_BIDIRECTIONAL_LINK, newBidirectionalValue));
		return commands;
	}

	public static boolean isTo(int direction)
	{
		return direction == TO;
	}

	public static boolean isFrom(int direction)
	{
		return direction == FROM;
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}

	public static DiagramLink find(ObjectManager objectManager, ORef diagramLinkRef)
	{
		return (DiagramLink) objectManager.findObject(diagramLinkRef);
	}
	
	public static DiagramLink find(Project project, ORef diagramLinkRef)
	{
		return find(project.getObjectManager(), diagramLinkRef);
	}
		
	@Override
	void clear()
	{
		super.clear();

		underlyingObjectId = new BaseIdData(TAG_WRAPPED_ID, FactorLink.getObjectType());
		fromId = new BaseIdData(TAG_FROM_DIAGRAM_FACTOR_ID, DiagramFactor.getObjectType());
		toId = new BaseIdData(TAG_TO_DIAGRAM_FACTOR_ID, DiagramFactor.getObjectType());
		bendPoints = new PointListData(TAG_BEND_POINTS);
		groupedDiagramLinkRefs = new ORefListData(TAG_GROUPED_DIAGRAM_LINK_REFS);
		color = new ChoiceData(TAG_COLOR, getQuestion(DiagramLinkColorQuestion.class));
		isBidirectionalLink = new BooleanData(TAG_IS_BIDIRECTIONAL_LINK);
		
		addNoClearField(TAG_WRAPPED_ID, underlyingObjectId);
		addNoClearField(TAG_FROM_DIAGRAM_FACTOR_ID, fromId);
		addNoClearField(TAG_TO_DIAGRAM_FACTOR_ID, toId);
		addField(TAG_BEND_POINTS, bendPoints);
		addField(TAG_GROUPED_DIAGRAM_LINK_REFS, groupedDiagramLinkRefs);
		addField(TAG_COLOR, color);
		addField(TAG_IS_BIDIRECTIONAL_LINK, isBidirectionalLink);
	}
	
	public static final String TAG_WRAPPED_ID = "WrappedLinkId";
	public static final String TAG_FROM_DIAGRAM_FACTOR_ID = "FromDiagramFactorId";
	public static final String TAG_TO_DIAGRAM_FACTOR_ID = "ToDiagramFactorId";
	public static final String TAG_BEND_POINTS = "BendPoints";
	public static final String TAG_GROUPED_DIAGRAM_LINK_REFS = "GroupedDiagramLinkRefs";
	public static final String TAG_COLOR = "Color";
	public static final String TAG_IS_BIDIRECTIONAL_LINK = "IsBidirectionalLink";
	
	public static final int FROM = 1;
	public static final int TO = 2;
	public static final String BIDIRECTIONAL_LINK = BooleanData.BOOLEAN_TRUE;
	
	public static final String OBJECT_NAME = "DiagramLink";
	
	private BaseIdData underlyingObjectId;
	private BaseIdData fromId;
	private BaseIdData toId;
	private PointListData bendPoints;
	private ORefListData groupedDiagramLinkRefs;
	private ChoiceData color;
	private ObjectData isBidirectionalLink;
}
