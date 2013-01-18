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
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ThreatStressRating;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.ThreatStressRatingSchema;

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
		verifyNormalCommand();			
		verifySideEffectCommandOusideOfSideEffectMode();
	}

	private void verifyNormalCommand() throws CommandFailedException
	{
		CommandCreateObject createStress = new CommandCreateObject(StressSchema.getObjectType());
		getProject().executeCommand(createStress);
	}

	private void verifySideEffectCommandOusideOfSideEffectMode()
	{
		assertTrue("should not be in side effect mode?", !getProject().isInCommandSideEffectMode());
		CommandCreateObject createTarget = new CommandCreateObject(TargetSchema.getObjectType());
		
		EAM.setLogToString();
		try
		{
			getProject().executeAsSideEffect(createTarget);
			fail("Should have thrown exception for executing a sideeffect command outside of sideeffect mode");
		}
		catch (Exception ignoreExpected)
		{
			assertContains("SEVERE", EAM.getLoggedString());
		}
		finally
		{
			EAM.setLogToConsole();
		}
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (event.isCreateCommandForThisType(StressSchema.getObjectType()))
			{
				ORef stressRef = new ORef(StressSchema.getObjectType(), new BaseId(100));
				ORef threatRef = new ORef(CauseSchema.getObjectType(), new BaseId(400));
				CommandCreateObject createThreatStressRating = new CommandCreateObject(ThreatStressRatingSchema.getObjectType());
				getProject().executeAsSideEffect(createThreatStressRating);
				
				final CommandSetObjectData setStressRefCommand = new CommandSetObjectData(createThreatStressRating.getObjectRef(), ThreatStressRating.TAG_STRESS_REF, stressRef.toString());
				getProject().executeAsSideEffect(setStressRefCommand);
				
				final CommandSetObjectData setThreatRefCommand = new CommandSetObjectData(createThreatStressRating.getObjectRef(), ThreatStressRating.TAG_THREAT_REF, threatRef.toString());
				getProject().executeAsSideEffect(setThreatRefCommand);
			}
		}
		catch (Exception e)
		{
			fail("failed to execute command as side effect");
		}
	}
}