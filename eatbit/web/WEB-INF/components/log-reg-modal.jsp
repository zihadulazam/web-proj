<!-- Modals -->

    <!-- Login Modal -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    <div class="modal fade login" id="loginModal">
            <div class="modal-dialog login animated">
                <div class="modal-content">
                    <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h3 class="modal-title">Login</h3>
                </div>
                <div class="modal-body">  
                    <div class="box">
                            <div class="content">
                            <div class="error"></div>
                            <div class="form loginBox">
                                <form method="post" accept-charset="ISO-8859-1" id="login-form">
                                    <!-- da verifivare id emailor Nickname -->
                                    <div class="form-group">
                                        <label for="emailorNickname">Email o Nickname:</label>
                                        <input id="emailorNickname" class="form-control" type="text" placeholder="Email o Nickname" name="emailOrNickname">
                                    </div>
                                    <div class="form-group">
                                        <label for="password">Password:</label>
                                        <input id="password" class="form-control" type="password" placeholder="Password" name="password">
                                    </div>
                                    <button class="btn btn-success btn-login" type="submit" aria-label="Left Align">
                                        <span class="glyphicon glyphicon glyphicon glyphicon-lock" aria-hidden="true"></span>
                                        Accedi
                                    </button>
                                </form>
                            </div>
                            </div>
                    </div>
                    
                    <!-- Register Modal -->
                    
                    <div class="box">
                        <div class="content registerBox" style="display:none;">
                            <div class="form">
                            <form method="post" html="{:multipart=>true}" data-remote="true" action="" accept-charset="UTF-8" id="register-form">
                                <div class="form-group">
                                    <label for="name">Nome:</label>
                                    <input id="name" class="form-control" type="text" placeholder="Nome" name="name">
                                </div>
                                <div class="form-group">
                                    <label for="surname">Cognome:</label>                                          
                                    <input id="surname" class="form-control" type="text" placeholder="Cognome" name="surname">
                                </div>
                                <div class="form-group">
                                    <label for="nickname">Nickname:</label> 
                                    <input id="nickname" class="form-control" type="text" placeholder="Nickname" name="nickname">
                                </div>
                                <div class="form-group">
                                    <label for="email">Email:</label> 
                                    <input id="email" class="form-control" type="text" placeholder="Email" name="email">
                                </div>
                                <div class="form-group">
                                    <label for="password">Password:</label> 
                                    <input id="regPassword" class="form-control" type="password" placeholder="Password" name="password">
                                </div>
                                <div class="form-group">
                                    <label for="password_confirmation">Ripeti Password:</label> 
                                    <input id="password_confirmation" class="form-control" type="password" placeholder="Ripeti Password" name="password_confirmation">
                                    <div class="error-pass"></div>
                                </div>
                                <div class="form-group">
                                <label for="password">*Privacy:</label> 
                                <pre class=".pre-scrollable" style="max-height: 200px;">
EatBit ("eatBit", "il sito", "il portale", "il sito web") è un servizio online che offre agli utenti ("utenti") consigli relativi a ristoranti. Il sito web, i componenti per dispositivi mobili e le applicazioni correlate (insieme indicati come il "portale") fanno parte di EatBit povo corporation.
Effettuando l'accesso al portale e utilizzando i servizi a disposizione, l'utente dichiara di aver letto e compreso la presente Informativa sulla privacy e le disposizioni in materia di raccolta e trattamento delle informazioni ivi incluse.
L'ultimo aggiornamento della presente Informativa sulla privacy risale al giorno 20 giugno 2014. EatBit si riserva il diritto di modificare periodicamente la Informativa sulla privacy, si consiglia pertanto di prenderne regolarmente visione al fine di essere al corrente degli aggiornamenti.
<strong>Dati raccolti</strong><br>
<strong>Informazioni di carattere generale.</strong><br>
EatBit riceve e archivia le informazioni inserite dagli utenti attraverso il portale o altro mezzo. Tali informazioni includono dati che consentono di identificare personalmente gli utenti o di contattarli direttamente ("dati personali").
I dati personali includono le informazioni fornite dagli utenti, ad esempio nome e cognome, indirizzo postale e email, nome utente e password.
L'utente ha la facoltà  di scegliere di non fornire tali dati personali a EatBit. Tuttavia, di norma, alcune informazioni sono necessarie al fine di creare un account. 
Altre informazioni che potrebbero essere richieste includono dati quali l'indirizzo IP, i dati di identificazione del dispositivo utilizzato e la cronologia di navigazione online, a condizione che tali informazioni siano esclusivamente pertinenti all'utente in questione.
Quando si utilizza un'applicazione su un dispositivo, EatBit, con il consenso dellâ€™utente, raccoglie le informazioni sull'utilizzo da parte dell'utente in modo analogo e per scopi simili rispetto all'uso del sito web di EatBit. Inoltre, potrebbero essere raccolte informazioni sulla località  qualora l'utente abbia impostato il dispositivo per l'invio di tali informazioni all'applicazione nelle impostazioni sulla privacy del dispositivo.
EatBit raccoglie informazioni attraverso cookie e altre tecnologie analoghe (ad esempio web beacon). I cookie sono piccoli file di testo automaticamente memorizzati sul computer o sul dispositivo mobile ogni volta che si visita un qualsiasi sito web. Vengono archiviati dal browser Internet e contengono informazioni di base sull'uso di Internet da parte dell'utente. A ogni nuova visita, il browser invia i cookie al sito in modo che il computer o il dispositivo mobile utilizzato possa essere riconosciuto al fine di migliorare e personalizzare l'esperienza.
<strong>Policy sui Cookies</strong>
Noi di EatBit vogliamo assicurarci che la vostra visita al nostro sito web sia agevole, affidabile e il più possibile utile per voi. Per aiutarci a farlo, utilizziamo cookies e tecnologie simili (collettivamente denominati "cookies" in questa policy).
<strong>Cosa sono i cookies?</strong>
I cookies sono piccoli file di testo che vengono automaticamente posizionati sul Vostro computer o dispositivo mobile quando visitate quasi tutti i siti web. Essi vengono memorizzati dal browser Internet. I cookies contengono informazioni di base sul vostro utilizzo di Internet, ma non vi identificano personalmente. Il vostro browser rinvia questi cookies al sito ogni volta che lo visitate nuovamente, cosÃ¬ che possa riconoscere il vostro computer o dispositivo mobile e personalizzare e migliorare la vostra esperienza sul sito.
Alcuni cookies, chiamati "cookies di sessione", rimangono sul vostro computer solo mentre il vostro browser è aperto e vengono eliminati automaticamente una volta che voi chiudete il browser. Altri cookies, chiamati "cookies permanenti", rimangono sul vostro computer o dispositivo mobile dopo che il browser viene chiuso. Ciò permette che i siti riconoscano il vostro computer o dispositivo mobile quando successivamente riaprirete il vostro browser per fornirvi una navigazione che sia la più agevole possibile.
Ad esempio, utilizziamo i cookies sul nostro sito web per memorizzare i vostri dettagli di accesso, in modo che voi non dobbiate reinserirli piÃ¹ volte durante la vostra visita al sito.
Noi di EatBit vogliamo rendere la vostra esperienza sul nostro sito il piÃ¹ agevole e il piÃ¹ piacevole possibile.
Inoltre, potete gestire i cookies attraverso le impostazioni del vostro browser Internet. Potete impostare il browser in modo tale che vi avverta quando ricevete un nuovo cookies, possiate cancellare i singoli cookies o cancellare tutti i cookies. Consultate il sito www.allaboutcookies.org per semplici istruzioni su come gestire i cookies su browser differenti. Consultate il sito www.adobe.com/eeurope/special/products/flashplayer/articles/lso/ per un utile sommario di come gestire i cookies Flash.
Vi preghiamo di notare che, se si sceglie di eliminare i cookies di EatBit, il vostro accesso ad alcune funzionalitÃ  e aree del nostro sito Web potrebbe essere peggiorato o limitato.
Per qualsiasi chiarimento potete contattarci utilizzando l'area di testo in fondo alla pagina (nel footer).
                                </pre>
                                </div>
                                <div class="form-group">
                                    <div class="checkbox">
                                        <label><input type="checkbox" id="privacy_accept">Accetto le norme di Privacy</label>
                                    </div>
                                </div>
                                <button class="btn btn-default btn-register" type="submit" name="commit" aria-label="Left Align" type="submit" id="btn-reg" disabled="disabled">
                                    <span class="glyphicon glyphicon glyphicon glyphicon-check" aria-hidden="true"></span>
                                    Crea un account
                                </button>
                            </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="forgot login-footer">
                        <!-- Da modificare e aggiungere la funzione recupera password-->
                        <span> 
                                <a href="javascript: recuperPsw();"> Recupera password</a>
                        </span>
                        <div class="division">
                                <div class="line l"></div>
                                    <span>o</span>
                                <div class="line r"></div>
                        </div>
                        <span>Vuoi 
                                <a href="javascript: showRegisterForm();">creare un account</a>
                        ?</span>
                    </div>
                    <div class="forgot register-footer" style="display:none">
                            <span>Hai gia' un account?</span>
                            <a href="javascript: showLoginForm();">Login</a>
                    </div>
                </div>        
                </div>
            </div>
        </div>
<!-- End Modals -->