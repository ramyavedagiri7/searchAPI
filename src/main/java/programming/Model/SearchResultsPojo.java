package programming.Model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by rvedagiri on 9/10/16.
 */
public class SearchResultsPojo {

    private static String artist_name;
    private static String country;
    private static String currency;

    public  SearchResultsPojo(@JsonProperty("artistName")String name,@JsonProperty("country")String cou, @JsonProperty("currency")String curr){
        this.artist_name=name;
        this.country=cou;
        this.currency=curr;
}

    public static String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public static String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public static String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}
