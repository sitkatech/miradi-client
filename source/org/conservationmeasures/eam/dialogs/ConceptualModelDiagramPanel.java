/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class ConceptualModelDiagramPanel extends DiagramPanel
{
	public ConceptualModelDiagramPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse, ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
	}
}
