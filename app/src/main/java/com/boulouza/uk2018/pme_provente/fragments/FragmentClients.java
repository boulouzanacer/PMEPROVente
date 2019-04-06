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

import com.boulouza.uk2018.pme_provente.ItemListActivity;
import com.boulouza.uk2018.pme_provente.R;
import com.boulouza.uk2018.pme_provente.adapters.RecyclerViewAdapterClients;
import com.boulouza.uk2018.pme_provente.databases.DATABASE;
import com.boulouza.uk2018.pme_provente.dummy.DummyContent;
import com.boulouza.uk2018.pme_provente.postData.PostData_Client;

import java.util.ArrayList;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class FragmentClients extends Fragment{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    RecyclerViewAdapterClients adapter;
    RecyclerView recyclerView;
    ArrayList<PostData_Client> clients;
    DATABASE controller;
    private MediaPlayer mp;

    public FragmentClients() {
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
        PostData_Client client = new PostData_Client();
        client.adresse = "ager, bab azzouar";
        client.client = "BOULOUZA ANA";
        client.code_client = "000034";
        client.mode_tarif = "1";
        client.tel = "0656232454";

        controller.Insert_into_client(client);

        // Set title bar
        ((ItemListActivity) getActivity()).setActionBarTitle("Clients");


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
        adapter = new RecyclerViewAdapterClients(recyclerView);
        adapter.setData(getItems(""));
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<PostData_Client> getItems(String qqry) {
        if(qqry.length() > 0){
            clients = new ArrayList<>();
            String querry = "SELECT * FROM Client WHERE CODE_CLIENT LIKE '%"+qqry+"%' OR CLIENT LIKE '%"+qqry+"%' OR TEL LIKE '%"+qqry+"%' ORDER BY CLIENT";
            // querry = "SELECT * FROM Events";
            clients = controller.select_clients_from_database(querry);
        }else {
            clients = new ArrayList<>();
            String querry = "SELECT * FROM Client ORDER BY CLIENT";
            // querry = "SELECT * FROM Events";
            clients = controller.select_clients_from_database(querry);
        }

        return clients;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clients, container, false);

        initViews(rootView);

        return rootView;
    }

}
