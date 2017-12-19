package com.scorpiusenterprises.accountability_engine;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Rick on 4/25/2016.
 */
public class NotiDialog extends DialogFragment implements View.OnClickListener {
    Spinner spnIcon, spnColor;
    EditText etxTitle, etxText, etxTicker;
    CheckBox chkVibrate, chkSound, chkAction, chkIntent;
    Button btnDone;

    ArrayList<Drawable> iconList;
    ArrayList<String> iconNameList;
    ArrayList<String> colorList;

    int icon, color;
    String title, text, ticker;
    boolean vibrate, sound, action, intent;

    private NotiDialogListener listener;

    SharedPreferences pref;

    public NotiDialog() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_blueprint, container);

        getDialog().setTitle("Notification");

        pref = getContext().getSharedPreferences("notiPrefs", Context.MODE_PRIVATE);

        spnIcon = (Spinner) view.findViewById(R.id.spnIcon);
        iconList = new ArrayList<>();
        iconNameList = new ArrayList<>();
        iconList.add(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
        iconNameList.add("Alert");
        iconList.add(getResources().getDrawable(android.R.drawable.ic_dialog_dialer));
        iconNameList.add("Dialer");
        iconList.add(getResources().getDrawable(android.R.drawable.ic_dialog_email));
        iconNameList.add("Email");
        iconList.add(getResources().getDrawable(android.R.drawable.ic_dialog_info));
        iconNameList.add("Info");
        iconList.add(getResources().getDrawable(android.R.drawable.ic_dialog_map));
        iconNameList.add("Map");
        //ArrayAdapter<Drawable> iconArrayAdapter = new ArrayAdapter<Drawable>(getContext(), R.layout.spinner_image, R.id.ivIcon, iconList);
        ArrayAdapter<String> iconNameArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, iconNameList);
        spnIcon.setAdapter(iconNameArrayAdapter);
        spnIcon.setSelection(pref.getInt("icon", 0));

        etxTitle = (EditText) view.findViewById(R.id.etxTitle);
        etxTitle.setText(pref.getString("title", ""));

        etxText = (EditText) view.findViewById(R.id.etxText);
        etxText.setText(pref.getString("text", ""));

        etxTicker = (EditText) view.findViewById(R.id.etxTicker);
        etxTicker.setText(pref.getString("ticker", ""));

        spnColor = (Spinner) view.findViewById(R.id.spnColor);
        colorList = new ArrayList<>();
        colorList.add("None");
        colorList.add("Red");
        colorList.add("Green");
        colorList.add("Blue");
        ArrayAdapter<String> colorArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, colorList);
        spnColor.setAdapter(colorArrayAdapter);
        spnColor.setSelection(pref.getInt("color", 0));

        chkVibrate = (CheckBox) view.findViewById(R.id.chkVibrate);
        chkVibrate.setChecked(pref.getBoolean("vibrate", false));

        chkSound = (CheckBox) view.findViewById(R.id.chkSound);
        chkSound.setChecked(pref.getBoolean("sound", false));

        chkAction = (CheckBox) view.findViewById(R.id.chkAction);
        chkAction.setChecked(pref.getBoolean("action", false));

        chkIntent = (CheckBox) view.findViewById(R.id.chkIntent);
        chkIntent.setChecked(pref.getBoolean("intent", false));

        btnDone = (Button) view.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        icon = spnIcon.getSelectedItemPosition();
        title = etxTitle.getText().toString();
        text = etxText.getText().toString();
        ticker = etxTicker.getText().toString();
        color = spnColor.getSelectedItemPosition();
        vibrate = chkVibrate.isChecked();
        sound = chkSound.isChecked();
        action = chkAction.isChecked();
        intent = chkIntent.isChecked();

        listener.onFinish(icon, title, text, ticker, color, vibrate, sound, action, intent);

        this.dismiss();
    }

    public interface  NotiDialogListener {
        void onFinish(int ic, String ti, String te, String tt, int c, boolean v, boolean s, boolean a, boolean in);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (NotiDialogListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
