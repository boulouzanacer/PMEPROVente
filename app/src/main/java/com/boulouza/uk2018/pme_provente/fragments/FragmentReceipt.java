package com.boulouza.uk2018.pme_provente.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.boulouza.uk2018.pme_provente.ItemListActivity;
import com.boulouza.uk2018.pme_provente.R;
import com.boulouza.uk2018.pme_provente.adapters.ListViewAdapter_Receipt;
import com.boulouza.uk2018.pme_provente.databases.DATABASE;
import com.boulouza.uk2018.pme_provente.interfaces.ClickInterface;
import com.boulouza.uk2018.pme_provente.postData.PostData_Bon2;

import java.util.ArrayList;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class FragmentReceipt extends Fragment implements ClickInterface {

    private SwipeMenuListView listview_receipt;
    private ListViewAdapter_Receipt Adapter_listview;
    ArrayList<PostData_Bon2> bon2s;
    DATABASE controller;

    private MediaPlayer mp;

    private ClickInterface listner;

    public FragmentReceipt() {
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
        PostData_Bon2 bon2 = new PostData_Bon2();
        bon2.num_bon = "000001";
        bon2.code_depot = "000001";
        bon2.codebarre = "2343444553434";
        bon2.qte = "34";
        bon2.montant_produit = "2324.553";
        bon2.p_u = "3200.00";
        bon2.produit = "sdsfsferfzz";
        bon2.stock_produit = "25";
        bon2.tva = "0.00";
        bon2.checked = false;
        bon2.pa_ht = "2344";
        bon2.recordid = 000001;

        controller.Insert_into_bon2("Bon2","000001","000001", bon2);

        setRecycle();
    }

    private void initViews(View root) {

        listview_receipt = (SwipeMenuListView) root.findViewById(R.id.listView22);
    }

    private void setRecycle() {
        Adapter_listview = new ListViewAdapter_Receipt(getActivity(), getItems());
        listview_receipt.setAdapter(Adapter_listview);
    }


    public ArrayList<PostData_Bon2> getItems() {
        bon2s = new ArrayList<>();
        bon2s.clear();

        String NUM_BON = "000001";
        bon2s =  controller.select_bon2_from_database("" +
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
                "WHERE Bon2.NUM_BON = '" + NUM_BON+ "'" );

        return bon2s;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_receipt, container, false);

        initViews(rootView);

        return rootView;
    }


    public void Sound(int SourceSound){
        mp = MediaPlayer.create(getActivity(), SourceSound);
        mp.start();
    }


    @Override
    public void recyclerAdapterOnClick(int position) {

    }

    @Override
    public void recyclerAdapterOnLongClick(int position) {

    }
}
