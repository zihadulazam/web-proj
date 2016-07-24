<!-- Modals -->

    <!-- Login Modal -->

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
                                <form method="post" accept-charset="UTF-8" id="login-form">
                                    <!-- da verifivare id emailor Nickname -->
                                    <div class="form-group">
                                        <label for="emailorNickname">Email o Nickname:</label>
                                        <input id="emailorNickname" class="form-control" type="text" placeholder="Email o Nickname" name="emailorNickname">
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
                                    <input id="regPassword" class="form-control" type="password" placeholder="Password" name="regPassword">
                                </div>
                                <div class="form-group">
                                    <label for="password_confirmation">Ripeti Password:</label> 
                                    <input id="password_confirmation" class="form-control" type="password" placeholder="Ripeti Password" name="password_confirmation">
                                    <div class="error-pass"></div>
                                </div>
                                <button class="btn btn-default btn-register" type="submit" name="commit" aria-label="Left Align" type="submit">
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
                                <a href="javascript: loginAjax();"> Recupera password</a>
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