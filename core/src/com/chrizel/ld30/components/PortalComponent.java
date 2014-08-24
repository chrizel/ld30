package com.chrizel.ld30.components;

import com.artemis.Component;

public class PortalComponent extends Component {
    public float portTime;
    public float state = -1f;
    
    public PortalComponent(float portTime) {
        this.portTime = portTime;
    }
}
