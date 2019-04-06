package com.boulouza.uk2018.pme_provente.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boulouza.uk2018.pme_provente.R;
import com.boulouza.uk2018.pme_provente.postData.ExpandListener;
import com.boulouza.uk2018.pme_provente.postData.PostData_Client;
import com.boulouza.uk2018.pme_provente.postData.PostData_Produit;

import java.util.ArrayList;

public class RecyclerViewAdapterProducts extends RecyclerView.Adapter<RecyclerViewAdapterProducts.ViewHolder> {
    private ArrayList<PostData_Produit> produits;
    private RecyclerView recyclerView;;
    private int lastExpandedCardPosition;
    private int i=0;
    public RecyclerViewAdapterProducts(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.card_item_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //collapssed
        holder.ProduitName_TextV.setText(produits.get(position).produit);
        holder.CodeBarre_TextV.setText(produits.get(position).code_barre);
        holder.stock_TextV.setText(produits.get(position).stock);
        holder.PicProduit_ImageV.setImageResource(R.drawable.ic_launcher_background);

        //Expanded
        holder.REFERENCE_expandedTextV.setText(produits.get(position).ref_produit);
        holder.PV1_expandedTextV.setText(produits.get(position).pv1_ht);
        holder.PV2_expandedTextV.setText(produits.get(position).pv2_ht);
        holder.PV3_expandedTextV.setText(produits.get(position).pv3_ht);
        holder.TVA_expandedTextV.setText(produits.get(position).tva);



        if(produits.get(position).isExpanded()){
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
        return produits.size();
    }

    public void setData(ArrayList<PostData_Produit> produits) {
        this.produits = produits;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ExpandableLinearLayout expandableView;
        TextView CodeBarre_TextV;
        TextView ProduitName_TextV;
        TextView stock_TextV;
        ImageView PicProduit_ImageV;

        TextView REFERENCE_expandedTextV;
        TextView PV1_expandedTextV;
        TextView PV2_expandedTextV;
        TextView PV3_expandedTextV;
        TextView TVA_expandedTextV;


        ExpandListener expandListener = new ExpandListener() {
            @Override
            public void onExpandComplete() {
                if(lastExpandedCardPosition!=getAdapterPosition() && recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition)!=null){
                    ((ExpandableLinearLayout)recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition).itemView.findViewById(R.id.expandableView)).setExpanded(false);
                    produits.get(lastExpandedCardPosition).setExpanded(false);
                    ((ExpandableLinearLayout)recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition).itemView.findViewById(R.id.expandableView)).toggle();
                }
                else if(lastExpandedCardPosition!=getAdapterPosition() && recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition)==null){
                    produits.get(lastExpandedCardPosition).setExpanded(false);
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
            ProduitName_TextV = itemView.findViewById(R.id.produit_name_textv);
            CodeBarre_TextV = itemView.findViewById(R.id.codebarre_textv);
            stock_TextV = itemView.findViewById(R.id.stock_textv);
            PicProduit_ImageV = itemView.findViewById(R.id.produit_pic_imgv);

            //Expanded
            REFERENCE_expandedTextV = itemView.findViewById(R.id.reference_textv);
            PV1_expandedTextV = itemView.findViewById(R.id.pv1_textv);
            PV2_expandedTextV = itemView.findViewById(R.id.pv2_textv);
            PV3_expandedTextV = itemView.findViewById(R.id.pv3_textv);
            TVA_expandedTextV = itemView.findViewById(R.id.tva_textv);

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
                        produits.get(getAdapterPosition()).setExpanded(false);
                    } else {
                        expandableView.setExpanded(true);
                        produits.get(getAdapterPosition()).setExpanded(true);
                        expandableView.toggle();
                    }
                }
            });
        }
    }
}
