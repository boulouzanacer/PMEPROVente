package com.boulouza.uk2018.pme_provente.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.boulouza.uk2018.pme_provente.ItemListActivity;
import com.boulouza.uk2018.pme_provente.R;
import com.boulouza.uk2018.pme_provente.databases.DATABASE;
import com.boulouza.uk2018.pme_provente.postData.PostData_Client;
import com.boulouza.uk2018.pme_provente.postData.PostData_Codebarre;
import com.boulouza.uk2018.pme_provente.postData.PostData_Produit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static android.content.Context.MODE_PRIVATE;


public class FragmentSync extends Fragment implements View.OnClickListener {


    private String Server;
    private String Username;
    private String Password;
    private String MY_PREFS_NAME = "ConfigNetwork";
    private String PREFS_CODE_DEPOT = "CODE_DEPOT_PREFS";
    private String PARAMS_PREFS_IMPORT_EXPORT = "IMPORT_EXPORT";
    private String Path;
    private DATABASE controller;
    private ProgressDialog mProgressDialog;
    private ProgressDialog mProgressDialog_Free;

    private MediaPlayer mp;

    private RelativeLayout Import_bon,Import_client, Import_produit, Export_vente, Export_commande, Export_inventaire, Export_achat, EtatV,   BonExported;
    private Context mContext;
    private String code_depot, code_vendeur;


    public FragmentSync() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initViews(View rootV) {

        Import_bon = (RelativeLayout) rootV.findViewById(R.id.rlt_import_bon);
        Import_bon.setOnClickListener(this);
        Import_client = (RelativeLayout) rootV.findViewById(R.id.rlt_import_client);
        Import_client.setOnClickListener(this);
       /* Export_vente = (RelativeLayout) rootV.findViewById(R.id.rlt_export_ventes);
        Export_commande = (RelativeLayout) rootV.findViewById(R.id.rlt_export_commandes);
        Export_inventaire = (RelativeLayout) rootV.findViewById(R.id.rlt_export_inventaires);
        Export_achat = (RelativeLayout) rootV.findViewById(R.id.rlt_export_achats);
        BonExported = (RelativeLayout) rootV.findViewById(R.id.rlt_exported_ventes);
        EtatV = (RelativeLayout) rootV.findViewById(R.id.rlt_etatv);*/
        mContext = getActivity();

    }

    @Override
    public void onResume() {
        super.onResume();

        controller = new DATABASE(getActivity());

        // Set title bar
        ((ItemListActivity) getActivity()).setActionBarTitle("Sync");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sync, container, false);

        initViews(rootView);

        return rootView;
    }

    @Override
    public void onClick(View v) {
//   SharedPreferences prefs3 = getActivity().getSharedPreferences(PARAMS_PREFS_IMPORT_EXPORT, MODE_PRIVATE);
        switch (v.getId()){
            case R.id.rlt_import_bon:
                // 1 check connection
                // 2 get all transfer bon for this deport
                // 3 propose those not exist in local database

                //Check_connection_bon_server_task check_connection = new Check_connection_bon_server_task();
                //check_connection.execute();

                break;
            case R.id.rlt_import_client:
                // 1 check connection
                // 2 synchronisation la list des client
                // 3 propose those not exist in local database

                Check_connection_client_server_for_sychronisation_client check_connection1 = new Check_connection_client_server_for_sychronisation_client();
                check_connection1.execute();

                break;
            case R.id.rlt_import_produit:
                // 1 check connection
                // 2 synchronisation la list des client
                // 3 propose those not exist in local database

                Check_connection_produit_server_for_sychronisation_produit check_connection_produit = new Check_connection_produit_server_for_sychronisation_produit();
                check_connection_produit.execute();

                break;
          /*  case R.id.rlt_export_ventes:


               Check_connection_export_server check_connection_export_data = new Check_connection_export_server("VENTE");
               check_connection_export_data.execute();

                break;
            case R.id.rlt_export_commandes:



              Check_connection_export_server check_connection_export_data = new Check_connection_export_server("COMMANDE");
              check_connection_export_data.execute();

                break;


            case R.id.rlt_export_inventaires:



              Check_connection_export_server check_connection_export_data = new Check_connection_export_server("INVENTAIRE");
              check_connection_export_data.execute();

                break;
            case R.id.rlt_export_achats:



              Check_connection_export_server check_connection_export_data = new Check_connection_export_server("ACHAT");
              check_connection_export_data.execute();


                break;
            case R.id.rlt_exported_ventes:

                Intent exported_ventes_intent = new Intent(ActivityImportsExport.this, ActivityExportedVentes.class);
                startActivity(exported_ventes_intent);

                break;
            case R.id.rlt_etatv:

                Intent etat_v_intent = new Intent(ActivityImportsExport.this, ActivityEtatV.class);
                startActivity(etat_v_intent);

                break;
                */
        }
    }

    // Importation liste client
    //==================== AsyncTask TO Load produits from server and store them in the local database (sqlite)
    public class Check_connection_client_server_for_sychronisation_client extends AsyncTask<Void, Integer, Integer> {
        Connection  con;
        Integer flag = 0;
        String Error_message = "";

        public Check_connection_client_server_for_sychronisation_client(){
            SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Server = prefs.getString("ip", "192.168.1.94");
            Path = prefs.getString("path", "C:/PMEPRO1122");
            Username = prefs.getString("username", "SYSDBA");
            Password = prefs.getString("password", "masterkey");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogCheckConnection();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                System.setProperty("FBAdbLog", "true");
                DriverManager.setLoginTimeout(5);
                Class.forName("org.firebirdsql.jdbc.FBDriver");
                String sCon = "jdbc:firebirdsql:" + Server + ":" + Path + ".FDB?encoding=ISO8859_1";
                con = DriverManager.getConnection(sCon, Username, Password);
                flag = 1;
            } catch (Exception e) {
                con = null;
                flag = 2;
                Log.e("TRACKKK", "ERROR : " + e.getMessage());
                Error_message = e.getMessage();

            }
            return flag;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            if(integer == 1){
                // get clients
                Import_client_from_server_task bon_client_task = new Import_client_from_server_task();
                bon_client_task.execute();
                mProgressDialog_Free.dismiss();

            }else if(integer ==2){
                mProgressDialog_Free.dismiss();
                Crouton.makeText(getActivity(), "Error : " + Error_message, Style.ALERT).show();
            }
            super.onPostExecute(integer);
        }

    }


    //=============== AsyncTask TO Load clients from server and store them in the local database (sqlite) ===================
    public class Import_client_from_server_task extends AsyncTask<Void, Integer, Integer> {

        Connection  con;
        Integer flag = 0;
        int compt = 0;
        int allrows=0;
        String Error_message = "";

        public Import_client_from_server_task(){

            SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Server = prefs.getString("ip", "192.168.1.94");
            Path = prefs.getString("path", "C:/PMEPRO1122");
            Username = prefs.getString("username", "SYSDBA");
            Password = prefs.getString("password", "masterkey");

            prefs = getActivity().getSharedPreferences(PREFS_CODE_DEPOT, MODE_PRIVATE);
            code_depot = prefs.getString("CODE_DEPOT", "000000");
            code_vendeur = prefs.getString("CODE_VENDEUR", "000000");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogConfigClient();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {


                ArrayList<PostData_Client> clients = new ArrayList<>();
                boolean executed = false ;

                System.setProperty("FBAdbLog", "true");
                DriverManager.setLoginTimeout(5);
                Class.forName("org.firebirdsql.jdbc.FBDriver");
                String sCon = "jdbc:firebirdsql:" + Server + ":" + Path + ".FDB?encoding=ISO8859_1";
                con = DriverManager.getConnection(sCon, Username, Password);

                Statement stmt = con.createStatement();

                if(code_depot.equals("000000")){

                    String sql12 = "SELECT  COUNT(*) FROM CLIENTS WHERE CODE_VENDEUR = '"+ code_vendeur +"'";
                    ResultSet rs12 = stmt.executeQuery(sql12);

                    while (rs12.next()) {
                        allrows = rs12.getInt("COUNT");
                    }
                    publishProgress(1);

                    //============================ GET Clients ===========================================
                    String sql0 = "SELECT  CLIENTS.CODE_CLIENT, CLIENTS.CLIENT, CLIENTS.TEL, CLIENTS.ADRESSE, coalesce(CLIENTS.MODE_TARIF , 0) AS MODE_TARIF, coalesce(CLIENTS.ACHATS,0) AS ACHATS, coalesce(CLIENTS.VERSER,0) AS VERSER, coalesce(CLIENTS.SOLDE,0) AS SOLDE, LATITUDE, LONGITUDE , coalesce(CLIENTS.CREDIT_LIMIT , 0) AS CREDIT_LIMIT FROM CLIENTS WHERE CODE_VENDEUR = '"+ code_vendeur +"' ";
                    ResultSet rs0 = stmt.executeQuery(sql0);
                    PostData_Client client;
                    while (rs0.next()) {

                        client = new PostData_Client();
                        client.code_client = rs0.getString("CODE_CLIENT");
                        client.client = rs0.getString("CLIENT");
                        client.tel = rs0.getString("TEL");
                        client.adresse = rs0.getString("ADRESSE");
                        client.mode_tarif =  rs0.getString("MODE_TARIF");
                        client.achat_montant =  rs0.getString("ACHATS");
                        client.verser_montant =  rs0.getString("VERSER");
                        client.solde_montant =  rs0.getString("SOLDE");
                        client.latitude =  rs0.getDouble("LATITUDE");
                        client.longitude =  rs0.getDouble("LONGITUDE");
                        client.credit_limit =  rs0.getDouble("CREDIT_LIMIT");

                        clients.add(client);

                        compt++;
                        publishProgress(compt);
                    }
                    executed = controller.ExecuteTransactionClient(clients);

                }else{

                    String sql12 = "SELECT  COUNT(*) FROM CLIENTS WHERE CODE_DEPOT = '"+ code_depot +"'";
                    ResultSet rs12 = stmt.executeQuery(sql12);

                    while (rs12.next()) {
                        allrows = rs12.getInt("COUNT");
                    }
                    publishProgress(1);

                    //============================ GET Clients ===========================================
                    String sql0 = "SELECT  CLIENTS.CODE_CLIENT, CLIENTS.CLIENT, CLIENTS.TEL, CLIENTS.ADRESSE, coalesce(CLIENTS.MODE_TARIF , 0) AS MODE_TARIF, coalesce(CLIENTS.ACHATS,0) AS ACHATS, coalesce(CLIENTS.VERSER,0) AS VERSER, coalesce(CLIENTS.SOLDE,0) AS SOLDE, LATITUDE, LONGITUDE , coalesce(CLIENTS.CREDIT_LIMIT , 0) AS CREDIT_LIMIT FROM CLIENTS WHERE CODE_DEPOT = '"+ code_depot +"' ";
                    ResultSet rs0 = stmt.executeQuery(sql0);
                    PostData_Client client;
                    while (rs0.next()) {

                        client = new PostData_Client();
                        client.code_client = rs0.getString("CODE_CLIENT");
                        client.client = rs0.getString("CLIENT");
                        client.tel = rs0.getString("TEL");
                        client.adresse = rs0.getString("ADRESSE");
                        client.mode_tarif =  rs0.getString("MODE_TARIF");
                        client.achat_montant =  rs0.getString("ACHATS");
                        client.verser_montant =  rs0.getString("VERSER");
                        client.solde_montant =  rs0.getString("SOLDE");
                        client.latitude =  rs0.getDouble("LATITUDE");
                        client.longitude =  rs0.getDouble("LONGITUDE");
                        client.credit_limit =  rs0.getDouble("CREDIT_LIMIT");

                        clients.add(client);

                        compt++;
                        publishProgress(compt);
                    }
                    executed = controller.ExecuteTransactionClient(clients);
                }

                publishProgress(1);

                stmt.close();

                if (executed) {
                    flag = 1;
                }else{
                    flag = 2;
                }

            } catch (Exception e) {
                con = null;
                flag = 2;
                Log.e("TRACKKK", "ERROR : " + e.getMessage());
                Error_message = e.getMessage();
            }

            return flag;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressDialog.setMax(allrows);
            mProgressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            mProgressDialog.dismiss();
            if(integer == 1){
                //completed
                Crouton.makeText(getActivity(), "Importation clients successfull !", Style.CONFIRM).show();
            }else if(integer ==2){
                Crouton.makeText(getActivity(), "Error : " + Error_message, Style.ALERT).show();
            }
        }

    }


    //==================== AsyncTask TO Load produits from server and store them in the local database (sqlite)
    public class Check_connection_produit_server_for_sychronisation_produit extends AsyncTask<Void, Integer, Integer> {
        Connection  con;
        Integer flag = 0;
        String Error_message = "";

        public Check_connection_produit_server_for_sychronisation_produit(){
            SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Server = prefs.getString("ip", "192.168.1.94");
            Path = prefs.getString("path", "C:/PMEPRO1122");
            Username = prefs.getString("username", "SYSDBA");
            Password = prefs.getString("password", "masterkey");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogCheckConnection();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {

                System.setProperty("FBAdbLog", "true");
                DriverManager.setLoginTimeout(5);
                Class.forName("org.firebirdsql.jdbc.FBDriver");
                String sCon = "jdbc:firebirdsql:" + Server + ":" + Path + ".FDB?encoding=ISO8859_1";
                con = DriverManager.getConnection(sCon, Username, Password);
                flag = 1;

            } catch (Exception e) {
                con = null;
                flag = 2;
                Log.e("TRACKKK", "ERROR : " + e.getMessage());
                Error_message = e.getMessage();
            }

            return flag;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            if(integer == 1){
                // get products from firebird database
                Import_produit_from_server_task produit_task = new Import_produit_from_server_task();
                produit_task.execute();
                mProgressDialog_Free.dismiss();

            }else if(integer ==2){
                mProgressDialog_Free.dismiss();
                Crouton.makeText(getActivity(), "Error : " + Error_message, Style.ALERT).show();
            }

            super.onPostExecute(integer);
        }

    }


    //==================== AsyncTask TO Load produits from server and store them in the local database (sqlite)
    public class Import_produit_from_server_task extends AsyncTask<Void, Integer, Integer> {

        Connection  con;
        Integer flag = 0;
        int compt = 0;
        int allrows=0;
        String Error_message = "";

        public Import_produit_from_server_task(){

            SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            Server = prefs.getString("ip", "192.168.1.94");
            Path = prefs.getString("path", "C:/PMEPRO1122");
            Username = prefs.getString("username", "SYSDBA");
            Password = prefs.getString("password", "masterkey");

            prefs = getActivity().getSharedPreferences(PREFS_CODE_DEPOT, MODE_PRIVATE);
            code_depot = prefs.getString("CODE_DEPOT", "000000");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogConfigProduit();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {


                ArrayList<PostData_Produit> produits = new ArrayList<>();
                ArrayList<PostData_Codebarre> codebarres = new ArrayList<>();
                Boolean  executed  = false;

                System.setProperty("FBAdbLog", "true");
                DriverManager.setLoginTimeout(5);
                Class.forName("org.firebirdsql.jdbc.FBDriver");
                String sCon = "jdbc:firebirdsql:" + Server + ":" + Path + ".FDB?encoding=ISO8859_1";
                con = DriverManager.getConnection(sCon, Username, Password);

                Statement stmt = con.createStatement();

                publishProgress(1);

                if(code_depot.toString().equals("000000")){

                    String sql12 = "SELECT  COUNT(*) FROM PRODUIT";
                    ResultSet rs12 = stmt.executeQuery(sql12);

                    while (rs12.next()) {
                        allrows = rs12.getInt("COUNT");
                    }

                    //Get product and  Insert it into produit tables
                    String sql3 = "SELECT  PRODUIT.CODE_BARRE, PRODUIT.REF_PRODUIT, PRODUIT.PRODUIT , coalesce(PRODUIT.PA_HT,0) AS PA_HT, coalesce(PRODUIT.TVA,0) AS TVA, cast(coalesce(PRODUIT.PV1_HT,0) as decimal (17,2)) AS PV1_HT , cast (coalesce(PRODUIT.PV2_HT,0) as decimal(17,2))  AS PV2_HT, cast (coalesce(PRODUIT.PV3_HT,0) as decimal(17,2)) AS PV3_HT , PRODUIT.PHOTO FROM PRODUIT";
                    ResultSet rs3 = stmt.executeQuery(sql3);

                    PostData_Produit produit_update;
                    while (rs3.next()) {

                        produit_update = new PostData_Produit();

                        produit_update.code_barre = rs3.getString("CODE_BARRE");
                        produit_update.ref_produit = rs3.getString("REF_PRODUIT");
                        produit_update.produit = rs3.getString("PRODUIT");
                        produit_update.pa_ht = rs3.getString("PA_HT");
                        produit_update.tva = rs3.getString("TVA");
                        produit_update.pv1_ht = rs3.getString("PV1_HT");
                        produit_update.pv2_ht =  rs3.getString("PV2_HT");
                        produit_update.pv3_ht =  rs3.getString("PV3_HT");
                        produit_update.stock =  "0";
                        produit_update.photo =  rs3.getBytes("PHOTO");

                        produits.add(produit_update);

                        compt++;
                        publishProgress(compt);
                    }

                    //Get all syn codebarre of this product  and  Insert it into codebarre tables
                    String sql4 = "SELECT  CODEBARRE.CODE_BARRE, CODEBARRE.CODE_BARRE_SYN FROM CODEBARRE ";
                    ResultSet rs4 = stmt.executeQuery(sql4);

                    PostData_Codebarre post_codebarre;
                    while (rs4.next()) {

                        post_codebarre = new PostData_Codebarre();
                        post_codebarre.code_barre = rs4.getString("CODE_BARRE");
                        post_codebarre.code_barre_syn = rs4.getString("CODE_BARRE_SYN");
                        codebarres.add(post_codebarre);
                    }
                    executed  = controller.ExecuteTransactionProduit(produits, codebarres);

                }else{

                    String sql12 = "SELECT  COUNT(*) FROM DEPOT2 WHERE CODE_DEPOT = '"+ code_depot+"' ";
                    ResultSet rs12 = stmt.executeQuery(sql12);

                    while (rs12.next()) {
                        allrows = rs12.getInt("COUNT");
                    }

                    //Get product and  Insert it into produit tables
                    String sql3 = "SELECT DEPOT2.CODE_BARRE, PRODUIT.REF_PRODUIT, coalesce(DEPOT2.stock,0) AS STOCK, PRODUIT.PRODUIT , coalesce(PRODUIT.PA_HT,0) AS PA_HT, coalesce(PRODUIT.TVA,0) AS TVA, cast(coalesce(PRODUIT.PV1_HT,0) as decimal (17,2)) AS PV1_HT , cast (coalesce(PRODUIT.PV2_HT,0) as decimal(17,2))  AS PV2_HT, cast (coalesce(PRODUIT.PV3_HT,0) as decimal(17,2)) AS PV3_HT , PRODUIT.PHOTO " +
                            " FROM DEPOT2 " +
                            " LEFT JOIN produit ON ( PRODUIT.code_barre = DEPOT2.code_barre ) " +
                            " WHERE DEPOT2.code_depot = '"+ code_depot+"' ";

                    ResultSet rs3 = stmt.executeQuery(sql3);
                    produits.clear();
                    PostData_Produit produit_update;
                    while (rs3.next()) {

                        produit_update = new PostData_Produit();

                        produit_update.code_barre = rs3.getString("CODE_BARRE");
                        produit_update.ref_produit = rs3.getString("REF_PRODUIT");
                        produit_update.produit = rs3.getString("PRODUIT");
                        produit_update.pa_ht = rs3.getString("PA_HT");
                        produit_update.tva = rs3.getString("TVA");
                        produit_update.pv1_ht = rs3.getString("PV1_HT");
                        produit_update.pv2_ht =  rs3.getString("PV2_HT");
                        produit_update.pv3_ht =  rs3.getString("PV3_HT");
                        produit_update.stock =  rs3.getString("STOCK");
                        produit_update.photo =  rs3.getBytes("PHOTO");
                        produits.add(produit_update);

                        compt++;
                        publishProgress(compt);
                    }

                    //Get all syn codebarre of this product  and  Insert it into codebarre tables
                    String sql4 = "SELECT  CODEBARRE.CODE_BARRE, CODEBARRE.CODE_BARRE_SYN FROM CODEBARRE where codebarre.code_barre in ( select CODE_BARRE FROM DEPOT2 WHERE DEPOT2.code_depot = '"+ code_depot+"')";
                    ResultSet rs4 = stmt.executeQuery(sql4);
                    codebarres.clear();
                    PostData_Codebarre post_codebarre;
                    while (rs4.next()) {

                        post_codebarre = new PostData_Codebarre();
                        post_codebarre.code_barre = rs4.getString("CODE_BARRE");
                        post_codebarre.code_barre_syn = rs4.getString("CODE_BARRE_SYN");
                        codebarres.add(post_codebarre);
                    }

                    executed  =   controller.ExecuteTransactionProduit(produits, codebarres);
                }

                stmt.close();

                if (executed) {
                    flag = 1;
                }else{
                    flag = 3;
                }

            } catch (Exception e) {
                con = null;
                flag = 2;
                Log.e("TRACKKK", "ERROR : " + e.getMessage());
                Error_message = e.getMessage();
            }

            return flag;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            mProgressDialog.setMax(allrows);

            mProgressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mProgressDialog.dismiss();
            if(integer == 1){
                //completed
                Crouton.makeText(getActivity(), "Importation products successfull !", Style.CONFIRM).show();
            }else if(integer ==2){
                Crouton.makeText(getActivity(), "Error : " + Error_message, Style.ALERT).show();
            }
        }
    }





























//================================ Progress dialog confis =========================================

    public void progressDialogCheckConnection(){
        mProgressDialog_Free = new ProgressDialog(getActivity());
        mProgressDialog_Free.setMessage("Vérificaion de la connexion ...");
        mProgressDialog_Free.setCancelable(true);
        mProgressDialog_Free.show();
    }

    public void progressDialogConfigClient(){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Importation clients...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    public void progressDialogConfigProduit(){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Importation produits...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }
}
