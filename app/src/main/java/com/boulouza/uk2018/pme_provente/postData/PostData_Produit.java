package com.boulouza.uk2018.pme_provente.postData;

import java.io.Serializable;

/**
 * Created by UK2015 on 31/08/2016.
 */
public class PostData_Produit implements Serializable {
    private boolean expanded;

    public String code_barre;
    public String ref_produit;
    public String produit;
    public String pa_ht;
    public String tva;
    public String pv1_ht;
    public String pv2_ht;
    public String pv3_ht;
    public String stock;
    public String qte_produit;
    public byte[]  photo;
    public Boolean exist;

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
