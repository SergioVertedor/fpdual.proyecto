package edu.fpdual.jsp.web.servlet;

import edu.fpdual.jsp.web.dto.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "ServletLogin",
    urlPatterns = {"/servlet-login"})
public class ServletLogin extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    doPost(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioSesion");
    if (usuario != null) {
      homePage(resp, usuario);
    } else {
      String usuarioConfigurado = getServletContext().getInitParameter("usuario");
      String passwordConfigurado = getServletContext().getInitParameter("password");
      String usuarioIntroducido = req.getParameter("usuario");
      String passwordIntroducido = req.getParameter("contrasena");
      if ((usuarioIntroducido != null && usuarioIntroducido.equals(usuarioConfigurado))
          && (passwordIntroducido != null && passwordIntroducido.equals(passwordConfigurado))) {
        usuario =
            Usuario.builder().usuario(usuarioIntroducido).password(passwordIntroducido).build();

        req.getSession().setAttribute("usuarioSesion", usuario);
        homePage(resp, usuario);
      } else {
        resp.sendRedirect("/jsp/login/login.jsp");
      }
    }
  }

  private void homePage(HttpServletResponse resp, Usuario usuario) throws IOException {
    PrintWriter writer = resp.getWriter();
    writer.println("<html>");
    writer.println("<body>");
    writer.println("<h2>Bienvenido --> " + usuario.getUsuario() + "</h2></br>");
    writer.println("<form action=\"/jsp/servlet-volver-login\" method=\"GET\">");
    writer.println("<button type=\"submit\">Volver Login</button>");
    writer.println("</form>");
    writer.println("</body>");
    writer.println("</html>");
  }
}
