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

package org.miradi.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.miradi.main.EAM;

import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;
import net.atlanticbb.tantlinger.ui.text.actions.PasteAction;

public class PasteSanitizedTextAction extends PasteAction
{
	@Override
    protected void wysiwygEditPerformed(ActionEvent e, JEditorPane editor)
    {        
        HTMLEditorKit editorKit = (HTMLEditorKit)editor.getEditorKit();
        HTMLDocument document = (HTMLDocument)editor.getDocument();        
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try 
        {
            CompoundUndoManager.beginCompoundEdit(document);
            Transferable content = clipboard.getContents(this);                           
            String text = content.getTransferData(new DataFlavor(String.class, "String")).toString();
            text = AbstractHtmlPane.getNormalizedAndSanitizedHtmlText(text);
        
            document.replace(editor.getSelectionStart(), editor.getSelectionEnd() - editor.getSelectionStart(), text, editorKit.getInputAttributes());
        } 
        catch(Exception exception) 
        {
        	EAM.alertUserOfNonFatalException(exception);
        }
        finally
        {
            CompoundUndoManager.endCompoundEdit(document);
        }
    }    
}