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

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;

public class TestStress extends ObjectTestCase
{
	public TestStress(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(ObjectType.STRESS);
	}
	
	public void testCalculateStressRating() throws Exception
	{
		ORef stressRef = getProject().createObject(Stress.getObjectType());
		Stress stress = (Stress) getProject().findObject(stressRef);
		assertEquals("has value?", "", stress.getCalculatedStressRating());
		
		stress.setData(Stress.TAG_SCOPE, "1");
		assertEquals("has value?", "", stress.getCalculatedStressRating());
		
		stress.setData(Stress.TAG_SEVERITY, "4");
		assertEquals("has value?", 1, stress.calculateStressRating());
		assertEquals("is not min value", 1, stress.calculateStressRating());
	}
	
	public void testIsShared() throws Exception
	{
		Stress stress = getProject().createStress();
		assertFalse("Stress is shared?", stress.mustBeDeletedBecauseParentIsGone());
		
		Target target = getProject().createTarget();
		ORefList stressRefs = new ORefList(stress.getRef());
		target.setData(Target.TAG_STRESS_REFS, stressRefs.toString());
		
		assertTrue("Stress is not shared?", stress.mustBeDeletedBecauseParentIsGone());
		
		DiagramFactor diagramFactor = getProject().createDiagramFactorAndAddToDiagram(Stress.getObjectType());
		assertTrue("Stress is not shared?", diagramFactor.getWrappedFactor().mustBeDeletedBecauseParentIsGone());
	}
}
