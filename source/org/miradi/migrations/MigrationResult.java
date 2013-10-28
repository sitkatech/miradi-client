/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.migrations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.miradi.main.EAM;


public class MigrationResult extends HashSet<String>
{
	private MigrationResult()
	{
		dataLossMessages = new Vector<String>();
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

	public void addDataLoss(String dataLossMessageToUse)
	{
		dataLossMessages.add(dataLossMessageToUse);
	}
	
	public int dataLossCount()
	{
		return dataLossMessages.size();
	}
	
	public void merge(MigrationResult migrationResult)
	{
		dataLossMessages.addAll(migrationResult.dataLossMessages);
		addAll(migrationResult);
	}
	
	public boolean didSucceed()
	{
		return contains(SUCCESS);
	}
	
	public boolean didLoseData()
	{
		return !dataLossMessages.isEmpty();
	}
	
	public boolean didFail()
	{
		return contains(FAILED);
	}
	
	public boolean cannotMigrate()
	{
		return contains(CANNOT_MIGRATE);
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
		HashMap<String, Integer> messageToCountMap = groupMessagesWithCount();
		Set<String> messages = messageToCountMap.keySet();
		StringBuffer messagesAsString = new StringBuffer();
		for(String message : messages)
		{
			int messageCount = messageToCountMap.get(message);
			String dataLossMessage = getDataLossMessage(message, messageCount);
			messagesAsString.append(dataLossMessage + "\n");
		}
		
		return messagesAsString.toString();
	}

	private String getDataLossMessage(String message, int messageCount)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%messageCount", Integer.toString(messageCount));
		tokenReplacementMap.put("%message", message);
		String dataLossMessage = EAM.substitute(EAM.text("%messageCount case(s) of: %message"), tokenReplacementMap);
		
		return dataLossMessage;
	}

	private HashMap<String, Integer> groupMessagesWithCount()
	{
		HashMap<String, Integer> messageToCountMap = new HashMap<String, Integer>();
		for (int index = 0; index < dataLossMessages.size(); ++index)
		{
			String message = dataLossMessages.get(index);
			int messageCount = 0;
			if (messageToCountMap.containsKey(message))
				messageCount = messageToCountMap.get(message);
			
			messageToCountMap.put(message, messageCount + 1);
		}
		
		return messageToCountMap;
	}

	private static final String SUCCESS = "Success";
	private static final String FAILED = "Failed";
	private static final String CANNOT_MIGRATE = "CannotMigrate";
	private static final String UNINITIALIZED = "Uninitialized";
	
	private Vector<String> dataLossMessages;
}
