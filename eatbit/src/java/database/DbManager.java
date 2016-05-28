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
import java.io.Serializable;
import static java.lang.Math.abs;
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
                    
                    if (!BCrypt.checkpw(password,user.getPassword()))
                    {
                        user = null;
                    } else
                    {
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
                    notification.setId(rs.getInt("ID"));
                    notification.setUser_id(rs.getInt("ID"));
                    notification.setDescription(rs.getString("DESCRIPTION"));
                    notifications.add(notification);
                }
                con.commit();
            }

        }
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
                    user.setReviews_counter(rs.getInt("REVIEWS_COUNTER"));
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
     * @param creationClaimBoth Flag che dice se questa è una creazione, claim, o entrambe di ristorante(0,1,2).
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
    public boolean addReview(Review review, Photo photo) throws SQLException
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
                st.setInt(11, addPhotoForReview(review.getId_restaurant(),user,photo));
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
     * Per settare la foto dello user.
     * Non viene controllato prima se lo user esiste perchè si assume che sia
     * preso dalla sessione e quindi corretto.
     * @param userId Id dell'utente.
     * @param photoPath Una stringa che è il path della foto all'interno dell'applicazione.
     * @return false se non ci sono stati problemi ,vero altrimenti
     * @throws SQLException 
     */
    public boolean modifyUserPhoto(int userId, String photoPath) throws SQLException
    {
        boolean res=true;
        try(PreparedStatement st= con.prepareStatement("UPDATE USERS SET AVATAR_PATH=? WHERE ID=?"))
        {
            st.setString(1, photoPath);
            st.setInt(2, userId);
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
    
    private int addPhotoForReview(int restaurantId, User user, Photo photo) throws SQLException
    {
        int res=-1;
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
                try(ResultSet rs= st.getGeneratedKeys())
                {
                    res=rs.getInt(1);
                }
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
     * Per inserire un ristorante.
     * @param restaurant Il ristorante da inserire.
     * @param cucine Il tipo di cucine che ha, una lista di stringhe. (devono essere uguali alle voci nel db)
     * @param coordinate Le coordinate del ristorante.
     * @param range Gli orari del ristorante, 7 HourRange.
     * @param userTextClaim Una stringa di descrizione che l'utente può mettere nella creazione o nel claim,
     * che verrà vista letta dall'admin quando decide se confermare o no.
     * @param photo Prima foto del ristorante, non è necessario l'id del ristorante.
     * @param min La spesa minima in questo ristorante.
     * @param max La spesa max in questo ristorante.
     * @param isClaim Booleano per dire se è anche un claim oltre che una creazione(true), o solo creazione(false).
     * @return True se ci sono stati problemi, false se ha avuto successo.
     * @throws SQLException 
     */
    public boolean addRestaurant(Restaurant restaurant,ArrayList<String> cucine,Coordinate coordinate,ArrayList<HourRange> range, String userTextClaim, Photo photo, double min, double max, boolean isClaim) throws SQLException
    {
        boolean res=true;
        try(PreparedStatement st= con.prepareStatement("INSERT INTO RESTAURANTS(NAME,DESCRIPTION,"
                + "WEB_SITE_URL,GLOBAL_VALUE,ID_OWNER,ID_CREATOR,ID_PRICE_RANGE,REVIEWS_COUNTER,VALIDATED"))
        {
            st.setString(1, restaurant.getName());
            st.setString(2, restaurant.getDescription());
            st.setString(3, restaurant.getWeb_site_url());
            st.setInt(4, 0);
            st.setInt(5, -1);
            st.setInt(6, restaurant.getId_creator());
            st.setInt(7, findClosestPrice(min,max));
            st.setInt(8, 0);
            st.setBoolean(9, false);
            st.executeUpdate();
            int restId=-1;
            try(ResultSet rs= st.getGeneratedKeys())
            {
                restId= rs.getInt(1);
                //per ogni cucina aggiunto la relazione, se quella cucina esiste nel nostro db
                for(int i=0;i<cucine.size();i++)
                {
                    int cuisineId= findCuisine(cucine.get(i));
                    if(cuisineId!=-1)
                    {
                        addRestaurantXCuisine(restId,cuisineId);
                    }
                }
                //aggiungo le coordinate
                addCoordinate(restId,coordinate);
                //aggiungo gli orari
                for(int i=0;i<range.size();i++)
                    addHourRange(restId,range.get(i));
                restaurant.setId(restId);
            }
            User user= new User();
            user.setId(restaurant.getId_creator());
            //aggiungo foto
            addPhotoToRestaurantFromUser(restId,user,photo);
            //aggiungo il ristorante alla lista dei ristoranti da essere
            //confermati da un admin, con flag per dire se è anche un claim
            addClaim(user,restaurant,userTextClaim,isClaim?2:0);
            con.commit();
            res=false;
        }
        return res;
    }
    
    private int findCuisine(String cuisine) throws SQLException
    {
        int res = -1;
        try (PreparedStatement st = con.prepareStatement("select * from CUISINES where NAME=?"))
        {
            st.setString(1, cuisine);
            try (ResultSet rs = st.executeQuery())
            {
                if(rs.next())
                    res= rs.getInt("ID");
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    private void addRestaurantXCuisine(int idRest, int cuisine) throws SQLException
    {
        try(PreparedStatement check= con.prepareStatement("SELECT * FROM RESTAURANT_CUISINE "
                + "WHERE ID_RESTAURANT=? AND ID_CUISINE=?");
                PreparedStatement st= con.prepareStatement("INSERT INTO RESTAURANT_CUISINE VALUES(?,?)"))
        {
            check.setInt(1,idRest);
            check.setInt(2, cuisine);
            try(ResultSet rs= check.executeQuery())
            {
                if(!rs.next())
                {
                    st.setInt(1, idRest);
                    st.setInt(2, cuisine);
                    st.executeUpdate();
                }
            }
        }
    }
    
    private void addHourRange(int idRest,HourRange range) throws SQLException
    {
        try(PreparedStatement ins= con.prepareStatement("INSERT INTO OPENING_HOURS_RANGES("
                + "DAY_OF_THE_WEEK,START_HOUR,END_HOUR) VALUES(?,?,?)");
            PreparedStatement ins2= con.prepareStatement("INSERT INTO OPENING_HOURS_RANGE_RESTAURANT"
                    + " VALUES(?,?)"))
        {
            ins.setInt(1, range.getDay());
            ins.setTime(2, range.getStart_hour());
            ins.setTime(3, range.getEnd_hour());
            ins.executeUpdate();
            try(ResultSet st= ins.getGeneratedKeys())
            {
                ins2.setInt(1, idRest);
                ins2.setInt(2, st.getInt(1));
                ins2.executeUpdate();
            }
        }
    }
    
    private void addCoordinate(int idRest, Coordinate cord) throws SQLException
    {
        try(PreparedStatement ins= con.prepareStatement("INSERT INTO COORDINATES("
                + "LATITUDE,LONGITUDE,ADDRESS,CITY,STATE) VALUES(?,?,?,?,?)");
            PreparedStatement ins2= con.prepareStatement("INSERT INTO RESTAURANT_COORDINATE"
                    + " VALUES(?,?)"))
        {
            ins.setDouble(1, cord.getLatitude());
            ins.setDouble(2,  cord.getLongitude());
            ins.setString(3, cord.getAddress());
            ins.setString(4, cord.getCity());
            ins.setString(5, cord.getState());
            ins.executeUpdate();
            try(ResultSet st= ins.getGeneratedKeys())
            {
                ins2.setInt(1, idRest);
                ins2.setInt(2, st.getInt(1));
                ins2.executeUpdate();
            }
        }
    }
    
    private int findClosestPrice(double min,double max) throws SQLException
    {
        int res=-1;
        double minDiff=10000000;
        try(PreparedStatement st= con.prepareStatement("SELECT * FROM PRICE_RANGES"))
        {
            try(ResultSet rs = st.executeQuery())
            {
                while(rs.next())
                {
                    double tmp=abs(rs.getDouble("MIN_VALUE")-min)+abs(rs.getDouble("MAX_VALUE")-max);
                    if(tmp<minDiff)
                    {
                        minDiff=tmp;
                        res=rs.getInt("ID");
                    }
                }
            }
        }
        return res;
    }
    
    /**
     * Metodo per permette all'admin di rimuovere una foto dalle foto reportate.
     * @param id Id della foto.
     * @throws SQLException 
     */
    public void unreportPhoto(int id) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("DELETE FROM REPORTED_PHOTOS WHERE ID=?"))
        {
             st.setInt(1, id);
             st.executeUpdate();
        }
        con.commit();
    }
    
    /**
     * Metodo per permette all'admin di rimuovere una foto che era stata reportata.
     * Questo metodo non rimuove la foto dal filesystem, ma solo il path dal db.
     * @param idPhoto
     * @param idUser
     * @throws SQLException 
     */
    public void removePhoto(int idPhoto, int idUser) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("DELETE FROM PHOTOS WHERE ID=?"))
        {
             Photo photo= getPhotoById(idPhoto);
             unreportPhoto(idPhoto);//rimuovo dalla lista reportati
             st.setInt(1, idPhoto);
             st.executeUpdate();
             if(photo!=null)
                 notifyUser(idUser,"La tua foto "+photo.getName()+ " è stata rimossa perchè non rispettava il nostro regolamento");
        }
        con.commit();
    }
    
    /**
     * Aggiunge la stringa alle notifiche dell'utente, che appariranno poi
     * nelle sue notifiche nella pagina profilo. Queste notifiche non riguardano
     * le notifiche di foto/review etc segnalate che arrivano agli admin.
     * @param idUser L'id dell'utente a cui fare arrivare la notifica.
     * @param notifica La stringa che verrà vista dall'utente come notifica.
     * @throws SQLException 
     */
    public void notifyUser(int idUser,String notifica) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("INSERT INTO NOTIFICATIONS"
                + "(USER_ID,DESCRIPTION) VALUES(?,?)"))
        {
            st.setInt(1, idUser);
            st.setString(2, notifica);
            st.executeUpdate();
        }
    }
    
    /**
     * Metodo per permette all'admin di rimuovere una review dalle review reportate.
     * @param id Id della review.
     * @throws SQLException 
     */
    public void unreportReview(int id) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("DELETE FROM REPORTED_REVIEWS WHERE ID=?"))
        {
             st.setInt(1, id);
             st.executeUpdate();
        }
        con.commit();
    }
    
    /**
     * Metodo per permette all'admin di rimuovere una review che era stata reportata.
     * Il ristorante risulterà avere una review in meno e la media cambiata, e l'utente creatore
     * perderà i like e dislikes a essa associati, e avrà una review in meno.
     * Verrà rimossa la foto dalla tabella foto e da quelle segnalate (se lo è).
     * La review verrà rimossa da quelle segnalate.
     * Verranno aggiornati i like nella tabella user_review_likes.
     * Viene cancellata la reply del ristoratore a questa review.
     * @param idReview
     * @param idUser Id di chi ha fatto la review.
     * @throws SQLException 
     */
    public void removeReview(int idReview, int idUser) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("DELETE FROM REVIEWS WHERE ID=?"))
        {
            unreportReview(idReview);
            Review review= getReviewById(idReview);
            st.setInt(1, idReview);
            st.executeUpdate();
            if(review!=null)
            {
                notifyUser(idUser,"La tua review "+review.getName()+ " è stata rimossa perchè non rispettava il nostro regolamento");
                try(PreparedStatement rm1= con.prepareStatement("UPDATE RESTAURANTS SET GLOBAL_VALUE=((GLOBAL_VALUE"
                        + "*REVIEWS_COUNTER)-?)/(REVIEWS_COUNTER-1),REVIEWS_COUNTER="
                        + "REVIEWS_COUNTER-1 WHERE ID=?");
                    PreparedStatement rm2= con.prepareStatement("UPDATE USERS SET REVIEWS_COUNTER=REVIEWS_COUNTER-1,"
                            + "REVIEWS_POSITIVE=REVIEWS_POSITIVE-?,REVIEWS_NEGATIVE=REVIEWS_NEGATIVE-? WHERE ID=?");
                    PreparedStatement rm3= con.prepareStatement("DELETE FROM PHOTOS WHERE ID=?");
                    PreparedStatement rm4= con.prepareStatement("DELETE FROM REPLIES WHERE ID_REVIEW=?");
                    PreparedStatement rm5= con.prepareStatement("DELETE FROM USER_REVIEW_LIKES WHERE ID_REVIEW=?");)
                {
                    //rm1 aggiorna ristorante
                    //rm2 aggiorna utente
                    //rm3 rimuove la photo della review
                    //rm4 rimuove la reply della review
                    //rm5 rimuove i like della review
                    rm1.setInt(1, review.getGlobal_value());
                    rm1.setInt(2, review.getId_restaurant());
                    rm2.setInt(1, review.getLikes());
                    rm2.setInt(2, review.getDislikes());
                    rm2.setInt(3, idUser);
                    rm3.setInt(1, review.getId_photo());
                    rm4.setInt(1, idReview);
                    rm5.setInt(1, idReview);
                    rm1.executeUpdate();
                    rm2.executeUpdate();
                    rm3.executeUpdate();
                    rm4.executeUpdate();
                    rm5.executeUpdate();
                    unreportPhoto(review.getId_photo());
                }
            }
        }
        con.commit();
    }
    
    /**
     *Per non accettare una reply di un ristoratore. La reply verrà cancellata
     * dal db e l'utente verrà notificato.
     * @param idReply
     * @throws SQLException
     */
    public void unconfirmReply(int idReply) throws SQLException
    {
        ReplyContext context= getReplyContext(idReply);
        try(PreparedStatement rm1= con.prepareStatement("DELETE FROM REPLIES_TO_BE_CONFIRMED WHERE ID=?");
            PreparedStatement rm2= con.prepareStatement("DELETE FROM REPLIES WHERE ID=?"))
        {
            rm1.setInt(1, idReply);
            rm2.setInt(1, idReply);
            rm1.executeUpdate();
            rm2.executeUpdate();
            if(context.getReply()!=null&&context.getReview()!=null&&context.getUser()!=null)
                notifyUser(context.getUser().getId(),"La tua reply alla review "+context.getReview().getName()+
                        " è stata rimossa perchè non rispettava il nostro regolamento.");
            con.commit();
        }
    }
    
    /**
     *Per accettare una reply di un ristoratore. La reply verrà cancellata
     * fra quelle in attesa e validata fra quelle "normali".
     * @param idAdmin L'admin che ha fatto la validazione.
     * @param idReply L'id della reply da validare.
     * @throws SQLException
     */
    public void confirmReply(int idReply,int idAdmin) throws SQLException
    {
        ReplyContext context= getReplyContext(idReply);
        try(PreparedStatement rm1= con.prepareStatement("DELETE FROM REPLIES_TO_BE_CONFIRMED WHERE ID=?");
            PreparedStatement rm2= con.prepareStatement("UPDATE REPLIES SET DATE_VALIDATION=?,VALIDATED=TRUE,ID_VALIDATOR=? WHERE ID=?"))
        {
            rm1.setInt(1, idReply);
            rm2.setTimestamp(1,new Timestamp(Calendar.getInstance().getTime().getTime()));
            rm2.setInt(2, idAdmin);
            rm2.setInt(3, idReply);
            rm1.executeUpdate();
            rm2.executeUpdate();
            if(context.getReply()!=null&&context.getReview()!=null&&context.getUser()!=null)
                notifyUser(context.getUser().getId(),"La tua reply alla review "+context.getReview().getName()+
                        " è stata accettata.");
            con.commit();
        }
    }
    
    /**
     * Per fare in modo che un utente segni una notifica come vista, e venga quindi eliminata
     * in modo da non disturbarlo più.
     * @param idNotification L'id della notifica.
     * @param user Serve lo user preso dalla sessione in modo che uno user non possa 
     * "forgiare" l'http request e mettersi a eliminare notifiche degli altri.
     * @throws SQLException 
     */
    public void acceptNotification(int idNotification,User user) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("DELETE FROM NOTIFICATIONS WHERE ID=? AND USER_ID=?"))
        {
             st.setInt(1, idNotification);
             st.setInt(2, user.getId());
             st.executeUpdate();
        }
        con.commit();
    }
    
    /**
     * Per fare in modo che la creazione o il claim di un ristorante venga accettato.
     * @param idUser
     * @param idRestaurant
     * @throws SQLException 
     */
    public void acceptRestaurantRequest(int idUser, int idRestaurant) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("DELETE FROM RESTAURANTS_"
                + "REQUESTS WHERE ID_USER=? AND ID_RESTAURANT=?");
            PreparedStatement valid= con.prepareStatement("UPDATE RESTAURANTS SET "
                    + "ID_OWNER=?,VALIDATED=TRUE WHERE ID=?");
            PreparedStatement updateUser= con.prepareStatement("UPDATE USERS SET "
                    + "USERTYPE=1 WHERE ID=? AND USERTYPE=0"))
        {
            int type=requestType(idUser,idRestaurant);
            Restaurant restaurant= getRestaurantById(idRestaurant);
            if(type!=-1&&restaurant!=null)
            {
                st.setInt(1, idUser);
                st.setInt(2, idRestaurant);
                if(type==1||type==2)//se è un claim o creazione+claim setto l'owner
                    valid.setInt(1, idUser);
                else
                    valid.setInt(1, -1);
                valid.setInt(2, idRestaurant);
                st.executeUpdate();
                valid.executeUpdate();
                String tmp;
                updateUser.setInt(1, idUser);
                switch (type)
                {
                    case 0:
                        tmp="creazione";
                        break;
                    case 1:
                        tmp="proprietà";
                        updateUser.executeUpdate();
                        break;
                    default:
                        tmp="creazione e proprietà";
                        updateUser.executeUpdate();
                        break;
                }
                notifyUser(idUser,"La tua richiesta di "+tmp+" del ristorante "+restaurant.getName()
                    +" è stata accettata.");
                
            }
            
            con.commit();
        }
    }
    
    /**
     * L'admin nega la creazione o il claim di un ristorante.
     * @param idUser L'utente che ha fatto la richiesta.
     * @param idRestaurant Il ristorante in questione.
     * @throws SQLException 
     */
    public void denyRestaurantRequest(int idUser, int idRestaurant) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("DELETE FROM RESTAURANTS_"
                + "REQUESTS WHERE ID_USER=? AND ID_RESTAURANT=?"))
        {
            int type=requestType(idUser,idRestaurant);
            Restaurant restaurant= getRestaurantById(idRestaurant);
            if(type!=-1&&restaurant!=null)
            {
                st.setInt(1, idUser);
                st.setInt(2, idRestaurant);
                st.executeUpdate();
                String tmp=null;
                switch (type)
                {
                    case 0:
                        tmp="creazione";
                        removeRestaurant(idRestaurant);
                        break;
                    case 1:
                        tmp="proprietà";
                        break;
                    default:
                        tmp="creazione e prorietà";
                        removeRestaurant(idRestaurant);
                        break;
                }
                notifyUser(idUser,"La tua richiesta di "+tmp+" del ristorante "+restaurant.getName()
                    +" non è stata accettata.");
            }
            con.commit();
        }
    }
    
    private void removeRestaurant(int idRestaurant) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("DELETE FROM RESTAURANTS WHERE ID=?"))
        {
            st.setInt(1, idRestaurant);
            removeRestaurantHourRange(idRestaurant);
            removeRestaurantCuisine(idRestaurant);
            removeRestaurantCoordinate(idRestaurant);
            st.executeUpdate();
        }
    }
    
    private void removeRestaurantHourRange(int idRestaurant) throws SQLException
    {
        try(PreparedStatement st1= con.prepareStatement("DELETE FROM "
                + "OPENING_HOURS_RANGE_RESTAURANT WHERE ID_RESTAURANT=?");
            PreparedStatement st2= con.prepareStatement("DELETE FROM "
                    + "OPENING_HOURS_RANGES WHERE ID NOT IN "
                    + "(SELECT ID_OPENING_HOURS_RANGE FROM OPENING_HOURS_RANGE_RESTAURANT"))
        {
            st1.setInt(1, idRestaurant);
            st1.executeUpdate();
            st2.executeUpdate();
        }
    }
    
    private void removeRestaurantCuisine(int idRestaurant) throws SQLException
    {
        try(PreparedStatement st= con.prepareStatement("DELETE FROM "
                + "RESTAURANT_CUISINE WHERE ID_RESTAURANT=?"))
        {
            st.setInt(1, idRestaurant);
        }
    }

     private void removeRestaurantCoordinate(int idRestaurant) throws SQLException
    {
        try(PreparedStatement st1= con.prepareStatement("DELETE FROM "
                + "RESTAURANT_COORDINATE WHERE ID_RESTAURANT=?");
            PreparedStatement st2= con.prepareStatement("DELETE FROM "
                    + "COORDINATES WHERE ID NOT IN "
                    + "(SELECT ID_COORDINATE FROM RESTAURANT_COORDINATE"))
        {
            st1.setInt(1, idRestaurant);
            st1.executeUpdate();
            st2.executeUpdate();
        }
    }
     
    private int requestType(int idUser, int idRestaurant) throws SQLException
    {
        int res=-1;
        try(PreparedStatement st= con.prepareStatement("SELECT * FROM RESTAURANTS_"
                + "REQUESTS WHERE ID_USER=? AND ID_RESTAURANT=?"))
        {
            st.setInt(1, idUser);
            st.setInt(2, idRestaurant);
            try(ResultSet rs= st.executeQuery())
            {
                if(rs.next())
                    res=rs.getInt("CREATION_CLAIM_BOTH_FLAG");
            }
        }
        return res;
    }
    
    /**
     * Permette la modifica di un ristorante da parte di un utente.
     * @param restaurant L'oggetto ristorante, serve che nome, id, descrizione e url siano
     * giusti (in caso vengano cambiati dall'utente), il resto viene passato 
     * tramite gli altri argomenti o trovato nel db.
     * @param cucine Nuove cucine rappresentate da stringhe.
     * @param coordinate Nuove coordinate.
     * @param range Nuovi orari (7 HourRange messi in ArrayList)-
     * @param min Nuovo prezzo minimo.
     * @param max Il nuovo prezzo massimo.
     * @return true se ci sono stati problemi, falso altrimenti
     * @throws SQLException 
     */
    public boolean modifyRestaurant(Restaurant restaurant,ArrayList<String> cucine,Coordinate coordinate,ArrayList<HourRange> range, double min, double max) throws SQLException
    {
        boolean res=true;
        try(PreparedStatement st= con.prepareStatement("UPDATE RESTAURANTS SET "
                + "NAME=?,DESCRIPTION=?,WEB_SITE_URL=?,GLOBAL_VALUE=?,ID_OWNER=?,"
                + "ID_CREATOR=?,ID_PRICE_RANGE=?,REVIEWS_COUNTER=?,VALIDATED=? "
                + "WHERE ID=?"))
        {
            Restaurant restaurantCheck= getRestaurantById(restaurant.getId());
            //controllo se il prorietario è lo stesso che fa la richiesta
            if(restaurantCheck!=null&&restaurantCheck.getId_owner()==restaurant.getId_owner())
            {
                st.setString(1, restaurant.getName());
                st.setString(2, restaurant.getDescription());
                st.setString(3, restaurant.getWeb_site_url());
                st.setInt(4, restaurantCheck.getGlobal_value());
                st.setInt(5, restaurantCheck.getId_owner());
                st.setInt(6, restaurantCheck.getId_creator());
                st.setInt(7, findClosestPrice(min,max));
                st.setInt(8, restaurantCheck.getReviews_counter());
                st.setBoolean(9, restaurantCheck.isValidated());
                st.executeUpdate();
                //per ogni cucina aggiunto la relazione, se quella cucina esiste nel nostro db
                //dopo aver pulito quelle prima
                removeRestaurantCuisine(restaurant.getId());
                for(int i=0;i<cucine.size();i++)
                {
                    int cuisineId= findCuisine(cucine.get(i));
                    if(cuisineId!=-1)
                    {
                        addRestaurantXCuisine(restaurant.getId(),cuisineId);
                    }
                }
                //pulisco e riaggiungo coordinate
                removeRestaurantCoordinate(restaurant.getId());
                addCoordinate(restaurant.getId(),coordinate);
                //pulisco e aggiungo gli orari
                removeRestaurantHourRange(restaurant.getId());
                for(int i=0;i<range.size();i++)
                    addHourRange(restaurant.getId(),range.get(i));
                con.commit();
            }
        }
        return res;
    }
    
    /**
     * Per settare la foto dello user. 
     * Non viene controllato prima se lo user esiste perchè si assume che sia
     * preso dalla sessione e quindi corretto.
     * @param userId Id dell utente.
     * @param email La nuova email.
     * @return false se non ci sono stati problemi ,vero altrimenti
     * @throws SQLException 
     */
    public boolean modifyUserEmail(int userId, String email) throws SQLException
    {
        boolean res=true;
        try(PreparedStatement checkEmail=con.prepareStatement("SELECT * FROM USERS WHERE EMAIL=?");
            PreparedStatement st= con.prepareStatement("UPDATE USERS SET EMAIL=? WHERE ID=?"))
        {
            checkEmail.setString(1,email);
            try(ResultSet rs= checkEmail.executeQuery())
            {
                if(!rs.next())
                {
                    st.setString(1, email);
                    st.setInt(2, userId);
                    con.commit();
                    res=false;
                }
            }
        }
        return res;
    }
    
    /**
     * Permette all'utente di modificare nome e cognome. 
     * @param user L'utente in questione, id, nome e cognome devono essere 
     * corretti, il resto non importa.
     * @return True se non è andato a buon fine, falso altrimenti.
     * @throws SQLException 
     */
    public boolean modifyUserNameSurname(User user) throws SQLException
    {
        boolean res=true;
        try(PreparedStatement st= con.prepareStatement("UPDATE USERS SET NAME=?,SURNAME=? WHERE ID=?"))
        {
            st.setString(1, user.getName());
            st.setString(2,user.getSurname());
            st.setInt(3, user.getId());
            con.commit();
            res=false;
        }
        return res;
    }
    
    /**
     * Per modificare la password di uno user.
     * @param userId L'id dell'utente.
     * @param password La nuova password.
     * @return
     * @throws SQLException 
     */
    public boolean modifyUserPassword(int userId,String password) throws SQLException
    {
        boolean res=true;
        try(PreparedStatement st= con.prepareStatement("UPDATE USERS SET PASSWORD=? WHERE ID=?"))
        {
            st.setString(1,BCrypt.hashpw(password, BCrypt.gensalt()));
            st.setInt(2, userId);
            con.commit();
            res=false;
        }
        return res;
    }
    
    /**
     * Metodo per l'autocomplete, torna (al max) 5 nomi del tipo LIKE term%.
     * @param term La stringa cui cercare.
     * @return Un ArrayList di nomi LIKE term
     * @throws SQLException 
     */
    public ArrayList<String> autoCompleteName(String term) throws SQLException
    {
        ArrayList<String> res=new ArrayList();
        try(PreparedStatement st= con.prepareStatement("SELECT NAME FROM RESTAURANTS WHERE"
                + " upper(NAME) like upper('?%') FETCH FIRST 5 ROWS ONLY"))
        {
            st.setString(1, term);
            try(ResultSet rs= st.executeQuery())
            {
                while(rs.next())
                {
                    res.add(rs.getString("NAME"));
                }
            }
        }
        return res;
    }
    
    /**
     * Restituisce un ArrayList di id (int) di ristoranti che hanno indirizzo,
     * città o stato pari a location.
     * @param location
     * @return
     * @throws SQLException 
     */
    public ArrayList<Integer> getRestIdsFromLocation(String location) throws SQLException
    {
        ArrayList<Integer> res= new ArrayList();
        try(PreparedStatement st= con.prepareStatement("SELECT RESTAURANTS.ID " +
        "FROM " +
        "(select RESTAURANT_COORDINATE.ID_RESTAURANT " +
        "FROM " +
        "(select ID FROM COORDINATES WHERE ADDRESS=? OR CITY=? OR STATE=?) IDCord, RESTAURANT_COORDINATE " +
        "WHERE IDCORD.ID=RESTAURANT_COORDINATE.ID_COORDINATE) RISTO, RESTAURANTS " +
        "WHERE RISTO.ID_RESTAURANT=RESTAURANTS.ID " +
        ""))
        {
            st.setString(1, location);
            st.setString(2, location);
            st.setString(3, location);
            try(ResultSet rs= st.executeQuery())
            {
                while(rs.next())
                    res.add(rs.getInt("ID"));
            }
        }
        return res;
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
