package com.scorpiusenterprises.computermessagingframework;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.scorpius_enterprises.net2.Client;
import com.scorpius_enterprises.net2.IDialog;
import com.scorpius_enterprises.net2.IDialogListener;
import com.scorpius_enterprises.net2.Server;


public class MainActivity extends AppCompatActivity implements IDialogListener
{
    EditText txtInput;
    TextView txtOutput;

    IDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtInput = (EditText) findViewById(R.id.txtInput);
        txtOutput = (TextView) findViewById(R.id.txtOutput);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                processInput(txtInput.getText().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void processInput(final String input)
    {
        txtInput.setText("");
        processOutput("You: " + input);

        if (input.startsWith("~"))
        {
            String[] splitInput = input.split(" ");

            switch (splitInput[0])
            {
                case "~connect":
                    if (splitInput.length > 1)
                    {
                        dialog = new Client(this, splitInput[1]);
                    }
                    else
                    {
                        processOutput("Invalid command: not enough parameters");
                    }
                    break;
                case "~host":
                    dialog = new Server(this);
                    break;
                case "~disconnect":
                    dialog.close();
                    dialog = null;
                    break;
                default:
                    processOutput("Unknown command");
                    break;
            }
        }
        else
        {
            if (dialog != null)
            {
                dialog.write(input);
            }
        }
    }

    private void processOutput(final String output)
    {
        String display = txtOutput.getText().toString() + "\n" + output;

        txtOutput.setText(display);
    }

    @Override
    public void notifyMessage(final String message)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                processOutput("Partner: " + message);
            }
        });
    }

    @Override
    public void notifyStatus(final String status)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                processOutput(status);
            }
        });
    }

    @Override
    public void notifyError(final String error)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                processOutput(error);
            }
        });
    }
}
