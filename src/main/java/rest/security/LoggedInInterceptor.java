/*
 * Demoiselle Framework
 * Copyright (C) 2010 SERPRO
 * ----------------------------------------------------------------------------
 * This file is part of Demoiselle Framework.
 * 
 * Demoiselle Framework is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License version 3
 * along with this program; if not,  see <http://www.gnu.org/licenses/>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 * ----------------------------------------------------------------------------
 * Este arquivo é parte do Framework Demoiselle.
 * 
 * O Framework Demoiselle é um software livre; você pode redistribuí-lo e/ou
 * modificá-lo dentro dos termos da GNU LGPL versão 3 como publicada pela Fundação
 * do Software Livre (FSF).
 * 
 * Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA
 * GARANTIA; sem uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou
 * APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU/LGPL em português
 * para maiores detalhes.
 * 
 * Você deve ter recebido uma cópia da GNU LGPL versão 3, sob o título
 * "LICENCA.txt", junto com esse programa. Se não, acesse <http://www.gnu.org/licenses/>
 * ou escreva para a Fundação do Software Livre (FSF) Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
 */
package rest.security;

import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.util.Beans;
import core.entity.User;
import core.security.Authenticator;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

@LoggedIn
@Interceptor
public class LoggedInInterceptor implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String AUTH_SCHEMA = "Bearer";

    @AroundInvoke
    public Object manage(final InvocationContext ic) throws Exception {
        Authenticator authenticator = Authenticator.getInstance();
        User loggedIn = authenticator.getLoggedIn();

        if (loggedIn == null) {
            String token = getToken();
            authenticator.authenticate(token);
        }

        return ic.proceed();
    }

    private String getToken() {
        String result = null;

        HttpServletRequest request = Beans.getReference(HttpServletRequest.class);
        String header = request.getHeader("Authorization");

        if (header != null && !header.isEmpty()) {
            result = extractToken(header);
        }

        return result;
    }

    private String extractToken(String header) {
        String result = null;

        String regexp = "^" + AUTH_SCHEMA + "[ \\n]+(.+)$";
        Pattern pattern = Pattern.compile(regexp, CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(header);

        if (matcher.matches()) {
            result = matcher.group(1);
        }

        return result;
    }
}
