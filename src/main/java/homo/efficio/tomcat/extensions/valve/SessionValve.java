package homo.efficio.tomcat.extensions.valve;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by hanmomhanda on 16. 2. 23.
 */
public class SessionValve extends ValveBase{
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String sessionId = request.getRequestedSessionId();
        System.out.println("=== session : " + session);
        System.out.println("=== sessionId : " + sessionId);

        getNext().invoke(request, response);
    }
}
