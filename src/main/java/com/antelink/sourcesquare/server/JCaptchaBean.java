package com.antelink.sourcesquare.server;

/**
 * Created by IntelliJ IDEA.
 * User: freddy
 * Date: 3/12/12
 * Time: 11:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class JCaptchaBean {

    private boolean error;

    public JCaptchaBean(){

    }

    public JCaptchaBean(boolean error) {
        this.error = error;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
