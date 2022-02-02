package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.JList;

import java.util.EventListener;

// Declare the listener class. It must extend EventListener.
// A class must implement this interface to get MyEvents.
public interface SwapListMyEventListener extends EventListener {
    public void myEventOccurred(SwapListMyChangedEvent evt);
}