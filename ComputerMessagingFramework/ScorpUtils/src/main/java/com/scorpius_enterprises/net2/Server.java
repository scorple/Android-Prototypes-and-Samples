package com.scorpius_enterprises.net2;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * {@link Server com.scorpius_enterprises.net2.Server}
 *
 * @author Scorple
 * @since 2017-05-02
 */
public class Server extends Dialog
{
    public Server(final IDialogListener listener)
    {
        this.listener = listener;

        ServerConnector serverConnector = new ServerConnector();
        serverConnector.start();

        status("server started");
    }

    private class ServerConnector extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                ServerSocket serverSocket = new ServerSocket(NetworkConstants.SOCKET);
                socket = serverSocket.accept();
                serverSocket.close();

                status("connected to client");

                open();
            }
            catch (IOException e)
            {
                e.printStackTrace();

                error("error connecting to client");

                close();
            }
        }
    }
}
