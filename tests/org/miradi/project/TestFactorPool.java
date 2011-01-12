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
package org.miradi.project;

import org.martus.util.TestCaseEnhanced;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objects.Factor;

public class TestFactorPool extends TestCaseEnhanced
{
	public TestFactorPool(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		idAssigner = new IdAssigner();
		project = new ProjectForTesting(getName());
		
		for (int i = 0; i < FACTOR_COUNT; ++i)
		{
			addNewlyCreatedNodeToPool(getObjectType());
		}
	}	
	
	protected void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}

	public int getObjectType() throws Exception
	{
		throw new Exception();
	}
	
	public void testBasics() throws Exception
	{
		assertEquals("wrong target count?", FACTOR_COUNT, project.getPool(getObjectType()).getIds().length);
	}

	protected FactorId addNewlyCreatedNodeToPool(int type) throws Exception
	{
		FactorId id = takeNextModelNodeId();		
		Factor node = Factor.createConceptualModelObject(project.getObjectManager(), id, type);
		project.getPool(type).put(node.getId(), node);
		
		return id;
	}

	private FactorId takeNextModelNodeId()
	{
		return new FactorId(idAssigner.takeNextId().asInt());
	}
	
	IdAssigner idAssigner;
	ProjectForTesting project;
	
	protected static final int FACTOR_COUNT = 3;
}
