package com.boulouza.uk2018.pme_provente.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boulouza.uk2018.pme_provente.R;
import com.boulouza.uk2018.pme_provente.interfaces.ClickInterface;
import com.boulouza.uk2018.pme_provente.postData.PostData_Bon1;
import com.boulouza.uk2018.pme_provente.utils.ColorGeneratorModified;
import com.boulouza.uk2018.pme_provente.utils.MyCardView2;
import com.haozhang.lib.SlantedTextView;

import java.text.DecimalFormat;
import java.util.List;

import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by UK2016 on 02/01/2017.
 */

public class RecyclerAdapterBon1 extends RecyclerView.Adapter<RecyclerAdapterBon1.MyViewHolder> {

    private List<PostData_Bon1> bon1List;
    private int color = 0;
    private ColorGeneratorModified generator;
    private Context mContext;
    private ClickInterface listner;


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView NumBon;
        TextView NomClient;
        TextView Montant;
        TextView nbrProduit;
        TextView Date_bon;
        CardView cardView;
        SlantedTextView blocage;

        MyViewHolder(View view) {
            super(view);

            cardView = (CardView) view.findViewById(R.id.item_root);
            NumBon = (TextView) view.findViewById(R.id.num_bon);
            NomClient = (TextView) view.findViewById(R.id.nom_client);
            Montant = (TextView) view.findViewById(R.id.montant);
            nbrProduit = (TextView) view.findViewById(R.id.nbr_p);
            Date_bon = (TextView) view.findViewById(R.id.date_bon);
            blocage = (SlantedTextView) view.findViewById(R.id.blocage);
        }
    }


    public RecyclerAdapterBon1(Context context, List<PostData_Bon1> itemList, ClickInterface listner) {
        this.bon1List = itemList;
        if (color == 0)
            generator = ColorGeneratorModified.MATERIAL;
        mContext = context;
        this.listner=listner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = new MyCardView2(parent.getContext(), R.layout.item_bon1_list);
        // View itemView = LayoutInflater.from(parent.getContext())
        //       .inflate(R.layout.item_list, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,int position) {
        PostData_Bon1 item = bon1List.get(position);

            holder.NumBon.setTextSize(17);
            holder.NumBon.setTypeface(null, Typeface.BOLD);
            holder.NumBon.setText(""+item.num_bon);

            holder.NomClient.setText(""+item.client);

            if(item.montant_bon == null){
                item.montant_bon = "0.00";
            }
            holder.Montant.setText(""+ new DecimalFormat("##,##0.00").format(Double.valueOf(item.montant_bon)) + " DA");

            if(item.nbr_p == null){
                item.nbr_p = "0";
            }
            final BadgeDrawable drawable1 = new BadgeDrawable.Builder()
                    .type(BadgeDrawable.TYPE_NUMBER)
                    .badgeColor(0xff303F9F)
                    .textSize(35)
                    .number(Integer.valueOf(item.nbr_p))
                    .build();


           //SpannableString spannableString1 = new SpannableString(TextUtils.concat(drawable1.toSpannable()));
            SpannableString spannableString1  = new SpannableString(TextUtils.concat(drawable1.getText1()));

            holder.nbrProduit.setText(spannableString1);

            holder.Date_bon.setText(item.date_bon);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.recyclerAdapterOnClick(holder.getAdapterPosition());
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listner.recyclerAdapterOnLongClick(holder.getAdapterPosition());
                return true;
            }
        });

        if(item.blocage.equals("F")){
            holder.blocage.setText("Validé")
                    .setTextColor(Color.WHITE)
                    .setSlantedBackgroundColor(Color.GREEN)
                    .setTextSize(21)
                    .setSlantedLength(50)
                    .setMode(SlantedTextView.MODE_RIGHT_BOTTOM);
        }else {
            holder.blocage.setText("En attente")
                    .setTextColor(Color.WHITE)
                    .setSlantedBackgroundColor(Color.RED)
                    .setTextSize(21)
                    .setSlantedLength(50)
                    .setMode(SlantedTextView.MODE_RIGHT_BOTTOM);
        }


        if (color == 0){
            if (generator!=null)
                color = generator.getColor(bon1List.get(position).num_bon);
        }
    }

    @Override
    public int getItemCount() {
        return bon1List.size();
    }

    public void refresh(List<PostData_Bon1> new_itemList){
        bon1List.clear();
        bon1List.addAll(new_itemList);
        notifyDataSetChanged();
    }
}