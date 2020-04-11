package logic.data;

import java.io.Serializable;


public class PlatformInfo  implements Serializable {

	private static final long serialVersionUID = 1L;
	private String store;
    private String company;
    private String saleUrl;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                            COSTRUCTORS                                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public PlatformInfo(){}
    PlatformInfo( String store , String company , String saleUrl ){

        this.store = store;
        this.company = company;
        this.saleUrl = saleUrl;

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               GETTER                                                           //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getStore(){ return store; }

    public String getCompany(){ return company; }

    public String getSaleUrl(){ return saleUrl; }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               SETTER                                                           //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setStore( String store ){ this.store = store; }

    public void setCompany( String company ){ this.company = company; }

    public void setSaleUrl( String saleUrl ){ this.saleUrl = saleUrl; }

}
