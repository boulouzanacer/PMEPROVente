package com.boulouza.uk2018.pme_provente.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.boulouza.uk2018.pme_provente.postData.PostData_Bon1;
import com.boulouza.uk2018.pme_provente.postData.PostData_Bon2;
import com.boulouza.uk2018.pme_provente.postData.PostData_Client;
import com.boulouza.uk2018.pme_provente.postData.PostData_Codebarre;
import com.boulouza.uk2018.pme_provente.postData.PostData_Produit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by UK2015 on 21/08/2016.
 */
public class DATABASE extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1; // Database version
    private static final String DATABASE_NAME = "pme_provente_test"; //Database name

    private Context mContext;

    //Constructor DATABASE
    public DATABASE(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub

        mContext = context;
      // context.deleteDatabase("pme_provente_test");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        Log.v("TRACKKK","================>  ONCREATE EXECUTED");


        db.execSQL("CREATE TABLE IF NOT EXISTS Produit(CODE_BARRE VARCHAR PRIMARY KEY, REF_PRODUIT VARCHAR, PRODUIT VARCHAR, PA_HT VARCHAR, TVA VARCHAR,  PV1_HT VARCHAR, PV2_HT VARCHAR, PV3_HT VARCHAR , STOCK VARCHAR, PHOTO BLOB)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Codebarre(CODE_BARRE VARCHAR PRIMARY KEY, CODE_BARRE_SYN VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Bon1(" +
                "RECORDID INTEGER, " +
                "NUM_BON VARCHAR PRIMARY KEY, " +
                "CODE_CLIENT VARCHAR, " +
                "DATE_BON VARCHAR, " +
                "HEURE VARCHAR, " +
                "NBR_P VARCHAR, " +
                "MODE_TARIF VARCHAR, " +
                "CODE_DEPOT VARCHAR, " +
                "MODE_RG VARCHAR, " +
                "CODE_VENDEUR VARCHAR, " +
                "TOT_HT VARCHAR, " +
                "TOT_TVA VARCHAR, " +
                "TOT_TTC VARCHAR, " +
                "TOT_TTC_REMISE VARCHAR, " +
                "TIMBRE VARCHAR, " +
                "TIMBRE_CHECK VARCHAR, " +
                "MONTANT_BON VARCHAR, " +
                "LATITUDE REAL DEFAULT 0.00, " +
                "LONGITUDE REAL DEFAULT 0.00, " +
                "REMISE VARCHAR, " +
                "REMISE_CHECK VARCHAR, " +
                "ANCIEN_SOLDE VARCHAR, " +
                "RESTE VARCHAR, " +
                "EXPORTATION VARCHAR, " +
                "BLOCAGE VARCHAR, " +
                "VERSER VARCHAR, "+
                "IS_EXPORTED BOOLEAN CHECK (IS_EXPORTED IN (0,1)) DEFAULT 0)");


        db.execSQL("CREATE TABLE IF NOT EXISTS Bon2(RECORDID INTEGER PRIMARY KEY, " +
                "CODE_BARRE VARCHAR, " +
                "NUM_BON VARCHAR , " +
                "PRODUIT VARCHAR , " +
                "QTE VARCHAR , " +
                "PV_HT VARCHAR, " +
                "TVA VARCHAR, " +
                "CODE_DEPOT VARCHAR, " +
                "PA_HT VARCHAR )");


        db.execSQL("CREATE TABLE IF NOT EXISTS Client(CODE_CLIENT VARCHAR PRIMARY KEY, CLIENT VARCHAR , TEL VARCHAR, ADRESSE VARCHAR, MODE_TARIF VARCHAR, LATITUDE REAL, LONGITUDE REAL, ACHATS VARCHAR, VERSER VARCHAR, SOLDE VARCHAR , RC VARCHAR, IFISCAL VARCHAR, RIB VARCHAR, CREDIT_LIMIT REAL DEFAULT 0.00)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.v("TRACKKK","================>  ON UPGRADE EXECUTED");
        /*
      String[] list_requet = new String[4];

      list_requet[0] = "ALTER TABLE Bon1 ADD COLUMN IS_EXPORTED BOOLEAN CHECK (IS_EXPORTED IN (0,1)) DEFAULT 0";
      list_requet[1] = "ALTER TABLE Bon1_Temp ADD COLUMN IS_EXPORTED BOOLEAN CHECK (IS_EXPORTED IN (0,1)) DEFAULT 0";
      list_requet[2] = "ALTER TABLE Client ADD COLUMN CREDIT_LIMIT REAL DEFAULT 0.00";
      list_requet[3] = "ALTER TABLE Carnet_c ADD COLUMN IS_EXPORTED BOOLEAN CHECK (IS_EXPORTED IN (0,1)) DEFAULT 0";

        for(int i = 0; i< list_requet.length; i++){
            try
            {
                db.execSQL(list_requet[i]);
            }
            catch (SQLiteException e)
            {
                Log.v("TRACKKK", "Failed to create column [{0}]. Most likely it already exists, which is fine.");
            }
        }
        */
    }

    ///////////////////////////////////////////////////////////////////////////////
    //////                             Functions                             //////
    ///////////////////////////////////////////////////////////////////////////////



    //============================== FUNCTION table client =================================
    public Boolean ExecuteTransactionClient(ArrayList<PostData_Client> clients){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                db.delete("Client", null , null);

                for(int i = 0;i< clients.size(); i++){

                        ContentValues values = new ContentValues();
                        values.put("CODE_CLIENT", clients.get(i).code_client);
                        values.put("CLIENT", clients.get(i).client);
                        values.put("TEL", clients.get(i).tel);
                        values.put("ADRESSE", clients.get(i).adresse);
                        values.put("MODE_TARIF", clients.get(i).mode_tarif);
                        values.put("ACHATS", clients.get(i).achat_montant);
                        values.put("VERSER", clients.get(i).verser_montant);
                        values.put("SOLDE", clients.get(i).solde_montant);
                        values.put("LATITUDE", clients.get(i).latitude);
                        values.put("LONGITUDE", clients.get(i).longitude);
                        values.put("CREDIT_LIMIT", clients.get(i).credit_limit);
                        db.insert("Client", null, values);
                }

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;
    }

    //=============================== FUNCTION TO INSERT INTO Client TABLE ===============================
    public boolean Insert_into_client(PostData_Client client){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                ContentValues values = new ContentValues();


                values.put("CODE_CLIENT", client.code_client);
                values.put("CLIENT", client.client);
                values.put("TEL", client.tel);
                values.put("ADRESSE", client.adresse);
                values.put("MODE_TARIF", client.mode_tarif);
                values.put("RC", client.rc);
                values.put("IFISCAL", client.ifiscal);
                values.put("RIB", client.rib);
                values.put("ACHATS", "0.00");
                values.put("VERSER", "0.00");
                values.put("SOLDE", "0.00");

                db.insert("Client", null, values);

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;
    }


    public void Get_max_code_client(){

        SQLiteDatabase db = this.getWritableDatabase();

        String querry  = "SELECT MAX(CODE_CLIENT) AS MAX_C FROM Client";
        Cursor cursor = db.rawQuery(querry, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
              //  max = cursor.getString(cursor.getColumnIndex("MAX_C"));
            } while (cursor.moveToNext());
        }

    }

    //=============================== FUNCTION TO INSERT INTO Produits TABLE ===============================
    public void Insert_into_produit(PostData_Produit produit){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("CODE_BARRE", produit.code_barre);
        values.put("REF_PRODUIT", produit.ref_produit);
        values.put("PRODUIT", produit.produit);
        values.put("PA_HT", produit.pa_ht);
        values.put("TVA", produit.tva);
        values.put("PV1_HT", produit.pv1_ht);
        values.put("PV2_HT", produit.pv2_ht);
        values.put("PV3_HT", produit.pv3_ht);
        values.put("STOCK", produit.stock);
        values.put("PHOTO", produit.photo);

        db.insert("Produit", null, values);
    }


    //============================== FUNCTION table client =================================
    public Boolean ExecuteTransactionProduit(ArrayList<PostData_Produit> produits, ArrayList<PostData_Codebarre> codebarres){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                db.delete("Produit", null , null);

                for(int i = 0;i< produits.size(); i++){

                    ContentValues values = new ContentValues();
                    values.put("CODE_BARRE", produits.get(i).code_barre);
                    values.put("REF_PRODUIT", produits.get(i).ref_produit);
                    values.put("PRODUIT", produits.get(i).produit);
                    values.put("PA_HT", produits.get(i).pa_ht);
                    values.put("TVA", produits.get(i).tva);
                    values.put("PV1_HT", produits.get(i).pv1_ht);
                    values.put("PV2_HT", produits.get(i).pv2_ht);
                    values.put("PV3_HT", produits.get(i).pv3_ht);
                    values.put("STOCK", produits.get(i).stock);
                    values.put("PHOTO", produits.get(i).photo);
                    db.insert("Produit", null, values);
                }


                db.delete("Codebarre", null , null);

                for(int h=0; h < codebarres.size(); h++){

                    ContentValues values = new ContentValues();
                    values.put("CODE_BARRE", codebarres.get(h).code_barre);
                    values.put("CODE_BARRE_SYN", codebarres.get(h).code_barre_syn);
                    db.insert("Codebarre", null, values);

                }

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;
    }



    //=============================== FUNCTION TO INSERT INTO Codebarre TABLE ===============================
    public void Insert_into_codebarre(PostData_Codebarre codebarre){

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("CODE_BARRE", codebarre.code_barre);
            values.put("CODE_BARRE_SYN", codebarre.code_barre_syn);
            db.insert("Codebarre", null, values);
    }


    //============================== FUNCTION SELECT FROM Produits TABLE ===============================
    public PostData_Produit check_product_if_exist(String code_barre){
        SQLiteDatabase db = this.getWritableDatabase();

        PostData_Produit produit = new PostData_Produit();
        produit.exist = false;
        String querry  = "SELECT STOCK FROM Produit WHERE CODE_BARRE = '" + code_barre + "'";
        Cursor cursor = db.rawQuery(querry, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                produit.stock = cursor.getString(cursor.getColumnIndex("STOCK"));
                produit.exist = true;
            } while (cursor.moveToNext());
        }
        return produit;
    }


    public Boolean check_client_if_exist(String  code_client){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean exist = false;

        String querry  = "SELECT * FROM Client WHERE CODE_CLIENT = '" + code_client + "'";
        Cursor cursor = db.rawQuery(querry, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                exist = true;
            } while (cursor.moveToNext());
        }
        return exist;
    }



    //================================== UPDATE TABLE (Inventaires1) =======================================
    public boolean Update_produit(PostData_Produit produit_to_update, String code_barre){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                ContentValues args = new ContentValues();
                args.put("PRODUIT", produit_to_update.produit);
                args.put("STOCK", produit_to_update.stock);
                args.put("PA_HT", produit_to_update.pa_ht);
                args.put("TVA", produit_to_update.tva);
                args.put("PV1_HT", produit_to_update.pv1_ht);
                args.put("PV2_HT", produit_to_update.pv2_ht);
                args.put("PV3_HT", produit_to_update.pv3_ht);
                args.put("PHOTO", produit_to_update.photo);
                String selection = "CODE_BARRE=?";
                String[] selectionArgs = {code_barre.toString()};
                db.update("Produit", args, selection, selectionArgs);

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;
    }

    //================================== UPDATE TABLE (Inventaires1) =======================================
    public boolean Delete_Codebarre(String code_barre){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                String selection = "CODE_BARRE=?";
                String[] selectionArgs = {code_barre.toString()};
                db.delete("Codebarre", selection, selectionArgs);

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;
    }

    //============================== FUNCTION SELECT Clients FROM Client TABLE ===============================
    public ArrayList<PostData_Client> select_clients_from_database(String querry){
        ArrayList<PostData_Client> clients = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querry, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PostData_Client client = new PostData_Client();

                client.code_client = cursor.getString(cursor.getColumnIndex("CODE_CLIENT"));
                client.client = cursor.getString(cursor.getColumnIndex("CLIENT"));
                client.tel = cursor.getString(cursor.getColumnIndex("TEL"));
                client.adresse = cursor.getString(cursor.getColumnIndex("ADRESSE"));
                client.mode_tarif = cursor.getString(cursor.getColumnIndex("MODE_TARIF"));
                client.latitude = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));
                client.longitude = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));
                client.achat_montant = cursor.getString(cursor.getColumnIndex("ACHATS"));
                client.verser_montant = cursor.getString(cursor.getColumnIndex("VERSER"));
                client.solde_montant = cursor.getString(cursor.getColumnIndex("SOLDE"));
                client.credit_limit = cursor.getDouble(cursor.getColumnIndex("CREDIT_LIMIT"));

                clients.add(client);
            } while (cursor.moveToNext());
        }
        return clients;
    }


    //============================== FUNCTION SELECT Clients FROM Client TABLE ===============================
    public PostData_Client select_client_from_database(String code_client){

        PostData_Client client = new PostData_Client();;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Client WHERE CODE_CLIENT = '"+code_client+"'", null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                client.code_client = cursor.getString(cursor.getColumnIndex("CODE_CLIENT"));
                client.client = cursor.getString(cursor.getColumnIndex("CLIENT"));
                client.tel = cursor.getString(cursor.getColumnIndex("TEL"));
                client.mode_tarif = cursor.getString(cursor.getColumnIndex("MODE_TARIF"));
                client.adresse = cursor.getString(cursor.getColumnIndex("ADRESSE"));
                client.latitude = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));
                client.longitude = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));
                client.achat_montant = cursor.getString(cursor.getColumnIndex("ACHATS"));
                client.verser_montant = cursor.getString(cursor.getColumnIndex("VERSER"));
                client.solde_montant = cursor.getString(cursor.getColumnIndex("SOLDE"));
                client.credit_limit = cursor.getDouble(cursor.getColumnIndex("CREDIT_LIMIT"));

                client.rc = cursor.getString(cursor.getColumnIndex("RC"));
                client.ifiscal = cursor.getString(cursor.getColumnIndex("IFISCAL"));
                client.rib = cursor.getString(cursor.getColumnIndex("RIB"));

            } while (cursor.moveToNext());
        }
        return client;
    }


    //============================== FUNCTION SELECT Clients FROM Client TABLE ===============================
    public String select_ancien_solde_client_from_database(String code_client){

        String ancien_solde = "0.00";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SOLDE FROM Client WHERE CODE_CLIENT = '"+code_client+"'", null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                ancien_solde = cursor.getString(cursor.getColumnIndex("SOLDE"));

            } while (cursor.moveToNext());
        }
        return ancien_solde;
    }

    //============================== FUNCTION SELECT Produits FROM Produit TABLE ===============================
    public ArrayList<PostData_Produit> select_produits_from_database(String querry){
        ArrayList<PostData_Produit> produits = new ArrayList<PostData_Produit>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querry, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PostData_Produit produit = new PostData_Produit();

                produit.code_barre = cursor.getString(cursor.getColumnIndex("CODE_BARRE"));
                produit.ref_produit = cursor.getString(cursor.getColumnIndex("REF_PRODUIT"));
                produit.produit = cursor.getString(cursor.getColumnIndex("PRODUIT"));
                produit.pa_ht = cursor.getString(cursor.getColumnIndex("PA_HT"));
                produit.tva = cursor.getString(cursor.getColumnIndex("TVA"));
                produit.pv1_ht = cursor.getString(cursor.getColumnIndex("PV1_HT"));
                produit.pv2_ht = cursor.getString(cursor.getColumnIndex("PV2_HT"));
                produit.pv3_ht = cursor.getString(cursor.getColumnIndex("PV3_HT"));
                produit.stock = cursor.getString(cursor.getColumnIndex("STOCK"));
                produit.photo = cursor.getBlob(cursor.getColumnIndex("PHOTO"));

                produits.add(produit);
            } while (cursor.moveToNext());
        }


        return produits;
    }


    //============================== FUNCTION SELECT Produits FROM Produit TABLE ===============================
    public String select_codebarre_from_database(String querry){
        String code_barre = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querry, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                code_barre = cursor.getString(cursor.getColumnIndex("CODE_BARRE"));

            } while (cursor.moveToNext());
        }


        return code_barre;
    }


    //============================== FUNCTION SELECT Produits FROM Produits TABLE ===============================
    public ArrayList<PostData_Bon1> select_vente_from_database(String querry){
        ArrayList<PostData_Bon1> bon1s = new ArrayList<PostData_Bon1>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querry, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PostData_Bon1 bon1 = new PostData_Bon1();


                bon1.recordid = cursor.getString(cursor.getColumnIndex("RECORDID"));
                bon1.num_bon = cursor.getString(cursor.getColumnIndex("NUM_BON"));
                bon1.code_client = cursor.getString(cursor.getColumnIndex("CODE_CLIENT"));
                bon1.client = cursor.getString(cursor.getColumnIndex("CLIENT"));
                bon1.date_bon = cursor.getString(cursor.getColumnIndex("DATE_BON"));
                bon1.heure = cursor.getString(cursor.getColumnIndex("HEURE"));
                bon1.nbr_p = cursor.getString(cursor.getColumnIndex("NBR_P"));
                bon1.mode_tarif = cursor.getString(cursor.getColumnIndex("MODE_TARIF"));
                bon1.code_depot = cursor.getString(cursor.getColumnIndex("CODE_DEPOT"));
                bon1.montant_bon = cursor.getString(cursor.getColumnIndex("MONTANT_BON"));
                bon1.mode_rg = cursor.getString(cursor.getColumnIndex("MODE_RG"));
                bon1.code_vendeur = cursor.getString(cursor.getColumnIndex("CODE_VENDEUR"));
                bon1.exportation = cursor.getString(cursor.getColumnIndex("EXPORTATION"));
                bon1.remise = cursor.getString(cursor.getColumnIndex("REMISE"));
                bon1.timbre = cursor.getString(cursor.getColumnIndex("TIMBRE"));
                bon1.verser = cursor.getString(cursor.getColumnIndex("VERSER"));
                bon1.reste = cursor.getString(cursor.getColumnIndex("RESTE"));
                bon1.blocage = cursor.getString(cursor.getColumnIndex("BLOCAGE"));
                bon1.solde_ancien = cursor.getString(cursor.getColumnIndex("ANCIEN_SOLDE"));

                bon1.latitude = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));
                bon1.longitude = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));


                bon1s.add(bon1);
            } while (cursor.moveToNext());
        }
        return bon1s;
    }


    //============================== FUNCTION SELECT Produits FROM Produits TABLE ===============================
    public PostData_Bon1 select_bon1_from_database2(String querry){
        PostData_Bon1 bon1 = new PostData_Bon1();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querry, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                bon1.num_bon = cursor.getString(cursor.getColumnIndex("NUM_BON"));
                bon1.code_client = cursor.getString(cursor.getColumnIndex("CODE_CLIENT"));
                bon1.client = cursor.getString(cursor.getColumnIndex("CLIENT"));
                bon1.credit_limit = cursor.getDouble(cursor.getColumnIndex("CREDIT_LIMIT"));
                bon1.date_bon = cursor.getString(cursor.getColumnIndex("DATE_BON"));
                bon1.heure = cursor.getString(cursor.getColumnIndex("HEURE"));
                bon1.code_depot = cursor.getString(cursor.getColumnIndex("CODE_DEPOT"));
                bon1.tot_ht = cursor.getString(cursor.getColumnIndex("TOT_HT"));
                bon1.tot_tva = cursor.getString(cursor.getColumnIndex("TOT_TVA"));
                bon1.tot_ttc = cursor.getString(cursor.getColumnIndex("TOT_TTC"));
                bon1.tot_ttc_remise = cursor.getString(cursor.getColumnIndex("TOT_TTC_REMISE"));
                bon1.montant_bon = cursor.getString(cursor.getColumnIndex("MONTANT_BON"));
                bon1.remise = cursor.getString(cursor.getColumnIndex("REMISE"));
                bon1.remise_ckecked = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("REMISE_CHECK")));
                bon1.timbre = cursor.getString(cursor.getColumnIndex("TIMBRE"));
                bon1.nbr_p = cursor.getString(cursor.getColumnIndex("NBR_P"));
                bon1.timbre_ckecked = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("TIMBRE_CHECK")));
                bon1.verser = cursor.getString(cursor.getColumnIndex("VERSER"));
                bon1.solde_ancien = cursor.getString(cursor.getColumnIndex("ANCIEN_SOLDE"));
                bon1.reste = cursor.getString(cursor.getColumnIndex("RESTE"));


                bon1.latitude = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));
                bon1.longitude = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));

            } while (cursor.moveToNext());
        }

        return bon1;
    }


    //================== GET MAX NUM_INV INTO TABLE Inventaires1 ===========================
    public String Select_max_num_bon_vente(String _table){
        String max = "0";
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT MAX(NUM_BON) AS max_id FROM " + _table + " WHERE NUM_BON IS NOT NULL";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount() > 0 ) {
            if (cursor.moveToFirst()) {
                if(cursor.getString(0) != null){
                    max   = cursor.getString(cursor.getColumnIndex("max_id"));
                }
            }
        }
        return String.valueOf(Integer.valueOf(max) + 1);
    }

    //=============================== FUNCTION TO INSERT INTO bon1 or bon1_temp TABLE ===============================
    public Boolean Insert_into_bon1(String _table, PostData_Bon1 bon1){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                Boolean bon1_exist = false;
                Cursor cursor0 = db.rawQuery("SELECT NUM_BON FROM "+ _table +" WHERE NUM_BON = '"+bon1.num_bon+"' ", null);
                // looping through all rows and adding to list
                if (cursor0.moveToFirst()) {
                    do {
                        bon1_exist = true;
                    } while (cursor0.moveToNext());
                }

                if(bon1_exist){

                    //update_bon1
                    ContentValues args1 = new ContentValues();
                    args1.put("CODE_CLIENT", bon1.code_client);
                    String selection1 = "NUM_BON=?";
                    String[] selectionArgs1 = {bon1.num_bon};
                    db.update(_table, args1, selection1, selectionArgs1);

                }else {
                    ContentValues values = new ContentValues();
                    values.put("NUM_BON", bon1.num_bon);
                    values.put("CODE_CLIENT", bon1.code_client);
                    values.put("DATE_BON", bon1.date_bon);
                    values.put("HEURE", bon1.heure);
                    values.put("CODE_DEPOT", bon1.code_depot);
                    values.put("MONTANT_BON", "0.00");
                    values.put("VERSER", "0.00");
                    values.put("BLOCAGE", "N");
                    values.put("EXPORTATION", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) +"");
                    db.insert(_table, null, values);
                }

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;

    }


    //=============================== FUNCTION TO INSERT INTO bon1 or bon1_temp TABLE ===============================
    public Boolean ExCommande_Export_to_ventes(String _table1, String _table2, PostData_Bon1 bon1, ArrayList<PostData_Bon2> bon2s, String new_num_bon){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                    // Insert into bon1
                    ContentValues values = new ContentValues();
                    values.put("NUM_BON", new_num_bon);
                    values.put("CODE_CLIENT", bon1.code_client);
                    values.put("DATE_BON", bon1.date_bon);
                    values.put("HEURE", bon1.heure);
                    values.put("CODE_DEPOT", bon1.code_depot);
                    values.put("VERSER", "0.00");
                    values.put("BLOCAGE", "N");
                    values.put("EXPORTATION", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) +"");
                    values.put("NBR_P", bon1.nbr_p);
                    values.put("MODE_TARIF", bon1.mode_tarif);
                    values.put("TOT_HT", bon1.tot_ht);
                    values.put("TOT_TVA", bon1.tot_tva);
                    values.put("TOT_TTC", bon1.tot_ttc);

                    values.put("REMISE", bon1.remise);
                    if(bon1.remise_ckecked){
                        values.put("REMISE_CHECK", "TRUE");
                    }else{
                        values.put("REMISE_CHECK", "FALSE");
                    }
                    values.put("TIMBRE", bon1.timbre);
                    if(bon1.timbre_ckecked){
                        values.put("TIMBRE_CHECK", "TRUE");
                    }else{
                        values.put("TIMBRE_CHECK", "FALSE");
                    }
                    values.put("TOT_TTC_REMISE", bon1.tot_ttc_remise);
                    values.put("MONTANT_BON", bon1.montant_bon);
                    values.put("CODE_VENDEUR", bon1.code_vendeur);
                    values.put("ANCIEN_SOLDE", bon1.solde_ancien);
                    values.put("RESTE", bon1.reste);
                    values.put("LATITUDE", bon1.latitude);
                    values.put("LONGITUDE", bon1.longitude);
                    db.insert(_table1, null, values);



                // Insert into bon2

                for(int i = 0; i< bon2s.size(); i++){
                    ContentValues values2 = new ContentValues();
                    values2.putNull("RECORDID");
                    values2.put("NUM_BON", new_num_bon);
                    values2.put("CODE_BARRE", bon2s.get(i).codebarre);
                    values2.put("PRODUIT", bon2s.get(i).produit);
                    values2.put("QTE", bon2s.get(i).qte);
                    values2.put("PV_HT", bon2s.get(i).p_u);
                    values2.put("TVA", bon2s.get(i).tva);
                    values2.put("CODE_DEPOT", bon1.code_depot);
                    values2.put("PA_HT", bon2s.get(i).pa_ht);

                    db.insert(_table2, null, values2);

                    destock_produit(bon2s.get(i));
                }


                // Update Bon1_temp
                ContentValues args1 = new ContentValues();
                args1.put("BLOCAGE", "V");
                String selection1 = "NUM_BON=?";
                String[] selectionArgs1 = {bon1.num_bon};
                db.update("Bon1_temp", args1, selection1, selectionArgs1);

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;

    }
    //============================== FUNCTION SELECT Produits FROM Produits TABLE ===============================
    public ArrayList<PostData_Bon1> select(String querry){
        ArrayList<PostData_Bon1> bon1s = new ArrayList<PostData_Bon1>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querry, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PostData_Bon1 bon1 = new PostData_Bon1();

                bon1.recordid = cursor.getString(cursor.getColumnIndex("NUM_BON"));

                bon1s.add(bon1);
            } while (cursor.moveToNext());
        }
        return bon1s;
    }
    //=============================== FUNCTION TO INSERT INTO Inventaires2 TABLE ===============================
    public Boolean Insert_into_bon2(String _table, String num_bon, String code_depot, PostData_Bon2 list_bon2){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                    ContentValues values2 = new ContentValues();
                    values2.putNull("RECORDID");
                    values2.put("NUM_BON", num_bon);
                    values2.put("CODE_BARRE", list_bon2.codebarre);
                    values2.put("PRODUIT", list_bon2.produit);
                    values2.put("QTE", list_bon2.qte);
                    values2.put("PV_HT", list_bon2.p_u);
                    values2.put("TVA", list_bon2.tva);
                    values2.put("CODE_DEPOT", code_depot);
                    values2.put("PA_HT", list_bon2.pa_ht);

                    db.insert(_table, null, values2);

                destock_produit(list_bon2);

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;

    }

    //=============================== FUNCTION TO UPDATE CLIENT ===============================
    public Boolean Update_into_bon2_qte(String num_bon, String code_barre, String qte){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                //update_bon1
                ContentValues args1 = new ContentValues();
                args1.put("QTE", qte);
                String selection1 = "CODE_BARRE=? AND NUM_BON=?";
                String[] selectionArgs1 = {code_barre, num_bon};
                db.update("Bon2", args1, selection1, selectionArgs1);

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;

    }

    //=============================== FUNCTION TO UPDATE CLIENT ===============================
    public Boolean Update_into_bon2_pv(String num_bon, String code_barre, String pv){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                //update_bon2
                ContentValues args1 = new ContentValues();
                args1.put("QTE", pv);
                String selection1 = "CODE_BARRE=? NUM_BON?";
                String[] selectionArgs1 = {code_barre, num_bon};
                db.update("Bon2", args1, selection1, selectionArgs1);

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;

    }

    ////////////////////////////////////// DELETING ////////////////////////////////////////////////
    public Boolean delete_from_bon2(Boolean isTemp, Integer recordid, String num_bon, PostData_Bon2 bon2){
        Boolean executed = false;
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();

            try {

                if(isTemp){

                String selection = "RECORDID=?";
                String[] selectionArgs = {recordid.toString()};
                db.delete("Bon2_temp", selection, selectionArgs);

                stock_produit(bon2);

                String nbr_p = "0";
                Cursor cursor1 = db.rawQuery("SELECT  NBR_P FROM Bon1_temp WHERE NUM_BON = '"+ num_bon + "'", null);
                // looping through all rows and adding to list
                if (cursor1.moveToFirst()) {
                    do {
                        nbr_p = cursor1.getString(cursor1.getColumnIndex("NBR_P"));

                    } while (cursor1.moveToNext());
                }

                //update_bon1
                if(nbr_p == null){
                    nbr_p  = "0";
                }
                ContentValues args1 = new ContentValues();
                args1.put("NBR_P", (Integer.valueOf(nbr_p) - 1));
                String selection1 = "NUM_BON=?";
                String[] selectionArgs1 = {num_bon};
                db.update("Bon1_temp", args1, selection1, selectionArgs1);

                }else{

                String selection = "RECORDID=?";
                String[] selectionArgs = {recordid.toString()};
                db.delete("Bon2", selection, selectionArgs);

                stock_produit(bon2);

                String nbr_p = "0";
                Cursor cursor1 = db.rawQuery("SELECT  NBR_P FROM Bon1 WHERE NUM_BON = '"+ num_bon + "'", null);
                // looping through all rows and adding to list
                if (cursor1.moveToFirst()) {
                    do {
                        nbr_p = cursor1.getString(cursor1.getColumnIndex("NBR_P"));

                    } while (cursor1.moveToNext());
                }

                cursor1.close();
                //update_bon1
                if(nbr_p == null){
                    nbr_p  = "0";
                }
                ContentValues args1 = new ContentValues();
                args1.put("NBR_P", (Integer.valueOf(nbr_p) - 1));
                String selection1 = "NUM_BON=?";
                String[] selectionArgs1 = {num_bon};
                db.update("Bon1", args1, selection1, selectionArgs1);

                }

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
            executed =  false;
        }
        return executed;
    }


    //=============================== FUNCTION TO UPDATE CLIENT ===============================
    public Boolean update_bon1_client(String num_bon, PostData_Bon1 bon1){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                //update_bon1
                ContentValues args1 = new ContentValues();
                args1.put("BLOCAGE", "F");
                args1.put("ANCIEN_SOLDE", bon1.solde_ancien);
                args1.put("VERSER", bon1.verser);
                if(Double.valueOf(bon1.verser) == 0){
                    args1.put("MODE_RG", "A TERME");
                }else{
                    args1.put("MODE_RG", "ESPECE");
                }
                args1.put("RESTE", bon1.reste);
                args1.put("LATITUDE", bon1.latitude);
                args1.put("LONGITUDE", bon1.longitude);
                String selection1 = "NUM_BON=?";
                String[] selectionArgs1 = {num_bon};
                db.update("Bon1", args1, selection1, selectionArgs1);

                // get information client
                PostData_Client client = new PostData_Client();
                Cursor cursor1 = db.rawQuery("SELECT  ACHATS, VERSER, SOLDE FROM Client WHERE CODE_CLIENT='"+ bon1.code_client+ "'", null);
                // looping through all rows and adding to list
                if (cursor1.moveToFirst()) {
                    do {
                        client.achat_montant = cursor1.getString(cursor1.getColumnIndex("ACHATS"));
                        client.verser_montant = cursor1.getString(cursor1.getColumnIndex("VERSER"));
                        client.solde_montant = cursor1.getString(cursor1.getColumnIndex("SOLDE"));

                    } while (cursor1.moveToNext());
                }

                cursor1.close();
                //update client

                ContentValues args = new ContentValues();
                args.put("ACHATS", String.valueOf(Double.valueOf(client.achat_montant) + Double.valueOf(bon1.montant_bon)));
                args.put("VERSER", String.valueOf(Double.valueOf(client.verser_montant) + Double.valueOf(bon1.verser)));
                args.put("SOLDE", String.valueOf((Double.valueOf(client.solde_montant) + Double.valueOf(bon1.montant_bon)) - Double.valueOf(bon1.verser)));
                String selection = "CODE_CLIENT=?";
                String[] selectionArgs = {bon1.code_client};
                db.update("Client", args, selection, selectionArgs);

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;

    }


    //=============================== FUNCTION TO UPDATE CLIENT ===============================
    public Boolean update_bon1_client_edit(String num_bon, String code_client, PostData_Bon1 bon1){

        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                //update_bon1
                ContentValues args1 = new ContentValues();
                args1.put("BLOCAGE", "N");
                args1.put("ANCIEN_SOLDE", "0.00");
                args1.put("VERSER", "0.00");
                args1.put("RESTE", "0.00");
                String selection1 = "NUM_BON=?";
                String[] selectionArgs1 = {num_bon};
                db.update("Bon1", args1, selection1, selectionArgs1);

                //update_client
                PostData_Client client = new PostData_Client();
                Cursor cursor1 = db.rawQuery("SELECT  ACHATS, VERSER, SOLDE FROM Client WHERE CODE_CLIENT='"+ code_client+ "'", null);
                // looping through all rows and adding to list
                if (cursor1.moveToFirst()) {
                    do {
                        client.achat_montant = cursor1.getString(cursor1.getColumnIndex("ACHATS"));
                        client.verser_montant = cursor1.getString(cursor1.getColumnIndex("VERSER"));
                        client.solde_montant = cursor1.getString(cursor1.getColumnIndex("SOLDE"));

                    } while (cursor1.moveToNext());
                }

                ContentValues args = new ContentValues();
                args.put("ACHATS", String.valueOf(Double.valueOf(client.achat_montant) - Double.valueOf(bon1.montant_bon)));
                args.put("VERSER", String.valueOf(Double.valueOf(client.verser_montant) - Double.valueOf(bon1.verser)));
                args.put("SOLDE", String.valueOf((Double.valueOf(client.solde_montant) - Double.valueOf(bon1.montant_bon)) +  Double.valueOf(bon1.verser)));
                String selection = "CODE_CLIENT=?";
                String[] selectionArgs = {bon1.code_client};
                db.update("Client", args, selection, selectionArgs);

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;

    }


    public Boolean Update_ventes_commandes_as_exported(Boolean isTemp, String num_bon){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                // Update Bon1 or Bon1_temp
                ContentValues args1 = new ContentValues();
                args1.put("IS_EXPORTED", 1);
                String selection1 = "NUM_BON=?";
                String[] selectionArgs1 = {num_bon};

                if(isTemp){
                    db.update("Bon1_temp", args1, selection1, selectionArgs1);
                }else{
                    db.update("Bon1", args1, selection1, selectionArgs1);
                }

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;

    }

    public boolean check_if_bon1_valide(String _table, String num_bon){
        //check if bon exist
        Boolean bon1_exist = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor0 = db.rawQuery("SELECT NUM_BON FROM " + _table + " WHERE NUM_BON = '"+num_bon+"' AND BLOCAGE = 'F'", null);
        // looping through all rows and adding to list
        if (cursor0.moveToFirst()) {
            do {
                bon1_exist = true;
            } while (cursor0.moveToNext());
        }

        return  bon1_exist;
    }

    //============================== FUNCTION SELECT Produits FROM Produits TABLE ===============================
    public ArrayList<PostData_Bon2> select_bon2_from_database(String querry){
        ArrayList<PostData_Bon2> bon2s = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querry, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PostData_Bon2 bon2 = new PostData_Bon2();

                bon2.recordid = cursor.getInt(cursor.getColumnIndex("RECORDID"));
                bon2.codebarre = cursor.getString(cursor.getColumnIndex("CODE_BARRE"));
                bon2.num_bon = cursor.getString(cursor.getColumnIndex("NUM_BON"));
                bon2.produit = cursor.getString(cursor.getColumnIndex("PRODUIT"));
                bon2.qte = cursor.getString(cursor.getColumnIndex("QTE"));
                bon2.p_u = cursor.getString(cursor.getColumnIndex("PV_HT"));
                bon2.tva = cursor.getString(cursor.getColumnIndex("TVA"));
                bon2.code_depot = cursor.getString(cursor.getColumnIndex("CODE_DEPOT"));
                bon2.pa_ht = cursor.getString(cursor.getColumnIndex("PA_HT"));
                bon2.stock_produit = cursor.getString(cursor.getColumnIndex("STOCK"));


                bon2s.add(bon2);
            } while (cursor.moveToNext());
        }
        return bon2s;
    }

    //================================== UPDATE TABLE (Inventaires1) =======================================
    public boolean stock_produit( PostData_Bon2 panier){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                    ContentValues args = new ContentValues();
                    args.put("STOCK", Double.parseDouble(panier.stock_produit) + Double.parseDouble(panier.qte));
                    String selection = "CODE_BARRE=?";
                    String[] selectionArgs = {panier.codebarre};
                    db.update("Produit", args, selection, selectionArgs);

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;
    }

    //================================== UPDATE TABLE (Inventaires1) =======================================
    public boolean destock_produit( PostData_Bon2 panier){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                    ContentValues args = new ContentValues();
                    args.put("STOCK", Double.parseDouble(panier.stock_produit) - Double.parseDouble(panier.qte));
                    String selection = "CODE_BARRE=?";
                    String[] selectionArgs = {panier.codebarre};
                    db.update("Produit", args, selection, selectionArgs);


                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;
    }


    //================================== UPDATE TABLE (Inventaires1) =======================================
    public boolean update_client(Double latitude , Double longitude, String code_client){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                ContentValues args = new ContentValues();
                args.put("LATITUDE", latitude);
                args.put("LONGITUDE", longitude);
                String selection = "CODE_CLIENT=?";
                String[] selectionArgs = {code_client};
                db.update("Client", args, selection, selectionArgs);

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;
    }

    //================================== UPDATE TABLE (Inventaires1) =======================================
    public boolean update_bon1(String _table, String num_bon, PostData_Bon1 bon1){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                ContentValues args = new ContentValues();

                args.put("NBR_P", bon1.nbr_p);
                args.put("MODE_TARIF", bon1.mode_tarif);
                args.put("TOT_HT", bon1.tot_ht);
                args.put("TOT_TVA", bon1.tot_tva);
                args.put("TOT_TTC", bon1.tot_ttc);
                args.put("REMISE", bon1.remise);
                if(bon1.remise_ckecked){
                    args.put("REMISE_CHECK", "TRUE");
                }else{
                    args.put("REMISE_CHECK", "FALSE");
                }
                args.put("TIMBRE", bon1.timbre);
                if(bon1.timbre_ckecked){
                    args.put("TIMBRE_CHECK", "TRUE");
                }else{
                    args.put("TIMBRE_CHECK", "FALSE");
                }
                args.put("TOT_TTC_REMISE", bon1.tot_ttc_remise);
                args.put("MONTANT_BON", bon1.montant_bon);
                args.put("MODE_RG", bon1.mode_rg);
                args.put("CODE_VENDEUR", bon1.code_vendeur);
                args.put("ANCIEN_SOLDE", bon1.solde_ancien);
                args.put("RESTE", bon1.reste);

                String selection = "NUM_BON=?";
                String[] selectionArgs = {num_bon};
                db.update(_table, args, selection, selectionArgs);

                db.setTransactionSuccessful();
                executed =  true;
            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
        }
        return executed;
    }


    ////////////////////////////////////// DELETING ////////////////////////////////////////////////
    public Boolean delete_bon_vente(Boolean isTemp, PostData_Bon1 bon1){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {
                if(isTemp){
                    String selection = "NUM_BON=?";
                    String[] selectionArgs = {bon1.num_bon};
                    db.delete("Bon1_temp", selection, selectionArgs);
                    db.delete("Bon2_temp", selection, selectionArgs);
                }else{

                    ArrayList<PostData_Bon2> bon2_delete;
                    bon2_delete = select_bon2_from_database("" +
                            "SELECT " +
                            "Bon2.RECORDID, " +
                            "Bon2.CODE_BARRE, " +
                            "Bon2.NUM_BON, " +
                            "Bon2.PRODUIT, " +
                            "Bon2.QTE, " +
                            "Bon2.PV_HT, " +
                            "Bon2.TVA, " +
                            "Bon2.CODE_DEPOT, " +
                            "Bon2.PA_HT, " +
                            "Produit.STOCK " +
                            "FROM Bon2 " +
                            "INNER JOIN " +
                            "Produit ON (Bon2.CODE_BARRE = Produit.CODE_BARRE) " +
                            "WHERE Bon2.NUM_BON = '" + bon1.num_bon + "'" );

                    for(int k=0; k< bon2_delete.size();k++) {

                        ContentValues args = new ContentValues();
                        args.put("STOCK", Double.valueOf(bon2_delete.get(k).stock_produit) + Double.valueOf(bon2_delete.get(k).qte) );

                        String selection = "CODE_BARRE=?";
                        String[] selectionArgs = {bon2_delete.get(k).codebarre};
                        db.update("Produit", args, selection, selectionArgs);
                    }


                    //update_client
                    PostData_Client client = new PostData_Client();
                    Cursor cursor1 = db.rawQuery("SELECT  ACHATS, VERSER, SOLDE FROM Client WHERE CODE_CLIENT='"+ bon1.code_client+ "'", null);
                    // looping through all rows and adding to list
                    if (cursor1.moveToFirst()) {
                        do {
                            client.achat_montant = cursor1.getString(cursor1.getColumnIndex("ACHATS"));
                            client.verser_montant = cursor1.getString(cursor1.getColumnIndex("VERSER"));
                            client.solde_montant = cursor1.getString(cursor1.getColumnIndex("SOLDE"));

                        } while (cursor1.moveToNext());
                    }

                    ContentValues args = new ContentValues();
                    args.put("ACHATS", String.valueOf(Double.valueOf(client.achat_montant) - Double.valueOf(bon1.montant_bon)));
                    args.put("VERSER", String.valueOf(Double.valueOf(client.verser_montant) - Double.valueOf(bon1.verser)));
                    args.put("SOLDE", String.valueOf((Double.valueOf(client.solde_montant) - Double.valueOf(bon1.montant_bon)) +  Double.valueOf(bon1.verser)));
                    String selection = "CODE_CLIENT=?";
                    String[] selectionArgs = {bon1.code_client};
                    db.update("Client", args, selection, selectionArgs);

                    // finally delete bon
                    String selection1 = "NUM_BON=?";
                    String[] selectionArgs1 = {bon1.num_bon};
                    db.delete("Bon1", selection1, selectionArgs1);
                    db.delete("Bon2", selection1, selectionArgs1);
                }

                db.setTransactionSuccessful();
                executed =  true;

            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
            executed =  false;
        }
        return executed;
    }


    public Boolean delete_bon_en_attente(Boolean isTemp, String num_bon){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {
                if(isTemp){
                    String selection = "NUM_BON=?";
                    String[] selectionArgs = {num_bon};
                    db.delete("Bon1_temp", selection, selectionArgs);
                    db.delete("Bon2_temp", selection, selectionArgs);
                }else{

                    ArrayList<PostData_Bon2> bon2_delete;
                    bon2_delete = select_bon2_from_database("" +
                            "SELECT " +
                            "Bon2.RECORDID, " +
                            "Bon2.CODE_BARRE, " +
                            "Bon2.NUM_BON, " +
                            "Bon2.PRODUIT, " +
                            "Bon2.QTE, " +
                            "Bon2.PV_HT, " +
                            "Bon2.TVA, " +
                            "Bon2.CODE_DEPOT, " +
                            "Bon2.PA_HT, " +
                            "Produit.STOCK " +
                            "FROM Bon2 " +
                            "INNER JOIN " +
                            "Produit ON (Bon2.CODE_BARRE = Produit.CODE_BARRE) " +
                            "WHERE Bon2.NUM_BON = '" + num_bon + "'" );

                    for(int k=0; k< bon2_delete.size();k++) {

                        ContentValues args = new ContentValues();
                        args.put("STOCK", Double.valueOf(bon2_delete.get(k).stock_produit) + Double.valueOf(bon2_delete.get(k).qte) );

                        String selection = "CODE_BARRE=?";
                        String[] selectionArgs = {bon2_delete.get(k).codebarre};
                        db.update("Produit", args, selection, selectionArgs);
                    }

                    // finally delete bon
                    String selection1 = "NUM_BON=?";
                    String[] selectionArgs1 = {num_bon};
                    db.delete("Bon1", selection1, selectionArgs1);
                    db.delete("Bon2", selection1, selectionArgs1);
                }

                db.setTransactionSuccessful();
                executed =  true;

            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
            executed =  false;
        }
        return executed;
    }


    public Boolean delete_bon_after_export(Boolean isTemp, String num_bon){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {
                if(isTemp){
                    String selection = "NUM_BON=?";
                    String[] selectionArgs = {num_bon};
                    db.delete("Bon1_temp", selection, selectionArgs);
                    db.delete("Bon2_temp", selection, selectionArgs);
                }else{
                    // finally delete bon
                    String selection1 = "NUM_BON=?";
                    String[] selectionArgs1 = {num_bon};
                    db.delete("Bon1", selection1, selectionArgs1);
                    db.delete("Bon2", selection1, selectionArgs1);
                }

                db.setTransactionSuccessful();
                executed =  true;

            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
            executed =  false;
        }
        return executed;
    }



    public Boolean delete_all_bon(Boolean isTemp){
        Boolean executed = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {
                if(isTemp){
                    //delete all temp bon

                    String sql_delete_bon2 = "DELETE FROM Bon2_Temp WHERE Bon2_Temp.NUM_BON IN ( SELECT Bon1_Temp.NUM_BON FROM Bon1_Temp WHERE Bon1_Temp.IS_EXPORTED = 1)";
                    String sql_delete_bon1 = "DELETE FROM Bon1_Temp WHERE IS_EXPORTED = 1";
                    db.execSQL(sql_delete_bon2);
                    db.execSQL(sql_delete_bon1);
                    /*
                    String selection = "NUM_BON=?";
                    String[] selectionArgs = {num_bon.toString()};
                    db.delete("Bon1_temp", selection, selectionArgs);
                    db.delete("Bon2_temp", selection, selectionArgs);
                    */
                }else{
                    // finally delete all bon
                    String sql_delete_bon2 = "DELETE FROM Bon2 WHERE Bon2.NUM_BON IN ( SELECT Bon1.NUM_BON FROM Bon1 WHERE Bon1.IS_EXPORTED = 1)";
                    String sql_delete_bon1 = "DELETE FROM Bon1 WHERE IS_EXPORTED = 1";
                    db.execSQL(sql_delete_bon2);
                    db.execSQL(sql_delete_bon1);
                    /*
                    String selection1 = "NUM_BON=?";
                    String[] selectionArgs1 = {num_bon.toString()};
                    db.delete("Bon1", selection1, selectionArgs1);
                    db.delete("Bon2", selection1, selectionArgs1);
                    */
                }

                db.setTransactionSuccessful();
                executed =  true;

            } finally {
                db.endTransaction();
            }
        }catch (SQLiteDatabaseLockedException sqlilock){
            Log.v("TRACKKK", sqlilock.getMessage());
            executed =  false;
        }
        return executed;
    }


    public void ResetPda(){
        mContext.deleteDatabase(DATABASE_NAME);
    }

    public File copyAppDbToDownloadFolder(String imei) {
        try {
            File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "backup_distribute_"+imei); // for example "my_data_backup.db"
            File currentDB = mContext.getDatabasePath(DATABASE_NAME); //databaseName=your current application database name, for example "my_data.db"
            if (currentDB.exists()) {
                FileInputStream fis = new FileInputStream(currentDB);
                FileOutputStream fos = new FileOutputStream(backupDB);
                fos.getChannel().transferFrom(fis.getChannel(), 0, fis.getChannel().size());
                // or fis.getChannel().transferTo(0, fis.getChannel().size(), fos.getChannel());
                fis.close();
                fos.close();
                Log.i("TRACKKK", " copied to download folder");
                return backupDB;
            } else {
                Log.i("TRACKKK", " fail, database not found");
                return null;
            }
        } catch (IOException e) {
            Log.d("TRACKKK", "fail, reason:", e);
            return null;
        }
    }




    public String Get_Digits_String(String number, Integer length){
        String _number = number;
        while(_number.length() < length){
            _number = "0" + _number;
        }
        Log.v("TRACKKK", _number);
        return _number;
    }


    //============================== FUNCTION SELECT FROM code_barre TABLE ===============================
    public PostData_Codebarre select_produit_codebarre(String scan_result){
        SQLiteDatabase db = this.getWritableDatabase();
        PostData_Codebarre codebarre = new PostData_Codebarre();
        codebarre.exist = false;
        String querry  = "SELECT * FROM Codebarre WHERE CODE_BARRE_SYN = '" + scan_result + "'";
        Cursor cursor = db.rawQuery(querry, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                codebarre.code_barre = cursor.getString(cursor.getColumnIndex("CODE_BARRE"));
                codebarre.exist = true;
            } while (cursor.moveToNext());
        }
        return codebarre;
    }
}