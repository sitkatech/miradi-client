/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objects;

import java.util.Set;

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
	
	
	public static boolean canReferToThisType(int type)
	{
		switch(type)
		{
			case ObjectType.CONCEPTUAL_MODEL_DIAGRAM:
			case ObjectType.RESULTS_CHAIN_DIAGRAM:
				return true;
			default:
				return false;
		}
	}
	
	public Set<String> getReferencedObjectTags()
	{
		Set<String> set = super.getReferencedObjectTags();
		set.add(TAG_DIAGRAM_OBJECT_REF);
		return set;
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