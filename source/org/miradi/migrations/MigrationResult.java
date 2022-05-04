/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.utils.HtmlUtilities;


public class MigrationResult extends HashSet<String>
{
	private MigrationResult()
	{
		dataLossMessages = new Vector<String>();
		cannotMigrateMessages = new Vector<String>();
	}
	
	public static MigrationResult createUninitializedResult()
	{
		MigrationResult migrationResult = new MigrationResult();
		migrationResult.add(UNINITIALIZED);
		
		return migrationResult;
	}
	
	public static MigrationResult createSuccess()
	{
		MigrationResult migrationResult = new MigrationResult();
		migrationResult.addSuccess();
		
		return migrationResult;
	}
	
	public static MigrationResult createDataLoss(String dataLossMessageToUse)
	{
		MigrationResult migrationResult = new MigrationResult();
		migrationResult.addDataLoss(dataLossMessageToUse);
		
		return migrationResult;
	}

	public static MigrationResult createCannotMigrate(String messageToUse)
	{
		MigrationResult migrationResult = new MigrationResult();
		migrationResult.add(CANNOT_MIGRATE);
		migrationResult.addCannotMigrate(messageToUse);

		return migrationResult;
	}

	public void addDataLoss(String dataLossMessageToUse)
	{
		dataLossMessages.add(dataLossMessageToUse);
	}
	
	public int dataLossCount()
	{
		return dataLossMessages.size();
	}

	public void addCannotMigrate(String messageToUse)
	{
		cannotMigrateMessages.add(messageToUse);
	}

	public int cannotMigrateCount()
	{
		return cannotMigrateMessages.size();
	}

	public void merge(MigrationResult migrationResult)
	{
		dataLossMessages.addAll(migrationResult.dataLossMessages);
		cannotMigrateMessages.addAll(migrationResult.cannotMigrateMessages);
		addAll(migrationResult);
	}
	
	public boolean didSucceed()
	{
		return contains(SUCCESS) || isUninitialized();
	}
	
	public boolean didLoseData()
	{
		return !dataLossMessages.isEmpty();
	}
	
	public boolean didFail()
	{
		return contains(FAILED) || !didSucceed();
	}
	
	public boolean cannotMigrate()
	{
		return contains(CANNOT_MIGRATE);
	}

	public boolean isUninitialized()
	{
		return this.size() == 1 && contains(UNINITIALIZED);
	}

	public void addSuccess()
	{
		setInitialized();
		add(SUCCESS);
	}

	private void setInitialized()
	{
		remove(UNINITIALIZED);
	}

	public String getUserFriendlyGroupedDataLossMessagesAsString()
	{
		HashMap<String, Integer> messageToCountMap = groupMessagesWithCount(this.dataLossMessages);
		Set<String> messages = messageToCountMap.keySet();
		StringBuffer messagesAsString = new StringBuffer();
		for(String message : messages)
		{
			int messageCount = messageToCountMap.get(message);
			String dataLossMessage = getMessageWithCount(message, messageCount);
			messagesAsString.append(HtmlUtilities.stripAllHtmlTags(dataLossMessage) + "\n");
		}
		
		return messagesAsString.toString();
	}

	public String getUserFriendlyGroupedCannotMigrateMessagesAsString()
	{
		HashMap<String, Integer> messageToCountMap = groupMessagesWithCount(this.cannotMigrateMessages);
		Set<String> messages = messageToCountMap.keySet();
		StringBuffer messagesAsString = new StringBuffer();
		for(String message : messages)
		{
			messagesAsString.append(HtmlUtilities.stripAllHtmlTags(message) + "\n");
		}

		return messagesAsString.toString();
	}

	private String getMessageWithCount(String message, int messageCount)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%messageCount", Integer.toString(messageCount));
		tokenReplacementMap.put("%migrationMessage", message);
		String migrationMessage = EAM.substitute(EAM.text("%messageCount case(s) of: %migrationMessage"), tokenReplacementMap);
		
		return migrationMessage;
	}

	private HashMap<String, Integer> groupMessagesWithCount(Vector<String> messages)
	{
		HashMap<String, Integer> messageToCountMap = new HashMap<String, Integer>();
		for (int index = 0; index < messages.size(); ++index)
		{
			String message = messages.get(index);
			int countOfThisMessage = 0;
			if (messageToCountMap.containsKey(message))
				countOfThisMessage = messageToCountMap.get(message);
			
			messageToCountMap.put(message, countOfThisMessage + 1);
		}
		
		return messageToCountMap;
	}

	private static final String SUCCESS = "Success";
	private static final String FAILED = "Failed";
	private static final String CANNOT_MIGRATE = "CannotMigrate";
	private static final String UNINITIALIZED = "Uninitialized";
	
	private Vector<String> dataLossMessages;
	private Vector<String> cannotMigrateMessages;
}
