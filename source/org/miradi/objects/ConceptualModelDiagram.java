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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;

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
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static ConceptualModelDiagram find(ObjectManager objectManager, ORef conceptualModelRef)
	{
		return (ConceptualModelDiagram) objectManager.findObject(conceptualModelRef);
	}
	
	public static ConceptualModelDiagram find(Project project, ORef conceptualModelRef)
	{
		return find(project.getObjectManager(), conceptualModelRef);
	}
	
	public static final String OBJECT_NAME = "ConceptualModelDiagram";
	public static final String DEFAULT_MAIN_NAME = EAM.text("[Main Diagram]");
	public static final String DEFAULT_BLANK_NAME = EAM.text("[Not Named]");
}
