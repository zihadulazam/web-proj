/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import database.contexts.AttemptContext;
import database.contexts.ReplyContext;
import database.contexts.ReviewContext;
import database.contexts.PhotoContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jacopo
 */
public class DbManager implements Serializable
{

    private transient Connection con;

    public DbManager(String url) throws ClassNotFoundException, SQLException
    {
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        con = DriverManager.getConnection(url);
        con.setAutoCommit(false);
    }

    /**
     * Prova ad inserire(registrare sul sito) un utente nel database, l'utente
     * così registrato sarà sempre un utente normale (non ristoratre e non
     * admin).
     * @param user Oggetto User con i dati all'iterno
     * @return 0 se è andata a buon fine, 1 se nn ha registrato xk esiste un
     * utente con quella email, 2 se esiste un utente con quel nick, 3 se non è
     * andata a buon fine per altri motivi
     * @throws java.sql.SQLException
     *
     */
    public int registerUser(User user) throws SQLException
    {
        int res = 3;
        try(PreparedStatement st = con.prepareStatement("insert into USERS(NAME,SURNAME,"
                + "NICKNAME,EMAIL,PASSWORD,AVATAR_PATH,REVIEWS_COUNTER,REVIEWS_POSITIVE,REVIEWS_NEGATIVE,USERTYPE) values(?,?,?,?,?,?,?,?,?,?)");)
        {
            if (findUserByEmail(user.getEmail()))
            {
                res = 1;
            } else if (findUserByNickname(user.getNickname()))
            {
                res = 2;
            }
            else
            {
                st.setString(1, user.getName());
                st.setString(2, user.getSurname());
                st.setString(3, user.getNickname());
                st.setString(4, user.getEmail());
                st.setString(5, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
                st.setString(6, user.getAvatar_path());
                st.setInt(7, 0);
                st.setInt(8, 0);
                st.setInt(9, 0);
                st.setInt(10, 0);
                st.executeUpdate();
                res = 0;
            } 
            con.commit();
        } catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * Per autenticare uno User, di cui si fornisce nick/mail e password.
     *
     * @param nickOrEmail nick o email dell utente che fa login
     * @param password password
     * @return null se la password non matcha o se l'utente non esiste, uno User
     * con password "pulita via" altrimenti
     * @throws java.sql.SQLException
     */
    public User loginUserByEmailOrNickname(String nickOrEmail, String password) throws SQLException
    {
        User user = null;
        try (PreparedStatement st = con.prepareStatement("select * from USERS where EMAIL=? OR NICKNAME=?"))
        {
            st.setString(1, nickOrEmail);
            st.setString(2, nickOrEmail);
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    user = new User();
                    user.setId(rs.getInt("ID"));
                    user.setName(rs.getString("NAME"));
                    user.setSurname(rs.getString("SURNAME"));
                    user.setNickname(rs.getString("NICKNAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setPassword(rs.getString("PASSWORD"));
                    user.setAvatar_path(rs.getString("AVATAR_PATH"));
                    user.setReviews_counter(rs.getInt("REVIEWS_COUNTER"));
                    user.setReviews_positive(rs.getInt("REVIEWS_POSITIVE"));
                    user.setReviews_negative(rs.getInt("REVIEWS_NEGATIVE"));
                    user.setType(rs.getInt("USERTYPE"));
                    if (!BCrypt.checkpw(password, user.getPassword()))
                    {
                        System.out.println("***************"+"pasword diversi");
                        user = null;
                    } else
                    {
                        System.out.println("***************"+"pasword ok");
                        user.setPassword("placeholder");
                    }
                }
                con.commit();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
/**
 * Attenzione! Non fa commit!
 * @param email
 * @return
 * @throws SQLException 
 */
    private boolean findUserByEmail(String email) throws SQLException
    {
        boolean res = true;
        try (PreparedStatement st = con.prepareStatement("select * from USERS where EMAIL=?"))
        {
            st.setString(1, email);
            try (ResultSet rs = st.executeQuery())
            {
                res = rs.next();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
/**
 * Attenzione! Non fa commit!
 * @param nick
 * @return
 * @throws SQLException 
 */
    private boolean findUserByNickname(String nick) throws SQLException
    {
        boolean res = true;
        try (PreparedStatement st = con.prepareStatement("select * from USERS where NICKNAME=?"))
        {
            st.setString(1, nick);
            try (ResultSet rs = st.executeQuery())
            {
                res = rs.next();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    /**
     * Restituisce le review di un utente.
     *
     * @param user L'utente di cui cercare le reviews.
     * @return Un ArrayList di oggetti Review dell'utente.
     * @throws java.sql.SQLException
     */
    public ArrayList<Review> getUserReviews(User user) throws SQLException
    {
        ArrayList<Review> reviews = new ArrayList();
        try (PreparedStatement st = con.prepareStatement("select * from REVIEWS where ID_CREATOR=?"))
        {
            st.setInt(1, user.getId());
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Review review = new Review();
                    review.setId(rs.getInt("ID"));
                    review.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    review.setFood(rs.getInt("FOOD"));
                    review.setService(rs.getInt("SERVICE"));
                    review.setValue_for_money(rs.getInt("VALUE_FOR_MONEY"));
                    review.setAtmosphere(rs.getInt("ATMOSPHERE"));
                    review.setName(rs.getString("NAME"));
                    review.setDescription(rs.getString("DESCRIPTION"));
                    review.setDate_creation(rs.getTimestamp("DATE_CREATION"));
                    review.setId_restaurant(rs.getInt("ID_RESTAURANT"));
                    review.setId_creator(rs.getInt("ID_CREATOR"));
                    review.setId_photo(rs.getInt("ID_PHOTO"));
                    review.setLikes(rs.getInt("LIKES"));
                    review.setDislikes(rs.getInt("DISLIKES"));
                    reviews.add(review);
                }
                con.commit();
            }
        }
        return reviews;
    }

    /**
     * Le notificazioni di un utente (n.b. NON per admin).
     *
     * @param user L'utente di cui si vuole cercare le notifiche.
     * @return Un ArrayList contenente oggetti di tipo Notification.
     * @throws SQLException
     */
    public ArrayList<Notification> getUserNotifications(User user) throws SQLException
    {
        ArrayList<Notification> notifications = new ArrayList();
        try (PreparedStatement st = con.prepareStatement("select * from NOTIFICATIONS where ID=?"))
        {
            st.setInt(1, user.getId());
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Notification notification = new Notification();
                    notification.setUser_id(rs.getInt("ID"));
                    notification.setDescription(rs.getString("DESCRIPTION"));
                    notifications.add(notification);
                }
                con.commit();
            }

        }
        //Timestamp currentTimestamp =new Timestamp(Calendar.getInstance().getTime().getTime());
        // SELECT * FROM SAMEPLE_TABLE FETCH FIRST 10 ROWS ONLY
        //roba per dopo
        return notifications;
    }

    /**
     * Restituisce un numero di contesti, una classe formata da una review, uno
     * user e una reply; lo user è chi ha fatto la reply, la review è ciò a cui
     * la reply vuole rispondere. Da usare p.e. per mostrare ad un admin per
     * confermare o no una reply. Il timestamp di un contesto "prelevato" viene
     * refrehsato al timestamp attuale in modo che non sarà prelevabile da altri
     * admin per 1 ora.
     *
     * @param many Quanti contesti (al max) da ricevere.
     * @return Un ArrayList di contesti.
     * @throws SQLException
     */
    public ArrayList<ReplyContext> getRepliesToBeConfirmed(int many) throws SQLException
    {
        ArrayList<ReplyContext> contesti = new ArrayList();
        try (PreparedStatement st = con.prepareStatement("select * from REPLIES_TO_BE_CONFIRMED"
                + " where {fn TIMESTAMPDIFF( SQL_TSI_HOUR, DATE_ADMIN_TOOK,?)} >= 1 "
                + "FETCH FIRST ? ROWS ONLY"))
        {
            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
            st.setTimestamp(1, timestamp);
            st.setInt(2, many);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    contesti.add(getReplyContext(rs.getInt("ID")));
                    rs.updateTimestamp("DATE_ADMIN_TOOK", timestamp);
                }
                con.commit();
            }
        }
        return contesti;
    }

    private ReplyContext getReplyContext(int id_reply) throws SQLException
    {
        ReplyContext contesto = new ReplyContext();
        contesto.setReply(getReplyFromId(id_reply));
        contesto.setReview(getReviewById(contesto.getReply().getId_review()));
        contesto.setUser(getUserById(contesto.getReply().getId_owner()));
        return contesto;
    }

    private Reply getReplyFromId(int id_reply) throws SQLException
    {
        Reply reply = null;
        try (PreparedStatement st = con.prepareStatement("select * from REPLIES where id=?"))
        {
            st.setInt(1, id_reply);
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    reply = new Reply();
                    reply.setId(id_reply);
                    reply.setDescription(rs.getString("DESCRIPTION"));
                    reply.setDate_creation(rs.getTimestamp("DATE_CREATION"));
                    reply.setId_review(rs.getInt("ID_REVIEW"));
                    reply.setId_owner(rs.getInt("ID_OWNER"));
                    reply.setDate_validation(rs.getTimestamp("DATE_VALIDATION"));
                    reply.setId_validator(rs.getInt("ID_VALIDATOR"));
                    reply.setValidated(rs.getBoolean("VALIDATED"));
                }
                con.commit();
            }
        }
        return reply;
    }

    private Review getReviewById(int id) throws SQLException
    {
        Review review = null;
        try (PreparedStatement st = con.prepareStatement("select * from REVIEWS where id=?"))
        {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery())
            {
                con.commit();
                if (rs.next())
                {
                    review = new Review();
                    review.setId(rs.getInt("ID"));
                    review.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    review.setFood(rs.getInt("FOOD"));
                    review.setService(rs.getInt("SERVICE"));
                    review.setValue_for_money(rs.getInt("VALUE_FOR_MONEY"));
                    review.setAtmosphere(rs.getInt("ATMOSPHERE"));
                    review.setName(rs.getString("NAME"));
                    review.setDescription(rs.getString("DESCRIPTION"));
                    review.setDate_creation(rs.getTimestamp("DATE_CREATION"));
                    review.setId_restaurant(rs.getInt("ID_RESTAURANT"));
                    review.setId_creator(rs.getInt("ID_CREATOR"));
                    review.setId_photo(rs.getInt("ID_PHOTO"));
                    review.setLikes(rs.getInt("LIKES"));
                    review.setDislikes(rs.getInt("DISLIKES"));
                }
            }
        }
        return review;
    }

    /**
     *
     * @param id
     * @return Uno user con la password rimossa, null se non esiste o qualcosa
     * va storto.
     * @throws SQLException
     */
    private User getUserById(int id) throws SQLException
    {
        User user = null;
        try (PreparedStatement st = con.prepareStatement("select * from USERS where id=?"))
        {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery())
            {
                con.commit();
                if (rs.next())
                {
                    user = new User();
                    user.setId(rs.getInt("ID"));
                    user.setName(rs.getString("NAME"));
                    user.setSurname(rs.getString("SURNAME"));
                    user.setNickname(rs.getString("NICKNAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setPassword("placeholder");
                    user.setAvatar_path(rs.getString("AVATAR_PATH"));
                    user.setReviews_counter(rs.getInt("REIVEWS_COUNTER"));
                    user.setReviews_positive(rs.getInt("REVIEWS_POSITIVE"));
                    user.setReviews_negative(rs.getInt("REVIEWS_NEGATIVE"));
                    user.setType(rs.getInt("USERTYPE"));
                }
            }
        }
        return user;
    }

    /**
     * Funzione per prendere una serie di photo segnalate e i loro contesti,
     * solitamente usato per l'admin. Un contesto è la foto più lo user che lha
     * postata. Una volta che un contesto è prelevato (restituito) non sarà
     * possibile restituirlo ancora tramite questo metodo per un ora.
     *
     * @param many Quanti contesti (al max) si vuole ricevere.
     * @return Un ArrayList di contesti.
     * @throws SQLException
     */
    public ArrayList<PhotoContext> getReportedPhotos(int many) throws SQLException
    {
        ArrayList<PhotoContext> contesti = new ArrayList();
        try (PreparedStatement st = con.prepareStatement("select * from REPORTED_PHOTOS"
                + " where {fn TIMESTAMPDIFF( SQL_TSI_HOUR, DATE_ADMIN_TOOK,?)} >= 1 "
                + "FETCH FIRST ? ROWS ONLY"))
        {
            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
            st.setTimestamp(1, timestamp);
            st.setInt(2, many);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    contesti.add(getPhotoContext(rs.getInt("ID")));
                    rs.updateTimestamp("DATE_ADMIN_TOOK", timestamp);
                }
                con.commit();
            }
        }
        return contesti;
    }

    private PhotoContext getPhotoContext(int id_photo) throws SQLException
    {
        PhotoContext contesto = new PhotoContext();
        contesto.setPhoto(getPhotoById(id_photo));
        contesto.setUser(getUserById(contesto.getPhoto().getId_owner()));
        return contesto;
    }

    private Photo getPhotoById(int id) throws SQLException
    {
        Photo photo = null;
        try (PreparedStatement st = con.prepareStatement("select * from PHOTOS where id=?"))
        {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    photo = new Photo();
                    photo.setId(id);
                    photo.setName(rs.getString("NAME"));
                    photo.setDescription(rs.getString("DESCRIPTION"));
                    photo.setPath(rs.getString("PATH"));
                    photo.setId_restaurant(rs.getInt("ID_RESTAURANT"));
                    photo.setId_owner(rs.getInt("ID_OWNER"));
                }
            }
        }
        return photo;
    }
    
    private boolean existReportedPhotoById(int id) throws SQLException
    {
        boolean res=true;
        try (PreparedStatement st = con.prepareStatement("select * from REPORTED_PHOTOS where id=?"))
        {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery())
            {
                res=rs.next();
            }
        }
        return res;
    }
    
    private boolean existReportedReviewById(int id) throws SQLException
    {
        boolean res=true;
        try (PreparedStatement st = con.prepareStatement("select * from REPORTED_REVIEWS where id=?"))
        {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery())
            {
                res=rs.next();
            }
        }
        return res;
    }
/**
 * Non fa commit.
 * @param id
 * @return
 * @throws SQLException 
 */
    private Restaurant getRestaurantById(int id) throws SQLException
    {
        Restaurant restaurant = null;
        try (PreparedStatement st = con.prepareStatement("select * from RESTAURANTS where id=?"))
        {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    restaurant = new Restaurant();
                    restaurant.setId(rs.getInt("ID"));
                    restaurant.setName(rs.getString("NAME"));
                    restaurant.setDescription(rs.getString("DESCRIPTION"));
                    restaurant.setWeb_site_url(rs.getString("WEB_SITE_URL"));
                    restaurant.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    restaurant.setId_owner(rs.getInt("ID_OWNER"));
                    restaurant.setId_creator(rs.getInt("ID_CREATOR"));
                    restaurant.setId_price_range(rs.getInt("ID_PRICE_RANGE"));
                    restaurant.setReviews_counter(rs.getInt("REVIEWS_COUNTER"));
                    restaurant.setValidated(rs.getBoolean("VALIDATED"));
                }
            }
        }
        return restaurant;
    }

    private AttemptContext getAttemptContext(int id_user, int id_restaurant, int creation_claim_both, String userTextClaim) throws SQLException
    {
        AttemptContext contesto = new AttemptContext();
        contesto.setUser(getUserById(id_user));
        contesto.setRestaurant(getRestaurantById(id_restaurant));
        contesto.setIsClaim(creation_claim_both);
        contesto.setUsertextclaim(userTextClaim);
        return contesto;
    }

    /**
     * Funzione per prendere una serie di richieste di ristoranti e i loro
     * contesti, solitamente usato per l'admin. Un contesto è l'utente che ha
     * fatto la richiesta, il ristorante riguarda il ristorante in questione e
     * una flag che indica il tipo di richiesta. Una volta che un contesto è
     * restituito da questa funzione non sarà possibile restituirlo ancora
     * tramite questo metodo per un ora.
     *
     * @param many Quanti contesti (al max) si vuole ricevere.
     * @return Un ArrayList di contesti.
     * @throws SQLException
     */
    public ArrayList<AttemptContext> getRestaurantsRequests(int many) throws SQLException
    {
        ArrayList<AttemptContext> contesti = new ArrayList();
        try (PreparedStatement st = con.prepareStatement("select * from RESTAURANTS_REQUESTS"
                + " where {fn TIMESTAMPDIFF( SQL_TSI_HOUR, DATE_ADMIN_TOOK,?)} >= 1 "
                + "FETCH FIRST ? ROWS ONLY"))
        {
            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
            st.setTimestamp(1, timestamp);
            st.setInt(2, many);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    contesti.add(getAttemptContext(rs.getInt("ID"), rs.getInt("ID_RESTAURANT"),
                            rs.getInt("CREATION_CLAIM_BOTH_FLAG"), rs.getString("USERTEXTCLAIM")));
                    rs.updateTimestamp("DATE_ADMIN_TOOK", timestamp);
                }
                con.commit();
            }
        }
        return contesti;
    }

    /**
     * Per ricevere un numero pari a many di contesti (user+review) riguardanti
     * le review che hanno subito un report. Una volta che un contesto è stato
     * restituito da questo metodo non sarà restituit per un ora.
     *
     * @param many Quanti contesti (al max) si vuole ricevere.
     * @return Un ArrayList con i contesti.
     * @throws SQLException
     */
    public ArrayList<ReviewContext> getReportedReviews(int many) throws SQLException
    {
        ArrayList<ReviewContext> contesti = new ArrayList();
        try (PreparedStatement st = con.prepareStatement("select * from REPORTED_REVIEWS"
                + " where {fn TIMESTAMPDIFF( SQL_TSI_HOUR, DATE_ADMIN_TOOK,?)} >= 1 "
                + "FETCH FIRST ? ROWS ONLY"))
        {
            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
            st.setTimestamp(1, timestamp);
            st.setInt(2, many);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    contesti.add(getReviewContext(rs.getInt("ID")));
                    rs.updateTimestamp("DATE_ADMIN_TOOK", timestamp);
                }
                con.commit();
            }
        }
        return contesti;
    }

    private ReviewContext getReviewContext(int review_id) throws SQLException
    {
        ReviewContext contesto = new ReviewContext();
        contesto.setReview(getReviewById(review_id));
        contesto.setUser(getUserById(contesto.getReview().getId_creator()));
        return contesto;
    }

    /**
     * Metodo per inserire una foto nelle foto reportate, utilizzando il suo id.
     * @param photo_id
     * @throws SQLException
     */
    public void reportPhoto(int photo_id) throws SQLException
    {
        try (PreparedStatement st2 = con.prepareStatement("INSERT INTO REPORTED_PHOTOS VALUES(?,?)");
                PreparedStatement st1 = con.prepareStatement("SELECT FROM PHOTOS WHERE ID=?"))
        {
            st1.setInt(1, photo_id);
            try (ResultSet rs1 = st1.executeQuery())
            {
                //se la foto esiste e non è già stata reportata inserisco il report
                if (rs1.next() && !existReportedPhotoById(photo_id))
                {
                    st2.setInt(1, photo_id);
                    st2.setTimestamp(2, new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    st2.executeUpdate();
                }
            }
            con.commit();
        }
    }
    /**
     * Metodo per inserire una review nelle review reportate, utilizzando il suo id.
     * @param review_id
     * @throws SQLException
     */
    public void reportReview(int review_id) throws SQLException
    {
        try (PreparedStatement st2 = con.prepareStatement("INSERT INTO REPORTED_REVIEWS VALUES(?,?)");
                PreparedStatement st1 = con.prepareStatement("SELECT FROM REVIEWS WHERE ID=?"))
        {
            st1.setInt(1, review_id);
            try (ResultSet rs1 = st1.executeQuery())
            {
                //se la review esiste e non è già stata reportata inserisco il report
                if (rs1.next() && !existReportedReviewById(review_id))
                {
                    st2.setInt(1, review_id);
                    st2.setTimestamp(2, new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    st2.executeUpdate();
                }
            }
            con.commit();
        }
    }
    
    
    /**
     * Per inserire una reply da parte di un ristoratore a una sua recensione,
     * verrà inserite anche fra le reply che gli admin devono controllare e
     * confermare. Non serve il tempo di creazione, di validazione, l'id del
     * validatore e se è valida o no prima di passare la Reply al metodo.
     *
     * @param reply
     * @throws SQLException
     */
    public void addReply(Reply reply) throws SQLException
    {
        try (PreparedStatement st1 = con.prepareStatement("INSERT INTO REPLIES(DESCRIPTION,DATE_CREATION,ID_REVIEW,ID_OWNER"
                + ",DATE_VALIDATION,ID_VALIDATOR,VALIDATED) VALUES (?,?,?.?,?,?,?)");
                PreparedStatement st2 = con.prepareStatement("INSERT INTO REPLIES_TO_BE_CONFIRMED VALUES (?,?)"))

        {
            st1.setString(1, reply.getDescription());
            st1.setTimestamp(2, new Timestamp(Calendar.getInstance().getTime().getTime()));
            st1.setInt(3, reply.getId_review());
            st1.setInt(4, reply.getId_owner());
            st1.setTimestamp(5, new Timestamp(Calendar.getInstance().getTime().getTime()));
            st1.setInt(6, -1);
            st1.setBoolean(7, false);
            st1.executeUpdate();
            try (ResultSet rs = st1.getGeneratedKeys())
            {
                if (rs.next())
                {
                    st2.setInt(1, rs.getInt(1));
                    st2.setTimestamp(2, new Timestamp(Calendar.getInstance().getTime().getTime()));
                    st2.executeUpdate();
                    con.commit();
                } else
                {
                    con.rollback();
                }
            }
        }
    }
    
    /**
     * Metodo per inserire nel db un claim di un ristorante, nelle classi User e Restaurant passate
     * basta che siano presenti gli id.
     * @param user L'utente che fa la creazione/claim del ristorante.
     * @param restaurant Il ristorante in questione.
     * @param userTextClaim Il testo che l'utente ha dato come giustificazione del claim, se esiste.
     * @param creationClaimBoth Flag che dice se questa è una creazione, claim, o entrambe di ristorante.
     * @throws SQLException 
     */
    public void addClaim(User user, Restaurant restaurant, String userTextClaim, int creationClaimBoth) throws SQLException
    {
        try(PreparedStatement st1= con.prepareStatement("select from RESTAURANTS_REQUESTS WHERRE ID_USER=? AND ID_RESTAURANT=?");
            PreparedStatement st2= con.prepareStatement("INSERT INTO RESTAURANT_REQUESTS VALUES(?,?,?,?,?)"))
        {
            st1.setInt(1, user.getId());
            st1.setInt(2, restaurant.getId());
            try(ResultSet st= st1.executeQuery())
            {//se questa request non esiste già
                if(!st.next())
                {
                    st2.setInt(1, user.getId());
                    st2.setInt(2, restaurant.getId());
                    st2.setTimestamp(3, new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    st2.setInt(4, creationClaimBoth);
                    st2.setString(5, userTextClaim);
                    st2.executeUpdate();
                    con.commit();
                }
            }
        }
    }
    /**
     * Aggiunge una review, in base al suo voto globale ciò farà variare la media
     * globale del ristorante in questione. Nell'oggetto review non è necessario
     * settare la data di creazione o il numero di likes.
     * @param review
     * @return false se non ci sono stati problemi, vero altrimenti
     * @throws SQLException 
     */
    public boolean addReview(Review review) throws SQLException
    {
        boolean res = true;
        try(PreparedStatement st = con.prepareStatement("insert into REVIEWS(GLOBAL_VALUE,FOOD,"
                + "SERVICE,VALUE_FOR_MONEY,ATMOSPHERE,NAME,DESCRIPTION,DATE_CREATION,ID_RESTAURANT,"
                + "ID_CREATOR,ID_PHOTO,LIKES,DISLIKES) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement updateSt= con.prepareStatement("UPDATE RESTAURANTS SET GLOBAL_VALUE=?, "
                    + "REVIEWS_COUNTER=? WHERE ID=?");
            PreparedStatement updateUs= con.prepareStatement("UPDATE USERS SET REVIEWS_COUNTER=? WHERE ID=?"))
                
        {
            Restaurant restaurant= null;
            //il ristorante esiste vado avanti
            if((restaurant=getRestaurantById(review.getId_restaurant()))!=null)
            {
                User user= getUserById(review.getId_creator());
                int newReviewsCounter= restaurant.getReviews_counter()+1;
                int newGlobal = (restaurant.getReviews_counter()*restaurant.getGlobal_value()+
                        review.getGlobal_value())/(newReviewsCounter);
                int newUserReviewCounter=user.getReviews_counter()+1;
                st.setInt(1, review.getGlobal_value());
                st.setInt(2, review.getFood());
                st.setInt(3, review.getService());
                st.setInt(4, review.getValue_for_money());
                st.setInt(5, review.getAtmosphere());
                st.setString(6, review.getName());
                st.setString(7, review.getDescription());
                st.setTimestamp(8, new Timestamp(Calendar.getInstance().getTime().getTime()));
                st.setInt(9, review.getId_restaurant());
                st.setInt(10, review.getId_creator());
                st.setInt(11, review.getId_photo());
                st.setInt(12, 0);
                st.setInt(13, 0);
                updateSt.setInt(1, newGlobal);
                updateSt.setInt(2, newReviewsCounter);
                updateSt.setInt(3, review.getId_restaurant());
                updateUs.setInt(1, newUserReviewCounter);
                updateUs.setInt(2, user.getId());
                st.executeUpdate();
                updateSt.executeUpdate();
                updateUs.executeUpdate();
                res=false;
            } 
            con.commit();
        } catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    /**
     * Per settare la foto dello user. L'oggetto user deve avere campi nickname 
     * e email corretti, il resto non importa (prenderlo dalla sessione).
     * @param user Lo User in questione.
     * @param photoPath Una stringa che è il path della foto all'interno dell'applicazione.
     * @return false se non ci sono stati problemi ,vero altrimenti
     * @throws SQLException 
     */
    public boolean setUserPhoto(User user, String photoPath) throws SQLException
    {
        //non viene controllato prima se lo user esiste xk si assume che sia preso dalla
        //sessione, e quindi corretto
        boolean res=true;
        try(PreparedStatement st= con.prepareStatement("UPDATE USERS SET AVATAR_PATH=? WHERE NICKNAME=? AND EMAIL=?"))
        {
            st.setString(1, photoPath);
            st.setString(2, user.getNickname());
            st.setString(3, user.getEmail());
            con.commit();
            res=false;
        }
        return res;
    }
    /**
     * Per inserire una foto da parte di uno user che riguarda un ristorante
     * non suo.
     * @param restaurantId Id del ristorante di cui mettere la foto.
     * @param user User che sta mettendo la foto.
     * @param photo Photo caricata.
     * @return Falso se è andato tutto bene, vero altrimenti.
     * @throws SQLException 
     */
    public boolean addPhotoToRestaurantFromUser(int restaurantId, User user, Photo photo) throws SQLException
    {
        boolean res=true;
        try(PreparedStatement st= con.prepareStatement("INSERT INTO PHOTOS(NAME,"
                + "DESCRIPTION,PATH,ID_RESTAURANT,ID_OWNER) VALUES(?,?,?,?,?)"))
        {
            if(getRestaurantById(restaurantId)!=null)
            {
                st.setString(1, photo.getName());
                st.setString(2, photo.getDescription());
                st.setString(3, photo.getPath());
                st.setInt(4, restaurantId);
                st.setInt(5, user.getId());
                st.executeUpdate();
                res=false;
            }
            con.commit();
        }
        return res;
    }
    /**
     * Per aggiungere il like/dislike di un utente rispetto a una review. Così facendo
     * verrà aggiornato il numero di like/dislike della review e del creatore della review.
     * Se il like dello user per la review esiste già ma il like è di un tipo diverso da quello
     * vecchio il like viene cambiato e Review e creatore review aggiornati.
     * @param reviewTarget Id della review target del like.
     * @param type Il tipo di like, deve essere 0(dislike) o 1(like).
     * @param liker L'utente che ha fatto il like.
     * @return True se sono sorti problemi, falso altrimenti.
     * @throws SQLException 
     */
    public boolean addLike(int reviewTarget, int type, User liker) throws SQLException
    {
        boolean res=true;
        try(PreparedStatement checkLikeExist= con.prepareStatement("SELECT * FROM USER_REVIEW_LIKES WHERE ID_USER=? "
                + "AND ID_REVIEW=? AND ID_CREATOR=?"))
        {
            Review review= getReviewById(reviewTarget);
            User creator= null;
            //controllo se utente creatore e review esistono
            if(review!=null && creator!=getUserById(review.getId_creator()))
            {
                checkLikeExist.setInt(1, liker.getId());
                checkLikeExist.setInt(2, reviewTarget);
                checkLikeExist.setInt(3, review.getId_creator());
                try(ResultSet rs= checkLikeExist.executeQuery())
                {
                    //se like non esiste lo metto io e aggiorno like del creatore review e review
                    if(!rs.next())
                    {
                        try(PreparedStatement makeLike= con.prepareStatement("INSERT INTO"
                                + " USER_REVIEW_LIKES VALUES(?,?,?,?,?)"))
                        {
                            makeLike.setInt(1,liker.getId());
                            makeLike.setInt(2, reviewTarget);
                            makeLike.setInt(3, creator.getId());
                            makeLike.setInt(4, type);
                            makeLike.setTimestamp(5,new Timestamp(Calendar.getInstance().getTime().getTime()));
                            makeLike.executeUpdate();
                            if(type==1)
                            {
                                incrementUserLikes(creator);
                                incrementReviewLikes(review);
                            }
                            else
                            {
                                incrementUserDislikes(creator);
                                incrementReviewDislikes(review);
                            }
                        }
                    }
                    else
                    {
                        //se esiste già lo modifico se è diverso(l'utente sta modificando
                        //il suo like) o lo ignoro se è uguale
                        int oldType=rs.getInt("LIKE_TYPE");
                        if(type!=oldType)
                        {
                            try(PreparedStatement changeLike= con.prepareStatement("UPDATE USER_REVIEW_LIKES "
                                    + "SET LIKE_TYPE=? WHERE ID_USER=? AND ID_REVIEW=? AND ID_CREATOR=?"))
                            {
                                changeLike.setInt(1, type);
                                changeLike.setInt(2, liker.getId());
                                changeLike.setInt(3, review.getId());
                                changeLike.setInt(4, review.getId_creator());
                                changeLike.executeUpdate();
                                //se likenuovo!=vecchio e quello nuovo è positivo
                                if(type==1)
                                {
                                    moveUserDislikeToLike(creator);
                                    moveReviewDislikeToLike(review);
                                    
                                }
                                else
                                {
                                    //se il nuovo like è negativo
                                    moveUserLikeToDislike(creator);
                                    moveReviewLikeToDislike(review);
                                }
                            }
                        }
                    }
                }
            }
            con.commit();
            res=false;
        }
        return res;
    }
    
    private void incrementUserLikes(User user) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("UPDATE USERS SET REVIEWS_POSITIVE=? WHERE ID=?"))
        {
            st.setInt(1, user.getReviews_positive()+1);
            st.setInt(2, user.getId());
            st.executeUpdate();
        }
    }
    
    private void incrementUserDislikes(User user) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("UPDATE USERS SET REVIEWS_NEGATIVE=? WHERE ID=?"))
        {
            st.setInt(1, user.getReviews_negative()+1);
            st.setInt(2, user.getId());
            st.executeUpdate();
        }
    }
    
    private void moveUserLikeToDislike(User user) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("UPDATE USERS SET REVIEWS_POSITIVE=?, REVIEWS_NEGATIVE=? WHERE ID=?"))
        {
            st.setInt(1, user.getReviews_positive()-1);
            st.setInt(2, user.getReviews_negative()+1);
            st.setInt(3, user.getId());
            st.executeUpdate();
        }
    }
    
    private void moveUserDislikeToLike(User user) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("UPDATE USERS SET REVIEWS_POSITIVE=?, REVIEWS_NEGATIVE=? WHERE ID=?"))
        {
            st.setInt(1, user.getReviews_positive()+1);
            st.setInt(2, user.getReviews_negative()-1);
            st.setInt(3, user.getId());
            st.executeUpdate();
        }
    }
    
    private void incrementReviewLikes(Review review) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("UPDATE REVIEWS SET LIKES=? WHERE ID=?"))
        {
            st.setInt(1, review.getLikes());
            st.setInt(2, review.getId());
            st.executeUpdate();
        }
    }
    
    private void incrementReviewDislikes(Review review) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("UPDATE REVIEWS SET DISLIKES WHERE ID=?"))
        {
            st.setInt(1, review.getDislikes()+1);
            st.setInt(2, review.getId());
            st.executeUpdate();
        }
    }
    
    private void moveReviewLikeToDislike(Review review) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("UPDATE REVIEWS SET LIKES=?, DISLIKES=? WHERE ID=?"))
        {
            st.setInt(1, review.getLikes()-1);
            st.setInt(2, review.getDislikes()+1);
            st.setInt(3, review.getId());
            st.executeUpdate();
        }
    }
    
    private void moveReviewDislikeToLike(Review review) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("UPDATE REVIEWS SET LIKES=?, DISLIKES=? WHERE ID=?"))
        {
            st.setInt(1, review.getLikes()+1);
            st.setInt(2, review.getDislikes()-1);
            st.setInt(3, review.getId());
            st.executeUpdate();
        }
    }
    
    
    
    /**
     * Chiude la connessione al database!
     */
    public static void shutdown()
    {
        try
        {
            DriverManager.getConnection("jdbc:derby://localhost:1527/eatbitDB;shutdown=true;user=eatbitDB;password=password");
        } catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
