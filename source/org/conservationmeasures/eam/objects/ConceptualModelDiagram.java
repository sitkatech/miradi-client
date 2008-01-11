/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ConceptualModelDiagram extends DiagramObject
{
	public ConceptualModelDiagram(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
	}
	
	public ConceptualModelDiagram(ObjectManager objectManager, int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, json);
	}
	
	public ConceptualModelDiagram(int idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, json);
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
		return ObjectType.CONCEPTUAL_MODEL_DIAGRAM;
	}
	
	public static final String OBJECT_NAME = "ConceptualModelDiagram";
	public static final String DEFAULT_MAIN_NAME = EAM.text("[Main Diagram]");
	public static final String DEFAULT_BLANK_NAME = EAM.text("[Not Named]");
}
