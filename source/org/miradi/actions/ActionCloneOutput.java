package org.miradi.actions;

import org.miradi.icons.OutputIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCloneOutput extends ObjectsAction
{
    public ActionCloneOutput(MainWindow mainWindowToUse)
    {
        super(mainWindowToUse, getLabel(), new OutputIcon());
    }

    private static String getLabel()
    {
        return EAM.text("Action|Manage|Create from Existing...");
    }

    @Override
    public String getToolTipText()
    {
        return EAM.text("TT|Create from an Existing Output");
    }
}