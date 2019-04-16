package com.boulouza.uk2018.pme_provente.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boulouza.uk2018.pme_provente.ItemListActivity;
import com.boulouza.uk2018.pme_provente.R;
import com.boulouza.uk2018.pme_provente.adapters.RecyclerAdapterAddProducts;
import com.boulouza.uk2018.pme_provente.databases.DATABASE;
import com.boulouza.uk2018.pme_provente.interfaces.ClickInterface;
import com.boulouza.uk2018.pme_provente.postData.PostData_Produit;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class FragmentProductsInSales extends Fragment implements ClickInterface {


    RecyclerAdapterAddProducts adapter;
    RecyclerView recyclerView;
    ArrayList<PostData_Produit> produits;
    DATABASE controller;
    private MediaPlayer mp;

    private ClickInterface listner;


    public FragmentProductsInSales() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listner = this;
    }

    private void initViews(View rootV) {

        recyclerView = (RecyclerView) rootV.findViewById(R.id.recyclerView);

    }

    @Override
    public void onResume() {
        super.onResume();

        controller = new DATABASE(getActivity());
        // Set title bar
        // getActivity().setActionBarTitle("Produits");

        setRecycle("");
    }

    private void setRecycle(String text_search) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView is passes to achieve expand/collapse functionality correctly.
        adapter = new RecyclerAdapterAddProducts(getActivity(), getItems(text_search, false), "1", "1" , listner);
        //adapter.setData(getItems(""));
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<PostData_Produit> getItems(String querry_search, Boolean isScan) {

        if(isScan){
            String querry = "SELECT * FROM Produit WHERE CODE_BARRE = '"+querry_search+"' OR REF_PRODUIT = '"+querry_search+"'";
            // querry = "SELECT * FROM Events";
            produits = controller.select_produits_from_database(querry);
            if(produits.size() > 0){

            }else{
                String querry1 = "SELECT * FROM Codebarre WHERE CODE_BARRE_SYN = '"+querry_search+"'";
                String code_barre = controller.select_codebarre_from_database(querry1);

                String querry2 = "SELECT * FROM Produit WHERE CODE_BARRE = '"+code_barre+"'";
                produits = controller.select_produits_from_database(querry2);
            }
        }else{
            if(querry_search.length() >0){

                String querry = "SELECT * FROM Produit WHERE ( PRODUIT LIKE '%"+querry_search+"%' OR CODE_BARRE LIKE '%"+querry_search+"%' OR REF_PRODUIT LIKE '%"+querry_search+"%')";
                // querry = "SELECT * FROM Events";
                produits = controller.select_produits_from_database(querry);

            }else {

                String querry = "SELECT * FROM Produit";
                // querry = "SELECT * FROM Events";
                produits = controller.select_produits_from_database(querry);

            }
        }

        return produits;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_products_in_sales, container, false);

        initViews(rootView);

        return rootView;
    }

    @Override
    public void recyclerAdapterOnClick(int position) {

        Crouton.makeText(getActivity(), "TESSST", Style.CONFIRM).show();
    }

    @Override
    public void recyclerAdapterOnLongClick(int position) {

    }
}
