/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.project.Project;

abstract public class AbstractEditorComponent extends DisposablePanel
{
	public AbstractEditorComponent(Project projectToUse)
	{
		super(new BorderLayout());
		
		project = projectToUse;
	}
	
	public Project getProject()
	{
		return project;
	}
	
	private Project project;
}
