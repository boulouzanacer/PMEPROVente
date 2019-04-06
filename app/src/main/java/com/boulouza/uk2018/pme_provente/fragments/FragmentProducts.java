package com.boulouza.uk2018.pme_provente.fragments;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boulouza.uk2018.pme_provente.ItemListActivity;
import com.boulouza.uk2018.pme_provente.R;
import com.boulouza.uk2018.pme_provente.adapters.RecyclerViewAdapterProducts;
import com.boulouza.uk2018.pme_provente.databases.DATABASE;
import com.boulouza.uk2018.pme_provente.dummy.DummyContent;
import com.boulouza.uk2018.pme_provente.postData.PostData_Produit;

import java.util.ArrayList;


public class FragmentProducts extends Fragment {


    RecyclerViewAdapterProducts adapter;
    RecyclerView recyclerView;
    ArrayList<PostData_Produit> produits;
    DATABASE controller;
    private MediaPlayer mp;

    public FragmentProducts() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initViews(View rootV) {

        recyclerView = (RecyclerView) rootV.findViewById(R.id.recyclerView);

        // Show the dummy content as text in a TextView.
      /*  if (mItem != null) {
            ((TextView) rootV.findViewById(R.id.item_detail)).setText("FragmentClients");
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();

        controller = new DATABASE(getActivity());
        PostData_Produit produit = new PostData_Produit();
        produit.code_barre = "3456724567864";
        produit.produit = "Marlhboro";
        produit.pa_ht = "100.34";
        produit.pv1_ht = "230.00";
        produit.pv2_ht = "200.00";
        produit.pv3_ht = "150.00";
        produit.stock = "40";
        produit.tva = "0.00";
        produit.exist = false;
        produit.ref_produit = "2345678945644";

        controller.Insert_into_produit(produit);
        // Set title bar
        ((ItemListActivity) getActivity()).setActionBarTitle("Produits");

        setRecycle("");
    }

    private void setRecycle(String text_search) {

       /* RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(getActivity(), getItems(text_search));
        recyclerView.setAdapter(adapter);
*/

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView is passes to achieve expand/collapse functionality correctly.
        adapter = new RecyclerViewAdapterProducts(recyclerView);
        adapter.setData(getItems(""));
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<PostData_Produit> getItems(String querry_search) {
        if(querry_search.length() >0){
            produits = new ArrayList<>();

            String querry = "SELECT * FROM Produit WHERE PRODUIT LIKE '%"+querry_search+"%' OR CODE_BARRE LIKE '%"+querry_search+"%' OR REF_PRODUIT LIKE '%"+querry_search+"%' ORDER BY PRODUIT";
            // querry = "SELECT * FROM Events";
            produits = controller.select_produits_from_database(querry);
        }else {
            produits = new ArrayList<>();

            String querry = "SELECT * FROM Produit ORDER BY PRODUIT";
            // querry = "SELECT * FROM Events";
            produits = controller.select_produits_from_database(querry);
        }

        return produits;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products, container, false);

        initViews(rootView);

        return rootView;
    }
}
