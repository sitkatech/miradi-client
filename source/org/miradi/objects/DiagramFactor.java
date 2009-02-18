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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorId;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.DimensionData;
import org.miradi.objectdata.ORefData;
import org.miradi.objectdata.ORefListData;
import org.miradi.objectdata.PointData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;
import org.miradi.utils.EnhancedJsonObject;

public class DiagramFactor extends BaseObject
{
	public DiagramFactor(ObjectManager objectManager, DiagramFactorId diagramFactorIdToUse, CreateDiagramFactorParameter extraInfo) throws Exception
	{
		super(objectManager, diagramFactorIdToUse);
		
		clear();
		setData(TAG_WRAPPED_REF, extraInfo.getFactorRef().toString());
		size.setDimension(getDefaultSize());
	}
	
	public DiagramFactor(ObjectManager objectManager, int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new DiagramFactorId(idToUse), json);
		
		ORef wrappedRef = ORef.createFromString(json.getString(TAG_WRAPPED_REF));
		underlyingObjectRef.set(wrappedRef);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject jsonObject = super.toJson();
		jsonObject.put(TAG_WRAPPED_REF, underlyingObjectRef.toString());
		
		return jsonObject;
	}
	
	public static Dimension getDefaultSize(int type)
	{
		if (type == ObjectType.TEXT_BOX)
			return new Dimension(180, 30);
		
		return getDefaultSize();
	}
	
	public static Dimension getDefaultSize()
	{
		return new Dimension(120, 60);
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
		return ObjectType.DIAGRAM_FACTOR;
	}
	
	public ORefList getAllObjectsToDeepCopy()
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy();
		deepObjectRefsToCopy.addAll(getGroupBoxChildrenRefs());
		
		return deepObjectRefsToCopy;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public DiagramFactorId getDiagramFactorId()
	{
		return (DiagramFactorId)getId(); 
	}
	
	public int getWrappedType()
	{
		return getWrappedORef().getObjectType();
	}
	
	public FactorId getWrappedId()
	{
		return new FactorId(getWrappedORef().getObjectId().asInt());
	}
	
	public ORef getWrappedORef()
	{
		return underlyingObjectRef.getRawRef();
	}
	
	public Factor getWrappedFactor()
	{
		return Factor.findFactor(objectManager, getWrappedORef());
	}

	public Dimension getSize()
	{
		return size.getDimension();
	}
	
	public Point getLocation()
	{
		return location.getPoint();
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle(getLocation().x, getLocation().y, getSize().width, getSize().height);
	}
	
	public String getFontSize()
	{
		return fontSize.get();
	}
	
	public String getFontColor()
	{
		return fontColor.get();
	}
	
	public String getFontStyle()
	{
		return fontStyle.get();
	}
	
	public String getBackgroundColor()
	{
		return backgroundColor.get();
	}
	
	public void setLocation(Point pointToUse)
	{
		location.setPoint(pointToUse);
	}
	
	public void setSize(Dimension dimensionToUse)
	{
		size.setDimension(dimensionToUse);
	}
	
	public ORefList getGroupBoxChildrenRefs()
	{
		return groupBoxChildrenRefs.getORefList();
	}
	
	public ORef getOwningGroupBoxRef()
	{
		ORefList diagramFactorReferers = findObjectsThatReferToUs();
		ORef groupBoxDiagramFactorRef = diagramFactorReferers.getRefForType(DiagramFactor.getObjectType());

		return groupBoxDiagramFactorRef;
	}
	
	public ORefList getSelfAndChildren()
	{
		ORefList selfAndChildren = new ORefList(getRef());
		if (isGroupBoxFactor())
			selfAndChildren.addAll(getGroupBoxChildrenRefs());
		
		return selfAndChildren;
	}
	
	public ORefList getSelfOrChildren()
	{
		if (isGroupBoxFactor())
			return getGroupBoxChildrenRefs();
		
		return new ORefList(getRef());
	}
	
	public boolean isGroupBoxFactor()
	{
		if (getWrappedType() == GroupBox.getObjectType())
			return true;
		
		return false;
	}
	
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_GROUP_BOX_CHILDREN_REFS))
			return GroupBox.getObjectType();
		
		return super.getAnnotationType(tag);
	}
	
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_GROUP_BOX_CHILDREN_REFS))
			return true;
		
		return super.isRefList(tag);
	}
	
	public Command[] createCommandsToMirror(DiagramFactorId newlyCreatedId)
	{
		Vector commands = new Vector();
		String sizeAsString = EnhancedJsonObject.convertFromDimension(getSize());
		CommandSetObjectData setSizeCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_SIZE, sizeAsString);
		commands.add(setSizeCommand);
		
		String locationAsString = EnhancedJsonObject.convertFromPoint(getLocation());
		CommandSetObjectData setLocationCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_LOCATION, locationAsString);
		commands.add(setLocationCommand);
		
		CommandSetObjectData setFontSizeCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_FONT_SIZE, fontSize.get());
		commands.add(setFontSizeCommand);
		
		CommandSetObjectData setFontColorCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_FOREGROUND_COLOR, fontColor.get());
		commands.add(setFontColorCommand);
		
		CommandSetObjectData setFontStyleCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_FONT_STYLE, fontStyle.get());
		commands.add(setFontStyleCommand);
		
		CommandSetObjectData setBackgroundColorCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newlyCreatedId, DiagramFactor.TAG_BACKGROUND_COLOR, backgroundColor.get());
		commands.add(setBackgroundColorCommand);
		
		return (Command[]) commands.toArray(new Command[0]);
	}
	
	public boolean isCoveredByGroupBox()
	{
		ORefList groupBoxFactors = findObjectsThatReferToUs(DiagramFactor.getObjectType());
		return (groupBoxFactors.size() > 0);
	}
	
	public static void ensureType(ORef diagramFactorRef)
	{
		if (!is(diagramFactorRef))
			throw new RuntimeException(diagramFactorRef + " is not of type DiagramFactor");
	}
	
	public boolean isDefaultZOrder()
	{
		return textBoxZOrderCode.get().length() == 0;
	}
		
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static DiagramFactor find(ObjectManager objectManager, ORef diagramFactorRef)
	{
		return (DiagramFactor) objectManager.findObject(diagramFactorRef);
	}
	
	public static DiagramFactor find(Project project, ORef diagramFactorRef)
	{
		return find(project.getObjectManager(), diagramFactorRef);
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateDiagramFactorParameter(getWrappedORef());
	}
	
	void clear()
	{
		super.clear();

		size = new DimensionData(TAG_SIZE);
		location = new PointData(TAG_LOCATION);
		underlyingObjectRef = new ORefData(TAG_WRAPPED_REF);
		fontSize = new ChoiceData(TAG_FONT_SIZE, getQuestion(DiagramFactorFontSizeQuestion.class));
		fontColor = new ChoiceData(TAG_FOREGROUND_COLOR, getQuestion(DiagramFactorFontColorQuestion.class));
		fontStyle = new ChoiceData(TAG_FONT_STYLE, getQuestion(DiagramFactorFontStyleQuestion.class));
		groupBoxChildrenRefs = new ORefListData(TAG_GROUP_BOX_CHILDREN_REFS);
		backgroundColor = new ChoiceData(TAG_BACKGROUND_COLOR, getQuestion(DiagramFactorBackgroundQuestion.class));
		textBoxZOrderCode = new StringData(TAG_TEXT_BOX_Z_ORDER_CODE);
		
		addField(TAG_SIZE, size);
		addField(TAG_LOCATION, location);
		addField(TAG_WRAPPED_REF, underlyingObjectRef);
		addField(TAG_FONT_SIZE, fontSize);
		addField(TAG_FOREGROUND_COLOR, fontColor);
		addField(TAG_FONT_STYLE, fontStyle);
		addField(TAG_GROUP_BOX_CHILDREN_REFS, groupBoxChildrenRefs);
		addField(TAG_BACKGROUND_COLOR, backgroundColor);
		addField(textBoxZOrderCode);
	}
	
	public static final String TAG_LOCATION = "Location";
	public static final String TAG_SIZE = "Size";
	public static final String TAG_WRAPPED_REF = "WrappedFactorRef";
	public static final String TAG_FONT_SIZE = "FontSize";
	public static final String TAG_FOREGROUND_COLOR = "FontColor";
	public static final String TAG_FONT_STYLE = "FontStyle";
	public static final String TAG_GROUP_BOX_CHILDREN_REFS = "GroupBoxChildrenRefs";
	public static final String TAG_BACKGROUND_COLOR = "BackgroundColor";
	public static final String TAG_TEXT_BOX_Z_ORDER_CODE = "TextBoxZOrderCode";
	
	static final String OBJECT_NAME = "DiagramFactor";
	
	public static final Dimension DEFAULT_STRESS_SIZE = new Dimension(60, 30);
	public static final Dimension DEFAULT_ACTIVITY_SIZE = new Dimension(60, 30);

	private DimensionData size;
	private PointData location;
	private ORefData underlyingObjectRef;
	private ChoiceData fontSize;
	private ChoiceData fontColor;
	private ChoiceData fontStyle;
	private ORefListData groupBoxChildrenRefs;
	private ChoiceData backgroundColor;
	public StringData textBoxZOrderCode;
}
