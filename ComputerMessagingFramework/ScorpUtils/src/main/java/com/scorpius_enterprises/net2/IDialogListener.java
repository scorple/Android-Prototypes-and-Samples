package com.scorpius_enterprises.net2;

/**
 * {@link IDialogListener com.scorpius_enterprises.net2.IDialogListener}
 *
 * @author Scorple
 * @since 2017-05-02
 */
public interface IDialogListener
{
    void notifyMessage(final String message);

    void notifyStatus(final String status);

    void notifyError(final String error);
}
