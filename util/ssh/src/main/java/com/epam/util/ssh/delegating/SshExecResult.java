package com.epam.util.ssh.delegating;

import com.epam.util.common.CheckingParamsUtil;
import com.epam.util.common.StringUtils;

import java.util.function.Consumer;

public class SshExecResult {
    private StringBuilder outMessage;
    private StringBuilder errMessage;

    private SshExecResult() { }

    public String getOutMessage() {
        return outMessage.toString();
    }

    public String getErrMessage() {
        return errMessage.toString();
    }

    public static class SshExecResultBuilder {
        private SshExecResult sshExecResult;

        private SshExecResultBuilder() {
            this( new SshExecResult() );
        }

        private SshExecResultBuilder( SshExecResult sshExecResult ) {
            this.sshExecResult = sshExecResult;
        }

        public static SshExecResultBuilder get() {
            return new SshExecResultBuilder().clearAll();
        }

        public static SshExecResultBuilder get( SshExecResult sshExecResult ) {
            return new SshExecResultBuilder( sshExecResult );
        }

        public SshExecResultBuilder appendToOut( String message ) {
            return verifyAndAddParam( message, ( param ) -> this.sshExecResult.outMessage.append( message ) );
        }

        public SshExecResultBuilder appendToErr( String message ) {
            return verifyAndAddParam( message, ( param ) -> this.sshExecResult.errMessage.append( message ) );
        }

        public SshExecResultBuilder clearOut() {
            this.sshExecResult.outMessage = new StringBuilder( StringUtils.EMPTY );

            return this;
        }

        public SshExecResultBuilder clearErr() {
            this.sshExecResult.errMessage = new StringBuilder( StringUtils.EMPTY );

            return this;
        }

        public SshExecResultBuilder clearAll() {
            clearErr().clearOut();

            return this;
        }

        public SshExecResultBuilder setOutMessage(String outMessage ) {
            return verifyAndAddParam( outMessage, ( param ) -> clearOut().appendToOut( outMessage ));
        }

        public SshExecResultBuilder setErrMessage(String errMessage ) {
            return verifyAndAddParam( errMessage, ( param ) -> clearErr().appendToErr( errMessage ));
        }

        public SshExecResult build() {
            return this.sshExecResult;
        }

        private SshExecResultBuilder verifyAndAddParam(String param, Consumer<String> addParamConsumer) {
            if (CheckingParamsUtil.isParamNotNull( param )) {
                addParamConsumer.accept( param );
            }

            return this;
        }
    }
}
