package util;

import paulscode.sound.SoundSystemLogger;

public class NoLogger extends SoundSystemLogger {
    @Override
    public boolean errorCheck(boolean error, String classname, String message, int indent) {
        return false;
    }

    @Override
    public void errorMessage(String classname, String message, int indent) {
    }

    @Override
    public void importantMessage(java.lang.String message, int indent) {
    }

    @Override
    public void message(java.lang.String message, int indent) {
    }

    @Override
    public void printExceptionMessage(java.lang.Exception e, int indent) {
    }

    @Override
    public void printStackTrace(java.lang.Exception e, int indent) {
    }
}
