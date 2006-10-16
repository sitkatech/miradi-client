/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.jgraph.graph.GraphLayoutCache;

public class TestDiagramComponent extends EAMTestCase 
{
    public TestDiagramComponent(String name)
    {
        super(name);
    }
    
    public void testSelectAll() throws Exception
    {
            ProjectForTesting project = new ProjectForTesting(getName());
            
            DiagramComponent dc = new DiagramComponent();
            dc.setModel(project.getDiagramModel());
            dc.setGraphLayoutCache(project.getGraphLayoutCache());
            
            CreateModelNodeParameter cmnp = new CreateModelNodeParameter(new NodeTypeFactor());
            ModelNodeId hiddenId = (ModelNodeId)project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, cmnp);
            ModelNodeId visibleId = (ModelNodeId)project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, cmnp);
            ConceptualModelLinkage cml = new ConceptualModelLinkage(new BaseId(100), hiddenId, visibleId);
            
            DiagramNode hiddenNode = dc.getDiagramModel().createNode(hiddenId);
            DiagramNode visibleNode = dc.getDiagramModel().createNode(visibleId);
            DiagramLinkage dL = dc.getDiagramModel().createLinkage(cml);
            
            GraphLayoutCache graphLayoutCache = dc.getGraphLayoutCache();
            graphLayoutCache.setVisible(cml, false);
            graphLayoutCache.setVisible(visibleNode, true);
            graphLayoutCache.setVisible(hiddenNode, false);
            
            assertFalse("Linkage still visible?", graphLayoutCache.isVisible(cml));
            assertFalse("Hidden Node still visible?", graphLayoutCache.isVisible(hiddenNode));
            assertTrue("Visible Node Not visible?", graphLayoutCache.isVisible(visibleNode));
            
            dc.selectAll();
            Object []selectionCells = dc.getSelectionCells();
            assertEquals("Selection count wrong?", 1, selectionCells.length);
            assertEquals("Wrong selection?", visibleNode, selectionCells[0]);
    }
    

}
