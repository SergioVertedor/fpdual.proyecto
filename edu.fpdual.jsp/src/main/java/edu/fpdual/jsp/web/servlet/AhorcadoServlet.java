package edu.fpdual.jsp.web.servlet;

import edu.fpdual.jsp.client.UsuarioClient;
import edu.fpdual.jsp.client.dto.UsuarioDto;
import java.io.IOException;
import java.util.Arrays;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ahorcado")
public class AhorcadoServlet extends HttpServlet {




    private String[] palabras = {"codigo", "binario", "string", "comando","bug", "depurar", "compilar","bucle",
            "array","variable","algoritmo","interfaz","clase","objeto","refactorizacion"};
    private String palabra = null;
    private char[] letrasAdivinadas;
    private int intentos = 10;

    private int puntuacion = 0;


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsuarioDto usuario = (UsuarioDto) request.getSession().getAttribute("usuarioSesion");

        if (palabra == null) {
            palabra = palabras[(int) (Math.random() * palabras.length)];
            letrasAdivinadas = new char[palabra.length()];
            Arrays.fill(letrasAdivinadas, '-');
            intentos = 10;
        }

        String letra = request.getParameter("letra");

        if (letra != null) {
            if (!letra.matches("[a-zA-Z]")) {
                request.setAttribute("mensaje", "Error: Ingresa solo letras.");
            } else {
                letra = letra.toLowerCase();
                boolean acierto = false;
                for (int i = 0; i < palabra.length(); i++) {
                    if (palabra.charAt(i) == letra.charAt(0)) {
                        letrasAdivinadas[i] = letra.charAt(0);
                        acierto = true;
                    }
                }
                if (!acierto) {
                    intentos--;
                }
            }
        }

        if (!String.valueOf(letrasAdivinadas).contains("-")) {
            request.setAttribute("mensaje", "¡ENHORABUENA, Ganaste! La palabra era " + palabra);
            request.setAttribute("reiniciar", true);
            palabra = null;
            puntuacion += 50; // Incrementar la puntuación en 50 puntos por adivinar la palabra
            new UsuarioClient().updatePuntos(puntuacion, usuario.getNombre());
        } else if (intentos == 0) {
            request.setAttribute("mensaje", "Perdiste :( La palabra era " + palabra);
            request.setAttribute("reiniciar", true);
            palabra = null;
        }


        request.setAttribute("letrasAdivinadas", String.valueOf(letrasAdivinadas));
        request.setAttribute("intentos", intentos);
        request.setAttribute("puntuacion", puntuacion);


        request.getRequestDispatcher("/comun/ahorcado.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }


}