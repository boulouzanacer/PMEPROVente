package com.boulouza.uk2018.pme_provente.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.boulouza.uk2018.pme_provente.ActivitySales;
import com.boulouza.uk2018.pme_provente.ItemListActivity;
import com.boulouza.uk2018.pme_provente.R;
import com.boulouza.uk2018.pme_provente.adapters.RecyclerAdapterBon1;
import com.boulouza.uk2018.pme_provente.databases.DATABASE;
import com.boulouza.uk2018.pme_provente.interfaces.ClickInterface;
import com.boulouza.uk2018.pme_provente.postData.PostData_Bon1;

import java.util.ArrayList;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class FragmentSales extends Fragment implements ClickInterface {

    RecyclerView recyclerView;
    RecyclerAdapterBon1 adapter;
    ArrayList<PostData_Bon1> bon1s;
    DATABASE controller;

    private MediaPlayer mp;

    private ClickInterface listner;

    public FragmentSales() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listner = this;
    }

    @Override
    public void onResume() {
        super.onResume();

        controller = new DATABASE(getActivity());
        PostData_Bon1 bon1 = new PostData_Bon1();

        bon1.num_bon = "000001";
        bon1.client = "NASSER";
        bon1.blocage = "F";
        bon1.code_client = "000034";
        bon1.code_depot = "000001";
        bon1.credit_limit = 30000.00;
        bon1.date_bon = "13/03/2019";
        bon1.heure = "23:44:54";
        bon1.mode_rg = "ESPECE";
        bon1.exportation = "NON";
        bon1.mode_tarif = "2";
        bon1.nbr_p = "2";
        bon1.timbre_ckecked = false;
        bon1.recordid = "000001";
        bon1.tot_ht = "3333.45";
        bon1.tot_ttc = "2344.34";
        bon1.tot_ttc_remise = "2344.22";
        bon1.tot_tva = "0.00";
        bon1.montant_bon = "23423.00";
        bon1.verser = "122.00";
        bon1.latitude = 3.34445;
        bon1.longitude = 36.23234;

        controller.Insert_into_bon1("Bon1", bon1);

        setRecycle();

        // Set title bar
        ((ItemListActivity) getActivity()).setActionBarTitle("Ventes");
    }

    private void initViews(View root) {

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
    }

    private void setRecycle() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapterBon1(getContext(), getItems(), listner );
        recyclerView.setAdapter(adapter);
    }


    public ArrayList<PostData_Bon1> getItems() {
        bon1s = new ArrayList<>();
        bon1s.clear();

        String querry = "SELECT " +
                "Bon1.RECORDID, " +
                "Bon1.NUM_BON, " +
                "Bon1.CODE_CLIENT, " +
                "Client.CLIENT, " +
                "Bon1.LATITUDE, " +
                "Bon1.LONGITUDE, " +
                "Bon1.DATE_BON, " +
                "Bon1.HEURE, " +
                "Bon1.NBR_P, " +
                "Bon1.MODE_TARIF, " +
                "Bon1.CODE_DEPOT, " +
                "Bon1.MONTANT_BON, " +
                "Bon1.MODE_RG, " +
                "Bon1.CODE_VENDEUR, " +
                "Bon1.EXPORTATION, " +
                "Bon1.REMISE, " +
                "Bon1.TIMBRE, " +
                "Bon1.VERSER, " +
                "Bon1.RESTE, " +
                "Bon1.BLOCAGE, " +
                "Bon1.ANCIEN_SOLDE " +
                "FROM Bon1 " +
                "LEFT JOIN Client ON " +
                "Bon1.CODE_CLIENT = Client.CODE_CLIENT " +
                "WHERE IS_EXPORTED = 0 ORDER BY Bon1.DATE_BON DESC";

        // querry = "SELECT * FROM Events";
        bon1s = controller.select_vente_from_database(querry);

        return bon1s;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sales, container, false);

        initViews(rootView);

        return rootView;
    }

    public void Sound(int SourceSound){
        mp = MediaPlayer.create(getActivity(), SourceSound);
        mp.start();
    }

    @Override
    public void recyclerAdapterOnClick(int position) {

        Sound(R.raw.beep);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator()); //add this
        fadeIn.setDuration(300);

        if(bon1s.get(position).blocage.equals("F")){
            Intent intent = new Intent(getActivity(), ActivitySales.class);
            intent.putExtra("NUM_BON", bon1s.get(position).num_bon);
            startActivity(intent);
        }else{
            Intent editIntent = new Intent(getActivity(), ActivitySales.class);
            editIntent.putExtra("NUM_BON", bon1s.get(position).num_bon);
            editIntent.putExtra("VALIDATED", "FALSE");
            startActivity(editIntent);
        }

        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public void recyclerAdapterOnLongClick(int position) {

    }
}
