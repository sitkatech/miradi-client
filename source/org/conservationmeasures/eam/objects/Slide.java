/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Set;

import org.conservationmeasures.eam.ids.SlideId;
import org.conservationmeasures.eam.objectdata.CodeListData;
import org.conservationmeasures.eam.objectdata.ORefData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

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
	
	public Slide(int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(new SlideId(idAsInt), jsonObject);
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
		diagramObjectRef = new ORefData();
		diagramObjectLabel = new PseudoStringData(PSEUDO_TAG_DIAGRAM_OBJECT_LABEL);
		legendSettings = new CodeListData();
		
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