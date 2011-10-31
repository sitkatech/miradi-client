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

import java.util.HashSet;
import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objectdata.PseudoRefListData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
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
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public boolean canContainFactorType(int objectType)
	{
		if(Cause.is(objectType))
			return true;
		if(Stress.is(objectType))
			return true;
		
		return super.canContainFactorType(objectType);
	}

	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	@Override
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_DRAFT_STRATEGY_REFS))
			return getDraftStrategyRefsAsString();
		
		return super.getPseudoData(fieldTag);
	}
	
	private String getDraftStrategyRefsAsString()
	{
		HashSet<Factor> strategies = getFactorsOfType(Strategy.getObjectType());
		Vector<Strategy> allDraftStrategies = getProject().getStrategyPool().getDraftStrategiesAsVector();
		allDraftStrategies.retainAll(strategies);
		
		return new ORefList(new Vector<BaseObject>(allDraftStrategies)).toString();
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
	
	@Override
	public void clear()
	{
		super.clear();
		
		draftStrategies = new PseudoRefListData(this, PSEUDO_DRAFT_STRATEGY_REFS);
		
		addField(PSEUDO_DRAFT_STRATEGY_REFS, draftStrategies);
	}

	public static final String PSEUDO_DRAFT_STRATEGY_REFS = "PseudoDraftStrategies";
	
	public static final String OBJECT_NAME = "ConceptualModelDiagram";
	public static final String DEFAULT_MAIN_NAME = EAM.text("[Main Diagram]");
	public static final String DEFAULT_BLANK_NAME = EAM.text("[Not Named]");
	
	private PseudoRefListData draftStrategies;
}
