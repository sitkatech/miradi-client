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

import org.miradi.commands.CommandCreateObject;
import org.miradi.ids.BaseId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.CreateThreatStressRatingParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.Stress;
import org.miradi.objects.ThreatStressRating;

public class TestProjectCommandExecutions extends TestCaseWithProject implements CommandExecutedListener
{
	public TestProjectCommandExecutions(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		getProject().addCommandExecutedListener(this);
	}
	
	@Override
	public void tearDown() throws Exception
	{
		getProject().removeCommandExecutedListener(this);
		super.tearDown();
	}
	
	public void testExecuteAsSideEffect() throws Exception
	{
		CommandCreateObject createStress = new CommandCreateObject(Stress.getObjectType());
		getProject().executeCommand(createStress);			
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (event.isCreateCommandForThisType(Stress.getObjectType()))
			{
				ORef stressRef = new ORef(Stress.getObjectType(), new BaseId(100));
				ORef threatRef = new ORef(Cause.getObjectType(), new BaseId(400));
				CreateThreatStressRatingParameter extraInfo = new CreateThreatStressRatingParameter(stressRef, threatRef);
				CommandCreateObject createThreatStressRating = new CommandCreateObject(ThreatStressRating.getObjectType(), extraInfo);
				getProject().executeAsSideEffect(createThreatStressRating);	
			}
		}
		catch (Exception e)
		{
			fail("failed to execute command as side effect");
		}
	}
}