/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ViewDoer;

public class SelectAllDoer extends ViewDoer 
{
    public SelectAllDoer()
    {
        super();
    }
    
    public boolean isAvailable()
    {
        if(!getProject().isOpen())
            return false;
        
        int nSize = getProject().getDiagramModel().getAllNodes().size();
        return (nSize > 0);
    }
    
    public void doIt() throws CommandFailedException 
    {
        DiagramView view = (DiagramView)getView();
        view.getDiagramComponent().selectAll();
    }
}
