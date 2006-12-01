/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.martus.swing.UiList;
import org.martus.swing.UiScrollPane;

public class CodeListEditorComponent extends DisposablePanel
{
	public CodeListEditorComponent(Project projectToUse)
	{
		super(new BorderLayout());
		project = projectToUse;

		resourceListTable = new UiList();
		rebuild();
		
		add(new UiScrollPane(resourceListTable), BorderLayout.CENTER);
	}
	
	public void dispose()
	{
		super.dispose();
	}
	
	public void setList(CodeList selectedCodeList)
	{
		//resourceListTable.add(codeList.getDataModel());
	}
	
	public void rebuild()
	{
	}
	
	
	Project project;
	UiList resourceListTable;
}