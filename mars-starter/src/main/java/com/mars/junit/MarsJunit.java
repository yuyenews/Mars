package com.mars.junit;

import com.mars.jdbc.load.InitJdbc;

/**
 * junit
 */
public abstract class MarsJunit extends BaseJunit {

    @Override
    public InitJdbc getInitJdbc() {
        return new InitJdbc();
    }
}
