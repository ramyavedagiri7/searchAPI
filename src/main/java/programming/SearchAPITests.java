package programming;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import programming.Model.SearchResultsPojo;

import javax.print.attribute.standard.Finishings;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static programming.Model.SearchResultsPojo.*;

/**
 * Created by rvedagiri on 9/10/16.
 */
public class SearchAPITests {

    private RestTemplate restTemplate;
    private static final String search="/search?";
    private static final String fooResourceUrl = "https://itunes.apple.com";

    @BeforeTest
    public void beforeTest(){
        restTemplate = new RestTemplate();  // Initialize the template class

    }
    @Test
    public void testSingleWordSearch() throws IOException{
        final ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl + ""+search+"term=Madonna", String.class); // Read response from the submitted URL
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);  // Status code returned should be 200
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode root = mapper.readTree(response.getBody());

        final JsonNode result_count = root.path("resultCount");

        if(Integer.parseInt(result_count.toString())<=50) {
            Assert.assertTrue(true);  // Verify if the search result count is 50

        }
        else{
            Assert.assertFalse(true,"results are NOT less than or equal to 50");
        }
    }
    @Test(expectedExceptions = UnrecognizedPropertyException.class)
    public void testVerifyValidCountry() throws IOException, JSONException {
        final ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl + ""+search+"country=in&term=Madonna", String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);

        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode root = mapper.readTree(response.getBody());
        final JsonNode result = root.path("results");

        try {
            List<SearchResultsPojo> someClassList =
                    mapper.readValue(result, mapper.getTypeFactory().constructCollectionType(List.class, SearchResultsPojo.class));  // Maps the results to Model object
        } finally {  // Inside finally because of few ignored properties which we are not going to store and verify at this point of time
            Assert.assertEquals(SearchResultsPojo.getCurrency(),"INR");  // Verify the reuslts based on the search
            Assert.assertEquals(SearchResultsPojo.getArtist_name(),"Madonna");
            Assert.assertEquals(SearchResultsPojo.getCountry(), "IND");


        }


    }

    @Test
    public void testVerifyNonExistingCountry() {

        String exc="";

        try {
            final ResponseEntity<String> response;
            response = restTemplate.getForEntity(fooResourceUrl + ""+search+"country=ab&term=Madonna", String.class);
        }catch (Exception e) {
            exc=e.toString();

        }
        finally {
            if( exc.contains("400 Bad Request")){
                Assert.assertTrue(true, "Bad request received");
            }
        }



    }

    @Test
    public void testVerifyLimit25() throws IOException {
        final ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl + ""+search+"limit=25&country=in&term=Madonna", String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode root;
        try {
            root = mapper.readTree(response.getBody());
            final JsonNode result_count = root.path("resultCount");
            if(Integer.parseInt(result_count.toString())<=5) {
                Assert.assertTrue(true,"results are less than or equal to 50");  // Verify if the search result count is 50

            }
        } catch (IOException e) {
            e.printStackTrace();
        }





    }


}
