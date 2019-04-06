package com.boulouza.uk2018.pme_provente.postData;

/**
 * Created by UK2015 on 31/08/2016.
 */
public class PostData_Client {
    private boolean expanded;

    public String code_client;
    public String client;
    public String tel;
    public String adresse;
    public Double latitude;
    public Double longitude;
    public String mode_tarif;
    public String achat_montant = "0.00";
    public String verser_montant = "0.00";
    public String solde_montant = "0.00";

    public String rc;
    public String ifiscal;
    public String rib;
    public Double credit_limit = 0.00;

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
