/*
 * ARX: Powerful Data Anonymization
 * Copyright 2012 - 2016 Fabian Prasser, Florian Kohlmayer and contributors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deidentifier.arx.gui.view.impl.common;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;

/**
 * A selection listener that defers execution for a given amount of time
 * 
 * @author Fabian Prasser
 */
public abstract class DelayedSelectionListener implements SelectionListener {

    /** Tick in millis */
    private static final int TICK = 100;

    /** Event */
    private SelectionEvent   event;
    /** Time */
    private long             time;
    /** Delay in milliseconds */
    private final long       delay;

    /**
     * Delay in milliseconds
     * @param delay
     */
    public DelayedSelectionListener(long delay) {
        this.delay = delay;
        
        // Create repeating task
        final Display display = Display.getCurrent();
        display.timerExec(TICK, new Runnable() {
            @Override
            public void run() {
                if (event != null && System.currentTimeMillis() > time) {
                    widgetDelayedSelected(event);
                    event = null;
                }
                display.timerExec(TICK, this);
            }
        });
    }
    
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
        this.event = arg0;
        this.time = System.currentTimeMillis() + delay;
    }

    @Override
    public void widgetSelected(SelectionEvent arg0) {
        this.event = arg0;
        this.time = System.currentTimeMillis() + delay;
    }

    /**
     * Implement this
     * @param arg0
     */
    public abstract void widgetDelayedSelected(SelectionEvent arg0);
}
