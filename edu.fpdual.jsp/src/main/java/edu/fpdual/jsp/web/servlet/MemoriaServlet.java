package edu.fpdual.jsp.web.servlet;

import edu.fpdual.jsp.client.UsuarioClient;
import edu.fpdual.jsp.client.dto.UsuarioDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/memorias")
public class MemoriaServlet extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("/comun/memoria.jsp").forward(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    UsuarioDto usuario = (UsuarioDto) request.getSession().getAttribute("usuarioSesion");
    String pregunta = request.getParameter("answer");
    int operacion = 32 * 1024 - (5 * 500);

    String mensaje = calcularMensaje(pregunta, operacion);
    int puntuacion = calcularPuntuacion(pregunta, operacion, usuario);

    request.setAttribute("mensaje", mensaje);
    request.setAttribute("puntuacion", puntuacion);

    request.getRequestDispatcher("/comun/memoria.jsp").forward(request, response);
  }

  private String calcularMensaje(String pregunta, int operacion) {
    return (String)
        ((java.util.function.Function<String, String>)
                (p) -> {
                  if (p.equals(String.valueOf(operacion))) {
                    return "¡Respuesta correcta! Al pendrive le queda "
                        + operacion
                        + " MB de memoria.";

                  } else {
                    return "Respuesta incorrecta. Inténtalo de nuevo.";
                  }
                })
            .apply(pregunta);
  }

  private int calcularPuntuacion(String pregunta, int operacion, UsuarioDto usuario) {
    if (pregunta != null && pregunta.equals(String.valueOf(operacion))) {
      new UsuarioClient().updatePuntos(100, usuario.getNombre());
      return 100;
    } else {
      return 0;
    }
  }
}
