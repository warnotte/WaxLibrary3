package io.github.warnotte.waxlib3.W2D.PanelGraphique;

import java.util.EventListener;
public interface SelectionChangeable      
extends EventListener
{
// event dispatch methods
void somethingNeedRefresh(SelectionChangedEvent e);


}