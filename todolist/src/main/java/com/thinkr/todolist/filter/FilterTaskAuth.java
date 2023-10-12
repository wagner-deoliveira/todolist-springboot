package com.thinkr.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.thinkr.todolist.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public FilterTaskAuth(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var servelPath = request.getServletPath();

        if (servelPath.startsWith("/tasks")) {

            var authorization = request.getHeader("Authorization");
            var authEncoded = authorization.substring("Basic".length()).trim();
            var authDecode = Base64.getDecoder().decode(authEncoded);

            var authString = new String(authDecode);
            var credentials = authString.split(":");

            var username = credentials[0];
            var password = credentials[1];

            var user = userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401, "User not authenticated");
                return;
            }

            var verification = BCrypt.verifyer().verify(password.getBytes(), user.getPassword().getBytes());

            if (verification.verified) {
                request.setAttribute("idUser", user.getId());
                filterChain.doFilter(request, response);
            } else {
                response.sendError(401);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
