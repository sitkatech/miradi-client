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
package org.miradi.rtf.viewExporters;

import org.miradi.dialogs.base.ObjectTableModel;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.rtf.RtfManagementExporter;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.CodeList;
import org.miradi.utils.ObjectTableModelExporter;

abstract public class RtfViewExporter
{
	public RtfViewExporter(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}
	
	protected void exportObjectTableModel(RtfWriter writer, ObjectTableModel objectTableModel, String translatedTableName) throws Exception
	{
		exportTable(writer, new ObjectTableModelExporter(objectTableModel), translatedTableName);
	}
	
	protected void exportTable(RtfWriter writer, AbstractTableExporter tableExporter, String translatedTableName) throws Exception
	{
		writer.startBlock();
		writer.writeHeading2Style();
		writer.writelnEncoded(translatedTableName);
		writer.writeParCommand();
		writer.endBlock();
		writer.newParagraph();
		
		createRtfManagementRtfExporter().writeManagement(tableExporter, writer);
		writer.newParagraph();
	}
	
	private RtfManagementExporter createRtfManagementRtfExporter()
	{
		return new RtfManagementExporter(getProject());
	}
		
	protected Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	protected MainWindow getMainWindow()
	{
		return mainWindow;
	}

	abstract public void exportView(RtfWriter writer, CodeList reportTemplateContent) throws Exception;
	
	private MainWindow mainWindow;
}
