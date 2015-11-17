/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
		getProject().fillObjectUsingCommand(strategy, Strategy.TAG_ASSIGNED_LEADER_RESOURCE, resourceAssignment.getResourceRef());
		verifyLeaderIsSet(strategy, resourceAssignment);
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_RESOURCE_ID, "");
		verifyLeader(strategy);
	}
	
	public void testResourceLeaderIsUpdatedWhenResourceAssignmentIsDeleted() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().addResourceAssignment(strategy);
		getProject().fillObjectUsingCommand(strategy, Strategy.TAG_ASSIGNED_LEADER_RESOURCE, resourceAssignment.getResourceRef());
		verifyLeaderIsSet(strategy, resourceAssignment);
		getProject().executeCommand(CommandSetObjectData.createRemoveIdCommand(strategy, Strategy.TAG_RESOURCE_ASSIGNMENT_IDS, resourceAssignment.getId()));
		verifyLeader(strategy);
	}

	public void testResourceLeaderIsNotChangedWithMultpleResourceAssignemntReferrersAndOneIsUpdated() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignmentWithSameResource1 = getProject().addResourceAssignment(strategy);
		ResourceAssignment resourceAssignmentWithSameResource2 = getProject().addResourceAssignment(strategy);
		getProject().fillObjectUsingCommand(resourceAssignmentWithSameResource2, ResourceAssignment.TAG_RESOURCE_ID, resourceAssignmentWithSameResource1.getId());
		getProject().fillObjectUsingCommand(strategy, Strategy.TAG_ASSIGNED_LEADER_RESOURCE, resourceAssignmentWithSameResource1.getResourceRef());
		verifyLeaderIsSet(strategy, resourceAssignmentWithSameResource1);
		getProject().fillObjectUsingCommand(resourceAssignmentWithSameResource2, ResourceAssignment.TAG_RESOURCE_ID, "");
		verifyCorrectLeader(strategy, resourceAssignmentWithSameResource1);
	}

	public void testResourceLeaderIsNotChangedWithMultipleResourceAssignemntReferrersAndOneIsDeleted() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignmentWithSameResource1 = getProject().addResourceAssignment(strategy);
		ResourceAssignment resourceAssignmentWithSameResource2 = getProject().addResourceAssignment(strategy);
		getProject().fillObjectUsingCommand(resourceAssignmentWithSameResource2, ResourceAssignment.TAG_RESOURCE_ID, resourceAssignmentWithSameResource1.getId());
		getProject().fillObjectUsingCommand(strategy, Strategy.TAG_ASSIGNED_LEADER_RESOURCE, resourceAssignmentWithSameResource1.getResourceRef());
		verifyLeaderIsSet(strategy, resourceAssignmentWithSameResource1);
		getProject().executeCommand(CommandSetObjectData.createRemoveIdCommand(strategy, Strategy.TAG_RESOURCE_ASSIGNMENT_IDS, resourceAssignmentWithSameResource2.getId()));
		verifyCorrectLeader(strategy, resourceAssignmentWithSameResource1);
	}

	private void verifyLeaderIsSet(Strategy strategy, ResourceAssignment resourceAssignment)
	{
		assertEquals("leader not set?", resourceAssignment.getResourceRef(), strategy.getLeaderResourceRef());
	}
	
	private void verifyCorrectLeader(Strategy strategy, ResourceAssignment resourceAssignment)
	{
		assertTrue("leader should not be invalid?", strategy.getLeaderResourceRef().isValid());
		assertEquals("leader removed?", resourceAssignment.getResourceRef(), strategy.getLeaderResourceRef());
	}
	
	public void verifyLeader(Strategy strategy)
	{
		assertEquals("leader not removed?", ORef.INVALID, strategy.getLeaderResourceRef());
	}
}
