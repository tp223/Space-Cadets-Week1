import java.util.List;
import com.unboundid.ldap.sdk.*;
import java.util.Scanner;
/*
Solution to space cadets Wk1 challenge using LDAP queries to get a users name from their email ID.
*/

public class App {
    public static void main(String[] args) throws LDAPException {
        // Initialize objects and variables for taking inputs and keeping a record of number of results (Should usually only be 1)
        Scanner inputObj = new Scanner(System.in);
        Integer resultCount = 0;

        while (true) {
            System.out.println("Enter a user ID: ");
            String userID = inputObj.nextLine();

            // BaseDN taken from https://github.com/jefftheprogrammer/email_grabber as they have already determined the base for the ldap server
            String baseDN = "OU=User,DC=soton,DC=ac,DC=uk";
            
            // The cn is equivalent to the iSolutions username which is used for emails
            String filter = "cn=" + userID;
            LDAPConnection connection = getConnection();
            try {
                List<SearchResultEntry> results = getResults(connection, baseDN, filter);

                resultCount = 0;
    
                for (SearchResultEntry e : results) {
                    resultCount += 1;
                    System.out.println("Name: " + e.getAttributeValue("personalTitle") + " " + e.getAttributeValue("givenName") + " " + e.getAttributeValue("sn"));
                }
                if (resultCount == 0) {
                    System.out.println("User could not be found");
                }
            } catch (Exception LDAPSearchException) {
                System.out.println("User could not be found");
            }
        }
    }

    public static LDAPConnection getConnection() throws LDAPException {
        // host, port, username and password
        return new LDAPConnection("ldap.soton.ac.uk", 389, "", "");
    }

    public static List<SearchResultEntry> getResults(LDAPConnection connection, String baseDN, String filter) throws LDAPSearchException {
        SearchResult searchResult;
    
        if (connection.isConnected()) {
            searchResult = connection.search(baseDN, SearchScope.SUB, filter);
    
            return searchResult.getSearchEntries();
        } else {
            System.out.println("Connection Failed");
        }
    
        return null;
    }
}
