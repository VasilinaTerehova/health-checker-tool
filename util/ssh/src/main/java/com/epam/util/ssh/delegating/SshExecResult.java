package com.epam.util.ssh.delegating;

import com.epam.util.common.StringUtils;

public class SshExecResult {
    private StringBuilder outMessage;
    private StringBuilder errMessage;

    public SshExecResult() {
        clearAll();
    }

    public void appendToOut( String message ) {
        outMessage.append( message );
    }

    public void appendToErr( String message ) {
        errMessage.append( message );
    }

    public void clearOut() {
        outMessage = new StringBuilder( StringUtils.EMPTY );
    }

    public void clearErr() {
        errMessage = new StringBuilder( StringUtils.EMPTY );
    }

    public void clearAll() {
        clearErr();
        clearOut();
    }

    public String getOutMessage() {
        return outMessage.toString();
    }

    public String getErrMessage() {
        return errMessage.toString();
    }

    public void setOutMessage( String outMessage ) {
        clearOut();
        appendToOut( outMessage );
    }

    public void setErrMessage( String errMessage ) {
        clearErr();
        appendToErr( errMessage );
    }
}
