package com.gitee.usl.app.interaction;

import com.gitee.usl.UslRunner;
import com.gitee.usl.api.Interaction;

/**
 * @author hongda.li
 */
public class EmptyInteraction implements Interaction {
    private static final EmptyInteraction INTERACTION = new EmptyInteraction();

    private EmptyInteraction() {
    }

    public static EmptyInteraction getInstance() {
        return INTERACTION;
    }

    @Override
    public void start(UslRunner runner) {
        // do nothing
    }
}
