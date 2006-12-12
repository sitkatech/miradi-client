/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.AccountingCodeData;
import org.conservationmeasures.eam.objecthelpers.AccountingCodeLoader;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;
import org.martus.swing.UiFileChooser;

public class ImportAccountingCodesDoer extends ViewDoer
{
	public boolean isAvailable() 
	{
		return true;
	}

	public void doIt() throws CommandFailedException 
	{
		File startingDirectory = UiFileChooser.getHomeDirectoryFile();
		String windowTitle = EAM.text("Import Accounting Codes");
		UiFileChooser.FileDialogResults results = UiFileChooser.displayFileOpenDialog(
				getMainWindow(), windowTitle, UiFileChooser.NO_FILE_SELECTED, startingDirectory, null, null);
		
		if (results.wasCancelChoosen())
			return;
		
		File fileToImport = results.getChosenFile();
		try 
		{
			Project project = getProject();
			importCodes(new BufferedReader(new FileReader(fileToImport)), project);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		
		EAM.notifyDialog(EAM.text("Import Competed"));
	}


	static public AccountingCode[] importCodes(String fileToImport, Project project) throws Exception
	{
		return importCodes(new BufferedReader(new StringReader(fileToImport)),project);
	}
	

	static public AccountingCode[] importCodes(BufferedReader fileToImport, Project project) throws Exception
	{
		AccountingCodeData[] accountingCodes = new AccountingCodeData[0];
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			accountingCodes = AccountingCodeLoader.load(fileToImport);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
		return processData(project, accountingCodes);
	}


	private static AccountingCode[] processData(Project project, AccountingCodeData[] accountingCodes) throws Exception
	{
		AccountMap map = new AccountMap(project);
		Vector accountingCodeVector = new Vector();
		for (int i=0; i<accountingCodes.length; ++i) 
		{
			AccountingCodeData AccountingCodeData = accountingCodes[i];
			
			if (map.isDuplicate(AccountingCodeData.getCode(), AccountingCodeData.getLabel()))
					continue;
			
			CommandCreateObject cmd = new CommandCreateObject(ObjectType.ACCOUNTING_CODE);
			project.executeCommand(cmd);
			
			BaseId baseId = cmd.getCreatedId();
			AccountingCode accountingCode = new AccountingCode(baseId);
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

class AccountMap extends HashMap
{
	public AccountMap(Project project)
	{
		super();
		EAMObjectPool accountingCodePool = project.getPool(ObjectType.ACCOUNTING_CODE);
		BaseId[] existingBaseIds = accountingCodePool.getIds();
		loadExistingCodes(project, existingBaseIds);
	}
	
	public boolean isDuplicate(String code, String label)
	{
		if (!containsKey(code))
			return false;
				
		return (get(code).equals(label));
	}
	
	private void loadExistingCodes(Project project, BaseId[] existingBaseIds)
	{
		for (int i =0; i< existingBaseIds.length; ++i)
		{
			String key = project.getObjectData(ObjectType.ACCOUNTING_CODE, existingBaseIds[i], AccountingCode.TAG_CODE);
			String value = project.getObjectData(ObjectType.ACCOUNTING_CODE, existingBaseIds[i], AccountingCode.TAG_LABEL);
			put(key, value);
		}
	}
}
