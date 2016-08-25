package database;

import utility.BCrypt;
import database.contexts.AttemptContext;
import database.contexts.OwnUserContext;
import database.contexts.ReplyContext;
import database.contexts.ReviewContext;
import database.contexts.PhotoContext;
import database.contexts.RestaurantContext;
import info.debatty.java.stringsimilarity.KShingling;
import info.debatty.java.utils.SparseIntegerVector;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
import java.sql.Blob;
import java.util.Collections;
import java.util.UUID;
import static java.lang.Math.abs;

/**
 *
 * @author jacopo
 */
public class DbManager implements Serializable
{

    private final static int USER_NOTIFICATIONS_TO_GET = 5;
    private final static double SIMILARITY_THRESHOLD = 0.36;
    private transient Connection con;
    private KShingling kshingling;

    public DbManager(final String url) throws ClassNotFoundException, SQLException, IOException
    {
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        con = DriverManager.getConnection(url);
        con.setAutoCommit(false);
        kshingling = getKShingling();
        /*if(kshingling==null)
        {
            System.out.println("no shingling found");
            kshingling= new KShingling(3);
        }
        else*/
        System.out.println("shingling found");
        //per testing
        kshingling = new KShingling(3);
        clearProfiles();
        createAllProfiles();
    }

    /**
     * Prova ad inserire(registrare sul sito) un utente nel database, l'utente
     * così registrato sarà sempre un utente normale (non ristoratore e non
     * admin). L'id dell'oggetto user passato sarà settato come l'id generato
     * dal db per l inserimento del record, in caso di successo.
     *
     * @param user Oggetto User con i dati all'iterno
     * @return un numero positivo che rappresenta l'id dell'utente se è andata a
     * buon fine, -1 se nn ha registrato xk esiste un utente con quella email (o
     * sia email e nick uguali), -2 se esiste un utente con quel nick
     * @throws java.sql.SQLException
     *
     */
    public int registerUser(final User user) throws SQLException
    {
        int res = -3;
        try (PreparedStatement st = con.prepareStatement("INSERT INTO USERS(NAME,SURNAME,"
                + "NICKNAME,EMAIL,PASSWORD,AVATAR_PATH,REVIEWS_COUNTER,REVIEWS_POSITIVE,"
                + "REVIEWS_NEGATIVE,USERTYPE,VERIFIED) values(?,?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS))
        {
            if (findUserByEmail(user.getEmail()))
            {
                res = -1;
            }
            else if (findUserByNickname(user.getNickname()))
            {
                res = -2;
            }
            else
            {
                st.setString(1, user.getName());
                st.setString(2, user.getSurname());
                st.setString(3, user.getNickname());
                st.setString(4, user.getEmail());
                //cappo la lunghezza della psw hashata per non superare la lunghezza massima impostata nel db
                st.setString(5, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
                st.setString(6, user.getAvatar_path());
                st.setInt(7, 0);
                st.setInt(8, 0);
                st.setInt(9, 0);
                st.setInt(10, 0);
                st.setBoolean(11, false);
                st.executeUpdate();
                try (ResultSet rs = st.getGeneratedKeys())
                {
                    if (rs.next())
                    {
                        user.setId(rs.getInt(1));
                        res = user.getId();
                    }
                    else
                    {
                        throw new SQLException("no generated key from user registration");
                    }
                }
                try (PreparedStatement st2 = con.prepareStatement("INSERT INTO USERS_TO_VERIFY(ID,TOKEN)"
                        + " VALUES(?,?)"))
                {
                    st2.setInt(1, user.getId());
                    st2.setString(2, BCrypt.hashpw(UUID.randomUUID().toString(), BCrypt.gensalt()));
                    st2.executeUpdate();
                    con.commit();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Metodo per rimuovere l'utente dalla tabella utente e da quella degli
     * utenti in attesa di verifica. (usato nella servlet registrazione utenti
     * in caso non vadi a buon fine la spedizione della email)
     *
     * @param id_user Id dell'utente.
     * @throws SQLException
     */
    public void unregisterUser(final int id_user) throws SQLException
    {
        try (PreparedStatement st2 = con.prepareStatement("DELETE FROM USERS WHERE ID=?");
                PreparedStatement st1 = con.prepareStatement("DELETE FROM USERS_TO_VERIFY WHERE ID=?"))
        {
            st1.setInt(1, id_user);
            st2.setInt(1, id_user);
            st1.executeUpdate();
            st2.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Setta un utente come admin, cambiandone lo USERTYPE nel db, che diventa
     * 2.
     *
     * @param id_user L'id dell'utente.
     * @throws SQLException
     */
    public void setUserToAdmin(final int id_user) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE USERS SET USERTYPE=2 WHERE ID=?"))
        {
            st.setInt(1, id_user);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Setta un utente come ristoratore, cambiandone lo USERTYPE nel db, che
     * diventa 1.
     *
     * @param id_user L'id dell'utente.
     * @throws SQLException
     */
    public void setUserToRestaurantOwner(final int id_user) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE USERS SET USERTYPE=1 WHERE ID=?"))
        {
            st.setInt(1, id_user);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Setta un utente come utente normale (no ristoratore no admin),
     * cambiandone lo USERTYPE nel db, che diventa 0.
     *
     * @param id_user L'id dell'utente.
     * @throws SQLException
     */
    public void setUserToDefaultUser(final int id_user) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE USERS SET USERTYPE=0 WHERE ID=?"))
        {
            st.setInt(1, id_user);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Metodo per avere il token di verifica corrispondente ad una certa email,
     * e quindi ad un utente.
     *
     * @param id Dell'utente di cui si cerca il token
     * @return Un token alfanumerico, null se non si ha trovato nulla.
     * @throws SQLException
     */
    public String getUserVerificationToken(final int id) throws SQLException
    {
        String res = null;
        try (PreparedStatement st = con.prepareStatement("SELECT TOKEN FROM USERS_TO_VERIFY WHERE ID=?"))
        {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    res = rs.getString("TOKEN");
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Verifica un utente, togliendolo dalla tabella USERS_TO_VERIFY e settando
     * la voce VERIFIED a true nella tabella USERS.
     *
     * @param id Id dell'utente da verificare.
     * @param token Il token alfanumerico fornito dall'utente.
     * @return True se la verifica è andata a buon fine, falso altrimenti.
     * @throws SQLException
     */
    public boolean verifyUser(final int id, String token) throws SQLException
    {
        boolean res = false;
        try (PreparedStatement st1 = con.prepareStatement("SELECT TOKEN FROM USERS_TO_VERIFY WHERE ID=?"))
        {
            st1.setInt(1, id);
            try (ResultSet rs = st1.executeQuery())
            {
                if (rs.next())
                {
                    String readToken = rs.getString("TOKEN");
                    if (readToken.equals(token))
                    {
                        try (PreparedStatement st2 = con.prepareStatement("DELETE FROM USERS_TO_VERIFY WHERE ID=?");
                                PreparedStatement st3 = con.prepareStatement("UPDATE USERS SET VERIFIED=TRUE WHERE ID=?"))
                        {
                            st2.setInt(1, id);
                            st3.setInt(1, id);
                            st2.executeUpdate();
                            st3.executeUpdate();
                            con.commit();
                            res = true;
                        }
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Inserisce l'utente in una tabella di utenti che sono in fase di cambio
     * password.
     *
     * @param id_user L'id dell'utente.
     * @return Una stringa che rappresenta il token di cambio password
     * dell'utente.
     * @throws SQLException
     */
    public String addToUsersToChangePassword(final int id_user) throws SQLException
    {
        String uuid = BCrypt.hashpw(UUID.randomUUID().toString(), BCrypt.gensalt());
        try (PreparedStatement del = con.prepareStatement("DELETE FROM USERS_TO_CHANGE_PSW WHERE ID=?");
                PreparedStatement st = con.prepareStatement("INSERT INTO USERS_TO_CHANGE_PSW(ID,TOKEN)"
                        + " VALUES(?,?)"))
        {
            del.setInt(1, id_user);
            del.executeUpdate();
            st.setInt(1, id_user);
            st.setString(2, uuid);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return uuid;
    }

    /**
     * Recupera l'id dell'utente in base al password token.
     *
     * @param token Il token per il cambio password.
     * @return L'id dell'utente, -1 se non c'è un id associato a questo token.
     * @throws SQLException
     */
    private int getIdFromPasswordToken(final String token) throws SQLException
    {
        int res = -1;
        try (PreparedStatement st = con.prepareStatement("SELECT ID FROM USERS_TO_CHANGE_PSW WHERE TOKEN=?"))
        {
            st.setString(1, token);
            ResultSet rs = st.executeQuery();
            if (rs.next())
            {
                res = rs.getInt("ID");
            }
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Verifica che la coppia (id, token di verifica) sia valida, ed elimina
     * l'utente (l'id) dalla tabella di USERS_TO_CHANGE_PSW (utenti in fase di
     * cambio psw).
     *
     * @param id_user L'id dell'utente.
     * @param token Il token di cambio password.
     * @return True se la verifica è andata a buon fine e la coppia è valida,
     * falso altrimenti.
     * @throws SQLException
     */
    public boolean verifyPasswordChangeToken(final int id_user, final String token) throws SQLException
    {
        boolean res = false;
        try (PreparedStatement del = con.prepareStatement("DELETE FROM USERS_TO_CHANGE_PSW WHERE ID=?"))
        {
            /*
            Se esiste un id corrispondente al token (quindi !=-1) e se
            l'id fornito come argomento coincide con quello delle tabella allora
            procedo.
             */
            int idFromTable = getIdFromPasswordToken(token);
            if (idFromTable != -1 && id_user == idFromTable)
            {
                del.setInt(1, id_user);
                del.executeUpdate();
                con.commit();
                res = true;
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Per autenticare uno User, di cui si fornisce nick/mail e password.
     *
     * @param nickOrEmail nick o email dell utente che fa login
     * @param password password
     * @return null se la password non matcha o se l'utente non esiste, uno User
     * con password "pulita via" e verified a falso se non ha verificato via
     * mail, uno User con psw pulita e verified a vero se la psw matcha ed è
     * verificato
     * @throws java.sql.SQLException
     */
    public User loginUserByEmailOrNickname(final String nickOrEmail, final String password) throws SQLException
    {

        User user = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM USERS WHERE EMAIL=? OR NICKNAME=?"))
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
                    user.setVerified(rs.getBoolean("VERIFIED"));

                    if (!BCrypt.checkpw(password, user.getPassword()))
                    {
                        user = null;
                    }
                    else
                    {
                        user.setPassword("placeholder");
                    }
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return user;
    }

    /**
     * Cerca se esiste già un utente con quella email. Non fa commit perchè è un
     * metodo privato usato come parte di metodi più grandi.
     *
     * @param email L'email dell'utente.
     * @return Vero se esiste già, falso altrimenti.
     * @throws SQLException
     */
    private boolean findUserByEmail(final String email) throws SQLException
    {
        boolean res = true;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM USERS WHERE EMAIL=?"))
        {
            st.setString(1, email);
            try (ResultSet rs = st.executeQuery())
            {
                res = rs.next();
            }
        }
        return res;
    }

    /**
     * Cerca se esiste già un utente con quel nickname. Non fa commit perchè è
     * un metodo privato usato come parte di metodi più grandi.
     *
     * @param nick Il nickname dell'utente.
     * @return Vero se esiste già, falso altrimenti.
     * @throws SQLException
     */
    private boolean findUserByNickname(final String nick) throws SQLException
    {
        boolean res = true;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM USERS WHERE NICKNAME=?"))
        {
            st.setString(1, nick);
            try (ResultSet rs = st.executeQuery())
            {
                res = rs.next();
            }
        }
        return res;
    }

    /**
     * Restituisce le review di un utente.
     *
     * @param id_user L'id dell''utente di cui cercare le reviews.
     * @return Un ArrayList di oggetti Review dell'utente.
     * @throws java.sql.SQLException
     */
    public ArrayList<Review> getUserReviews(final int id_user) throws SQLException
    {
        ArrayList<Review> reviews = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM REVIEWS WHERE ID_CREATOR=?"))
        {
            st.setInt(1, id_user);
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
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return reviews;
    }

    /**
     * Restituisce i contesti delle review di un utente.
     *
     * @param id_user L'id dell''utente di cui cercare le reviews.
     * @return Un ArrayList di oggetti ReviewContext dell'utente.
     * @throws java.sql.SQLException
     */
    public ArrayList<ReviewContext> getUserReviewContext(final int id_user) throws SQLException
    {
        ArrayList<ReviewContext> reviews = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT ID FROM REVIEWS WHERE ID_CREATOR=?"))
        {
            st.setInt(1, id_user);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    ReviewContext reviewContext = getReviewContext(rs.getInt("ID"));
                    reviews.add(reviewContext);
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return reviews;
    }

    /**
     * Le notifiche di foto di un utente ristoratore, non le notifiche di task
     * da svolgere per un admin.
     *
     * @param id_user L'id dell'utente di cui si vuole cercare le notifiche di
     * foto.
     * @param many Quante notifiche al max recuperare.
     * @return Un ArrayList contenente al max many oggetti di tipo
     * PhotoNotification.
     * @throws SQLException
     */
    public ArrayList<PhotoNotification> getUserPhotoNotifications(final int id_user, final int many) throws SQLException
    {
        ArrayList<PhotoNotification> notifications = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM PHOTO_NOTIFICATIONS WHERE ID_USER=? ORDER BY CREATION DESC FETCH FIRST ? ROWS ONLY"))
        {
            st.setInt(1, id_user);
            st.setInt(2, many);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Photo photo = getPhotoById(rs.getInt("ID_PHOTO"));
                    PhotoNotification notification = new PhotoNotification();
                    notification.setId(rs.getInt("ID"));
                    notification.setPhoto(photo);
                    notification.setRestaurant_name(getRestaurantById(photo.getId_restaurant()).getName());
                    notification.setCreation(rs.getTimestamp("CREATION"));
                    notification.setUser(getUserById(id_user));
                    notifications.add(notification);
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return notifications;
    }

    /**
     * Recupera tutte le notifiche di foto di un utente ristoratore, non le
     * notifiche di task da svolgere per un admin.
     *
     * @param id_user L'id dell'utente di cui si vuole cercare le notifiche di
     * foto.
     * @return Un ArrayList contenente oggetti di tipo PhotoNotification.
     * @throws SQLException
     */
    public ArrayList<PhotoNotification> getAllUserPhotoNotifications(final int id_user) throws SQLException
    {
        ArrayList<PhotoNotification> notifications = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM PHOTO_NOTIFICATIONS WHERE ID_USER=? ORDER BY CREATION DESC"))
        {
            st.setInt(1, id_user);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Photo photo = getPhotoById(rs.getInt("ID_PHOTO"));
                    PhotoNotification notification = new PhotoNotification();
                    notification.setId(rs.getInt("ID"));
                    notification.setPhoto(photo);
                    notification.setRestaurant_name(getRestaurantById(photo.getId_restaurant()).getName());
                    notification.setCreation(rs.getTimestamp("CREATION"));
                    notification.setUser(getUserById(id_user));
                    notifications.add(notification);
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return notifications;
    }

    /**
     * Le notifiche di review di un utente ristoratore, non le notifiche di task
     * da svolgere per un admin.
     *
     * @param id_user L'id dell'utente di cui si vuole cercare le notifiche di
     * review.
     * @param many Quante notifiche al max recuperare.
     * @return Un ArrayList contenente al max many oggetti di tipo
     * ReviewNotification.
     * @throws SQLException
     */
    public ArrayList<ReviewNotification> getUserReviewNotifications(final int id_user, final int many) throws SQLException
    {
        ArrayList<ReviewNotification> notifications = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM REVIEW_NOTIFICATIONS WHERE ID_USER=? ORDER BY CREATION DESC FETCH FIRST ? ROWS ONLY"))
        {
            st.setInt(1, id_user);
            st.setInt(2, many);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Review review = getReviewById(rs.getInt("ID_REVIEW"));
                    ReviewNotification notification = new ReviewNotification();
                    notification.setId(rs.getInt("ID"));
                    notification.setReview(review);
                    notification.setRestaurant_name(getRestaurantById(review.getId_restaurant()).getName());
                    notification.setCreation(rs.getTimestamp("CREATION"));
                    notification.setUser(getUserById(id_user));
                    notifications.add(notification);
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return notifications;
    }

    /**
     * Recupera tutte le notifiche di review di un utente ristoratore, non le
     * notifiche di task da svolgere per un admin.
     *
     * @param id_user L'id dell'utente di cui si vuole cercare le notifiche di
     * review.
     * @return Un ArrayList contenente oggetti di tipo ReviewNotification.
     * @throws SQLException
     */
    public ArrayList<ReviewNotification> getAllUserReviewNotifications(final int id_user) throws SQLException
    {
        ArrayList<ReviewNotification> notifications = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM REVIEW_NOTIFICATIONS WHERE ID_USER=? ORDER BY CREATION DESC"))
        {
            st.setInt(1, id_user);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Review review = getReviewById(rs.getInt("ID_REVIEW"));
                    ReviewNotification notification = new ReviewNotification();
                    notification.setId(rs.getInt("ID"));
                    notification.setReview(review);
                    notification.setRestaurant_name(getRestaurantById(review.getId_restaurant()).getName());
                    notification.setCreation(rs.getTimestamp("CREATION"));
                    notification.setUser(getUserById(id_user));
                    notifications.add(notification);
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return notifications;
    }

    /**
     * Restituisce un numero di contesti, una classe formata da una review, uno
     * user e una reply; lo user è chi ha fatto la reply, la review è ciò a cui
     * la reply vuole rispondere. Da usare p.e. per mostrare ad un admin il
     * contesto di una reply per decidere se confermarla o no. Il timestamp di
     * un contesto "prelevato" viene refrehsato al timestamp attuale in modo che
     * non sarà prelevabile da altri admin per 1 ora.
     *
     * @param many Quanti contesti (al max) da ricevere, many deve essere
     * maggiore o uguale a 1 o causerà una eccezione.
     * @return Un ArrayList di contesti.
     * @throws SQLException
     */
    public ArrayList<ReplyContext> getRepliesToBeConfirmed(final int many) throws SQLException
    {
        ArrayList<ReplyContext> contesti = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT ID FROM REPLIES_TO_BE_CONFIRMED"
                + " WHERE {fn TIMESTAMPDIFF( SQL_TSI_HOUR, DATE_ADMIN_TOOK,?)} >= 1 "
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
                    updateRepliesToBeConfirmedDate(rs.getInt("ID"), timestamp);
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return contesti;
    }

    /**
     * Recupera tutti i contesti di reply dei ristoratori che non sono ancora
     * state processate, e quindi presenti nel sistema. Non modifica il
     * timestamp di date_admin_took, e quindi non funge da "prenotazione" di una
     * richiesta da processare.
     *
     * @return Un ArrayList contenente tutte le richieste(i contesti) di reply
     * da processare.
     * @throws SQLException
     */
    public ArrayList<ReplyContext> getAllRepliesToBeConfirmed() throws SQLException
    {
        ArrayList<ReplyContext> contesti = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT ID FROM REPLIES_TO_BE_CONFIRMED"
                + " ORDER BY DATE_ADMIN_TOOK ASC"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    contesti.add(getReplyContext(rs.getInt("ID")));
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return contesti;
    }

    /**
     * Recupera il contesto di una reply, che comprende reply, la review
     * relativa alla reply, e lo user che ha fatto la reply. Se la reply non
     * esiste allora Review e User del contesto saranno null, ma possono essere
     * anche se la reply esiste ma non è possibile trovare uno User e una Review
     * a partire dagli id contenuti nella reply.
     *
     * @param id_reply Id della reply.
     * @return Un oggetto ReplyContext.
     * @throws SQLException
     */
    private ReplyContext getReplyContext(final int id_reply) throws SQLException
    {
        ReplyContext contesto = new ReplyContext();
        contesto.setReply(getReplyById(id_reply));
        if (contesto.getReply() != null)
        {
            contesto.setReview(getReviewById(contesto.getReply().getId_review()));
            contesto.setUser(getUserById(contesto.getReply().getId_owner()));
        }
        else
        {
            contesto.setReview(null);
            contesto.setUser(null);
        }
        return contesto;
    }

    /**
     * Recupera una Reply a partire dal suo id.
     *
     * @param id_reply Id della reply.
     * @return Un oggetto Reply, null se non esiste con quell'id.
     * @throws SQLException
     */
    private Reply getReplyById(final int id_reply) throws SQLException
    {
        Reply reply = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM REPLIES WHERE ID=? AND VALIDATED=TRUE"))
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
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return reply;
    }

    /**
     * Recupera una Reply a partire dall'id della review relative alla reply.
     *
     * @param id_review Id della review a cui la reply risponde.
     * @return Un oggetto Reply, null se non esiste nulla relativo a quella
     * review.
     * @throws SQLException
     */
    private Reply getReplyByIdReview(final int id_review) throws SQLException
    {
        Reply reply = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM REPLIES WHERE ID_REVIEW=? AND VALIDATED=TRUE"))
        {
            st.setInt(1, id_review);
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    reply = new Reply();
                    reply.setId(rs.getInt("ID"));
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
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return reply;
    }

    /**
     * Recupera una review a partire dal suo id.
     *
     * @param id_review L'id della review da recuperare.
     * @return Un oggetto Review, null se non esiste con quell'id.
     * @throws SQLException
     */
    private Review getReviewById(final int id_review) throws SQLException
    {
        Review review = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM REVIEWS WHERE ID=?"))
        {
            st.setInt(1, id_review);
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
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return review;
    }

    /**
     * Recupera un utente a partire dal suo id.
     *
     * @param id_user Id dell'utente da recuperare.
     * @return Uno user con la password rimossa, null se non esiste con
     * quell'id.
     * @throws SQLException
     */
    public User getUserById(final int id_user) throws SQLException
    {
        User user = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM USERS WHERE ID=?"))
        {
            st.setInt(1, id_user);
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
                    user.setVerified(rs.getBoolean("VERIFIED"));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return user;
    }

    /**
     * Recupera un utente a partire dalla sua email.
     *
     * @param email Email dell'utente.
     * @return Uno user con la password rimossa, null se non esiste con
     * quella email.
     * @throws SQLException
     */
    public User getUserByEmail(final String email) throws SQLException
    {
        User user = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM USERS WHERE EMAIL=?"))
        {
            st.setString(1, email);
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
                    user.setVerified(rs.getBoolean("VERIFIED"));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return user;
    }
    
    /**
     * Funzione per prendere una serie di photo segnalate e i loro contesti,
     * solitamente usato per l'admin. Un contesto è la foto più lo user che lha
     * postata. Una volta che un contesto è prelevato (restituito) non sarà
     * possibile restituirlo ancora tramite questo metodo per un ora.
     *
     * @param many Quanti contesti (al max) da ricevere, many deve essere
     * maggiore o uguale a 1 o causerà una eccezione.
     * @return Un ArrayList di contesti.
     * @throws SQLException
     */
    public ArrayList<PhotoContext> getReportedPhotos(final int many) throws SQLException
    {
        ArrayList<PhotoContext> contesti = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM REPORTED_PHOTOS"
                + " WHERE {fn TIMESTAMPDIFF( SQL_TSI_HOUR, DATE_ADMIN_TOOK,?)} >= 1 "
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
                    updateReportedPhotoDate(rs.getInt("ID"), timestamp);
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return contesti;
    }

    private void updateReportedPhotoDate(final int id_photo, final Timestamp timestamp) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE REPORTED_PHOTOS "
                + "SET DATE_ADMIN_TOOK=? WHERE ID=?"))
        {
            st.setTimestamp(1, timestamp);
            st.setInt(2, id_photo);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    private void updateReportedReviewDate(final int id_review, final Timestamp timestamp) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE REPORTED_REVIEWS "
                + "SET DATE_ADMIN_TOOK=? WHERE ID=?"))
        {
            st.setTimestamp(1, timestamp);
            st.setInt(2, id_review);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    private void updateRepliesToBeConfirmedDate(final int id_reply, final Timestamp timestamp) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE REPLIES_TO_BE_CONFIRMED "
                + "SET DATE_ADMIN_TOOK=? WHERE ID=?"))
        {
            st.setTimestamp(1, timestamp);
            st.setInt(2, id_reply);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    private void updateRestaurantRequestDate(final int id_user, final int id_restaurant, Timestamp timestamp) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE RESTAURANTS_REQUESTS "
                + "SET DATE_ADMIN_TOOK=? WHERE ID_USER=? AND ID_RESTAURANT=?"))
        {
            st.setTimestamp(1, timestamp);
            st.setInt(2, id_user);
            st.setInt(3, id_restaurant);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Recupera tutti i contesti di foto segnalate che non sono ancora state
     * processate, e quindi presenti nel sistema. Non modifica il timestamp di
     * date_admin_took, e quindi non funge da "prenotazione" di una richiesta da
     * processare.
     *
     * @return Un ArrayList contenente tutte i contesti di foto segnalate.
     * @throws SQLException
     */
    public ArrayList<PhotoContext> getAllReportedPhotos() throws SQLException
    {
        ArrayList<PhotoContext> contesti = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT ID FROM REPORTED_PHOTOS"
                + " ORDER BY DATE_ADMIN_TOOK ASC"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    contesti.add(getPhotoContext(rs.getInt("ID")));
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return contesti;
    }

    /**
     * Recupera il contesto di una foto a partire dal suo id. Se la foto è null
     * allora anche l'utente sarà null.
     *
     * @param id_photo Id della foto.
     * @return PhotoContext Il contesto di una foto, composto dalla foto e
     * dall'utente che l'ha uploadata.
     * @throws SQLException
     */
    private PhotoContext getPhotoContext(final int id_photo) throws SQLException
    {
        PhotoContext contesto = new PhotoContext();
        contesto.setPhoto(getPhotoById(id_photo));
        if (contesto.getPhoto() != null)
        {
            contesto.setUser(getUserById(contesto.getPhoto().getId_owner()));
        }
        else
        {
            contesto.setUser(null);
        }
        return contesto;
    }

    /**
     * Recupera una foto a partire dall id.
     *
     * @param id_photo L'id della foto.
     * @return Un oggetto Photo, null se non esiste con quell id.
     * @throws SQLException
     */
    public Photo getPhotoById(final int id_photo) throws SQLException
    {
        Photo photo = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM PHOTOS WHERE ID=?"))
        {
            st.setInt(1, id_photo);
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    photo = new Photo();
                    photo.setId(id_photo);
                    photo.setName(rs.getString("NAME"));
                    photo.setDescription(rs.getString("DESCRIPTION"));
                    photo.setPath(rs.getString("PATH"));
                    photo.setId_restaurant(rs.getInt("ID_RESTAURANT"));
                    photo.setId_owner(rs.getInt("ID_OWNER"));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return photo;
    }

    /**
     * Controlla se una foto con un dato id esiste già nella tabella delle foto
     * segnalate.
     *
     * @param id_photo Id della foto da controllare se esiste già in
     * REPORTED_PHOTOS.
     * @return Vero se esiste già, falso altrimenti.
     * @throws SQLException
     */
    private boolean existReportedPhotoById(final int id_photo) throws SQLException
    {
        boolean res = true;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM REPORTED_PHOTOS WHERE ID=?"))
        {
            st.setInt(1, id_photo);
            try (ResultSet rs = st.executeQuery())
            {
                res = rs.next();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Controlla se una review con un dato id esiste già nella tabella delle
     * review segnalate.
     *
     * @param id_review Id della review da controllare se esiste già in
     * REPORTED_REVIEWS.
     * @return Vero se esiste già, falso altrimenti.
     * @throws SQLException
     */
    private boolean existReportedReviewById(final int id_review) throws SQLException
    {
        boolean res = true;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM REPORTED_REVIEWS WHERE ID=?"))
        {
            st.setInt(1, id_review);
            try (ResultSet rs = st.executeQuery())
            {
                res = rs.next();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Recupera un ristorante a partire dall id.
     *
     * @param id_rest Id del ristorante da recuperare.
     * @return Un oggetto Restaurant, null se non esiste con quell'id.
     * @throws SQLException
     */
    public Restaurant getRestaurantById(final int id_rest) throws SQLException
    {
        Restaurant restaurant = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM RESTAURANTS WHERE ID=?"))
        {
            st.setInt(1, id_rest);
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
                    restaurant.setVotes_counter(rs.getInt("VOTES_COUNTER"));
                    restaurant.setValidated(rs.getBoolean("VALIDATED"));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return restaurant;
    }

    /**
     * Recupera il contesto di un tentativo di claim o creazione di un
     * ristorante da parte di un utente. User e restaurant nel contesto saranno
     * null se non vengono trovati a partire dagli id forniti.
     *
     * @param id_user Lo user che ha fatto la creazione o la richiesta di
     * possesso.
     * @param id_restaurant Id del ristorante in questione.
     * @param creation_claim_both Il tipo di claim.
     * @param userTextClaim Una descrizione o commento che l'utente può mettere
     * al proprio claim.
     * @return Un oggetto AttemptContext, fatto da User, Restaurant, dalla flag
     * che dice che tipo di claim è, e dalla descrizione del claim.
     * @throws SQLException
     */
    private AttemptContext getAttemptContext(final int id_user, final int id_restaurant, 
            final int creation_claim_both, String userTextClaim) throws SQLException
    {
        if (userTextClaim == null)
        {
            userTextClaim = new String("");
        }
        AttemptContext contesto = new AttemptContext();
        contesto.setUser(getUserById(id_user));
        contesto.setRestaurant(getRestaurantById(id_restaurant));
        contesto.setIsClaim(creation_claim_both);
        contesto.setUsertextclaim(userTextClaim);
        return contesto;
    }

    /**
     * Recupera al max "many" contesti di una restaurant request, rappresentati
     * da un oggetto AttemptContext, che è composto da User, Restaurant, una
     * flag IsClaim che dice che tipo di claim è, e da UserTextClaim, una
     * descrizione che l'utente può dare alla sua richiesta.
     *
     * @param many Quanti contesti (al max) da ricevere, many deve essere
     * maggiore o uguale a 1 o causerà una eccezione.
     * @return Un ArrayList di AttemptContext.
     * @throws SQLException
     */
    public ArrayList<AttemptContext> getRestaurantsRequests(final int many) throws SQLException
    {
        ArrayList<AttemptContext> contesti = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM RESTAURANTS_REQUESTS"
                + " WHERE {fn TIMESTAMPDIFF( SQL_TSI_HOUR, DATE_ADMIN_TOOK,?)} >= 1 "
                + "FETCH FIRST ? ROWS ONLY"))
        {
            Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
            st.setTimestamp(1, timestamp);
            st.setInt(2, many);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    contesti.add(getAttemptContext(rs.getInt("ID_USER"), rs.getInt("ID_RESTAURANT"),
                            rs.getInt("CREATION_CLAIM_BOTH_FLAG"), rs.getString("USERTEXTCLAIM")));
                    this.updateRestaurantRequestDate(rs.getInt("ID_USER"), rs.getInt("ID_RESTAURANT"), timestamp);
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return contesti;
    }

    /**
     * Recupera tutti i contesti di richieste di ristorante che non sono ancora
     * state processate, e quindi presenti nel sistema. Non modifica il
     * timestamp di date_admin_took, e quindi non funge da "prenotazione" di una
     * richiesta da processare.
     *
     * @return Un ArrayList contenente tutte le richieste(i contesti) di
     * ristoranti da processare.
     * @throws SQLException
     */
    public ArrayList<AttemptContext> getAllRestaurantsRequests() throws SQLException
    {
        ArrayList<AttemptContext> contesti = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM RESTAURANTS_REQUESTS"
                + " ORDER BY DATE_ADMIN_TOOK ASC"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    contesti.add(getAttemptContext(rs.getInt("ID_USER"), rs.getInt("ID_RESTAURANT"),
                            rs.getInt("CREATION_CLAIM_BOTH_FLAG"), rs.getString("USERTEXTCLAIM")));
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return contesti;
    }

    /**
     * Per ricevere al max "many" di contesti (user+review) riguardanti le
     * review che hanno subito un report. Una volta che un contesto è stato
     * restituito da questo metodo non sarà restituito per un ora.
     *
     * @param many Quanti contesti (al max) da ricevere, many deve essere
     * maggiore o uguale a 1 o causerà una eccezione.
     * @return Un ArrayList con i contesti.
     * @throws SQLException
     */
    public ArrayList<ReviewContext> getReportedReviews(final int many) throws SQLException
    {
        ArrayList<ReviewContext> contesti = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT ID FROM REPORTED_REVIEWS"
                + " WHERE {fn TIMESTAMPDIFF( SQL_TSI_HOUR, DATE_ADMIN_TOOK,?)} >= 1 "
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
                    updateReportedReviewDate(rs.getInt("ID"), timestamp);
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return contesti;
    }

    /**
     * Recupera tutti i contesti di review segnalate che non sono ancora state
     * processate, e quindi presenti nel sistema, ordinate per DATE_ADMIN_TOOK.
     * Non modifica il timestamp di date_admin_took, e quindi non funge da
     * "prenotazione" di una richiesta da processare.
     *
     * @return Un ArrayList contenente tutte i contesti di review segnalate.
     * @throws SQLException
     */
    public ArrayList<ReviewContext> getAllReportedReviews() throws SQLException
    {
        ArrayList<ReviewContext> contesti = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT ID FROM REPORTED_REVIEWS "
                + " ORDER BY DATE_ADMIN_TOOK ASC"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    contesti.add(getReviewContext(rs.getInt("ID")));
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return contesti;
    }

    /**
     * Recupera il contesto di una review a partire dall'id. Se la review non
     * viene trovata (=null) o non è possibile trovare gli altri campi del
     * context a partire dalla review allora quei campi saranno null.
     *
     * @param id_review Id della review.
     * @return Un oggetto ReviewContext (review, user e eventuale reply che può
     * essere null se non c'è una reply per quella review).
     * @throws SQLException
     */
    public ReviewContext getReviewContext(final int id_review) throws SQLException
    {
        ReviewContext context = new ReviewContext();
        context.setReview(getReviewById(id_review));
        context.setReply(getReplyByIdReview(id_review));
        if (context.getReview() != null)
        {
            context.setUser(getUserById(context.getReview().getId_creator()));
            context.setPhoto((getPhotoById(context.getReview().getId_photo())));
            context.setRestaurantName(getRestaurantById(context.getReview().getId_restaurant()).getName());
        }
        else
        {
            context.setUser(null);
            context.setPhoto(null);
            context.setRestaurantName(null);
        }
        return context;
    }

    /**
     * Metodo per inserire una foto nelle foto reportate, utilizzando il suo id.
     * L'ora della segnalazione è settata un ora prima rispetto a quella
     * attuale, in modo che gli admin possano recuperare subito la segnalazione
     * per controllarla.
     *
     * @param id_photo Id della foto da segnalare.
     * @throws SQLException
     */
    public void reportPhoto(final int id_photo) throws SQLException
    {
        try (PreparedStatement st1 = con.prepareStatement("INSERT INTO REPORTED_PHOTOS VALUES(?,?)"))
        {
            //se la foto esiste e non esiste fra quelle reportate allora la inserisco
            Photo photo = getPhotoById(id_photo);
            if (photo != null && !existReportedPhotoById(id_photo))
            {
                st1.setInt(1, id_photo);
                st1.setTimestamp(2, new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                st1.executeUpdate();
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Metodo per inserire una review nelle review reportate, utilizzando il suo
     * id. L'ora della segnalazione è settata un ora prima rispetto a quella
     * attuale, in modo che gli admin possano recuperare subito la segnalazione
     * per controllarla.
     *
     * @param id_review Id della review da segnalare.
     * @throws SQLException
     */
    public void reportReview(final int id_review) throws SQLException
    {
        try (PreparedStatement st1 = con.prepareStatement("INSERT INTO REPORTED_REVIEWS VALUES(?,?)"))
        {
            Review review = getReviewById(id_review);
            //se non è già segnalata la inserisco
            if (review != null && !existReportedReviewById(id_review))
            {
                st1.setInt(1, id_review);
                st1.setTimestamp(2, new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                st1.executeUpdate();
                con.commit();
            }

        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Per inserire una reply da parte di un ristoratore a una sua recensione,
     * verrà inserite anche fra le reply che gli admin devono controllare e
     * confermare. Non serve il tempo di creazione, di validazione, l'id del
     * validatore e se è valida o no prima di passare la Reply al metodo.
     *
     * @param reply
     * @return True se è andata a buon fine, falso se esisteva già o l'owner
     * della reply non è il proprietario del ristorante che riguarda la review.
     * @throws SQLException
     */
    public boolean addReply(final Reply reply) throws SQLException
    {
        //se esiste già una reply da parte del ristoratore per questa review ritorna falso, altrimenti procedi
        if (replyForReviewExist(reply.getId_review()))
        {
            return false;
        }
        //se il proprietario della reply è diverso da quello del ristorante ritorna falso, altrimenti procedi
        if (getRestaurantById(getReviewById(reply.getId_review()).getId_restaurant()).getId_owner() != reply.getId_owner())
        {
            return false;
        }
        try (PreparedStatement st1 = con.prepareStatement("INSERT INTO REPLIES(DESCRIPTION,DATE_CREATION,ID_REVIEW,ID_OWNER"
                + ",DATE_VALIDATION,ID_VALIDATOR,VALIDATED) VALUES (?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
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
                    st2.setTimestamp(2, new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    st2.executeUpdate();
                    con.commit();
                }
                else
                {
                    con.rollback();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return true;
    }

    /**
     * Controlla se esiste già una reply destinata ad una review.
     *
     * @param id_review Id della review.
     * @return Vero se esiste già, falso altrimenti.
     * @throws SQLException
     */
    private boolean replyForReviewExist(final int id_review) throws SQLException
    {
        boolean res = true;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM REPLIES WHERE ID_REVIEW=?"))
        {
            st.setInt(1, id_review);
            try (ResultSet rs = st.executeQuery())
            {
                res = rs.next();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Metodo per inserire nel db un claim di un ristorante.
     *
     * @param id_user Id dell'utente che fa la creazione/claim del ristorante.
     * @param id_restaurant Id del ristorante in questione.
     * @param userTextClaim Il testo che l'utente ha dato come giustificazione
     * del claim
     * @param creationClaimBoth Flag che dice se questa è una creazione, claim,
     * o entrambe di ristorante(0,1,2).
     * @throws SQLException
     */
    public void addClaim(final int id_user, final int id_restaurant, final String userTextClaim, final int creationClaimBoth) throws SQLException
    {
        try (PreparedStatement st1 = con.prepareStatement("SELECT * FROM RESTAURANTS_REQUESTS WHERE ID_USER=? AND ID_RESTAURANT=?");
                PreparedStatement st2 = con.prepareStatement("INSERT INTO RESTAURANTS_REQUESTS VALUES(?,?,?,?,?)"))
        {
            st1.setInt(1, id_user);
            st1.setInt(2, id_restaurant);
            try (ResultSet st = st1.executeQuery())
            {//se questa request non esiste già
                if (!st.next())
                {
                    st2.setInt(1, id_user);
                    st2.setInt(2, id_restaurant);
                    st2.setTimestamp(3, new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    st2.setInt(4, creationClaimBoth);
                    st2.setString(5, userTextClaim);
                    st2.executeUpdate();
                    con.commit();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Aggiunge una review, in base al suo voto globale, ciò farà variare la
     * media globale del ristorante in questione. Nell'oggetto review non è
     * necessario settare la data di creazione o il numero di likes. (n.b. una
     * recensione comprende anche una foto, ricordarsi di caricare una foto a
     * livello servlet e settare l'id_photo in review) Ritorna falso se l'utente
     * ha già dato un voto o una recensione a questo ristorante nelle ultime 24
     * ore.
     *
     * @param review Review da inserire.
     * @return Un numero positivo se il metodo è andato a buon fine, questo
     * numero rappresenta l'id della recensione, -1 se il ristorante non esiste,
     * -2 se l'utente ha già fatto un voto o recensione per questo ristorante
     * nelle ultime 24, -3 se l'utente che fa la review è il prorietario del
     * ristorante.
     * @throws SQLException
     */
    public int addReview(final Review review) throws SQLException
    {
        int res = -1;
        Restaurant restaurant = getRestaurantById(review.getId_restaurant());
        //controllo che ristorante esista
        if (restaurant == null)
        {
            return -1;
        }
        //controllo che proprietario!=recensore
        if (restaurant.getId_owner() == review.getId_creator())
        {
            return -3;
        }
        //controllo che nn esista già voto o review per questo ristorante nelle u ltime 24h
        if (!addOrRefreshVote(review.getId_creator(), review.getId_restaurant()))
        {
            return -2;
        }
        try (PreparedStatement st = con.prepareStatement("INSERT INTO REVIEWS(GLOBAL_VALUE,FOOD,"
                + "SERVICE,VALUE_FOR_MONEY,ATMOSPHERE,NAME,DESCRIPTION,DATE_CREATION,ID_RESTAURANT,"
                + "ID_CREATOR,ID_PHOTO,LIKES,DISLIKES) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement updateSt = con.prepareStatement("UPDATE RESTAURANTS SET GLOBAL_VALUE=?, "
                        + "REVIEWS_COUNTER=? WHERE ID=?");
                PreparedStatement updateUs = con.prepareStatement("UPDATE USERS SET REVIEWS_COUNTER=? WHERE ID=?"))
        {
            User user = getUserById(review.getId_creator());
            int newReviewsCounter = restaurant.getReviews_counter() + 1;
            int newGlobal = ((restaurant.getReviews_counter() + restaurant.getVotes_counter()) * restaurant.getGlobal_value()
                    + review.getGlobal_value()) / (newReviewsCounter + restaurant.getVotes_counter());
            // (( (#rec+#voti)*media ) + valore_recensione) /(#rec+1+#voti)
            int newUserReviewCounter = user.getReviews_counter() + 1;
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
            try (ResultSet rs = st.getGeneratedKeys())
            {
                if (rs.next())
                {
                    res = rs.getInt(1);
                }
                else
                {
                    con.rollback();
                    return res;
                }
            }
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Per settare la foto dello user. Non viene controllato prima se lo user
     * esiste perchè si assume che sia preso dalla sessione e quindi corretto.
     *
     * @param userId Id dell'utente.
     * @param photoPath Una stringa che è il path della foto all'interno
     * dell'applicazione.
     * @throws SQLException
     */
    public void modifyUserPhoto(final int userId, final String photoPath) throws SQLException
    {
        boolean res = false;
        try (PreparedStatement st = con.prepareStatement("UPDATE USERS SET AVATAR_PATH=? WHERE ID=?"))
        {
            st.setString(1, photoPath);
            st.setInt(2, userId);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Aggiunge una foto ad un ristorante.
     *
     * @param photo L'oggetto foto da inserire.
     * @return Id del record creato nel db(id della foto), -1 se il ristorante
     * con id nel campo id_restaurant della foto non esiste.
     * @throws SQLException
     */
    public int addPhoto(final Photo photo) throws SQLException
    {
        int res = -1;
        try (PreparedStatement st = con.prepareStatement("INSERT INTO PHOTOS(NAME,"
                + "DESCRIPTION,PATH,ID_RESTAURANT,ID_OWNER) VALUES(?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS))
        {
            if (getRestaurantById(photo.getId_restaurant()) != null)
            {
                st.setString(1, photo.getName());
                st.setString(2, photo.getDescription());
                st.setString(3, photo.getPath());
                st.setInt(4, photo.getId_restaurant());
                st.setInt(5, photo.getId_owner());
                st.executeUpdate();
                try (ResultSet rs = st.getGeneratedKeys())
                {
                    if (rs.next())
                    {
                        res = rs.getInt(1);
                    }
                }
            }
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Per aggiungere il like/dislike di un utente rispetto a una review. Così
     * facendo verrà aggiornato il numero di like/dislike della review e del
     * creatore della review. Se il like dello user per la review esiste già ma
     * il like è di un tipo diverso da quello vecchio il like viene cambiato e
     * Review e creatore review aggiornati.
     *
     * @param id_review Id della review target del like.
     * @param type Il tipo di like, deve essere 0(dislike) o 1(like).
     * @param id_user L'utente che ha fatto il like.
     * @return 2 se esisteva già un like e ha cambiato il suo valore, 1 se il
     * like ha avto successo ed era la prima volta che faceva like a questa rev,
     * -2 se l'utente ha già fatto 1 like uguale a questa review, se la review
     * non esiste o se l'utente sta tentando di fare like alla sua review
     * @throws SQLException
     */
    public int addLike(final int id_review, final int type, final int id_user) throws SQLException
    {
        int res = -2;
        try (PreparedStatement checkLikeExist = con.prepareStatement("SELECT * FROM USER_REVIEW_LIKES WHERE ID_USER=? "
                + "AND ID_REVIEW=? AND ID_CREATOR=?"))
        {
            Review review = getReviewById(id_review);
            //controllo se review esiste e se l'utente che fa like nn è chi ha fatto la review
            if (review != null && review.getId_creator() != id_user)
            {
                User creator = getUserById(review.getId_creator());
                checkLikeExist.setInt(1, id_user);
                checkLikeExist.setInt(2, id_review);
                checkLikeExist.setInt(3, review.getId_creator());
                try (ResultSet rs = checkLikeExist.executeQuery())
                {
                    //se like non esiste lo metto io e aggiorno like del creatore review e review
                    if (!rs.next())
                    {
                        res = 1;
                        try (PreparedStatement makeLike = con.prepareStatement("INSERT INTO"
                                + " USER_REVIEW_LIKES VALUES(?,?,?,?,?)"))
                        {
                            makeLike.setInt(1, id_user);
                            makeLike.setInt(2, id_review);
                            makeLike.setInt(3, review.getId_creator());
                            makeLike.setInt(4, type);
                            makeLike.setTimestamp(5, new Timestamp(Calendar.getInstance().getTime().getTime()));
                            makeLike.executeUpdate();
                            if (type == 1)
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
                    else //se il like esiste già lo modifico se è diverso da quello
                    //esistente, non faccio niente se è uguale
                    {
                        if (type != rs.getInt("LIKE_TYPE"))
                        {
                            res = 2;//sto cambiando il like, quindi il valore cambia
                            try (PreparedStatement changeLike = con.prepareStatement("UPDATE USER_REVIEW_LIKES "
                                    + "SET LIKE_TYPE=? WHERE ID_USER=? AND ID_REVIEW=? AND ID_CREATOR=?"))
                            {
                                changeLike.setInt(1, type);
                                changeLike.setInt(2, id_user);
                                changeLike.setInt(3, id_review);
                                changeLike.setInt(4, review.getId_creator());
                                changeLike.executeUpdate();
                                //se likenuovo!=vecchio e quello nuovo è positivo
                                if (type == 1)
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
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    private void incrementUserLikes(final User user) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE USERS SET REVIEWS_POSITIVE=? WHERE ID=?"))
        {
            st.setInt(1, user.getReviews_positive() + 1);
            st.setInt(2, user.getId());
            st.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    private void incrementUserDislikes(final User user) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE USERS SET REVIEWS_NEGATIVE=? WHERE ID=?"))
        {
            st.setInt(1, user.getReviews_negative() + 1);
            st.setInt(2, user.getId());
            st.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    private void moveUserLikeToDislike(final User user) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE USERS SET REVIEWS_POSITIVE=?, REVIEWS_NEGATIVE=? WHERE ID=?"))
        {
            st.setInt(1, user.getReviews_positive() - 1);
            st.setInt(2, user.getReviews_negative() + 1);
            st.setInt(3, user.getId());
            st.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    private void moveUserDislikeToLike(final User user) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE USERS SET REVIEWS_POSITIVE=?, REVIEWS_NEGATIVE=? WHERE ID=?"))
        {
            st.setInt(1, user.getReviews_positive() + 1);
            st.setInt(2, user.getReviews_negative() - 1);
            st.setInt(3, user.getId());
            st.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    private void incrementReviewLikes(final Review review) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE REVIEWS SET LIKES=? WHERE ID=?"))
        {
            st.setInt(1, review.getLikes() + 1);
            st.setInt(2, review.getId());
            st.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    private void incrementReviewDislikes(final Review review) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE REVIEWS SET DISLIKES=? WHERE ID=?"))
        {
            st.setInt(1, review.getDislikes() + 1);
            st.setInt(2, review.getId());
            st.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    private void moveReviewLikeToDislike(final Review review) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE REVIEWS SET LIKES=?, DISLIKES=? WHERE ID=?"))
        {
            st.setInt(1, review.getLikes() - 1);
            st.setInt(2, review.getDislikes() + 1);
            st.setInt(3, review.getId());
            st.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    private void moveReviewDislikeToLike(final Review review) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE REVIEWS SET LIKES=?, DISLIKES=? WHERE ID=?"))
        {
            st.setInt(1, review.getLikes() + 1);
            st.setInt(2, review.getDislikes() - 1);
            st.setInt(3, review.getId());
            st.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Per inserire un ristorante.(la prima foto del ristorante va aggiunta a
     * parte, a livello servlet, dopo aver ottenuto l'id
     *
     * @param restaurant Il ristorante da inserire.
     * @param cucine Il tipo di cucine che ha, una lista di stringhe. (devono
     * essere uguali alle voci nel db)
     * @param coordinate Le coordinate del ristorante.
     * @param range Gli orari del ristorante, 7 HourRange.
     * @param userTextClaim Una stringa di descrizione che l'utente può mettere
     * nella creazione o nel claim, che verrà vista letta dall'admin quando
     * decide se confermare o no.
     * @param min La spesa minima in questo ristorante.
     * @param max La spesa max in questo ristorante.
     * @param isClaim Booleano per dire se è anche un claim oltre che una
     * creazione(true), o solo creazione(false).
     * @return Id del ristorante creato, -1 se la creazione è fallita.
     * @throws SQLException
     * @throws java.io.IOException
     */
    public int addRestaurant(Restaurant restaurant, final String[] cucine, 
            final Coordinate coordinate, final ArrayList<HoursRange> range, 
            final String userTextClaim, final double min, final double max, final boolean isClaim) throws SQLException, IOException
    {
        int restId = -1;
        try (PreparedStatement st = con.prepareStatement("INSERT INTO RESTAURANTS(NAME,DESCRIPTION,"
                + "WEB_SITE_URL,GLOBAL_VALUE,ID_OWNER,ID_CREATOR,ID_PRICE_RANGE,REVIEWS_COUNTER,VOTES_COUNTER,VALIDATED)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS))
        {
            st.setString(1, restaurant.getName());
            st.setString(2, restaurant.getDescription());
            st.setString(3, restaurant.getWeb_site_url());
            st.setInt(4, 0);
            st.setInt(5, -1);
            st.setInt(6, restaurant.getId_creator());
            st.setInt(7, findClosestPrice(min, max));
            st.setInt(8, 0);
            st.setInt(9, 1);
            st.setBoolean(10, false);
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys())
            {
                if (rs.next())
                {
                    restId = rs.getInt(1);
                    //per ogni cucina aggiungo la relazione, se quella cucina esiste nel nostro db
                    for (String cucine1 : cucine)
                    {
                        int cuisineId = findCuisine(cucine1);
                        if (cuisineId != -1)
                        {
                            addRestaurantXCuisine(restId, cuisineId);
                        }
                    }
                    //aggiungo le coordinate
                    addCoordinate(restId, coordinate);
                    //aggiungo gli orari
                    for (int i = 0; i < range.size(); i++)
                    {
                        addHourRange(restId, range.get(i));
                    }
                    restaurant.setId(restId);
                    //aggiungo il ristorante alla lista dei ristoranti da essere
                    //confermati da un admin, con flag per dire se è anche un claim
                    addClaim(restaurant.getId_creator(), restaurant.getId(), userTextClaim, isClaim ? 2 : 0);
                    con.commit();
                }
                else
                {
                    con.rollback();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return restId;
    }

    /**
     * Controlla se esiste una cucina con quel nome nel db, e ritorna il suo id.
     *
     * @param cuisine Stringa che rappresenta il tipo di cucina (il nome).
     * @return Un intero positivo se la cucina esiste, che rappresenterà il suo
     * id, -1 se non esiste una cucina di quel tipo.
     * @throws SQLException
     */
    private int findCuisine(final String cuisine) throws SQLException
    {
        int res = -1;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM CUISINES WHERE upper(NAME)=?"))
        {
            st.setString(1, cuisine.toUpperCase());
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    res = rs.getInt("ID");
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Aggiunge la coppia id_ristorante e id_cucina alla tabella.
     * 
     * @param idRest Id del ristorante.
     * @param cuisine Id della cucina.
     * @throws SQLException
     */
    private void addRestaurantXCuisine(final int idRest, final int cuisine) throws SQLException
    {
        try (/*PreparedStatement check = con.prepareStatement("SELECT * FROM RESTAURANT_CUISINE "
                + "WHERE ID_RESTAURANT=? AND ID_CUISINE=?");*/
                PreparedStatement st = con.prepareStatement("INSERT INTO RESTAURANT_CUISINE VALUES(?,?)"))
        {
            /*check.setInt(1, idRest);
            check.setInt(2, cuisine);
            try (ResultSet rs = check.executeQuery())
            {
                if (!rs.next())
                {*/
                    st.setInt(1, idRest);
                    st.setInt(2, cuisine);
                    st.executeUpdate();
               // }
            //}
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Metodo per inserire un hour range di un ristorante nel db.
     *
     * @param idRest Id del ristorante.
     * @param range Oggetto HoursRange.
     * @throws SQLException
     */
    private void addHourRange(final int idRest, final HoursRange range) throws SQLException
    {
        try (PreparedStatement ins = con.prepareStatement("INSERT INTO OPENING_HOURS_RANGES("
                + "DAY_OF_THE_WEEK,START_HOUR,END_HOUR) VALUES(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement ins2 = con.prepareStatement("INSERT INTO OPENING_HOURS_RANGE_RESTAURANT"
                        + " VALUES(?,?)"))
        {
            ins.setInt(1, range.getDay());
            ins.setTime(2, range.getStart_hour());
            ins.setTime(3, range.getEnd_hour());
            ins.executeUpdate();
            try (ResultSet st = ins.getGeneratedKeys())
            {
                if (st.next())
                {
                    ins2.setInt(1, idRest);
                    ins2.setInt(2, st.getInt(1));
                    ins2.executeUpdate();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Metodo per inserire le coordinate di un ristorante nel db e la relativa
     * coppia di chiavi nella tabella che mappa chiavi di ristorante a chiavi di
     * coordinate.
     *
     * @param idRest Id del ristorante.
     * @param cord Oggetto coordinata.
     * @throws SQLException
     */
    private void addCoordinate(final int idRest, final Coordinate cord) throws SQLException
    {
        try (PreparedStatement ins = con.prepareStatement("INSERT INTO COORDINATES("
                + "LATITUDE,LONGITUDE,ADDRESS,CITY,PROVINCE,STATE,COMPLETE_LOCATION) "
                + "VALUES(?,?,?,?,?,?,?||', '||?||', '||?||', '||?)", PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement ins2 = con.prepareStatement("INSERT INTO RESTAURANT_COORDINATE"
                        + " VALUES(?,?)"))
        {
            ins.setDouble(1, cord.getLatitude());
            ins.setDouble(2, cord.getLongitude());
            ins.setString(3, cord.getAddress());
            ins.setString(4, cord.getCity());
            ins.setString(5, cord.getProvince());
            ins.setString(6, cord.getState());
            ins.setString(7, cord.getAddress());
            ins.setString(8, cord.getCity());
            ins.setString(9, cord.getProvince());
            ins.setString(10, cord.getState());
            ins.executeUpdate();
            try (ResultSet st = ins.getGeneratedKeys())
            {
                if (st.next())
                {
                    ins2.setInt(1, idRest);
                    ins2.setInt(2, st.getInt(1));
                    ins2.executeUpdate();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Trova l'id del pricerange più vicino rispetto al min e al max forniti. La
     * distanza con un pricerange è calcolata come: abs( (differenza fra min e
     * min del price range) )+ abs( (differenza fra max e max del price range)
     * ).
     *
     * @param min Un intero che indica il minimo della spesa.
     * @param max Un intero che indica il massimo della spesa.
     * @return L'id del price range che ha min e max più simili possibili a min
     * e max forniti, -1 se non trova nessun prezzo in PRICE_RANGES.
     * @throws SQLException
     */
    private int findClosestPrice(final double min, final double max) throws SQLException
    {
        int res = -1;
        double minDiff = 10000000;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM PRICE_RANGES"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    while (rs.next())
                    {
                        double tmp = abs(rs.getDouble("MIN_VALUE") - min) + abs(rs.getDouble("MAX_VALUE") - max);
                        if (tmp < minDiff)
                        {
                            minDiff = tmp;
                            res = rs.getInt("ID");
                        }
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Metodo per permette all'admin di rimuovere una foto dalle foto reportate.
     *
     * @param id_photo Id della foto.
     * @throws SQLException
     */
    public void unreportPhoto(final int id_photo) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM REPORTED_PHOTOS WHERE ID=?"))
        {
            st.setInt(1, id_photo);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Metodo per permette all'admin di rimuovere una foto che era stata
     * reportata. Questo metodo non rimuove la foto dal filesystem, ma solo il
     * path dal db.
     *
     * @param id_photo Id della foto da rimuovere dal db.
     * @throws SQLException
     */
    public void removePhoto(final int id_photo) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM PHOTOS WHERE ID=?"))
        {
            //unreportPhoto(id_photo);//rimuovo dalla lista reportati non necessario causa ON DELETE CASCADE
            st.setInt(1, id_photo);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Aggiunge una notifica di foto ad un utente ristoratore, usate poi per
     * informare questi utenti dell'avvenuto upload di una foto che riguarda il
     * loro ristorante. Queste notifiche non riguardano le notifiche di
     * foto/review etc segnalate che arrivano agli admin.
     *
     * @param id_rest Id del ristorante di cui il proprietario va notificato, se esiste.
     * @param id_photo Id della foto di cui l'utente va avvisato della
     * creazione.
     * @throws SQLException
     */
    public void notifyPhoto(final int id_rest, final int id_photo) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("INSERT INTO PHOTO_NOTIFICATIONS"
                + "(ID_USER,ID_PHOTO) VALUES(?,?)"))
        {
            Restaurant rest= getRestaurantById(id_rest);
            if(rest!=null && rest.getId_owner()!=-1)
            {
                st.setInt(1,rest.getId_owner() );
                st.setInt(2, id_photo);
                st.executeUpdate();
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Aggiunge una notifica di review ad un utente ristoratore, usate poi per
     * informare questi utenti dell'avvenuta recensione del loro ristorante.
     * Queste notifiche non riguardano le notifiche di foto/review etc segnalate
     * che arrivano agli admin.
     *
     * @param id_rest Id del ristorante di cui avvisare il prorietario, se esiste.
     * @param id_review Id della review di cui l'utente va avvisato della
     * creazione.
     * @throws SQLException
     */
    public void notifyReview(final int id_rest, final int id_review) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("INSERT INTO REVIEW_NOTIFICATIONS"
                + "(ID_USER,ID_REVIEW) VALUES(?,?)"))
        {
            Restaurant rest= getRestaurantById(id_rest);
            if(rest!=null && rest.getId_owner()!=-1)
            {
                st.setInt(1, rest.getId_owner());
                st.setInt(2, id_review);
                st.executeUpdate();
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Metodo per permette all'admin di rimuovere una review dalle review
     * reportate.
     *
     * @param id Id della review.
     * @throws SQLException
     */
    public void unreportReview(final int id) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM REPORTED_REVIEWS WHERE ID=?"))
        {
            st.setInt(1, id);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Metodo per permette all'admin di rimuovere una review che era stata
     * reportata. Il ristorante risulterà avere una review in meno e la media
     * cambiata, e l'utente creatore perderà i like e dislikes a essa associati,
     * e avrà una review in meno. Verrà rimossa la foto dalla tabella foto e da
     * quelle segnalate (se lo è). La review verrà rimossa da quelle segnalate.
     * Verranno aggiornati i like nella tabella user_review_likes. Viene
     * cancellata la reply del ristoratore a questa review. Viene restituito il
     * path della foto in modo che si possa cancellare dal filesystem.
     *
     * @param id_review Id della review da rimuovere.
     * @return String Il path della foto che riguardava la review.
     * @throws SQLException
     */
    public String removeReview(final int id_review) throws SQLException
    {
        String res = null;
        try (PreparedStatement st = con.prepareStatement("DELETE FROM REVIEWS WHERE ID=?"))
        {
            Review review = getReviewById(id_review);
            if (review != null)
            {
                //unreportReview(id_review);//rimuovo review dalla tabella delle review segnalate nn necessario causa ON DELETE CASCADE
                st.setInt(1, id_review);
                try (PreparedStatement rm1 = con.prepareStatement("UPDATE RESTAURANTS SET GLOBAL_VALUE=((GLOBAL_VALUE"
                        + "*(REVIEWS_COUNTER+VOTES_COUNTER))-?)/(REVIEWS_COUNTER+VOTES_COUNTER-1),REVIEWS_COUNTER="
                        + "REVIEWS_COUNTER-1 WHERE ID=?");
                        PreparedStatement rm2 = con.prepareStatement("UPDATE USERS SET REVIEWS_COUNTER=REVIEWS_COUNTER-1,"
                                + "REVIEWS_POSITIVE=REVIEWS_POSITIVE-?,REVIEWS_NEGATIVE=REVIEWS_NEGATIVE-? WHERE ID=?");
                        PreparedStatement rm3 = con.prepareStatement("DELETE FROM PHOTOS WHERE ID=?");
                        PreparedStatement rm4 = con.prepareStatement("DELETE FROM REPLIES WHERE ID_REVIEW=?");
                        PreparedStatement rm5 = con.prepareStatement("DELETE FROM USER_REVIEW_LIKES WHERE ID_REVIEW=?");)
                {
                    Photo photo = getPhotoById(review.getId_photo());
                    res = photo.getPath();
                    //rm1 aggiorna ristorante
                    //rm2 aggiorna utente
                    //rm3 rimuove la photo della review
                    //rm4 rimuove la reply della review
                    //rm5 rimuove i like della review
                    rm1.setInt(1, review.getGlobal_value());
                    rm1.setInt(2, review.getId_restaurant());
                    rm2.setInt(1, review.getLikes());
                    rm2.setInt(2, review.getDislikes());
                    rm2.setInt(3, review.getId_creator());
                    rm3.setInt(1, review.getId_photo());
                    rm4.setInt(1, id_review);
                    rm5.setInt(1, id_review);
                    //ordine di rimozione per rispettare costraint
                    rm1.executeUpdate();
                    rm2.executeUpdate();
                    //rm4.executeUpdate();fatto da db con delete on cascade
                    rm5.executeUpdate();
                    st.executeUpdate();
                    rm3.executeUpdate();
                }
            }
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Per non accettare una reply di un ristoratore. La reply verrà cancellata
     * dal db e l'utente verrà notificato.
     *
     * @param id_reply Id della reply da non accettare,
     * @throws SQLException
     */
    public void unconfirmReply(final int id_reply) throws SQLException
    {
        ReplyContext context = getReplyContext(id_reply);
        try (PreparedStatement rm1 = con.prepareStatement("DELETE FROM REPLIES_TO_BE_CONFIRMED WHERE ID=?");
                PreparedStatement rm2 = con.prepareStatement("DELETE FROM REPLIES WHERE ID=?"))
        {
            rm1.setInt(1, id_reply);
            rm2.setInt(1, id_reply);
            rm1.executeUpdate();
            rm2.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Per accettare una reply di un ristoratore. La reply verrà rimossa dalla
     * tabella delle reply in attesa, mentre il flag VALIDATED nella tabella
     * REPLIES verrà messo a true.
     *
     * @param id_admin L'admin che ha fatto la validazione.
     * @param id_reply L'id della reply da validare.
     * @throws SQLException
     */
    public void confirmReply(final int id_reply, final int id_admin) throws SQLException
    {
        ReplyContext context = getReplyContext(id_reply);
        try (PreparedStatement rm1 = con.prepareStatement("DELETE FROM REPLIES_TO_BE_CONFIRMED WHERE ID=?");
                PreparedStatement up1 = con.prepareStatement("UPDATE REPLIES SET DATE_VALIDATION=?,VALIDATED=TRUE,ID_VALIDATOR=? WHERE ID=?"))
        {
            rm1.setInt(1, id_reply);
            up1.setTimestamp(1, new Timestamp(Calendar.getInstance().getTime().getTime()));
            up1.setInt(2, id_admin);
            up1.setInt(3, id_reply);
            rm1.executeUpdate();
            up1.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Per fare in modo che un utente segni una notifica di foto come vista, e
     * venga quindi eliminata in modo che non venga riconsegnata.
     *
     * @param id_user Id dell'utente proprietario della notifica.
     * @param id_notification L'id della notifica.
     * @throws SQLException
     */
    public void removePhotoNotification(final int id_user, final int id_notification) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM PHOTO_NOTIFICATIONS WHERE ID=? AND ID_USER=?"))
        {
            st.setInt(1, id_notification);
            st.setInt(2, id_user);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Per fare in modo che un utente segni una notifica di review come vista, e
     * venga quindi eliminata in modo che non venga riconsegnata.
     *
     * @param id_user Id dell'utente proprietario della notifica.
     * @param id_notification L'id della notifica.
     * @throws SQLException
     */
    public void removeReviewNotification(final int id_user, final int id_notification) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM REVIEW_NOTIFICATIONS WHERE ID=? AND ID_USER=?"))
        {
            st.setInt(1, id_notification);
            st.setInt(2, id_user);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Permette di accettare la richiesta di creazione o claim di possesso di un
     * ristorante, solitamente fatto da un admin.
     *
     * @param id_user Id dell'utente che ha fatto la request.
     * @param id_restaurant Id del ristorante da confermare.
     * @throws SQLException
     */
    public void acceptRestaurantRequest(final int id_user, final int id_restaurant) throws SQLException, IOException
    {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM RESTAURANTS_"
                + "REQUESTS WHERE ID_USER=? AND ID_RESTAURANT=?");
                PreparedStatement valid = con.prepareStatement("UPDATE RESTAURANTS SET "
                        + "ID_OWNER=?,VALIDATED=TRUE WHERE ID=?");
                PreparedStatement updateUser = con.prepareStatement("UPDATE USERS SET "
                        + "USERTYPE=1 WHERE ID=? AND USERTYPE=0"))
        {
            int type = requestType(id_user, id_restaurant);
            Restaurant restaurant = getRestaurantById(id_restaurant);
            if (type != -1 && restaurant != null)
            {
                switch (type)
                {
                //se è claim setto l owner
                    case 1:
                        valid.setInt(1, id_user);
                        break;
                //se è claim+creazione faccio owner e profilo
                    case 2:
                        valid.setInt(1, id_user);
                        setProfile(restaurant.getId(), restaurant.getName());
                        break;
                    default:
                        //se è creazione setto owner a -1 e setto il profilo per la ricerca fuzzy
                        valid.setInt(1, -1);
                        setProfile(restaurant.getId(), restaurant.getName());
                        break;
                }
                st.setInt(1, id_user);
                st.setInt(2, id_restaurant);
                st.executeUpdate();
                valid.setInt(2, id_restaurant);
                valid.executeUpdate();
                if (type > 0)
                {
                    updateUser.setInt(1, id_user);
                    updateUser.executeUpdate();
                }
            }
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * L'admin nega la creazione o il claim di un ristorante.
     *
     * @param id_user L'utente che ha fatto la richiesta.
     * @param id_restaurant Il ristorante in questione.
     * @throws SQLException
     */
    public void denyRestaurantRequest(final int id_user, final int id_restaurant) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM RESTAURANTS_"
                + "REQUESTS WHERE ID_USER=? AND ID_RESTAURANT=?"))
        {
            int type = requestType(id_user, id_restaurant);
            Restaurant restaurant = getRestaurantById(id_restaurant);
            if (type != -1 && restaurant != null)
            {
                st.setInt(1, id_user);
                st.setInt(2, id_restaurant);
                st.executeUpdate();
                if (type == 0 || type == 2)
                {
                    removeRestaurant(id_restaurant);
                }
            }
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Rimuove un ristorante e i dati a esso associati presenti in altre tabelle
     * (hoursrange, cuisines, coordinates).
     *
     * @param id_restaurant Id del ristorante da rimuovere.
     * @throws SQLException
     */
    private void removeRestaurant(final int id_restaurant) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM RESTAURANTS WHERE ID=?"))
        {
            st.setInt(1, id_restaurant);
            removeRestaurantPhotos(id_restaurant);
            removeRestaurantHourRange(id_restaurant);
            removeRestaurantCuisine(id_restaurant);
            removeRestaurantCoordinate(id_restaurant);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Rimuove le foto associate a un ristorante.
     *
     * @param id_restaurant Id del ristorante da rimuovere.
     * @throws SQLException
     */
    private void removeRestaurantPhotos(final int id_restaurant) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM PHOTOS WHERE ID_RESTAURANT=?"))
        {
            st.setInt(1, id_restaurant);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }
    
    
    
    /**
     * Rimuove gli orari di un ristorante a partire dal suo id.
     *
     * @param id_restaurant Id del ristorante.
     * @throws SQLException
     */
    private void removeRestaurantHourRange(final int id_restaurant) throws SQLException
    {
        try(PreparedStatement st1 = con.prepareStatement("DELETE FROM "
                        + "OPENING_HOURS_RANGES WHERE ID IN "
                        + "(SELECT ID_OPENING_HOURS_RANGE FROM OPENING_HOURS_RANGE_RESTAURANT WHERE ID_RESTAURANT=?)"))
        {
            st1.setInt(1, id_restaurant);
            st1.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Rimuove i record nella tabella RESTAURANT_CUISINE relativi al ristorante
     * che ha id_restaurant come id.
     *
     * @param id_restaurant Id del ristorante.
     * @throws SQLException
     */
    private void removeRestaurantCuisine(final int id_restaurant) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM "
                + "RESTAURANT_CUISINE WHERE ID_RESTAURANT=?"))
        {
            st.setInt(1, id_restaurant);
            st.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Rimuove i dati sulle coordinate di un ristorante, a partire dall'id.
     *
     * @param id_restaurant
     * @throws SQLException
     */
    private void removeRestaurantCoordinate(final int id_restaurant) throws SQLException
    {
        try (PreparedStatement st1 = con.prepareStatement("DELETE FROM "
                        + "COORDINATES WHERE ID IN "
                        + "(SELECT ID_COORDINATE FROM RESTAURANT_COORDINATE WHERE ID_RESTAURANT=?)"))
        {
            st1.setInt(1, id_restaurant);
            st1.executeUpdate();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Funzione per trovare il tipo di richiesta che l'utente ha fatto per un
     * ristorante.
     *
     * @param id_user Id dell'utente che ha fatto la richiesta.
     * @param id_restaurant Id del ristorante relativo alla richiesta.
     * @return -1 se non esiste una richiesta di questo utente per questo
     * ristorante, 0 se è una richiesta per creazione, 1 per claim, 2 per
     * creazione e claim insieme
     * @throws SQLException
     */
    private int requestType(final int id_user, final int id_restaurant) throws SQLException
    {
        int res = -1;
        try (PreparedStatement st = con.prepareStatement("SELECT CREATION_CLAIM_BOTH_FLAG FROM RESTAURANTS_"
                + "REQUESTS WHERE ID_USER=? AND ID_RESTAURANT=?"))
        {
            st.setInt(1, id_user);
            st.setInt(2, id_restaurant);
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    res = rs.getInt("CREATION_CLAIM_BOTH_FLAG");
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Permette la modifica di un ristorante da parte di un utente.
     *
     * @param restaurant L'oggetto ristorante, serve che nome, id, descrizione e
     * url siano giusti (in caso vengano cambiati dall'utente), va inoltre
     * settato l'id del proprietario (per esempio prendendolo dalla sessione) in
     * modo che si possa verificare che sia il proprietario.
     * @param cucine Nuove cucine rappresentate da stringhe.
     * @param coordinate Nuove coordinate.
     * @param range Nuovi orari (7 HourRange messi in ArrayList)-
     * @param min Nuovo prezzo minimo.
     * @param max Il nuovo prezzo massimo.
     * @throws SQLException
     * @throws java.io.IOException
     */
    public void modifyRestaurant(final Restaurant restaurant, final String[] cucine, 
            final Coordinate coordinate, final ArrayList<HoursRange> range, final double min, double max) throws SQLException, IOException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE RESTAURANTS SET "
                + "NAME=?,DESCRIPTION=?,WEB_SITE_URL=?,GLOBAL_VALUE=?,ID_OWNER=?,"
                + "ID_CREATOR=?,ID_PRICE_RANGE=?,REVIEWS_COUNTER=?,VOTES_COUNTER=?,VALIDATED=? "
                + "WHERE ID=?"))
        {
            Restaurant restaurantCheck = getRestaurantById(restaurant.getId());
            //controllo se il prorietario è lo stesso che fa la richiesta
            if (restaurantCheck != null && restaurantCheck.getId_owner() == restaurant.getId_owner())
            {
                st.setString(1, restaurant.getName());
                st.setString(2, restaurant.getDescription());
                st.setString(3, restaurant.getWeb_site_url());
                st.setInt(4, restaurantCheck.getGlobal_value());
                st.setInt(5, restaurantCheck.getId_owner());
                st.setInt(6, restaurantCheck.getId_creator());
                st.setInt(7, findClosestPrice(min, max));
                st.setInt(8, restaurantCheck.getReviews_counter());
                st.setInt(9, restaurantCheck.getVotes_counter());
                st.setBoolean(10, restaurantCheck.isValidated());
                st.setInt(11, restaurantCheck.getId());
                st.executeUpdate();
                //per ogni cucina aggiunto la relazione, se quella cucina esiste nel nostro db
                //dopo aver pulito quelle prima
                removeRestaurantCuisine(restaurant.getId());
                for (int i = 0; i < cucine.length; i++)
                {
                    int cuisineId = findCuisine(cucine[i]);
                    if (cuisineId != -1)
                    {
                        addRestaurantXCuisine(restaurant.getId(), cuisineId);
                    }
                }
                //pulisco e riaggiungo coordinate
                removeRestaurantCoordinate(restaurant.getId());
                addCoordinate(restaurant.getId(), coordinate);
                //pulisco e riaggiungo gli orari
                removeRestaurantHourRange(restaurant.getId());
                for (int i = 0; i < range.size(); i++)
                {
                    addHourRange(restaurant.getId(), range.get(i));
                }
                //aggiorno il profilo da usare per la fuzzy search
                updateProfile(restaurantCheck.getId(), restaurant.getName());
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Per settare la email dello user. Non viene controllato prima se lo user
     * esiste perchè si assume che sia preso dalla sessione e quindi corretto.
     *
     * @param id_user Id dell utente.
     * @param email La nuova email.
     * @return True se è andata a buon fine, falso se non si poteva settare
     * quella email perchè è già in uso.
     * @throws SQLException
     */
    public boolean modifyUserEmail(final int id_user, final String email) throws SQLException
    {
        boolean res = false;
        try (PreparedStatement checkEmail = con.prepareStatement("SELECT * FROM USERS WHERE EMAIL=?");
                PreparedStatement st = con.prepareStatement("UPDATE USERS SET EMAIL=? WHERE ID=?"))
        {
            checkEmail.setString(1, email);
            try (ResultSet rs = checkEmail.executeQuery())
            {
                if (!rs.next())
                {
                    st.setString(1, email);
                    st.setInt(2, id_user);
                    con.commit();
                    res = true;
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Permette di mofidicare nome e/o cognome dell'utente.
     *
     * @param id_user Id dell'utente.
     * @param newName Nuovo nome.
     * @param newSurname Nuovo cognome.
     * @throws SQLException
     */
    public void modifyUserNameSurname(final int id_user, final String newName, final String newSurname) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE USERS SET NAME=?,SURNAME=? WHERE ID=?"))
        {
            st.setString(1, newName);
            st.setString(2, newSurname);
            st.setInt(3, id_user);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Per modificare la password di uno user.
     *
     * @param id_user L'id dell'utente.
     * @param password La nuova password.
     * @throws SQLException
     */
    public void modifyUserPassword(final int id_user, final String password) throws SQLException
    {
        try (PreparedStatement st = con.prepareStatement("UPDATE USERS SET PASSWORD=? WHERE ID=?"))
        {
            st.setString(1, BCrypt.hashpw(password, BCrypt.gensalt()));
            st.setInt(2, id_user);
            st.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Metodo per l'autocomplete, restituisce (al max) 5 nomi del tipo LIKE
     * term%. N.b. considera solo ristoranti validati (VALIDATED=TRUE).
     *
     * @param term La stringa cui cercare.
     * @return Un ArrayList di nomi LIKE term%.
     * @throws SQLException
     */
    public ArrayList<String> autoCompleteName(String term) throws SQLException
    {
        ArrayList<String> res = new ArrayList<>();
        if (term == null || term.length() == 0)
        {
            return res;
        }
        try (PreparedStatement st = con.prepareStatement("(SELECT NAME FROM CUISINES "
                + "WHERE upper(NAME) like upper(?) ESCAPE '!' FETCH FIRST 5 ROWS ONLY) "
                + "UNION (SELECT NAME FROM RESTAURANTS WHERE upper(NAME) like upper(?) "
                + "ESCAPE '!' AND VALIDATED=TRUE FETCH FIRST 5 ROWS ONLY)"))
        {
            term = term
                    .replace("!", "!!")
                    .replace("%", "!%")
                    .replace("_", "!_")
                    .replace("[", "![");
            st.setString(1, term + "%");
            st.setString(2, term + "%");
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    res.add(rs.getString("NAME").toLowerCase());
                }
            }
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Per autocomplete a livello di luoghi, del tipo %term%. Se per esempio
     * esiste nel db via pinco pallino, Povo, Italia e via pinco pallino, riva
     * del garda, Italia, e un utente cerca "via pinco" l'autocomplete darà
     * entrambi i result. Scrivere invece riva proporrà solo la seconda entry.
     *
     * @param term La stringa di cui cercare località complete.
     * @return 5 Stringhe del tipo %term%.
     * @throws SQLException
     */
    public ArrayList<String> autoCompleteLocation(String term) throws SQLException
    {
        ArrayList<String> res = new ArrayList<>();
        if (term == null || term.length() == 0)
        {
            return res;
        }
        try (PreparedStatement st = con.prepareStatement("SELECT COMPLETE_LOCATION FROM COORDINATES WHERE"
                + " upper(COMPLETE_LOCATION) like upper(?) ESCAPE '!' FETCH FIRST 5 ROWS ONLY"))
        {
            term = term
                    .replace("!", "!!")
                    .replace("%", "!%")
                    .replace("_", "!_")
                    .replace("[", "![");
            st.setString(1, "%" + term + "%");
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    res.add(rs.getString("COMPLETE_LOCATION"));
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce un ArrayList di id (int) di ristoranti che hanno indirizzo,
     * città, stato, o complete_location pari a location, e che sono validati.
     *
     * @param location L'indirizzo, la città, lo stato, o complete location del
     * ristorante.
     * @return Un ArrayList di id di ristoranti.
     * @throws SQLException
     */
    private ArrayList<Integer> getRestIdsFromLocation(final String location) throws SQLException
    {
        ArrayList<Integer> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT RESTAURANTS.ID "
                + "FROM "
                + "(select RESTAURANT_COORDINATE.ID_RESTAURANT "
                + "FROM "
                + "(SELECT ID FROM COORDINATES WHERE upper(ADDRESS)=? OR upper(CITY)=? "
                + "OR upper(PROVINCE)=? OR upper(STATE)=? OR upper(COMPLETE_LOCATION)=?) IDCord, RESTAURANT_COORDINATE "
                + "WHERE IDCORD.ID=RESTAURANT_COORDINATE.ID_COORDINATE) RISTO, RESTAURANTS "
                + "WHERE RISTO.ID_RESTAURANT=RESTAURANTS.ID AND RESTAURANTS.VALIDATED=TRUE"))
        {
            st.setString(1, location.toUpperCase());
            st.setString(2, location.toUpperCase());
            st.setString(3, location.toUpperCase());
            st.setString(4, location.toUpperCase());
            st.setString(5, location.toUpperCase());
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    res.add(rs.getInt("ID"));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Per prendere il contesto di uno user per la sua pagina personale. Il
     * contesto contiene le informazioni necessarie per la pagina utente.
     *
     * @param id_user Id dell'utente di cui si vuole il contesto.
     * @return Il contesto dell'utente
     * (User,photos,reviews,restaurants,notifactions).
     * @throws SQLException
     */
    public OwnUserContext getUserContext(final int id_user) throws SQLException
    {
        OwnUserContext contesto = new OwnUserContext();
        try
        {

            contesto.setUser(getUserById(id_user));
            contesto.setPhotos(getUserPhotos(id_user));
            contesto.setReviewContext(getUserReviewContextsByDateCreation(id_user));
            contesto.setRestaurant(getUserRestaurants(id_user));
            contesto.setPhoto_notifications(getUserPhotoNotifications(id_user, USER_NOTIFICATIONS_TO_GET));
            contesto.setReview_notifications(getUserReviewNotifications(id_user, USER_NOTIFICATIONS_TO_GET));
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return contesto;
    }

    /**
     * Trova le foto di un utente in base al suo id.
     *
     * @param id_user Id dell'utente di cui cercare le foto.
     * @return Un ArrayList<Photo> relative all'utente con l'id fornito.
     * @throws SQLException
     */
    private ArrayList<Photo> getUserPhotos(final int id_user) throws SQLException
    {
        ArrayList<Photo> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM PHOTOS WHERE ID_OWNER=?"))
        {
            st.setInt(1, id_user);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Photo photo = new Photo();
                    photo.setId(rs.getInt("ID"));
                    photo.setName(rs.getString("NAME"));
                    photo.setDescription(rs.getString("DESCRIPTION"));
                    photo.setPath(rs.getString("PATH"));
                    photo.setId_restaurant(rs.getInt("ID_RESTAURANT"));
                    photo.setId_owner(rs.getInt("ID_OWNER"));
                    res.add(photo);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce le review di un utente, ordinate per DATE_CREATION.
     *
     * @param id_user L'id dell''utente di cui cercare le reviews.
     * @return Un ArrayList di oggetti Review dell'utente, ordinate per
     * DATE_CREATION, la prima è la più recente.
     * @throws SQLException
     */
    public ArrayList<Review> getUserReviewsByDateCreation(final int id_user) throws SQLException
    {
        ArrayList<Review> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM REVIEWS WHERE ID_CREATOR=? "
                + "ORDER BY DATE_CREATION DESC"))
        {
            st.setInt(1, id_user);
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
                    res.add(review);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce i contesti delle review di un utente, ordinate per
     * DATE_CREATION.
     *
     * @param id_user L'id dell''utente di cui cercare le reviews.
     * @return Un ArrayList di oggetti ReviewContext dell'utente, ordinate per
     * DATE_CREATION, la prima è la più recente.
     * @throws SQLException
     */
    public ArrayList<ReviewContext> getUserReviewContextsByDateCreation(final int id_user) throws SQLException
    {
        ArrayList<ReviewContext> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT ID FROM REVIEWS WHERE ID_CREATOR=? "
                + "ORDER BY DATE_CREATION DESC"))
        {
            st.setInt(1, id_user);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    ReviewContext reviewContext = getReviewContext(rs.getInt("ID"));
                    res.add(reviewContext);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce i ristoranti di un utente, a partire dal suo id, ordinati per
     * REVIEWS_COUNTER (descending).
     *
     * @param id_user L'id dell''utente di cui cercare i ristoranti..
     * @return Un ArrayList di oggetti Ristoranti di un utente.
     * @throws SQLException
     */
    private ArrayList<Restaurant> getUserRestaurants(final int id) throws SQLException
    {
        ArrayList<Restaurant> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM RESTAURANTS WHERE ID_OWNER=? "
                + "ORDER BY REVIEWS_COUNTER DESC"))
        {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setId(rs.getInt("ID"));
                    restaurant.setName(rs.getString("NAME"));
                    restaurant.setDescription(rs.getString("DESCRIPTION"));
                    restaurant.setWeb_site_url(rs.getString("WEB_SITE_URL"));
                    restaurant.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    restaurant.setId_owner(rs.getInt("ID_OWNER"));
                    restaurant.setId_creator(rs.getInt("ID_CREATOR"));
                    restaurant.setId_price_range(rs.getInt("ID_PRICE_RANGE"));
                    restaurant.setReviews_counter(rs.getInt("REVIEWS_COUNTER"));
                    restaurant.setVotes_counter(rs.getInt("VOTES_COUNTER"));
                    restaurant.setValidated(rs.getBoolean("VALIDATED"));
                    res.add(restaurant);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce le cucine di un ristorante a partire dal suo id.
     *
     * @param id_restaurant Id del ristorante di cui cercare le cucine.
     * @return Un ArrayList<String>, rappresentante le cucine del ristorante.
     * @throws SQLException
     */
    private ArrayList<String> getRestaurantCuisines(final int id_restaurant) throws SQLException
    {
        ArrayList<String> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT NAME "
                + "FROM "
                + "(SELECT ID_CUISINE FROM RESTAURANT_CUISINE WHERE ID_RESTAURANT=?) IDC, CUISINES "
                + "WHERE IDC.ID_CUISINE=CUISINES.ID"))
        {
            st.setInt(1, id_restaurant);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    res.add(rs.getString("NAME"));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce (al massimo) 5 ristoranti, ordinati per global value, il
     * primo ha quello più alto.
     *
     * @return Un ArrayList contenente i ristoranti.
     * @throws SQLException
     */
    public ArrayList<Restaurant> getTop5RestaurantsByValue() throws SQLException
    {
        ArrayList<Restaurant> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM RESTAURANTS "
                + "ORDER BY GLOBAL_VALUE DESC FETCH FIRST 5 ROWS ONLY"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setId(rs.getInt("ID"));
                    restaurant.setName(rs.getString("NAME"));
                    restaurant.setDescription(rs.getString("DESCRIPTION"));
                    restaurant.setWeb_site_url(rs.getString("WEB_SITE_URL"));
                    restaurant.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    restaurant.setId_owner(rs.getInt("ID_OWNER"));
                    restaurant.setId_creator(rs.getInt("ID_CREATOR"));
                    restaurant.setId_price_range(rs.getInt("ID_PRICE_RANGE"));
                    restaurant.setReviews_counter(rs.getInt("REVIEWS_COUNTER"));
                    restaurant.setVotes_counter(rs.getInt("VOTES_COUNTER"));
                    restaurant.setValidated(rs.getBoolean("VALIDATED"));
                    res.add(restaurant);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce (al massimo) 5 ristoranti, ordinati per reviews_counter, il
     * primo ha quello più alto.
     *
     * @return Un ArrayList contenente i ristoranti.
     * @throws SQLException
     */
    public ArrayList<Restaurant> getTop5RestaurantsByReviewsCounter() throws SQLException
    {
        ArrayList<Restaurant> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM RESTAURANTS "
                + "ORDER BY REVIEWS_COUNTER DESC FETCH FIRST 5 ROWS ONLY"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setId(rs.getInt("ID"));
                    restaurant.setName(rs.getString("NAME"));
                    restaurant.setDescription(rs.getString("DESCRIPTION"));
                    restaurant.setWeb_site_url(rs.getString("WEB_SITE_URL"));
                    restaurant.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    restaurant.setId_owner(rs.getInt("ID_OWNER"));
                    restaurant.setId_creator(rs.getInt("ID_CREATOR"));
                    restaurant.setId_price_range(rs.getInt("ID_PRICE_RANGE"));
                    restaurant.setReviews_counter(rs.getInt("REVIEWS_COUNTER"));
                    restaurant.setVotes_counter(rs.getInt("VOTES_COUNTER"));
                    restaurant.setValidated(rs.getBoolean("VALIDATED"));
                    res.add(restaurant);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce (al massimo) 5 utenti, ordinati per reviews_positive, dal più
     * alto al più basso, reviews positive sarebbe i like alle sue review.
     *
     * @return Un ArrayList contenente gli utenti.
     * @throws SQLException
     */
    public ArrayList<User> getTop5UsersByReviewsPositive() throws SQLException
    {
        ArrayList<User> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM USERS "
                + "ORDER BY REVIEWS_POSITIVE DESC FETCH FIRST 5 ROWS ONLY"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    User user = new User();
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
                    user.setVerified(rs.getBoolean("VERIFIED"));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce (al massimo) 5 review, le più recenti. La prima è la più
     * recente.
     *
     * @return Un ArrayList contenente i ristoranti.
     * @throws SQLException
     */
    public ArrayList<Review> getLast5Reviews() throws SQLException
    {
        ArrayList<Review> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM REVIEWS "
                + "ORDER BY DATE_CREATION DESC FETCH FIRST 5 ROWS ONLY"))
        {
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
                    res.add(review);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    private PriceRange getRestaurantPriceRange(final int priceId) throws SQLException
    {
        PriceRange res = new PriceRange();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM PRICE_RANGES WHERE ID=?"))
        {
            st.setInt(1, priceId);
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    res.setName(rs.getString("NAME"));
                    res.setMin(rs.getDouble("MIN_VALUE"));
                    res.setMax(rs.getDouble("MAX_VALUE"));
                }
                else
                {
                    res = null;
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Se l'utente ha già fatto un voto a questo ristorante refresha la data a
     * quella corrente, altrimenti crea un record con un timestamp pari alla
     * data corrente.
     *
     * @param id_user Id dell'utente che fa il voto.
     * @param id_restaurant Id del ristorante votato.
     * @return res Vero se non ha mai votato questo ristorante o se il voto
     * dell'utente più recente per questo ristorante era più vecchio di 24h,
     * false se l'ultimo voto ha meno di 24h.
     */
    private boolean addOrRefreshVote(final int id_user, final int id_restaurant) throws SQLException
    {
        boolean res = false;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM USER_VOTES_ON_RESTAURANTS"
                + " WHERE ID_USER=? AND ID_RESTAURANT=? "))
        {
            st.setInt(1, id_user);
            st.setInt(2, id_restaurant);
            try (ResultSet rs = st.executeQuery())
            {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                //se non ha mai votato allora inserisco un record, altrimenti
                //controllo che la differenza fra i 2 timestamp sia di almeno 24h
                if (rs.next())
                {
                    Timestamp old = rs.getTimestamp(3);
                    if (compareTwoTimestamps(old, now) > 24)
                    {
                        try (PreparedStatement updateSt = con.prepareStatement(
                                "UPDATE USER_VOTES_ON_RESTAURANTS SET TIME_OF_VOTE=? WHERE ID_USER=?"
                                + "AND ID_RESTAURANT=?"))
                        {
                            updateSt.setTimestamp(1, now);
                            updateSt.setInt(2, id_user);
                            updateSt.setInt(3, id_restaurant);
                            updateSt.executeUpdate();
                        }
                        res = true;
                    }
                }
                else
                {
                    try (PreparedStatement createSt = con.prepareStatement("INSERT "
                            + "INTO USER_VOTES_ON_RESTAURANTS VALUES(?,?,?)"))
                    {
                        createSt.setInt(1, id_user);
                        createSt.setInt(2, id_restaurant);
                        createSt.setTimestamp(3, now);
                        createSt.executeUpdate();
                    }
                    res = true;
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Per aggiungere il voto di un utente ad un ristorante. Un utente può
     * votare lo stesso ristorante 1 volta ogni 24h.
     *
     * @param vote Il voto dell'utente, da 1 a 5.
     * @param id_user Id dell'utente che ha aggiunto il voto.
     * @param id_restaurant Id del ristorante a cui è destinato il voto.
     * @return Un intero, che se è positivo rappresenta il nuovo voto del
     * ristorante, (che potrebbe comunque essere uguale a prima), se è 0 indica
     * che il voto dell'utente non ha avuto alcun effetto perchè aveva votato
     * prima di 24h ore fa, -1 se non esiste un ristorante con quell'id o
     * perchè l'utente è il prorietario.
     * @throws SQLException
     */
    public int addUserVoteOnRestaurant(final int vote, final int id_user, final int id_restaurant) throws SQLException
    {
        int res = 0;
        Restaurant restaurant = getRestaurantById(id_restaurant);
        if (restaurant == null || restaurant.getId_owner() == id_user)
        {
            res=-1;
            return res;
        }
        boolean canVote = addOrRefreshVote(id_user, id_restaurant);
        if (canVote)//se può votare xk nn ha mai votato o è più vecchio di 24h
        {
            try (PreparedStatement updateSt = con.prepareStatement("UPDATE RESTAURANTS SET GLOBAL_VALUE=?, "
                    + "VOTES_COUNTER=? WHERE ID=?"))
            {
                restaurant = getRestaurantById(id_restaurant);
                int newGlobal = ((restaurant.getReviews_counter() + restaurant.getVotes_counter()) * restaurant.getGlobal_value()
                        + vote) / (restaurant.getReviews_counter() + restaurant.getVotes_counter() + 1);
                // (( (#rec+#voti)*media ) + valore_voto) /(#rec+#voti+1)
                updateSt.setInt(1, newGlobal);
                updateSt.setInt(2, restaurant.getVotes_counter() + 1);
                updateSt.setInt(3, id_restaurant);
                updateSt.executeUpdate();
                res = newGlobal;
                con.commit();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
                con.rollback();
                throw ex;
            }
        }
        return res;
    }

    /**
     * Per trovare la differenza fra 2 Timestamp.
     *
     * @param oldTime
     * @param currentTime
     * @return
     */
    private static long compareTwoTimestamps(Timestamp oldTime, Timestamp currentTime)
    {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();
        long diff = milliseconds2 - milliseconds1;
        //long diffSeconds = diff / 1000;
        //long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        //long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffHours;
    }

    /**
     * Recupera i ristoranti che hanno un determinato nome.
     *
     * @param name Nome dei ristoranti da recuperare.
     * @return Un oggetto ArrayList<Restaurant>, vuoto se non esistono
     * ristoranti con quel nome.
     * @throws SQLException
     */
    public ArrayList<Restaurant> getRestaurantsByName(final String name) throws SQLException
    {
        ArrayList<Restaurant> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM RESTAURANTS WHERE NAME=?"))
        {
            st.setString(1, name);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setId(rs.getInt("ID"));
                    restaurant.setName(rs.getString("NAME"));
                    restaurant.setDescription(rs.getString("DESCRIPTION"));
                    restaurant.setWeb_site_url(rs.getString("WEB_SITE_URL"));
                    restaurant.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    restaurant.setId_owner(rs.getInt("ID_OWNER"));
                    restaurant.setId_creator(rs.getInt("ID_CREATOR"));
                    restaurant.setId_price_range(rs.getInt("ID_PRICE_RANGE"));
                    restaurant.setReviews_counter(rs.getInt("REVIEWS_COUNTER"));
                    restaurant.setVotes_counter(rs.getInt("VOTES_COUNTER"));
                    restaurant.setValidated(rs.getBoolean("VALIDATED"));
                    res.add(restaurant);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Recupera id dei ristoranti che hanno un determinato nome.
     *
     * @param name Nome dei ristoranti da recuperare.
     * @return Un oggetto ArrayList<Integer>, vuoto se non esistono ristoranti
     * con quel nome.
     * @throws SQLException
     */
    public ArrayList<Integer> getRestaurantsIdByName(final String name) throws SQLException
    {
        ArrayList<Integer> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT ID FROM RESTAURANTS WHERE NAME=?"))
        {
            st.setString(1, name);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    res.add(rs.getInt("ID"));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce un ArrayList di ristoranti che hanno indirizzo, città, stato,
     * o complete_location pari a location.
     *
     * @param location
     * @return Un oggetto ArrayList<Restaurant>, vuoto se non ci sono ristoranti
     * con quella location.
     * @throws SQLException
     */
    public ArrayList<Restaurant> getRestaurantsByLocation(final String location) throws SQLException
    {
        ArrayList<Restaurant> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * "
                + "FROM "
                + "(select RESTAURANT_COORDINATE.ID_RESTAURANT "
                + "FROM "
                + "(select ID FROM COORDINATES WHERE upper(ADDRESS)=? OR upper(CITY)=? "
                + "OR upper(PROVINCE)=? OR upper(STATE)=? OR upper(COMPLETE_LOCATION)=?) IDCord, RESTAURANT_COORDINATE "
                + "WHERE IDCORD.ID=RESTAURANT_COORDINATE.ID_COORDINATE) RISTO, RESTAURANTS "
                + "WHERE RISTO.ID_RESTAURANT=RESTAURANTS.ID"))
        {
            st.setString(1, location.toUpperCase());
            st.setString(2, location.toUpperCase());
            st.setString(3, location.toUpperCase());
            st.setString(4, location.toUpperCase());
            st.setString(5, location.toUpperCase());
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setId(rs.getInt("ID"));
                    restaurant.setName(rs.getString("NAME"));
                    restaurant.setDescription(rs.getString("DESCRIPTION"));
                    restaurant.setWeb_site_url(rs.getString("WEB_SITE_URL"));
                    restaurant.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    restaurant.setId_owner(rs.getInt("ID_OWNER"));
                    restaurant.setId_creator(rs.getInt("ID_CREATOR"));
                    restaurant.setId_price_range(rs.getInt("ID_PRICE_RANGE"));
                    restaurant.setReviews_counter(rs.getInt("REVIEWS_COUNTER"));
                    restaurant.setVotes_counter(rs.getInt("VOTES_COUNTER"));
                    restaurant.setValidated(rs.getBoolean("VALIDATED"));
                    res.add(restaurant);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce un ArrayList di id di ristorantiche hanno indirizzo, città,
     * stato, o complete_location pari a location.
     *
     * @param location
     * @return Un oggetto ArrayList<int>, vuoto se non ci sono ristoranti con
     * quella location.
     * @throws SQLException
     */
    public ArrayList<Integer> getRestaurantsIdByLocation(final String location) throws SQLException
    {
        ArrayList<Integer> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT RESTAURANT_COORDINATE.ID_RESTAURANT "
                + "AS ID FROM (select ID FROM COORDINATES WHERE upper(ADDRESS)=? "
                + "OR upper(PROVINCE)=? OR upper(CITY)=? OR upper(STATE)=? OR upper(COMPLETE_LOCATION)=?) "
                + "IDCord, RESTAURANT_COORDINATE WHERE IDCORD.ID=RESTAURANT_COORDINATE.ID_COORDINATE"))
        {
            st.setString(1, location.toUpperCase());
            st.setString(2, location.toUpperCase());
            st.setString(3, location.toUpperCase());
            st.setString(4, location.toUpperCase());
            st.setString(5, location.toUpperCase());
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    res.add(rs.getInt("ID"));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Recupera i ristoranti che hanno un determinato proprietario.
     *
     * @param id_owner Id del proprietario dei ristoranti.
     * @return Un oggetto ArrayList<Restaurant>, vuoto se non esistono
     * ristoranti con quel proprietario.
     * @throws SQLException
     */
    public ArrayList<Restaurant> getRestaurantsByIdOwner(final int id_owner) throws SQLException
    {
        ArrayList<Restaurant> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM RESTAURANTS WHERE ID_OWNER=?"))
        {
            st.setInt(1, id_owner);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setId(rs.getInt("ID"));
                    restaurant.setName(rs.getString("NAME"));
                    restaurant.setDescription(rs.getString("DESCRIPTION"));
                    restaurant.setWeb_site_url(rs.getString("WEB_SITE_URL"));
                    restaurant.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    restaurant.setId_owner(rs.getInt("ID_OWNER"));
                    restaurant.setId_creator(rs.getInt("ID_CREATOR"));
                    restaurant.setId_price_range(rs.getInt("ID_PRICE_RANGE"));
                    restaurant.setReviews_counter(rs.getInt("REVIEWS_COUNTER"));
                    restaurant.setVotes_counter(rs.getInt("VOTES_COUNTER"));
                    restaurant.setValidated(rs.getBoolean("VALIDATED"));
                    res.add(restaurant);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Recupera i ristoranti che hanno un determinato creatore.
     *
     * @param id_creator Id del creatore dei ristoranti.
     * @return Un oggetto ArrayList<Restaurant>, vuoto se non esistono
     * ristoranti creati da quell'utente.
     * @throws SQLException
     */
    public ArrayList<Restaurant> getRestaurantsByIdCreator(final int id_creator) throws SQLException
    {
        ArrayList<Restaurant> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM RESTAURANTS WHERE ID_CREATOR=?"))
        {
            st.setInt(1, id_creator);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setId(rs.getInt("ID"));
                    restaurant.setName(rs.getString("NAME"));
                    restaurant.setDescription(rs.getString("DESCRIPTION"));
                    restaurant.setWeb_site_url(rs.getString("WEB_SITE_URL"));
                    restaurant.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    restaurant.setId_owner(rs.getInt("ID_OWNER"));
                    restaurant.setId_creator(rs.getInt("ID_CREATOR"));
                    restaurant.setId_price_range(rs.getInt("ID_PRICE_RANGE"));
                    restaurant.setReviews_counter(rs.getInt("REVIEWS_COUNTER"));
                    restaurant.setVotes_counter(rs.getInt("VOTES_COUNTER"));
                    restaurant.setValidated(rs.getBoolean("VALIDATED"));
                    res.add(restaurant);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce il contesto di un ristorante a partire dall'id. Le review del
     * ristorante sono ordinate a partire dalla più recente alla più vecchia. I
     * campi all'interno del contesto potrebbero essere null se non vengono
     * trovati o se non è possibile trovarli perchè qualcosa di necessario per
     * trovarli è null. CityPosition sarà uguale a -1 se restaurant o coordinate
     * saranno null.
     *
     * @param id_restaurant Id del ristorante.
     * @return Un oggetto RestaurantContext.
     * @throws SQLException
     */
    public RestaurantContext getRestaurantContext(final int id_restaurant) throws SQLException
    {
        RestaurantContext context = new RestaurantContext();
        Coordinate coordinate = getRestaurantCoordinate(id_restaurant);
        Restaurant restaurant = getRestaurantById(id_restaurant);
        context.setRestaurant(restaurant);
        context.setHoursRanges(getRestaurantHoursRanges(id_restaurant));
        context.setPhotos(getRestaurantPhotos(id_restaurant));
        context.setReviewsContextsByNewest(getRestaurantReviewsContextsByNewest(id_restaurant));
        context.setCoordinate(coordinate);
        if (restaurant != null)
        {
            //roba che ha bisogno di restaurant != null
            if (restaurant.getId_owner() != 0)//se ha un owner
            {
                context.setOwner(getUserById(restaurant.getId_owner()));
            }
            else
            {
                context.setOwner(null);
            }
            context.setCuisines(getRestaurantCuisines(restaurant.getId()));
            context.setPriceRange(getRestaurantPriceRange(restaurant.getId_price_range()));
            //roba che ha bisogno coordinate != null
            if (coordinate != null)
            {
                context.setCityPosition(getRestaurantCityPosition(restaurant.getGlobal_value(),
                        coordinate.getCity(), coordinate.getState()));
            }
            else
            {
                context.setCityPosition(-1);
            }
        }
        else
        {
            context.setOwner(null);
            context.setCuisines(new ArrayList<>());
            context.setPriceRange(null);
            context.setCityPosition(-1);
        }
        return context;
    }

    /**
     * Restituisce i contesti delle review di un ristorante a partire dall'id
     * del ristorante, ordinati dalla review più nuova a quella più vecchia.
     *
     * @param id_restaurant L'id del ristorante.
     * @return Un ArrayList di ReviewContext.
     * @throws SQLException
     */
    private ArrayList<ReviewContext> getRestaurantReviewsContextsByNewest(final int id_restaurant) throws SQLException
    {
        ArrayList<ReviewContext> reviews = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT ID"
                + " FROM REVIEWS WHERE ID_RESTAURANT=? ORDER BY DATE_CREATION DESC"))
        {
            st.setInt(1, id_restaurant);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    reviews.add(getReviewContext(rs.getInt("ID")));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return reviews;
    }

    /**
     * Per trovare le coordinate di un ristorante a partire dal suo id.
     *
     * @param id_restaurant L'id del ristorante.
     * @return Un oggetto Coordinate relativo al ristorante.
     * @throws SQLException
     */
    private Coordinate getRestaurantCoordinate(final int id_restaurant) throws SQLException
    {
        Coordinate coordinate = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM COORDINATES, "
                + "RESTAURANT_COORDINATE WHERE RESTAURANT_COORDINATE.ID_RESTAURANT=? "
                + "AND RESTAURANT_COORDINATE.ID_COORDINATE=COORDINATES.ID"))
        {
            st.setInt(1, id_restaurant);
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    coordinate = new Coordinate();
                    coordinate.setLatitude(rs.getDouble("LATITUDE"));
                    coordinate.setLongitude(rs.getDouble("LONGITUDE"));
                    coordinate.setAddress(rs.getString("ADDRESS"));
                    coordinate.setCity(rs.getString("CITY"));
                    coordinate.setProvince(rs.getString("PROVINCE"));
                    coordinate.setState(rs.getString("STATE"));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return coordinate;
    }

    /**
     * Restituisce le HoursRange di un ristorante partendo dall'id del
     * ristorante.
     *
     * @param id_restaurant Id del ristorante.
     * @return Un ArrayList contentente oggetti HoursRange.
     * @throws SQLException
     */
    private ArrayList<HoursRange> getRestaurantHoursRanges(final int id_restaurant) throws SQLException
    {
        ArrayList<HoursRange> hours = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT "
                + "OPENING_HOURS_RANGES.DAY_OF_THE_WEEK, "
                + "OPENING_HOURS_RANGES.START_HOUR, "
                + "OPENING_HOURS_RANGES.END_HOUR FROM OPENING_HOURS_RANGES, "
                + "OPENING_HOURS_RANGE_RESTAURANT WHERE "
                + "OPENING_HOURS_RANGE_RESTAURANT.ID_RESTAURANT=? "
                + "AND OPENING_HOURS_RANGE_RESTAURANT.ID_OPENING_HOURS_RANGE="
                + "OPENING_HOURS_RANGES.ID ORDER BY DAY_OF_THE_WEEK ASC"))
        {
            st.setInt(1, id_restaurant);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    HoursRange hoursrange = new HoursRange();
                    hoursrange.setDay(rs.getInt("DAY_OF_THE_WEEK"));
                    hoursrange.setStart_hour(rs.getTime("START_HOUR"));
                    hoursrange.setEnd_hour(rs.getTime("END_HOUR"));
                    hours.add(hoursrange);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return hours;
    }

    /**
     * Un metodo per avere la posizione di un ristorante in una città (nello
     * stesso stato) rispetto al global_value. Se un ristorante avrà il
     * global_value più alto rispetto agli altri ristoranti nella sua città
     * allora il risultato sarà 1.
     *
     * @param global_value Il global_value del ristorante.
     * @param city La città del ristorante.
     * @param state Lo stato del ristorante.
     * @return Un intero pari alla posizione del ristorante in quella città in
     * base al global_value.
     * @throws SQLException
     */
    public int getRestaurantCityPosition(final int global_value, final String city, final String state) throws SQLException
    {
        int res = 1;
        try (PreparedStatement st = con.prepareStatement(
                "SELECT COUNT(*) AS COUNT FROM (SELECT RESTAURANTS.GLOBAL_VALUE FROM RESTAURANTS,"
                + "RESTAURANT_COORDINATE, COORDINATES WHERE "
                + "upper(COORDINATES.CITY)=? AND upper(COORDINATES.STATE)=? AND "
                + "RESTAURANTS.ID=RESTAURANT_COORDINATE.ID_RESTAURANT AND "
                + "RESTAURANT_COORDINATE.ID_COORDINATE=COORDINATES.ID) GLOBAL_VALUES "
                + "WHERE GLOBAL_VALUES.GLOBAL_VALUE > ?"))
        {
            st.setString(1, city.toUpperCase());
            st.setString(2, state.toUpperCase());
            st.setInt(3, global_value);
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    res = rs.getInt("COUNT") + 1;
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Recupera tutte le foto di un ristorante.
     *
     * @param id_restaurant L'id del ristorante.
     * @return Un ArrayList contenente oggetti Photo.
     * @throws SQLException
     */
    private ArrayList<Photo> getRestaurantPhotos(final int id_restaurant) throws SQLException
    {
        ArrayList<Photo> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM PHOTOS WHERE ID_RESTAURANT=?"))
        {
            st.setInt(1, id_restaurant);
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Photo photo = new Photo();
                    photo.setId(rs.getInt("ID"));
                    photo.setName(rs.getString("NAME"));
                    photo.setDescription(rs.getString("DESCRIPTION"));
                    photo.setPath(rs.getString("PATH"));
                    photo.setId_restaurant(rs.getInt("ID_RESTAURANT"));
                    photo.setId_owner(rs.getInt("ID_OWNER"));
                    res.add(photo);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce (al massimo) 5 contesti di ristoranti, ordinati per global
     * value, il primo ha quello più alto.
     *
     * @return Un ArrayList contenente i contesti.
     * @throws SQLException
     */
    public ArrayList<RestaurantContext> getTop5RestaurantContextsByValue() throws SQLException
    {
        ArrayList<RestaurantContext> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT ID FROM RESTAURANTS "
                + "ORDER BY GLOBAL_VALUE DESC FETCH FIRST 5 ROWS ONLY"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    RestaurantContext restaurantContext = getRestaurantContext(rs.getInt("ID"));
                    res.add(restaurantContext);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce (al massimo) 5 contesti di ristoranti, ordinati per
     * reviews_counter, il primo ha quello più alto.
     *
     * @return Un ArrayList contenente i contesti.
     * @throws SQLException
     */
    public ArrayList<RestaurantContext> getTop5RestaurantContextsByReviewsCounter() throws SQLException
    {
        ArrayList<RestaurantContext> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT ID FROM RESTAURANTS "
                + "ORDER BY REVIEWS_COUNTER DESC FETCH FIRST 5 ROWS ONLY"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    RestaurantContext restaurantContext = getRestaurantContext(rs.getInt("ID"));
                    res.add(restaurantContext);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce (al massimo) 5 contesti delle 5 review più recenti,
     * discendente.
     *
     * @return Un ArrayList contenente i contesti.
     * @throws SQLException
     */
    public ArrayList<ReviewContext> getLast5ReviewContexts() throws SQLException
    {
        ArrayList<ReviewContext> res = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT ID FROM REVIEWS "
                + "ORDER BY DATE_CREATION DESC FETCH FIRST 5 ROWS ONLY"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    ReviewContext reviewContext = getReviewContext(rs.getInt("ID"));
                    res.add(reviewContext);
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    private KShingling getKShingling() throws SQLException, IOException, ClassNotFoundException
    {
        KShingling res = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM KSHINGLING"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Blob blob = rs.getBlob("CONTENT");
                    InputStream ip = blob.getBinaryStream();
                    ObjectInputStream obs = new ObjectInputStream(ip);
                    res = (KShingling) obs.readObject();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    private void saveKShingling() throws SQLException, IOException
    {
        try (PreparedStatement st1 = con.prepareStatement("DELETE FROM KSHINGLING");
                PreparedStatement st2 = con.prepareStatement("INSERT INTO KSHINGLING VALUES(?)");)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(kshingling);
            byte[] byteS = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteS);
            st2.setBinaryStream(1, bais, byteS.length);
            st1.executeUpdate();
            st2.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Crea il profilo del ristorante nella tabella restaurant_profiles usato
     * per fuzzy search, se si sta modificando il ristorante usare
     * updateProfile.
     *
     * @param id Id del ristorante.
     * @param name Nome del ristorante.
     * @throws IOException
     * @throws SQLException
     */
    private void setProfile(final int id, final String name) throws IOException, SQLException
    {
        try (PreparedStatement st1 = con.prepareStatement("INSERT INTO RESTAURANTS_PROFILES VALUES(?,?)"))
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(kshingling.getProfile(name.toUpperCase()).getSparseVector());
            byte[] byteS = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteS);
            st1.setInt(1, id);
            st1.setBinaryStream(2, bais, byteS.length);
            st1.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Aggiorna il profilo del ristorante nella tabella restaurant_profiles,
     * usato per fuzzy search, se si sta creando il ristorante usate setProfile.
     *
     * @param id Id del ristorante.
     * @param name Nome del ristorante.
     * @throws IOException
     * @throws SQLException
     */
    private void updateProfile(final int id, final String name) throws IOException, SQLException
    {
        try (PreparedStatement st1 = con.prepareStatement("UPDATE RESTAURANTS_PROFILES SET VECTOR=? WHERE ID=?"))
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(kshingling.getProfile(name.toUpperCase()).getSparseVector());
            byte[] byteS = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteS);
            st1.setInt(2, id);
            st1.setBinaryStream(1, bais, byteS.length);
            st1.executeUpdate();
            con.commit();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    public void test(final String input) throws IOException, SQLException, ClassNotFoundException
    {
        SparseIntegerVector inputV = kshingling.getProfile(input).getSparseVector();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM TEST"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    String s = rs.getString("STRINGA");
                    Blob blob = rs.getBlob("VECTOR");
                    InputStream ip = blob.getBinaryStream();
                    ObjectInputStream obs = new ObjectInputStream(ip);
                    SparseIntegerVector res = (SparseIntegerVector) obs.readObject();
                    System.out.println(s + " = " + inputV.cosineSimilarity(res));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }

    /**
     * Recupera id dei ristoranti che hanno un nome simile a name.
     *
     * @param name Nome dei ristoranti da recuperare.
     * @return Un oggetto ArrayList<Integer>, vuoto se non esistono ristoranti
     * con quel nome o nomi simili.
     * @throws SQLException
     */
    public ArrayList<Integer> getRestaurantsIdByNameFuzzy(final String name) throws SQLException
    {
        SparseIntegerVector nameV = kshingling.getProfile(name.toUpperCase()).getSparseVector();
        ArrayList<Integer> res = new ArrayList<>();
        ArrayList<SimplePair> tmpRes = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM RESTAURANTS_PROFILES"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Blob blob = rs.getBlob("VECTOR");
                    InputStream ip = blob.getBinaryStream();
                    ObjectInputStream obs = new ObjectInputStream(ip);
                    SparseIntegerVector tmp = (SparseIntegerVector) obs.readObject();
                    double similarity = nameV.cosineSimilarity(tmp);
                    if (similarity >= SIMILARITY_THRESHOLD)
                    {
                        tmpRes.add(new SimplePair(rs.getInt("ID"), similarity));
                    }
                }
                Collections.sort(tmpRes);
                for (int i = 0; i < tmpRes.size(); i++)
                {
                    res.add(tmpRes.get(i).getId());
                }
            }
            catch (IOException | ClassNotFoundException ex)
            {
                Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce un ArrayList di id di ristoranti che hanno indirizzo, città,
     * stato, o complete_location pari a location, e nome simile a name.
     *
     * @param location Luogo del ristorante da cercare.
     * @param name Nome del ristorante da cercare.
     * @return Un oggetto ArrayList<int>, vuoto se non ci sono ristoranti con
     * quella location.
     * @throws SQLException
     */
    private ArrayList<Integer> getRestaurantsIdByLocationAndNameFuzzy(final String location, final String name) throws SQLException
    {
        SparseIntegerVector nameV = kshingling.getProfile(name.toUpperCase()).getSparseVector();
        ArrayList<Integer> res = new ArrayList<>();
        ArrayList<SimplePair> tmpRes = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT RISTO.ID,VECTOR FROM "
                + "(select RESTAURANT_COORDINATE.ID_RESTAURANT AS ID FROM "
                + "(select ID FROM COORDINATES WHERE upper(ADDRESS)=? OR upper(CITY)=? "
                + "OR upper(PROVINCE)=? OR upper(STATE)=? OR upper(COMPLETE_LOCATION)=?) IDCord, "
                + "RESTAURANT_COORDINATE WHERE IDCORD.ID=RESTAURANT_COORDINATE.ID_COORDINATE) "
                + "RISTO, RESTAURANTS_PROFILES WHERE RISTO.ID=RESTAURANTS_PROFILES.ID"))
        {
            st.setString(1, location.toUpperCase());
            st.setString(2, location.toUpperCase());
            st.setString(3, location.toUpperCase());
            st.setString(4, location.toUpperCase());
            st.setString(5, location.toUpperCase());
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    Blob blob = rs.getBlob("VECTOR");
                    InputStream ip = blob.getBinaryStream();
                    ObjectInputStream obs = new ObjectInputStream(ip);
                    SparseIntegerVector tmp = (SparseIntegerVector) obs.readObject();
                    double similarity = nameV.cosineSimilarity(tmp);
                    if (similarity >= SIMILARITY_THRESHOLD)
                    {
                        tmpRes.add(new SimplePair(rs.getInt("ID"), similarity));
                    }
                }
                Collections.sort(tmpRes);
                for (int i = 0; i < tmpRes.size(); i++)
                {
                    res.add(tmpRes.get(i).getId());
                }
            }
            catch (IOException | ClassNotFoundException ex)
            {
                Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }

    /**
     * Restituisce contesti di ristoranti cercandoli per location e name/cucina. La
     * ricerca su name è fuzzy, mentre location può essere pari a via, citta ,
     * stato o complete location. Location e/o name posso essere null. Quando la
     * ricerca comprende il nome/cucina vengono prima inseriti i ristoranti che hanno
     * una cucina corrispondente a nameOrCuisine, e poi i ristoranti in base alla
     * somiglianza del loro nome con name, in ordine discendente.
     *
     * @param location Luogo del ristorante.
     * @param nameOrCuisine Nome d ristorante.
     * @return I contesti dei ristoranti trovati.
     * @throws SQLException
     */
    public ArrayList<RestaurantContext> searchRestaurant(final String location, final String nameOrCuisine) throws SQLException
    {
        ArrayList<RestaurantContext> res = new ArrayList<>();
        ArrayList<Integer> tmp;
        if (location == null && nameOrCuisine == null)
        {
            return res;
        }
        else if (location == null)
        {
            tmp = getRestaurantIdsByCuisine(nameOrCuisine);
            ArrayList<Integer> tmp2= getRestaurantsIdByNameFuzzy(nameOrCuisine);
            for(Integer id: tmp2)
                if(!tmp.contains(id))
                    tmp.add(id);
        }
        else if (nameOrCuisine == null)
        {
            tmp = getRestaurantsIdByLocation(location);
        }
        else
        {
            tmp= getRestaurantsIdByLocationCuisine(location, nameOrCuisine);
            ArrayList<Integer> tmp2 = getRestaurantsIdByLocationAndNameFuzzy(location, nameOrCuisine);
            for(Integer id: tmp2)
                if(!tmp.contains(id))
                    tmp.add(id);
        }
        for (int i = 0; i < tmp.size(); i++)
        {
            res.add(getRestaurantContext(tmp.get(i)));
        }
        return res;
    }

    /**
     * Chiude la connessione al database. N.b. derby restituisce sempre un
     * eccezzione (error code 45000) quando si chiude con successo.
     */
    public void shutdown()
    {

        try
        {
            saveKShingling();
            System.out.println("saved shingling");
            DriverManager.getConnection("jdbc:derby://localhost:1527/eatbitDB;shutdown=true;user=eatbitDB;password=password;");
        }
        catch (SQLException | IOException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class SimplePair implements Comparable
    {

        private int id;
        private double similarity;

        public SimplePair(int id, double similiraty)
        {
            this.id = id;
            this.similarity = similiraty;
        }

        @Override
        public int hashCode()
        {
            return (int) (id * similarity * 10);
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final SimplePair other = (SimplePair) obj;
            if (this.id != other.id)
            {
                return false;
            }
            if (Double.doubleToLongBits(this.similarity) != Double.doubleToLongBits(other.similarity))
            {
                return false;
            }
            return true;
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public double getSimilarity()
        {
            return similarity;
        }

        public void setSimilarity(final double similarity)
        {
            this.similarity = similarity;
        }

        @Override
        public int compareTo(Object t)
        {//ordinare in ordine descending
            if (((SimplePair) t).getSimilarity() < similarity)
            {
                return -1;
            }
            else if (((SimplePair) t).getSimilarity() == similarity)
            {
                return 0;
            }
            else
            {
                return 1;
            }
        }

    }

    private void clearProfiles() throws SQLException
    {
        PreparedStatement st = con.prepareStatement("DELETE FROM RESTAURANTS_PROFILES");
        st.executeUpdate();
    }

    private void createAllProfiles() throws IOException, SQLException
    {
        try (PreparedStatement st = con.prepareStatement("SELECT ID,NAME FROM RESTAURANTS"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                {
                    setProfile(rs.getInt("ID"), rs.getString("NAME"));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
    }
    
    /**
     * Recupera il numero le notifiche di foto di un utente ristoratore, non le
     * notifiche di task da svolgere per un admin.
     *
     * @param id_user L'id dell'utente di cui si vuole cercare il numero di notifiche di
     * foto.
     * @return Il numero di notifiche di foto dell'utente.
     * @throws SQLException
     */
    public int getUserPhotoNotificationsCount(final int id_user) throws SQLException
    {
        int res=0;
        try (PreparedStatement st = con.prepareStatement("SELECT COUNT(*) FROM PHOTO_NOTIFICATIONS WHERE ID_USER=?"))
        {
            st.setInt(1, id_user);
            try (ResultSet rs = st.executeQuery())
            {
                if(rs.next())
                    res=rs.getInt(1);
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }
    
    /**
     * Recupera il numero le notifiche di review di un utente ristoratore, non le
     * notifiche di task da svolgere per un admin.
     * @param id_user L'id dell'utente di cui si vuole cercare le notifiche di
     * review.
     * @return Il numero di notifiche di review dell'utente.
     * @throws SQLException
     */
    public int getUserReviewNotificationsCount(final int id_user) throws SQLException
    {
        int res=0;
        try (PreparedStatement st = con.prepareStatement("SELECT COUNT(*) FROM REVIEW_NOTIFICATIONS WHERE ID_USER=?"))
        {
            st.setInt(1, id_user);
            try (ResultSet rs = st.executeQuery())
            {
                if(rs.next())
                    res=rs.getInt(1);
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }
    
    /**
     * Restituisce un array di oggetti Neighbour (nome,latitudine,longitudine) relativi
     * ad un ristorante. Vengono considerati vicini di un ristorante i ristoranti
     * che sono a una distanza minore del parametro distance, da considerarsi in
     * km.
     * Al momento nel vicinato di un ristorante è presente il ristorante stesso.
     * @param id_restaurant Id del ristorante di cui cercare i vicini.
     * @param distance Distanza in km dal ristorante.
     * @return I vicini del ristorante.
     * @throws SQLException 
     */
    public ArrayList<Neighbour> getRestaurantNeighbourhood(final int id_restaurant, final double distance) throws SQLException
    {
        ArrayList<Neighbour> res= new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT R.NAME,IDS.LATITUDE,IDS.LONGITUDE "
                + "FROM (SELECT C.ID,C.LATITUDE,C.LONGITUDE FROM COORDINATES C, "
                + "(SELECT ID,LATITUDE,LONGITUDE FROM COORDINATES WHERE ID =?) TARGET WHERE "
                + "(12742*ASIN(SQRT(0.5 - COS((TARGET.LATITUDE - C.LATITUDE) * "
                + "0.017453292519943295)/2 + COS(C.LATITUDE * 0.017453292519943295) * "
                + "COS(TARGET.LATITUDE * 0.017453292519943295) * (1- COS((TARGET.LONGITUDE - C.LONGITUDE) * "
                + "0.017453292519943295))/2))) < ?) IDS, RESTAURANTS R, RESTAURANT_COORDINATE RC "
                + "WHERE R.ID=RC.ID_RESTAURANT AND RC.ID_COORDINATE=IDS.ID"))
        {
            st.setInt(1, id_restaurant);
            st.setDouble(2, distance);
            try (ResultSet rs = st.executeQuery())
            {
                while(rs.next())
                {
                    Neighbour n= new Neighbour();
                    n.setName(rs.getString("NAME"));
                    n.setLatitude(rs.getDouble("LATITUDE"));
                    n.setLongitude(rs.getDouble("LONGITUDE"));
                    res.add(n);
                }
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }
    
    /**
     * Restituisce tutte i tipi di cucina al momento presenti nel db.
     * @return
     * @throws SQLException 
     */
    public ArrayList<String> getAllCuisines() throws SQLException
    {
        ArrayList<String> res=new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT NAME FROM CUISINES"))
        {
            try (ResultSet rs = st.executeQuery())
            {
                while(rs.next())
                    res.add(rs.getString("NAME"));
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }
    
    public PriceRange getPriceRangeById(int id) throws SQLException{
        PriceRange priceRange = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM PRICE_RANGES WHERE ID=?"))
        {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery())
            {
                if (rs.next())
                {
                    priceRange = new PriceRange();
                    priceRange.setMax(rs.getInt("MAX_VALUE"));
                    priceRange.setMin(rs.getInt("MIN_VALUE"));
                    priceRange.setName(rs.getString("NAME"));
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return priceRange;
    }
    
    /**
     * Restituisce gli id dei ristoranti con una distanza pari a distance
     * dal luogo indicato dai parametri longitude e latitude, e che hanno
     * fra le loro cucine quella indicata da cuisine.
     * Gli id restituiti sono restituiti per ordine di GLOBAL_VALUE dei 
     * relativi ristoranti.
     * @param cuisine La cucina che i ristoranti di cui si ricava l'id devono
     * avere.
     * @param longitude Longitudine del punto di partenza della ricerca.
     * @param latitude Latitudine del punto di partenza della ricerca.
     * @param distance Distanza in km dal ristorante.
     * @return Id dei ristoranti vicini con quella cucina.
     * @throws SQLException 
     */
    public ArrayList<Integer> getRestaurantIdsNearLocationByCuisine(final double longitude, final double latitude, final double distance, final String cuisine) throws SQLException
    {
        ArrayList<Integer> res= new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT RDIST.ID FROM "
                + "(SELECT R.ID, GLOBAL_VALUE FROM (SELECT C.ID FROM "
                + "COORDINATES C WHERE (12742*ASIN(SQRT(0.5 - COS((? - C.LATITUDE) "
                + "* 0.017453292519943295)/2 + COS(C.LATITUDE * 0.017453292519943295) "
                + "* COS(? * 0.017453292519943295) * (1- COS((? - C.LONGITUDE) * 0.017453292519943295))/2))) "
                + "< ?) IDS, RESTAURANTS R, RESTAURANT_COORDINATE RC WHERE R.ID=RC.ID_RESTAURANT "
                + "AND RC.ID_COORDINATE=IDS.ID) RDIST, RESTAURANT_CUISINE, CUISINES WHERE upper(CUISINES.NAME)=? "
                + "AND RDIST.ID=RESTAURANT_CUISINE.ID_RESTAURANT AND RESTAURANT_CUISINE.ID_CUISINE=CUISINES.ID "
                + "ORDER BY RDIST.GLOBAL_VALUE DESC"))
        {
            st.setDouble(1, latitude);
            st.setDouble(2, latitude);
            st.setDouble(3, longitude);
            st.setDouble(4, distance);
            st.setString(5, cuisine.toUpperCase());
            try (ResultSet rs = st.executeQuery())
            {
                while(rs.next())
                    res.add(rs.getInt("ID"));
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }
 
    /**
     * Restituisce gli id dei ristoranti con una distanza pari a distance
     * dal luogo indicato dai parametri longitude e latitude.
     * Gli id restituiti sono restituiti per ordine di GLOBAL_VALUE dei 
     * relativi ristoranti.
     * @param longitude Longitudine del punto di partenza della ricerca.
     * @param latitude Latitudine del punto di partenza della ricerca.
     * @param distance Distanza in km dal ristorante.
     * @return Id dei ristoranti vicini con quella cucina.
     * @throws SQLException 
     */
    public ArrayList<Integer> getRestaurantIdsNearLocation(final double longitude, final double latitude, final double distance) throws SQLException
    {
        ArrayList<Integer> res= new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT RDIST.ID FROM "
                + "(SELECT R.ID, GLOBAL_VALUE FROM (SELECT C.ID "
                + "FROM COORDINATES C WHERE (12742*ASIN(SQRT(0.5 - COS((? - C.LATITUDE) "
                + "* 0.017453292519943295)/2 + COS(C.LATITUDE * 0.017453292519943295) * "
                + "COS(? * 0.017453292519943295) * (1- COS((? - C.LONGITUDE) * 0.017453292519943295))/2))) "
                + "< ?) IDS, RESTAURANTS R, RESTAURANT_COORDINATE RC WHERE R.ID=RC.ID_RESTAURANT "
                + "AND RC.ID_COORDINATE=IDS.ID) RDIST ORDER BY RDIST.GLOBAL_VALUE DESC"))
        {
            st.setDouble(1, latitude);
            st.setDouble(2, latitude);
            st.setDouble(3, longitude);
            st.setDouble(4, distance);
            try (ResultSet rs = st.executeQuery())
            {
                while(rs.next())
                    res.add(rs.getInt("ID"));
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }
    
    /**
     * Restituisce gli id di ristoranti che hanno una certa cucina, ordinati per
     * GLOBAL_VALUE.
     * @param cuisine
     * @return
     * @throws SQLException 
     */
    public ArrayList<Integer> getRestaurantIdsByCuisine(String cuisine) throws SQLException{
        ArrayList<Integer> res= new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT R.ID FROM RESTAURANTS R, "
                + "RESTAURANT_CUISINE RC, CUISINES C WHERE upper(C.NAME)=? "
                + "AND R.ID=RC.ID_RESTAURANT AND RC.ID_CUISINE=C.ID ORDER BY R.GLOBAL_VALUE DESC"))
        {
            st.setString(1, cuisine.toUpperCase());
            try (ResultSet rs = st.executeQuery())
            {
                while(rs.next())
                    res.add(rs.getInt("ID"));
                con.commit();
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }
    
    /**
     * Restituisce un ArrayList di id di ristoranti che hanno indirizzo, città,
     * stato, o complete_location pari a location, e (una) cucina uguale a cuisine.
     *
     * @param location Luogo del ristorante da cercare.
     * @param cuisine Nome della cucina.
     * @return Un oggetto ArrayList<int>, vuoto se non ci sono ristoranti con
     * quella location o con quella cucina in quella location.
     * @throws SQLException
     */
    private ArrayList<Integer> getRestaurantsIdByLocationCuisine(final String location, final String cuisine) throws SQLException
    {
        ArrayList<Integer> res= new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT RISTO.ID FROM "
                + "(select RESTAURANT_COORDINATE.ID_RESTAURANT AS ID FROM "
                + "(select ID FROM COORDINATES WHERE upper(ADDRESS)=? OR "
                + "upper(CITY)=? OR upper(PROVINCE)=? OR upper(STATE)=? OR upper(COMPLETE_LOCATION)=?) "
                + "IDCord, RESTAURANT_COORDINATE WHERE "
                + "IDCORD.ID=RESTAURANT_COORDINATE.ID_COORDINATE) "
                + "RISTO, RESTAURANT_CUISINE RC, CUISINES C WHERE upper(C.NAME)=? "
                + "AND RISTO.ID=RC.ID_RESTAURANT AND RC.ID_CUISINE=C.ID"))
        {
            st.setString(1, location.toUpperCase());
            st.setString(2, location.toUpperCase());
            st.setString(3, location.toUpperCase());
            st.setString(4, location.toUpperCase());
            st.setString(5, location.toUpperCase());
            st.setString(6, cuisine.toUpperCase());
            try (ResultSet rs = st.executeQuery())
            {
                while (rs.next())
                    res.add(rs.getInt("ID"));
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }
    
    /**
     * Restituisce contesti di ristoranti cercandoli per vicinanza a una longitudine e 
     * latitudine e se hanno una cucina=nameOrCuisine o nome simile a nameOrCuisine.
     * La ricerca su name è fuzzy, nameOrCuisine può essere null per indicare una ricerca
     * basata solo sulla distanza. Quando la ricerca comprende il nome/cucina vengono 
     * prima inseriti i ristoranti che hanno una cucina corrispondente a nameOrCuisine, 
     * e poi i ristoranti in base alla somiglianza del loro nome con name, in ordine discendente.
     *
     * @param longitude
     * @param nameOrCuisine Nome del ristorante o sua cucina.
     * @param latitude 
     * @param distance Distanza in km.
     * @return I contesti dei ristoranti trovati.
     * @throws SQLException
     */
    public ArrayList<RestaurantContext> searchRestaurantNear(final double longitude, final double latitude, final double distance, final String nameOrCuisine) throws SQLException
    {
        ArrayList<RestaurantContext> res = new ArrayList<>();
        ArrayList<Integer> tmp;
        if(nameOrCuisine==null)
            tmp= getRestaurantIdsNearLocation(longitude, latitude, distance);
        else
        {
            ArrayList<Integer> tmp2= getRestaurantIdsNearLocationByName(longitude,latitude,distance,nameOrCuisine);
            tmp=getRestaurantIdsNearLocationByCuisine(longitude,latitude,distance,nameOrCuisine);
            for(Integer id: tmp2)
                if(!tmp.contains(id))
                    tmp.add(id);
        }
        for (int i = 0; i < tmp.size(); i++)
        {
            res.add(getRestaurantContext(tmp.get(i)));
        }
        return res;
    }
    
    /**
     * Restituisce gli id dei ristoranti con una distanza pari a distance
     * dal luogo indicato dai parametri longitude e latitude e che hanno
     * un nome simile al parametro name.
     * Gli id restituiti sono restituiti per ordine di somiglianza fra il loro
     * nome e il parametro name.
     * @param longitude Longitudine del punto di partenza della ricerca.
     * @param latitude Latitudine del punto di partenza della ricerca.
     * @param distance Distanza in km dal ristorante.
     * @param name Nome del ristorante da cercare in maniera fuzzy.
     * @return Id dei ristoranti vicini con quella cucina.
     * @throws SQLException 
     */
    public ArrayList<Integer> getRestaurantIdsNearLocationByName(final double longitude, final double latitude, final double distance, final String name) throws SQLException
    {
        SparseIntegerVector nameV = kshingling.getProfile(name.toUpperCase()).getSparseVector();
        ArrayList<Integer> res= new ArrayList<>();
        ArrayList<SimplePair> tmpRes = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement("SELECT DISTID.ID, "
                + "VECTOR FROM (SELECT C.ID FROM COORDINATES C WHERE "
                + "(12742*ASIN(SQRT(0.5 - COS((? - C.LATITUDE) * 0.017453292519943295)/2 + "
                + "COS(C.LATITUDE * 0.017453292519943295) * COS(? * 0.017453292519943295) "
                + "* (1- COS((? - C.LONGITUDE) * 0.017453292519943295))/2))) < ?) DISTID, "
                + "RESTAURANTS_PROFILES RP WHERE RP.ID=DISTID.ID"))
        {
            st.setDouble(1, latitude);
            st.setDouble(2, latitude);
            st.setDouble(3, longitude);
            st.setDouble(4, distance);
            try (ResultSet rs = st.executeQuery())
            {
                while(rs.next())
                {
                    Blob blob = rs.getBlob("VECTOR");
                    InputStream ip = blob.getBinaryStream();
                    ObjectInputStream obs = new ObjectInputStream(ip);
                    SparseIntegerVector tmp = (SparseIntegerVector) obs.readObject();
                    double similarity = nameV.cosineSimilarity(tmp);
                    if (similarity >= SIMILARITY_THRESHOLD)
                    {
                        tmpRes.add(new SimplePair(rs.getInt("ID"), similarity));
                    }
                }
                Collections.sort(tmpRes);
                for (int i = 0; i < tmpRes.size(); i++)
                {
                    res.add(tmpRes.get(i).getId());
                }
                con.commit();
            }
            catch (IOException | ClassNotFoundException ex)
            {
                Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            con.rollback();
            throw ex;
        }
        return res;
    }
    }
