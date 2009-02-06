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

import org.miradi.ids.SlideId;
import org.miradi.objectdata.CodeListData;
import org.miradi.objectdata.ORefData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.questions.InternalQuestionWithoutValues;
import org.miradi.utils.EnhancedJsonObject;

public class Slide extends BaseObject
{
	public Slide(ObjectManager objectManager, SlideId id)
	{
		super(objectManager, id);
		clear();
	}
	
	public Slide(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new SlideId(idAsInt), jsonObject);
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
		return ObjectType.SLIDE;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_DIAGRAM_OBJECT_LABEL))
			return getDiagramObjectLabel();
		return super.getPseudoData(fieldTag);
	}
	
	public ORef getDiagramRef()
	{
		return diagramObjectRef.getRawRef();
	}
	
	public String getDiagramObjectLabel()
	{
		if (!diagramObjectRef.isValid())
			return "*Diagram was deleted*";

		BaseObject object = getObjectManager().findObject(diagramObjectRef.getRawRef());
		return object.getLabel();
	}

	void clear()
	{
		super.clear();
		diagramObjectRef = new ORefData(TAG_DIAGRAM_OBJECT_REF);
		diagramObjectLabel = new PseudoStringData(PSEUDO_TAG_DIAGRAM_OBJECT_LABEL);
		legendSettings = new CodeListData(TAG_DIAGRAM_LEGEND_SETTINGS, getQuestion(InternalQuestionWithoutValues.class));
		
		addField(TAG_DIAGRAM_OBJECT_REF, diagramObjectRef);
		addField(PSEUDO_TAG_DIAGRAM_OBJECT_LABEL, diagramObjectLabel);
		addField(TAG_DIAGRAM_LEGEND_SETTINGS, legendSettings);
	}
	
	public static String TAG_DIAGRAM_LEGEND_SETTINGS = "DiagramLegendSettings";
	public static String TAG_DIAGRAM_OBJECT_REF = "DiagramObjectRef";
	public static String PSEUDO_TAG_DIAGRAM_OBJECT_LABEL = "DiagramObjectLabel";

	public static final String OBJECT_NAME = "Slide";

	
	private ORefData diagramObjectRef;
	PseudoStringData diagramObjectLabel;
	CodeListData legendSettings;
}