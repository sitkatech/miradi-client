/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

import java.awt.datatransfer.DataFlavor;

import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

public class TransferableMiradiListVersion4 extends AbstractTransferableMiradiList
{
	public TransferableMiradiListVersion4(Project projectToUse,	ORef diagramObjectRefCopiedFromToUse)
	{
		super(projectToUse, diagramObjectRefCopiedFromToUse);
	}

	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor[] flavorArray = {miradi4ListDataFlavor };
		return flavorArray;
	}

	public static DataFlavor miradi4ListDataFlavor = new DataFlavor(TransferableMiradiListVersion4.class, "Miradi version 4 Objects");
}
