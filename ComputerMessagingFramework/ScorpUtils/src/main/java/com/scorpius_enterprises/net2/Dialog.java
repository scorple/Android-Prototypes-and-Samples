package com.scorpius_enterprises.net2;

import java.io.IOException;
import java.net.Socket;

/**
 * {@link Dialog com.scorpius_enterprises.net2.Dialog}
 *
 * @author Scorple
 * @since 2017-05-02
 */
abstract class Dialog implements IDialog
{
    private boolean open;
    private boolean closed = false;

    Socket socket;

    IDialogListener listener;

    void open()
    {
        open = true;
        closed = false;

        Reader reader = new Reader();
        reader.start();

        status("dialog opened");
    }

    private void read(final String message)
    {
        listener.notifyMessage(message);
    }

    void status(final String status)
    {
        listener.notifyStatus(status);
    }

    void error(final String error)
    {
        listener.notifyError(error);
    }

    @Override
    public void write(final String out)
    {
        int res = SocketUtils.writeUTF(socket, out);

        if (res < 0 && !closed)
        {
            error("write error");
            close();
        }
    }

    @Override
    public boolean isReady()
    {
        return (socket != null && socket.isBound() && socket.isConnected() && !socket.isClosed());
    }

    @Override
    public void close()
    {
        open = false;
        closed = true;

        if (socket != null)
        {
            try
            {
                socket.close();

                status("connection closed");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        socket = null;
    }

    private class Reader extends Thread implements IReadListener
    {
        @Override
        public void run()
        {
            while (open)
            {
                if (isReady())
                {
                    int res = SocketUtils.readUTF(this, socket);

                    if (res < -1 && !closed)
                    {
                        error("read error");
                        close();
                    }
                }
            }
        }

        @Override
        public void read(final String in)
        {
            Dialog.this.read(in);
        }
    }
}
