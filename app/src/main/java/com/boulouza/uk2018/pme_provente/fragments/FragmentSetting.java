package com.boulouza.uk2018.pme_provente.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.boulouza.uk2018.pme_provente.ItemListActivity;
import com.boulouza.uk2018.pme_provente.R;
import com.boulouza.uk2018.pme_provente.databases.DATABASE;
import com.github.ybq.android.spinkit.style.Circle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import pl.coreorb.selectiondialogs.data.SelectableIcon;
import pl.coreorb.selectiondialogs.dialogs.IconSelectDialog;
import pl.coreorb.selectiondialogs.views.SelectedItemView;

import static android.content.Context.MODE_PRIVATE;


public class FragmentSetting extends Fragment implements IconSelectDialog.OnIconSelectedListener{

    private static final String TAG_SELECT_COLOR_DIALOG = "TAG_SELECT_COLOR_DIALOG";

    private DATABASE controller;
    public Circle circle;
    private MediaPlayer mp;
    private EditText ip, pathdatabase;

    private TextView textView, code_depot, code_vendeur;
    private LinearLayout code_depot_lnr , mode_tarif_lnr, code_vendeur_lnr;

    private SelectedItemView mode_tarif;

    private Button btntest;

    private String PREFS_CONNEXION = "ConfigNetwork";
    private String PREFS_AUTRE = "ConfigAutre";
    private String PARAMS_PREFS_IMPORT_EXPORT = "IMPORT_EXPORT";
    private String PARAMS_PREFS_CODE_DEPOT = "CODE_DEPOT_PREFS";


    private final Boolean[] isRunning = {false};
    private String username;
    private String password;

    private Switch switch_gps;
    private CheckBox chkbx_stock_moins, chkbx_achats_show;
    private LinearLayout reset_pda;

    private Context mContext;

    private TelephonyManager telephonyManager;
    private boolean read_write_permission = false;

    public FragmentSetting() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = new DATABASE(getActivity());


        mContext = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Set title bar
        ((ItemListActivity) getActivity()).setActionBarTitle("paramètres");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        initViews(rootView);

        return rootView;
    }


    private void initViews(View rootView) {

        //EditText
        ip = (EditText) rootView.findViewById(R.id.ip);
        pathdatabase = (EditText) rootView.findViewById(R.id.database);


        // Chekbox
        chkbx_stock_moins = (CheckBox) rootView.findViewById(R.id.chkbx_stock_moins);
        chkbx_achats_show = (CheckBox) rootView.findViewById(R.id.chkbx_achats_show);



        // checkbox stock moins
        SharedPreferences prefs3 = getActivity().getSharedPreferences(PREFS_AUTRE, MODE_PRIVATE);
        if (prefs3.getBoolean("STOCK_MOINS", false)) {
            chkbx_stock_moins.setChecked(true);
        } else {
            chkbx_stock_moins.setChecked(false);
        }

        chkbx_stock_moins.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_AUTRE, MODE_PRIVATE).edit();
                if (isChecked) {
                    editor.putBoolean("STOCK_MOINS", true);
                } else {
                    editor.putBoolean("STOCK_MOINS", false);
                }
                editor.commit();
            }
        });


        // checkbox achats show
        prefs3 = getActivity().getSharedPreferences(PREFS_AUTRE, MODE_PRIVATE);
        if (prefs3.getBoolean("ACHATS_SHOW", false)) {
            chkbx_achats_show.setChecked(true);
        } else {
            chkbx_achats_show.setChecked(false);
        }

        chkbx_achats_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_AUTRE, MODE_PRIVATE).edit();
                if (isChecked) {
                    editor.putBoolean("ACHATS_SHOW", true);
                } else {
                    editor.putBoolean("ACHATS_SHOW", false);
                }
                editor.commit();
            }
        });



        mode_tarif = (SelectedItemView) rootView.findViewById(R.id.mode_tarif_select);
        mode_tarif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIconSelectDialog();
            }
        });


        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_CONNEXION, MODE_PRIVATE);
        ip.setText(prefs.getString("ip", "192.168.1.93"));
        pathdatabase.setText(prefs.getString("path", "C:/PMEPRO"));
        username = prefs.getString("username", "SYSDBA");
        password = prefs.getString("password", "masterkey");


        if(prefs.getString("PV_ID", "PV1").equals("PV1")){
            mode_tarif.setSelectedName("Prix de vente 1");
        }else if(prefs.getString("PV_ID", "PV1").equals("PV2")){
            mode_tarif.setSelectedName("Prix de vente 2");
        }else{
            mode_tarif.setSelectedName("Prix de vente 3");
        }



        //Button
        btntest = (Button) rootView.findViewById(R.id.check);
        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning[0]) {
                    new TestConnection_Setting(ip.getText().toString(), pathdatabase.getText().toString(),
                            username, password).execute();
                    isRunning[0] = true;
                } else {
                    Crouton.showText(getActivity(), "Test Connection is running !", Style.INFO);
                }
            }
        });



        //TextView
        code_depot = (TextView) rootView.findViewById(R.id.code_depot);
        code_vendeur = (TextView) rootView.findViewById(R.id.code_vendeur);
        textView = (TextView) rootView.findViewById(R.id.progress);
        circle = new Circle();
        circle.setBounds(0, 0, 60, 60);
        //noinspection deprecation
        circle.setColor(getResources().getColor(R.color.colorAccent));
        textView.setCompoundDrawables(null, null, circle, null);
        textView.setVisibility(View.GONE);

        //Relative layout
        code_depot_lnr = (LinearLayout) rootView.findViewById(R.id.code_depot_lnr);
        code_vendeur_lnr = (LinearLayout) rootView.findViewById(R.id.code_vendeur_lnr);
        mode_tarif_lnr = (LinearLayout) rootView.findViewById(R.id.mode_tarif_lnr);

        ///////////////////
        SharedPreferences prefs2 = getActivity().getSharedPreferences(PARAMS_PREFS_CODE_DEPOT, MODE_PRIVATE);
        code_depot.setText(prefs2.getString("CODE_DEPOT", "000000"));
        code_vendeur.setText(prefs2.getString("CODE_VENDEUR", "000000"));


        //Switch
        switch_gps = (Switch) rootView.findViewById(R.id.switch_gps);

        //////////////////////////////// SWITCH GPS LOCALISATION ///////////////////////////////////

        SharedPreferences prefs4 = getActivity().getSharedPreferences(PREFS_AUTRE, MODE_PRIVATE);
        if (prefs4.getBoolean("GPS_LOCALISATION", false)) {
            switch_gps.setChecked(true);
        } else {
            switch_gps.setChecked(false);
        }

        switch_gps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_AUTRE, MODE_PRIVATE).edit();
                    editor.putBoolean("GPS_LOCALISATION", true);
                    editor.commit();
                } else {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_AUTRE, MODE_PRIVATE).edit();
                    editor.putBoolean("GPS_LOCALISATION", false);
                    editor.commit();
                }
            }
        });


        // Reset database
        reset_pda = (LinearLayout) rootView.findViewById(R.id.reset_pda);
    }

    /**
     * Shows icon selection dialog with sample icons.
     */
    private void showIconSelectDialog() {
        new IconSelectDialog.Builder(getActivity())
                .setIcons(sampleIcons())
                .setTitle("Séléctionner mode tarif")
                .setSortIconsByName(true)
                .setOnIconSelectedListener(this)
                .build().show(getActivity().getSupportFragmentManager(), TAG_SELECT_COLOR_DIALOG );
    }


    /**
     * Creates sample ArrayList of icons to display in dialog.
     * @return sample icons
     */
    private static ArrayList<SelectableIcon> sampleIcons() {
        ArrayList<SelectableIcon> selectionDialogsColors = new ArrayList<>();
        selectionDialogsColors.add(new SelectableIcon("PV1", "Prix vente 1", R.drawable.pv1));
        selectionDialogsColors.add(new SelectableIcon("PV2", "Prix vente 2", R.drawable.pv2));
        selectionDialogsColors.add(new SelectableIcon("PV3", "Prix vente 3", R.drawable.pv3));
        return selectionDialogsColors;
    }


    @Override
    public void onIconSelected(SelectableIcon selectedItem) {
        mode_tarif.setSelectedIcon(selectedItem);
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_AUTRE, MODE_PRIVATE).edit();
        String TAG = "1";
        switch(selectedItem.getId()){

            case "PV1":
                editor.putString("PV_ID", "PV1");
                TAG = "1";
                break;
            case "PV2":
                editor.putString("PV_ID", "PV2");
                TAG = "2";
                break;
            case "PV3":
                editor.putString("PV_ID", "PV3");
                TAG = "3";
                break;
        }
        editor.commit();
        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success!")
                .setContentText("Prix de vente "+TAG+" bien séléctionné !")
                .show();
    }

    //class to check cnx to FireBird Database
    //====================================
    public class TestConnection_Setting extends AsyncTask<Void,Void,Boolean> {

        Boolean executed = false;
        String _Server;
        String _pathDatabase;
        String _Username;
        String _Password;
        Connection con = null;

        public TestConnection_Setting(String server, String database, String username, String password) {
            super();
            // do stuff
            _Server = server;
            _pathDatabase = database;
            _Username = username;
            _Password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView.setVisibility(View.VISIBLE);
            circle.start();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {

                System.setProperty("FBAdbLog", "true");
                java.sql.DriverManager.setLoginTimeout(5);
                Class.forName("org.firebirdsql.jdbc.FBDriver");
                String sCon = "jdbc:firebirdsql:" + _Server + ":" + _pathDatabase + ".FDB";
                con = DriverManager.getConnection(sCon, _Username, _Password);
                executed = true;
                con = null;

            }catch (Exception ex ){
                con = null;
                Log.e("TRACKKK", "FAILED TO CONNECT WITH SERVER " + ex.getMessage());
            }
            return executed;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            circle.stop();
            isRunning[0] = false;
            textView.setVisibility(View.GONE);

            SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_CONNEXION, MODE_PRIVATE).edit();
            editor.putString("ip", _Server);
            editor.putString("path", _pathDatabase);
            editor.putString("username", _Username);
            editor.putString("password", _Password);
            editor.commit();

            if(aBoolean){
                View customView = getLayoutInflater().inflate(R.layout.custom_cruton_style, null);
                Crouton.show(getActivity(), customView);
                Sound(R.raw.login);
            }else{
                Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shakanimation);
                btntest.startAnimation(shake);
                Sound(R.raw.error);
                // Crouton.showText(_context , " Failed to Connect !", Style.ALERT);
            }
            super.onPostExecute(aBoolean);
        }
    }
    //==================================================


    // function for playing sound
    public void Sound(int SourceSound){
        mp = MediaPlayer.create(getActivity(), SourceSound);
        mp.start();
    }
}
