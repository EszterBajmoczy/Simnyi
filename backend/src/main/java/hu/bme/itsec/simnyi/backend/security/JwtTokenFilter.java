package hu.bme.itsec.simnyi.backend.security;

import hu.bme.itsec.simnyi.backend.repository.UserRepository;
import hu.bme.itsec.simnyi.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!jwtTokenUtil.validate(token)) {
            chain.doFilter(request, response);
            return;
        }

        userRepository.findUserByUsername(jwtTokenUtil.getUsernameFromToken(token))
                .ifPresentOrElse(userDetails -> jwtTokenUtil.setSecurityContextHolder(userDetails, request), () -> {
                    logger.error("token hiba");
                    jwtTokenUtil.setSecurityContextHolder(null, request);
                });

        chain.doFilter(request, response);
    }




}
