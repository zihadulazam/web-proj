/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Collections;

/**
 *
 * @author jacopo
 */
public class IpFinder
{
    private IpFinder()
    {}
    
    /**
     * Tenta di recuperare l'ip pubblico con il quale si contatta il resto
     * di internet, se ciò non è possibile proverà a recuperare l'ip
     * locale.
     * @return Una stringa che rappresenta l'ip pubblico, in dotted decimal. Se
     *  non è stato possibile trovare nessun ip la stringa sarà pari a "ERROR".
     */
    final public static String getPublicIp()
    {
        //prova a cercare l'ip pubblico
        try
        {
            URL url = new URL("https://api.ipify.org");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ipAddress;
            ipAddress = (in.readLine()).trim();
            /*se il sito non risponde o non si è conessi a internet
            provo a recuperare l'ip locale
            */
            if (!(ipAddress.length() > 0))
            {
                try
                {
                    InetAddress ip = InetAddress.getLocalHost();
                    return ((ip.getHostAddress()).trim());
                }
                catch(Exception ex)
                {
                    return "ERROR";
                }
            }
            return (ipAddress);
        }
        catch(Exception e)
        {
            try
            {
                InetAddress ip = InetAddress.getLocalHost();
                return ((ip.getHostAddress()).trim());
            }
            catch(Exception ex)
            {
                return "ERROR";
            }
        }
    }
    
    /**
     * Tenta di recuperare l'ip locale,  manda una eccezzione se non è possibile
     * recuperare nessun ip.
     * @return Una stringa in dotted decimal che rappresenta l'ip.
     * @throws java.net.SocketException 
     */
    final public static String getLocalIp() throws SocketException
    {
    return Collections.list(NetworkInterface.getNetworkInterfaces()).stream()
            .flatMap(i -> Collections.list(i.getInetAddresses()).stream())
            .filter(ip -> ip instanceof Inet4Address && ip.isSiteLocalAddress())
            .findFirst().orElseThrow(RuntimeException::new)
            .getHostAddress();
    }
}
