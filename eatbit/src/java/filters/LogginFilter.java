package filters;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author User
 */
public class LogginFilter implements Filter {
    
  
    public void init(FilterConfig fc) throws ServletException {
        
    }

    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest)sr;
        final HttpServletResponse resp = (HttpServletResponse)sr1;

        // controllo se la sessione esiste e contiene un utente loggato
        // se non esiste o non conteiene un utente, ridirigo alla pagina iniziale di login
        HttpSession session = (req).getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath());
        } else {
            fc.doFilter(sr, sr1);
        }
    }

    public void destroy() {
        
    }
}
