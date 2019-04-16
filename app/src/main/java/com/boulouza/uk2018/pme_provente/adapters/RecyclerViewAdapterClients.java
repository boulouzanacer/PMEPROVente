package com.boulouza.uk2018.pme_provente.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boulouza.uk2018.pme_provente.R;
import com.boulouza.uk2018.pme_provente.postData.ExpandListener;
import com.boulouza.uk2018.pme_provente.postData.PostData_Client;

import java.util.ArrayList;

import cn.nekocode.badge.BadgeDrawable;

public class RecyclerViewAdapterClients extends RecyclerView.Adapter<RecyclerViewAdapterClients.ViewHolder> {
    private ArrayList<PostData_Client> clients;
    private RecyclerView recyclerView;;
    private int lastExpandedCardPosition;
    private int i=0;
    public RecyclerViewAdapterClients(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.card_item_client, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //collapssed
        holder.Client_C_TextV.setText(clients.get(position).code_client);
        holder.Client_N_TextV.setText(clients.get(position).client);
        holder.Client_Pic_ImageV.setImageResource(R.mipmap.client_pic);

        //Expanded
        holder.Tel_expandedTextV.setText("  " + clients.get(position).tel);
        holder.Adress_expandedTextV.setText("  " +clients.get(position).adresse);

        if(clients.get(position).mode_tarif != null){
            if(clients.get(position).mode_tarif.equals("1")){
                final BadgeDrawable drawable = new BadgeDrawable.Builder()
                                .type(BadgeDrawable.TYPE_WITH_TWO_TEXT)
                                .badgeColor(0xffCC9999)
                                .text1("TARIF MODE")
                                .text2("TARIFF 1")
                                .build();
                SpannableString spannableString = new SpannableString(TextUtils.concat("  ", drawable.toSpannable()));
                holder.TarrifMode_expandedTextV.setText(spannableString);

            }else if(clients.get(position).mode_tarif.equals("2")){
                final BadgeDrawable drawable = new BadgeDrawable.Builder()
                                .type(BadgeDrawable.TYPE_WITH_TWO_TEXT)
                                .badgeColor(0xffCC9999)
                                .text1("TARIF MODE")
                                .text2("TARIFF 2")
                                .build();
                SpannableString spannableString = new SpannableString(TextUtils.concat("  ", drawable.toSpannable()));
                holder.TarrifMode_expandedTextV.setText(spannableString);

            }else{
                final BadgeDrawable drawable = new BadgeDrawable.Builder()
                                .type(BadgeDrawable.TYPE_WITH_TWO_TEXT)
                                .badgeColor(0xffCC9999)
                                .text1("TARIF MODE")
                                .text2("TARIFF 3")
                                .build();
                SpannableString spannableString = new SpannableString(TextUtils.concat("  ", drawable.toSpannable()));
                holder.TarrifMode_expandedTextV.setText(spannableString);

            }
        }else{
            holder.TarrifMode_expandedTextV.setText("?");
        }

        if(clients.get(position).isExpanded()){
            holder.expandableView.setVisibility(View.VISIBLE);
            holder.expandableView.setExpanded(true);
        }
        else{
            holder.expandableView.setVisibility(View.GONE);
            holder.expandableView.setExpanded(false);
        }
    }
    @Override
    public int getItemCount() {
        return clients.size();
    }

    public void setData(ArrayList<PostData_Client> clients) {
        this.clients = clients;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ExpandableLinearLayout expandableView;
        TextView Client_C_TextV;
        TextView Client_N_TextV;
        ImageView Client_Pic_ImageV;

        TextView Tel_expandedTextV;
        TextView Adress_expandedTextV;
        TextView TarrifMode_expandedTextV;

        ExpandListener expandListener = new ExpandListener() {
            @Override
            public void onExpandComplete() {
                if(lastExpandedCardPosition!=getAdapterPosition() && recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition)!=null){
                    ((ExpandableLinearLayout)recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition).itemView.findViewById(R.id.expandableView)).setExpanded(false);
                    clients.get(lastExpandedCardPosition).setExpanded(false);
                    ((ExpandableLinearLayout)recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition).itemView.findViewById(R.id.expandableView)).toggle();
                }
                else if(lastExpandedCardPosition!=getAdapterPosition() && recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition)==null){
                    clients.get(lastExpandedCardPosition).setExpanded(false);
                }
                lastExpandedCardPosition = getAdapterPosition();
            }

            @Override
            public void onCollapseComplete() {

            }
        };
        ViewHolder(View itemView) {
            super(itemView);
            //collapssed
            Client_C_TextV = itemView.findViewById(R.id.clientid_textv);
            Client_N_TextV = itemView.findViewById(R.id.clientname_textv);
            Client_Pic_ImageV = itemView.findViewById(R.id.clientP_imgv);
            //Expanded
            Tel_expandedTextV = itemView.findViewById(R.id.tel_textv);
            Adress_expandedTextV = itemView.findViewById(R.id.adresse_textv);
            TarrifMode_expandedTextV = itemView.findViewById(R.id.tarifmode_textv);

            expandableView = itemView.findViewById(R.id.expandableView);
            expandableView.setExpandListener(expandListener);

            initializeClicks();
        }

        private void initializeClicks() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (expandableView.isExpanded()) {
                        expandableView.setExpanded(false);
                        expandableView.toggle();
                        clients.get(getAdapterPosition()).setExpanded(false);
                    } else {
                        expandableView.setExpanded(true);
                        clients.get(getAdapterPosition()).setExpanded(true);
                        expandableView.toggle();
                    }
                }
            });
        }
    }
}
