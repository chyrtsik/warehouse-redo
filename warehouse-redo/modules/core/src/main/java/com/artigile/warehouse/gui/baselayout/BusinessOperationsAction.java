package com.artigile.warehouse.gui.baselayout;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.windows.TopComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@ActionID(category = "Window", id = "com.artigile.warehouse.gui.baselayout.BusinessOperationsTopComponent")
@ActionRegistration(displayName="#CTL_BusinessOperationsAction")
@ActionReference(path = "Menu/Window", position = 100)
public class BusinessOperationsAction implements ActionListener {
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = BusinessOperationsTopComponent.findInstance();
        win.open();
        win.requestActive();
    }    
}
