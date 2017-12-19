package com.scorpius_enterprises.net2;

/**
 * {@link IDialog com.scorpius_enterprises.net2.IDialog}
 *
 * @author Scorple
 * @since 2017-05-02
 */
public interface IDialog
{
    void write(final String out);

    boolean isReady();

    void close();
}
