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
package org.miradi.main;

import org.miradi.diagram.DiagramModel;
import org.miradi.project.ObjectManager;
import org.miradi.project.ProjectForTesting;

public class TestCaseWithProject extends EAMTestCase
{
	public TestCaseWithProject(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}

	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}

	public ProjectForTesting getProject()
	{
		return project;
	}
	
	public DiagramModel getDiagramModel()
	{
		return getProject().getDiagramModel();
	}
	
	public ObjectManager getObjectManager()
	{
		return getProject().getObjectManager();
	}
	
	private ProjectForTesting project;
}
