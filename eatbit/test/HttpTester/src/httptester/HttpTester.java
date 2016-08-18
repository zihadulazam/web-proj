/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httptester;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 *
 * @author jacopo
 */
public class HttpTester
{
    public static int failedTests=0;
    public static int totalTests=0;
    public static ArrayList<String> failedTestsNames= new ArrayList<>();
    public static ArrayList<String> failedTestsExpectedRes= new ArrayList<>();
    public static ArrayList<String> failedTestsRes= new ArrayList<>();
    public static final String BASEURL="http://localhost:8084/eatbit/";
    public static final int POST=0;
    public static final int GET=1;
    public static StringBuilder parametri;
    public static String sessionId = "";
    /**
     * 
     * @param args the command line arguments
     * @throws java.io.UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException, IOException
    {
        /*
        Programmino per testare l'applicazione, l'applicazione deve runnare
        sullo stesso pc su cui si esegue il test. Se si vuole cambiare ciò basta 
        cambiare BASEURL con quello che si preferisce.
        COME FUNZIA?
        
        1)creiamo i parametri...., addParameter(NOMEPARAM,VALOREPARAM)
        parametri = new StringBuilder ();
        addParameter("password","lol231");
        addParameter("name","lol12321");
        addParameter("surname","lol1231");
        addParameter("email","lol@kek.com");
        2)lanciamo EXPECT O ASSERT, la differenza è che ASSERT esce subito dal programma
        se il test fallisce, questo perchè decidiamo che andare avanti dopo che quel test fallisce
        ha poco senso
        EXPECT(TIPO METODO,NOME TEST A MIA PREFERENZA, NOME SERVLET, LA RISPOSTA CHE CI ASPETTIAMO);
        
        EXPECT(POST,"addUserMissingParameter","RegisterUserServlet","-1");
        
        alla fine dell'esecuzione o prima di uscire per un ASSERT fallito viene
        generato un piccolo report che dice quanti test sono falliti,  e per
        ognuno di questi ci dice il valore che ci aspettavamo e quello realmente arrivato
        
        quando testate ricordate se i test che state scrivendo sono dipendenti o no
        dal contenuto attuale del vostro database! se scrivete test per tutti
        mandateli nel canale e assicuratevi che siano indipendenti dal contenuto del db
        
        qua ci sono un paio di test prefatti giuto per controllare che runnando
        il programma nn esploda qualcosa
        */
        //test di RegisterUserServlet
        //mando questa richiesta per registrare un utente senza EXPECT o ASSERT, in modo
        //che per le richieste dopo ci sia sicuramente l'utente già registrato
        parametri = new StringBuilder ();
        addParameter("password","lol231");
        addParameter("name","lol12321");
        addParameter("surname","lol1231");
        addParameter("nickname","1a1a1a1a1a1a2345566");
        addParameter("email","1a1a1a1a1a1a2345566@7nani.foresta");
        executePost(BASEURL+"RegisterUserServlet", parametri.toString());
                
        parametri = new StringBuilder ();
        addParameter("name","lol12321");
        addParameter("surname","lol1231");
        addParameter("nickname","1a1a1a1a1a1a2345566");
        addParameter("email","lol@kek.com");
        ASSERT(POST,"addUserMissingParameter1","RegisterUserServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("password","lol231");
        addParameter("surname","lol1231");
        addParameter("nickname","1a1a1a1a1a1a2345566");
        addParameter("email","lol@kek.com");
        ASSERT(POST,"addUserMissingParameter2","RegisterUserServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("password","lol231");
        addParameter("name","lol12321");
        addParameter("nickname","1a1a1a1a1a1a2345566");
        addParameter("email","lol@kek.com");
        ASSERT(POST,"addUserMissingParameter3","RegisterUserServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("password","lol231");
        addParameter("name","lol12321");
        addParameter("surname","lol1231");
        addParameter("email","lol@kek.com");
        ASSERT(POST,"addUserMissingParameter4","RegisterUserServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("password","lol231");
        addParameter("name","lol12321");
        addParameter("surname","lol1231");
        addParameter("nickname","1a1a1a1a1a1a2345566");
        ASSERT(POST,"addUserMissingParameter5","RegisterUserServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("password","lol231");
        addParameter("name","lol12321");
        addParameter("surname","lol1231");
        addParameter("nickname","1a1a1a1a1a1a2345566");
        addParameter("email","1a1a1a1a1a1a2345566@7nani.foresta");
        ASSERT(POST,"addUserExistingEmail","RegisterUserServlet","-2");
        
        parametri = new StringBuilder ();
        addParameter("password","lol231");
        addParameter("name","lol12321");
        addParameter("surname","lol1231");
        addParameter("nickname","1a1a1a1a1a1a2345566");
        addParameter("email","banzai");
        ASSERT(POST,"addUserExistingNick","RegisterUserServlet","-3");
        
        parametri = new StringBuilder ();
        addParameter("password","lol231");
        addParameter("name","lol12321");
        addParameter("surname","zzzzz");
        addParameter("nickname","zzzzzzzzz");
        addParameter("email","irraggiungibile");
        ASSERT(POST,"addUserImpossibleEmail","RegisterUserServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("password","lol231");
        addParameter("name","lol12321");
        addParameter("surname","zzzzz");
        addParameter("nickname","zzzzzzzzz");
        addParameter("email","");
        ASSERT(POST,"addUserImpossibleEmail2","RegisterUserServlet","0");
        
        //test di ReportPhotoServlet
        parametri= new StringBuilder();
        addParameter("id_photo","231");
        EXPECT(GET,"reportPhotoSuccess","ReportPhotoServlet","1");
        
        parametri= new StringBuilder();
        addParameter("id_photo","ciao");
        EXPECT(GET,"reportPhotoWithInvalidID","ReportPhotoServlet","0");
        
        parametri= new StringBuilder();
        EXPECT(GET,"reportPhotoWithMissingID","ReportPhotoServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_photo","");
        EXPECT(GET,"reportPhotoWithEmptyId","ReportPhotoServlet","0");
        
        //test di ReportReviewServlet
        parametri= new StringBuilder();
        addParameter("id_review","231");
        EXPECT(GET,"reportReviewSuccess","ReportReviewServlet","1");
        
        parametri= new StringBuilder();
        addParameter("id_review","ciao");
        EXPECT(GET,"reportReviewWithInvalidID","ReportReviewServlet","0");
        
        parametri= new StringBuilder();
        EXPECT(GET,"reportReviewWithMissingID","ReportReviewServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_review","");
        EXPECT(GET,"reportReviewWithEmptyId","ReportReviewServlet","0");
        
        //test per contact servlet
        parametri = new StringBuilder ();
        addParameter("email","ciao");
        addParameter("text","ciao");
        EXPECT(GET,"ContactSuccess","ContactServlet","1");
        
        parametri = new StringBuilder ();
        addParameter("email","ciao");
        EXPECT(GET,"ContactMissingText","ContactServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("text","ciao");
        EXPECT(GET,"ContactMissingEmail","ContactServlet","-1");
        
        //test per NameAutocompleteServlet
        parametri = new StringBuilder ();
        EXPECT(GET,"NameCompleteMissingkey","NameAutocompleteServlet","-1");
        
        //test per LocationAutoCompleteServlet
        parametri = new StringBuilder ();
        EXPECT(GET,"LocationCompleteMissingkey","LocationAutoCompleteServlet","-1");
        
        //test LoginServlet
        parametri = new StringBuilder ();
        addParameter("password","ciao");
        EXPECT(POST,"LoginMissingEmail","LoginServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("emailOrNickName","ciao");
        EXPECT(POST,"LoginMissingPassword","LoginServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("emailOrNickname","UtenteNonEsistente");
        addParameter("password","qwerty");
        EXPECT(POST,"LoginFail","LoginServlet","0");
        
        //test SendPswVeerificationEmailServlet
        
        parametri = new StringBuilder ();
        EXPECT(GET,"VerificationMailMissingParam","SendPswVerificationEmailServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_user","ciao");
        EXPECT(GET,"VerificationMailNoSession","SendPswVerificationEmailServlet","-1");
        
        //CHIAMATE DIPENDENTI DA STATO DATABASE, 
        //INFORMAZIONI DI UN UTENTE CHE DEVE ESISTERE, e deve essere admin (usertype=2)
        String id_user= "1";
        String nick="ayy";
        String psw="lol";
        //
        //INFORMAZIONI DI UN RISTORANTE CHE DEVE ESISTERE, il proprietario deve essere l'utente qua sopra
        String id_rest_owned="2";
        //INFORMAZIONI DI UN RISTORANTE CHE DEVE ESISTERE, il proprietario NON deve essere l'utente qua sopra
        String id_rest_not_owned="3";
        //INFORMAZIONI DI UNA REVIEW DHE DEVE ESISTERE, il proprietario deve essere l'utente qua sopra
        String id_rev_owned="2";
        //INFORMAZIONI DI UNA REVIEW DHE DEVE ESISTERE, il proprietario non deve essere l'utente qua sopra
        String id_rev_not_owned="1";
        //INFORMAZIONI DI UNA REVIEW CHE DEVE ESISTERE E CHE RIGUARDA UN RISTORANTE NON POSSEDUTO DALL'UTENTE SOPRA DEFINITO
        String id_rev_rest_not_owned="1";
        //INFORMAZIONI DI UNA REVIEW CHE DEVE ESISTERE E CHE RIGUARDA UN RISTORANTE POSSEDUTO DALL'UTENTE SOPRA DEFINITO
        String id_rev_rest_owned="3";
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        printResults();
        clearResults();
        System.out.println("\nstarting database dependant tests and resetting stats\n");
        
        //faccio login 
        parametri = new StringBuilder ();
        addParameter("emailOrNickname",nick);
        addParameter("password",psw);
        executePost(BASEURL+"LoginServlet", parametri.toString());
        
        parametri = new StringBuilder ();
        addParameter("id_user","666");
        EXPECT(GET,"VerificationMailWrongId","SendPswVerificationEmailServlet","-2");
        
        parametri = new StringBuilder ();
        addParameter("id_user",id_user);
        EXPECT(GET,"VerificationMailSuccess","SendPswVerificationEmailServlet","1");
                
        //test UserVoteServlet
        
        parametri = new StringBuilder ();
        addParameter("vote","5");
        EXPECT(GET,"UserVoteMissingParam1","UserVoteServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_restaurant","5");
        EXPECT(GET,"UserVoteMissingParam2","UserVoteServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("vote","wrongFormat");
        addParameter("id_restaurant","5");
        EXPECT(GET,"UserVoteMalformedParam2","UserVoteServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("vote","5");
        addParameter("id_restaurant","wrongFormat");
        EXPECT(GET,"UserVoteMalformedParam2","UserVoteServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("vote","5");
        addParameter("id_restaurant","-1");
        EXPECT(GET,"UserVoteNonExistantRest","UserVoteServlet","-2");
        
        parametri = new StringBuilder ();
        addParameter("vote","5");
        addParameter("id_restaurant","");
        EXPECT(GET,"UserVoteEmptyParam2","UserVoteServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("vote","");
        addParameter("id_restaurant","2");
        EXPECT(GET,"UserVoteEmptyParam1","UserVoteServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("vote","2");
        addParameter("id_restaurant",id_rest_owned);
        EXPECT(GET,"UserVoteFailOwnerCantVoteItsRest","UserVoteServlet","-2");
        
        //mi assicuro che sia già stato fatto un voto da parte mia nelle ultime 24h
        parametri = new StringBuilder ();
        addParameter("vote","2");
        addParameter("id_restaurant",id_rest_not_owned);
        executeGet(BASEURL+"UserVoteServlet", parametri.toString());
        
        parametri = new StringBuilder ();
        addParameter("vote","2");
        addParameter("id_restaurant",id_rest_not_owned);
        EXPECT(GET,"UserVoteFail24h","UserVoteServlet","-2");
        
        //test UserLikeDislikeReviewServlet
        
        parametri = new StringBuilder ();
        addParameter("id_review","5");
        EXPECT(GET,"LikeMissingParam1","UserLikeDislikeReviewServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("like_type","1");
        EXPECT(GET,"LikeVoteMissingParam2","UserLikeDislikeReviewServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("like_type","");
        addParameter("id_review","5");
        EXPECT(GET,"LikeVoteMalformedParam1","UserLikeDislikeReviewServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("like_type","1");
        addParameter("id_review","");
        EXPECT(GET,"LikeVoteMalformedParam2","UserLikeDislikeReviewServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("like_type","1");
        addParameter("id_review","-1");
        EXPECT(GET,"LikeNonExistantRev","UserLikeDislikeReviewServlet","-2");
        
        parametri = new StringBuilder ();
        addParameter("like_type","1");
        addParameter("id_review",id_rev_owned);
        EXPECT(GET,"LikeCantLikeItsOwnRev","UserLikeDislikeReviewServlet","-2");
        
        //mi assicuro che sia già stato fatto un like da parte mia
        parametri = new StringBuilder ();
        addParameter("like_type","1");
        addParameter("id_review",id_rev_not_owned);
        executeGet(BASEURL+"UserLikeDislikeReviewServlet", parametri.toString());
        
        parametri = new StringBuilder ();
        addParameter("like_type","1");
        addParameter("id_review",id_rev_not_owned);
        EXPECT(GET,"LikeAlreadyExisting","UserLikeDislikeReviewServlet","-2");
        
        //test AddReplyServlet
        
        parametri = new StringBuilder ();
        addParameter("id_review",id_rev_rest_not_owned);
        EXPECT(POST,"AddReplyMissingParam1","AddReplyServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("description","descrizione");
        EXPECT(POST,"AddReplyMissingParam2","AddReplyServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("description","descrizione");
        addParameter("id_review",id_rev_rest_not_owned);
        ASSERT(POST,"AddReplyNotTheOwnerOfTheRest","AddReplyServlet","-2");
        
        //mi assicuro che esista già una reply, per il prossimo test
        parametri = new StringBuilder ();
        addParameter("description","descrizione");
        addParameter("id_review",id_rev_rest_owned);
        executePost(BASEURL+"AddReplyServlet", parametri.toString());
        
        parametri = new StringBuilder ();
        addParameter("description","descrizione");
        addParameter("id_review",id_rev_rest_owned);
        EXPECT(POST,"AddReplyReplyAlreadyExisting","AddReplyServlet","-2");
        
        //test RemovePhotoByAdminServlet
        parametri = new StringBuilder ();
        EXPECT(GET,"RemovePhotoMissingParam1","RemovePhotoByAdminServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_photo","testo");
        EXPECT(GET,"RemovePhotoMalformedParam1","RemovePhotoByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_photo","");
        EXPECT(GET,"RemovePhotoMalformedParam2","RemovePhotoByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_photo","-1");
        EXPECT(GET,"RemovePhotoSuccess","RemovePhotoByAdminServlet","1");
        
        //test RemoveReviewByAdminServlet
        parametri = new StringBuilder ();
        EXPECT(GET,"RemoveRevMissingParam1","RemoveReviewByAdminServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_review","testo");
        EXPECT(GET,"RemoveRevMalformedParam1","RemoveReviewByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_review","");
        EXPECT(GET,"RemoveRevMalformedParam2","RemoveReviewByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_review","-1");
        EXPECT(GET,"RemoveRevSuccess","RemoveReviewByAdminServlet","1");
        
        //test di AcceptReplyByAdminServlet
        parametri = new StringBuilder ();
        EXPECT(GET,"AcceptReplyMissingParam1","AcceptReplyByAdminServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_reply","testo");
        EXPECT(GET,"AcceptReplyMalformedParam1","AcceptReplyByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_reply","");
        EXPECT(GET,"AcceptReplyMalformedParam2","AcceptReplyByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_reply","-1");
        EXPECT(GET,"AcceptReplySuccess","AcceptReplyByAdminServlet","1");
        
        //test di DenyReplyByAdminServlet
        parametri = new StringBuilder ();
        EXPECT(GET,"DenyReplyMissingParam1","DenyReplyByAdminServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_reply","testo");
        EXPECT(GET,"DenyReplyMalformedParam1","DenyReplyByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_reply","");
        EXPECT(GET,"DenyReplyMalformedParam2","DenyReplyByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_reply","-1");
        EXPECT(GET,"DenyReplySuccess","DenyReplyByAdminServlet","1");
        
        //test di UnreportPhotoByAdminServlet
        parametri = new StringBuilder ();
        EXPECT(GET,"UnreportPhotoMissingParam1","UnreportPhotoByAdminServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_photo","testo");
        EXPECT(GET,"UnreportPhotoMalformedParam1","UnreportPhotoByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_photo","");
        EXPECT(GET,"UnreportPhotoMalformedParam2","UnreportPhotoByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_photo","-1");
        EXPECT(GET,"UnreportPhotoSuccess","UnreportPhotoByAdminServlet","1");
        
        //test di UnreportReviewByAdminServlet
        
        parametri = new StringBuilder ();
        EXPECT(GET,"UnreportReviewMissingParam1","UnreportReviewByAdminServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_review","testo");
        EXPECT(GET,"UnreportReviewMalformedParam1","UnreportReviewByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_review","");
        EXPECT(GET,"UnreportReviewMalformedParam2","UnreportReviewByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_review","-1");
        EXPECT(GET,"UnreportReviewSuccess","UnreportReviewByAdminServlet","1");
        
        //test di AcceptRestaurantRequestByAdminServlet
        
        parametri = new StringBuilder ();
        addParameter("id_restaurant","-1");
        EXPECT(GET,"AcceptRestaurantRequestMissingParam1","AcceptRestaurantRequestByAdminServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_user","testo");
        EXPECT(GET,"AcceptRestaurantRequestMissingParam2","AcceptRestaurantRequestByAdminServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_user","testo");
        addParameter("id_restaurant","-1");
        EXPECT(GET,"AcceptRestaurantRequestMalformedParam1","AcceptRestaurantRequestByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_user","-1");
        addParameter("id_restaurant","test");
        EXPECT(GET,"AcceptRestaurantRequestMalformedParam2","AcceptRestaurantRequestByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_user","-1");
        addParameter("id_restaurant","-1");
        EXPECT(GET,"AcceptRestaurantRequestSuccess","AcceptRestaurantRequestByAdminServlet","1");
        
        //test di DenyRestaurantRequestByAdminServlet
        
        parametri = new StringBuilder ();
        addParameter("id_restaurant","-1");
        EXPECT(GET,"DenyRestaurantRequestMissingParam1","DenyRestaurantRequestByAdminServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_user","testo");
        EXPECT(GET,"DenyRestaurantRequestMissingParam2","DenyRestaurantRequestByAdminServlet","-1");
        
        parametri = new StringBuilder ();
        addParameter("id_user","testo");
        addParameter("id_restaurant","-1");
        EXPECT(GET,"DenyRestaurantRequestMalformedParam1","DenyRestaurantRequestByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_user","-1");
        addParameter("id_restaurant","test");
        EXPECT(GET,"DenyRestaurantRequestMalformedParam2","DenyRestaurantRequestByAdminServlet","0");
        
        parametri = new StringBuilder ();
        addParameter("id_user","-1");
        addParameter("id_restaurant","-1");
        EXPECT(GET,"DenyRestaurantRequestSuccess","DenyRestaurantRequestByAdminServlet","1");
        
        
        
        
        printResults();
    }
    
    
    public static String executePost(String targetURL, String urlParameters) 
    {
        HttpURLConnection connection = null;
        try {
        //crea connessione
        URL url = new URL(targetURL);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", 
            "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", 
            Integer.toString(urlParameters.getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");  
        if(sessionId.compareTo("")!=0)
            connection.setRequestProperty("Cookie",sessionId);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        //Send request
        DataOutputStream wr = new DataOutputStream (
            connection.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.close();

        //Get Response  
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
        String line;
        if(connection.getHeaderField("Set-Cookie")!=null)
            sessionId = connection.getHeaderField("Set-Cookie");
        while ((line = rd.readLine()) != null) 
          response.append(line);
        rd.close();
        return response.toString();
      } 
      catch (Exception e)
      {
        e.printStackTrace();
        return null;
      } 
      finally 
      {
        if (connection != null) 
          connection.disconnect();
      }
    }
    
    public static String executeGet(String targetURL, String urlParameters) throws IOException 
    {
        String res="";
        URL obj = new URL(targetURL+"?"+urlParameters);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        if(sessionId.compareTo("")!=0)
            connection.setRequestProperty("Cookie",sessionId);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        if(connection.getHeaderField("Set-Cookie")!=null)
            sessionId = connection.getHeaderField("Set-Cookie");
        while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
    
    public static void addParameter(String parameter, String value) throws UnsupportedEncodingException
    {
        if(parametri.length()==0)
            parametri.append(parameter+"="+URLEncoder.encode(value, "UTF-8"));
        else
            parametri.append("&"+parameter+"="+URLEncoder.encode(value, "UTF-8"));
    }
    
    public static void EXPECT(int type, String testName,String servlet, String result) throws UnsupportedEncodingException, IOException
    {
        totalTests++;
        String res="";
        if(type==POST)
            res= executePost(BASEURL+servlet, parametri.toString());
        else if(type==GET)
            res= executeGet(BASEURL+servlet, parametri.toString());
        if(res.compareTo(result)!=0)
        {
            failedTests++;
            failedTestsNames.add(testName);
            failedTestsExpectedRes.add(result);
            failedTestsRes.add(res);
        }
    }
    
    public static void ASSERT(int type, String testName,String servlet, String result) throws IOException
    {
        totalTests++;
        String res="";
        if(type==POST)
            res= executePost(BASEURL+servlet, parametri.toString());
        else if(type==GET)
            res= executeGet(BASEURL+servlet, parametri.toString());
        if(res.compareTo(result)!=0)
        {
            failedTests++;
            failedTestsNames.add(testName);
            failedTestsExpectedRes.add(result);
            failedTestsRes.add(res);
            System.out.println("ASSERTION FAILED ON TEST: "+testName+", exiting...");
            printResults();
            System.exit(1);
        }
    }
    
    public static void printResults()
    {
        System.out.println(totalTests+" tests have been run");
        System.out.println(failedTests+" tests have failed");
        if(failedTestsNames.size()>0)
        {
            System.out.println("TESTNAME            EXPECTED            ACTUAL");
            for(int i=0;i<failedTestsNames.size();i++)
            {
                System.out.println(failedTestsNames.get(i)+"            "
                +failedTestsExpectedRes.get(i)+"                "
                +failedTestsRes.get(i));
            }
        }
    }
    
    public static void clearResults()
    {
        totalTests=0;
        failedTests=0;
        failedTestsNames.clear();
        failedTestsExpectedRes.clear();
        failedTestsRes.clear();
    }
}
