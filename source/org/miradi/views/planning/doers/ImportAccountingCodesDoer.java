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
package org.miradi.views.planning.doers;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Vector;

import org.martus.swing.UiFileChooser;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.AccountingCodeData;
import org.miradi.objecthelpers.AccountingCodeLoader;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.AccountingCode;
import org.miradi.project.Project;
import org.miradi.views.ViewDoer;

public class ImportAccountingCodesDoer extends ViewDoer
{
	public boolean isAvailable() 
	{
		return getProject().isOpen();
	}

	public void doIt() throws CommandFailedException 
	{
		if (!isAvailable())
			return;
		
		File startingDirectory = UiFileChooser.getHomeDirectoryFile();
		String windowTitle = EAM.text("Import Accounting Codes");
		UiFileChooser.FileDialogResults results = UiFileChooser.displayFileOpenDialog(
				getMainWindow(), windowTitle, UiFileChooser.NO_FILE_SELECTED, startingDirectory, null, null);
		
		if (results.wasCancelChoosen())
			return;
		
		File fileToImport = results.getChosenFile();
		try 
		{
			FileReader fileReader  = new FileReader(fileToImport);
			try
			{
				importCodes(fileReader, getProject());
				EAM.notifyDialog(EAM.text("Import Completed"));
			}
			catch (ImportFileErrorException e)
			{
				EAM.errorDialog(EAM.text("Unable to process file: verify file format"));
			}
			finally
			{
				fileReader.close();
			}
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}


	static public AccountingCode[] importCodes(Reader fileToImport, Project project) throws Exception 
	{
		AccountingCodeData[] accountingCodes = new AccountingCodeData[0];

		project.executeCommand(new CommandBeginTransaction());
		try
		{
			accountingCodes = AccountingCodeLoader.load(fileToImport);
		}
		catch (Exception e)
		{
			throw(new ImportFileErrorException());
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
		
		return createAccountingCodeObjectsFromDataObjects(project, accountingCodes);
	}


	private static AccountingCode[] createAccountingCodeObjectsFromDataObjects(Project project, AccountingCodeData[] accountingCodes) throws Exception
	{
		AccountingCodesDataMap map = new AccountingCodesDataMap(project);
		Vector accountingCodeVector = new Vector();
		for (int i=0; i<accountingCodes.length; ++i) 
		{
			AccountingCodeData AccountingCodeData = accountingCodes[i];
			
			if (map.isDuplicate(AccountingCodeData.getCode(), AccountingCodeData.getLabel()))
					continue;
			
			CommandCreateObject cmd = new CommandCreateObject(ObjectType.ACCOUNTING_CODE);
			project.executeCommand(cmd);
			
			BaseId baseId = cmd.getCreatedId();
			AccountingCode accountingCode = (AccountingCode)project.findObject(ObjectType.ACCOUNTING_CODE, baseId);
			accountingCodeVector.add(accountingCode);

			project.executeCommand(new CommandSetObjectData(
					ObjectType.ACCOUNTING_CODE, baseId, 
					AccountingCode.TAG_LABEL, AccountingCodeData.getLabel()));
			
			project.executeCommand(new CommandSetObjectData(
					ObjectType.ACCOUNTING_CODE, baseId, 
					AccountingCode.TAG_CODE, AccountingCodeData.getCode()));
		}

		AccountingCode[] toArray = (AccountingCode[])accountingCodeVector.toArray(new AccountingCode[0]);
		return toArray;
	}
	
}


class ImportFileErrorException extends Exception {}

class AccountingCodesDataMap extends HashMap
{
	public AccountingCodesDataMap(Project project)
	{
		super();
		loadExistingCodes(project);
	}
	
	public boolean isDuplicate(String code, String label)
	{
		if (!containsKey(code))
			return false;
				
		return (get(code).equals(label));
	}
	
	private void loadExistingCodes(Project project)
	{
		EAMObjectPool accountingCodePool = project.getPool(ObjectType.ACCOUNTING_CODE);
		BaseId[] existingBaseIds = accountingCodePool.getIds();
		for (int i =0; i< existingBaseIds.length; ++i)
		{
			String key = project.getObjectData(ObjectType.ACCOUNTING_CODE, existingBaseIds[i], AccountingCode.TAG_CODE);
			String value = project.getObjectData(ObjectType.ACCOUNTING_CODE, existingBaseIds[i], AccountingCode.TAG_LABEL);
			put(key, value);
		}
	}
}
