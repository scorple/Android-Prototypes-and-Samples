package com.scorpius_enterprises.net2;

//import com.scorpius_enterprises.log.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * {@link SocketUtils networking.SocketUtils}
 *
 * @author Scorple
 * @since 2017-04-29
 */
abstract class SocketUtils
{
    static int readUTF(final IReadListener listener, final Socket socket)
    {
        String[] in = new String[1];

        int res = readUTF(socket, in);

        if (res >= 0)
        {
            listener.read(in[0]);
        }

        return res;
    }

    static int readUTF(final Socket socket, final String[] out)
    {
        //Logger.logD("enter trace");

        if (out != null)
        {
            //Logger.logD("out != null");

            out[0] = null;

            if (socket != null)
            {
                //Logger.logD("socket != null");

                try
                {
                    //Logger.logD("getting dis");

                    DataInputStream dis = new DataInputStream(socket.getInputStream());

                    //Logger.logD("got dis");

                    try
                    {
                        //Logger.logD("checking available");

                        if (dis.available() > 0)
                        {
                            //Logger.logD("performing read");

                            out[0] = dis.readUTF();

                            //Logger.logD("exit trace");

                            return 0;
                        }

                        //Logger.logD("exit trace");

                        return -1;
                    }
                    catch (EOFException e)
                    {
                        //Logger.logD("exit trace");

                        return -1;
                    }
                    catch (Exception e)
                    {
                        //Logger.logD("exit trace");

                        return -5;
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();

                    //Logger.logD("exit trace");

                    return -4;
                }
            }
            //Logger.logD("exit trace");

            return -3;
        }
        //Logger.logD("exit trace");

        return -2;
    }

    static int writeUTF(final Socket socket, final String out)
    {
        //Logger.logD("enter trace");

        if (out != null)
        {
            //Logger.logD("out != null");

            if (socket != null)
            {
                //Logger.logD("socket != null");

                try
                {
                    //Logger.logD("getting dos");

                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                    //Logger.logD("got dos");

                    //Logger.logD("performing write");

                    dos.writeUTF(out);

                    //Logger.logD("performing flush");

                    dos.flush();

                    //Logger.logD("exit trace");

                    return 0;
                }
                catch (IOException e)
                {
                    e.printStackTrace();

                    //Logger.logD("exit trace");

                    return -3;
                }
            }
            //Logger.logD("exit trace");

            return -2;
        }
        //Logger.logD("exit trace");

        return -1;
    }
}
