package com.boulouza.uk2018.pme_provente.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.boulouza.uk2018.pme_provente.R;
import com.boulouza.uk2018.pme_provente.interfaces.ClickInterface;
import com.boulouza.uk2018.pme_provente.postData.PostData_Bon2;
import com.boulouza.uk2018.pme_provente.postData.PostData_Produit;
import com.boulouza.uk2018.pme_provente.utils.ColorGeneratorModified;
import com.boulouza.uk2018.pme_provente.utils.MyCardView2;
import com.boulouza.uk2018.pme_provente.utils.ScalingActivityAnimator;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by UK2016 on 02/01/2017.
 */

public class RecyclerAdapterAddProducts extends RecyclerView.Adapter<RecyclerAdapterAddProducts.MyViewHolder> {

    private List<PostData_Produit> produitList;
    private ArrayList<PostData_Bon2> bon2_list;
    private int color = 0;
    private ColorGeneratorModified generator;
    private Context mContext;
    private Activity mActivity;
    private EventBus bus;
    private String mode_tarif;
    private String PREFS_AUTRE = "ConfigAutre";
    private boolean stock_moins = false;
    private String name_class;

    private ClickInterface listner;


    class MyViewHolder extends RecyclerView.ViewHolder {

        Button BtnAdd;
        TextView Produit, Prix_vente, Total_produit, Qte_r;
        CardView cardView;
        Button Increase, Decrease;
        EditText qte;
        ScalingActivityAnimator mScalingActivityAnimator ;

        MyViewHolder(View view) {
            super(view);

            cardView = (CardView) view.findViewById(R.id.item_root);

            BtnAdd = (Button) view.findViewById(R.id.BtnAdd);
            Produit = (TextView) view.findViewById(R.id.produit);
            Prix_vente = (TextView) view.findViewById(R.id.prix_u);
            Total_produit = (TextView) view.findViewById(R.id.total_produit);
            Qte_r = (TextView) view.findViewById(R.id.qte_r);
            Increase = (Button) view.findViewById(R.id.btnInc);
            Decrease = (Button) view.findViewById(R.id.btnDec);
            qte = (EditText) view.findViewById(R.id.qte);
            mScalingActivityAnimator = new ScalingActivityAnimator(mContext, mActivity, R.id.root_view, R.layout.pop_view);
            this.setIsRecyclable(false);
        }
    }

    public RecyclerAdapterAddProducts(Activity activity, List<PostData_Produit> itemList, String mode_tarif, String name_class, ClickInterface listner) {
        this.produitList = itemList;
        if (color == 0)
            generator = ColorGeneratorModified.MATERIAL;
        mContext = activity.getBaseContext();
        mActivity = activity;
        bus = EventBus.getDefault();
        this.mode_tarif = mode_tarif;
        bon2_list = new ArrayList<>();
        SharedPreferences prefs3 = mContext.getSharedPreferences(PREFS_AUTRE, mContext.MODE_PRIVATE);
        if (prefs3.getBoolean("STOCK_MOINS", false)) {
            stock_moins = true;
        } else {
            stock_moins = false;
        }
        this.name_class = name_class;
        setHasStableIds(true);
        this.listner = listner;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = new MyCardView2(parent.getContext(), R.layout.item_add_products);
        // View itemView = LayoutInflater.from(parent.getContext())
        //       .inflate(R.layout.item_list, parent, false);

        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PostData_Produit item = produitList.get(position);

        holder.Produit.setTextSize(14);
        holder.Produit.setTypeface(null, Typeface.BOLD);
        holder.Produit.setText(item.produit);


        if(mode_tarif.equals("3")){
            holder.Prix_vente.setText(item.pv3_ht);
        }else if(mode_tarif.equals("2")){
            holder.Prix_vente.setText(item.pv2_ht);
        } else
            holder.Prix_vente.setText(item.pv1_ht);


        if(!holder.qte.getText().toString().isEmpty()) {
            holder.Total_produit.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(holder.qte.getText().toString()) * Double.valueOf(holder.Prix_vente.getText().toString())));
            holder.Qte_r.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(item.stock) - Double.valueOf(holder.qte.getText().toString())));

        }



        holder.Prix_vente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!holder.qte.getText().toString().isEmpty())
                    holder.Total_produit.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(holder.qte.getText().toString()) * Double.valueOf(holder.Prix_vente.getText().toString())));
                else
                    holder.qte.setText("0");
            }
        });


        holder.qte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}


            @Override
            public void afterTextChanged(Editable s) {
                if((!holder.qte.getText().toString().isEmpty()) || (holder.qte.getText().toString().length() < 0)){

                    if(holder.qte.getText().toString().equals("-")){
                        return;
                    }
                    else if(holder.qte.getText().toString().equals(".")){
                       return;
                    }
                    else{
                        if(!stock_moins){
                            if(Double.valueOf(item.stock) < Double.valueOf(holder.qte.getText().toString())) {
                                StockOverFlowMessage(Double.valueOf(holder.qte.getText().toString()));
                                //holder.qte.setText("0");
                                holder.Total_produit.setText(new DecimalFormat("##,##0.00").format(0.0 * Double.valueOf(holder.Prix_vente.getText().toString())));
                                holder.Qte_r.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(item.stock)));
                                return;
                            }else{
                                holder.Total_produit.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(holder.qte.getText().toString()) * Double.valueOf(holder.Prix_vente.getText().toString())));
                                holder.Qte_r.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(item.stock) - Double.valueOf(holder.qte.getText().toString())));
                            }
                        }else{

                            holder.Total_produit.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(holder.qte.getText().toString()) * Double.valueOf(holder.Prix_vente.getText().toString())));
                            holder.Qte_r.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(item.stock) - Double.valueOf(holder.qte.getText().toString())));
                        }
                    }
                }else{
                    holder.qte.setText("0");
                    holder.Total_produit.setText(new DecimalFormat("##,##0.00").format(0.0 * Double.valueOf(holder.Prix_vente.getText().toString())));
                    holder.Qte_r.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(item.stock)));
                }
            }
        });

        holder.BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.recyclerAdapterOnClick(holder.getAdapterPosition());
            }
        });



        holder.Increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.qte.getText().toString().isEmpty()) {

                    if(!stock_moins){
                        if(Double.valueOf(item.stock) <= Double.valueOf(holder.qte.getText().toString())) {
                            StockOverFlowMessage(Double.valueOf(holder.qte.getText().toString()) + 1.0);
                            return;
                        }else{
                            holder.qte.setText(String.valueOf(Integer.valueOf(holder.qte.getText().toString()) + 1));
                            holder.Total_produit.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(holder.qte.getText().toString()) * Double.valueOf(holder.Prix_vente.getText().toString())));
                            holder.Qte_r.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(item.stock) - Double.valueOf(holder.qte.getText().toString())));
                        }
                    }else{
                        holder.qte.setText(String.valueOf(Integer.valueOf(holder.qte.getText().toString()) + 1));
                        holder.Total_produit.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(holder.qte.getText().toString()) * Double.valueOf(holder.Prix_vente.getText().toString())));
                        holder.Qte_r.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(item.stock) - Double.valueOf(holder.qte.getText().toString())));
                    }
                }else{
                    holder.qte.setText("0");
                    holder.Total_produit.setText(new DecimalFormat("##,##0.00").format(0.0 * Double.valueOf(holder.Prix_vente.getText().toString())));
                    holder.Qte_r.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(item.stock)));

                }

            }
        });


        holder.Decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!holder.qte.getText().toString().isEmpty())) {
                    holder.qte.setText(String.valueOf(Integer.valueOf(holder.qte.getText().toString()) - 1));
                }else{
                    holder.qte.setText("0");
                }
                holder.Total_produit.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(holder.qte.getText().toString()) * Double.valueOf(holder.Prix_vente.getText().toString())));
                holder.Qte_r.setText(new DecimalFormat("##,##0.00").format(Double.valueOf(item.stock) - Double.valueOf(holder.qte.getText().toString())));

            }
        });


        holder.Prix_vente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mScalingActivityAnimator.setPopViewHeightIsTwoThirdOfScreen();
                View popView1 = holder.mScalingActivityAnimator.start();
                Button mButtonBack1 = (Button) popView1.findViewById(R.id.btn_cancel);
                Button mButtonBack2 = (Button) popView1.findViewById(R.id.btn_sure);
                final EditText Val_P_v = (EditText) popView1.findViewById(R.id.edited_prix);
                Val_P_v.setText(holder.Prix_vente.getText().toString());
                mButtonBack1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.mScalingActivityAnimator.resume();
                    }
                });

                mButtonBack2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!Val_P_v.getEditableText().toString().isEmpty()){

                            holder.Prix_vente.setText(Val_P_v.getText().toString());
                        }else{
                            holder.Prix_vente.setText("0.00");
                        }

                        holder.mScalingActivityAnimator.resume();
                    }
                });
            }
        });


        if (color == 0){
            if (generator!=null)
                color = generator.getColor(produitList.get(position).produit);
        }


    }

    @Override
    public int getItemCount() {
        return produitList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface ItemClick{
        void onClick(View v, int position);
    }

    public void refresh(List<PostData_Produit> new_itemList){
        produitList.clear();
        produitList.addAll(new_itemList);
        notifyDataSetChanged();
    }

    private void StockOverFlowMessage(Double qte){
        if(name_class.toString().equals("1")){
            Crouton.makeText(mActivity, "Stock non disponible, Quantité "+String .valueOf(qte)+" est superieur du stock ", Style.ALERT).show();
        }else{
            Crouton.makeText(mActivity, "Stock non disponible, Quantité "+String .valueOf(qte)+" est superieur du stock ", Style.ALERT).show();
        }
    }
}