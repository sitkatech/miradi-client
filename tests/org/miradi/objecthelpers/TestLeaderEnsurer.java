/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.objecthelpers;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Strategy;

public class TestLeaderEnsurer extends TestCaseWithProject
{
	public TestLeaderEnsurer(String name)
	{
		super(name);
	}
	
	public void testResourceLeaderIsUpdatedWhenReourceAssignmentIsUpdated() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().addResourceAssignment(strategy);
		getProject().fillObjectUsingCommand(strategy, Strategy.TAG_LEADER_RESOURCE, resourceAssignment.getResourceRef());
		assertEquals("leader not set?", resourceAssignment.getResourceRef(), strategy.getLeaderResourceRef());
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_RESOURCE_ID, "");
		assertEquals("leader not removed", ORef.INVALID, strategy.getLeaderResourceRef());
	}
	
	public void testResourceLeaderIsUpdatedWhenResourceAssignmentIsDeleted() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().addResourceAssignment(strategy);
		getProject().fillObjectUsingCommand(strategy, Strategy.TAG_LEADER_RESOURCE, resourceAssignment.getResourceRef());
		assertEquals("leader not set?", resourceAssignment.getResourceRef(), strategy.getLeaderResourceRef());
		getProject().executeCommand(CommandSetObjectData.createRemoveIdCommand(strategy, Strategy.TAG_RESOURCE_ASSIGNMENT_IDS, resourceAssignment.getId()));
		assertEquals("leader not removed", ORef.INVALID, strategy.getLeaderResourceRef());
	}
}
