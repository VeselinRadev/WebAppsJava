package com.fmi.insurance.service;

import com.fmi.insurance.dto.AuthResponseDto;
import com.fmi.insurance.dto.RegisterRequestDto;
import com.fmi.insurance.error.UnauthorizedException;
import com.fmi.insurance.model.Insurer;
import com.fmi.insurance.model.RefreshToken;
import com.fmi.insurance.repository.InsurerRepository;
import com.fmi.insurance.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    public static final int REFRESH_TOKEN_EXPIRY = 7;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final InsurerRepository insurerRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public void register(RegisterRequestDto request) {
        if (insurerRepository.existsByUsername(request.getUsername()))
            throw new UnauthorizedException("Username already exists");

        if (insurerRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new UnauthorizedException("Phone number already exists");

        Insurer insurer = Insurer.builder()
                .username(request.getUsername())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        insurerRepository.save(insurer);
    }

    public AuthResponseDto login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid username or password");
        }

        Insurer insurer = (Insurer) customUserDetailsService.loadUserByUsername(username);

        refreshTokenRepository.deleteAllByInsurerId(insurer.getId());
        String accessToken = jwtService.generateToken(insurer);
        String refreshToken = createRefreshToken(insurer);
        return new AuthResponseDto(accessToken, refreshToken);
    }

    public AuthResponseDto refresh(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new UnauthorizedException("Refresh token expired");
        }

        String newAccessToken = jwtService.generateToken(token.getInsurer());
        refreshTokenRepository.deleteByToken(refreshToken);
        String newRefreshToken = createRefreshToken(token.getInsurer());

        return new AuthResponseDto(newAccessToken, newRefreshToken);
    }

    private String createRefreshToken(Insurer insurer) {
        String tokenStr = UUID.randomUUID().toString();
        RefreshToken token = RefreshToken.builder()
                .insurer(insurer)
                .token(tokenStr)
                .expiryDate(Instant.now().plus(REFRESH_TOKEN_EXPIRY, ChronoUnit.DAYS))
                .build();
        refreshTokenRepository.save(token);
        return tokenStr;
    }

    public void logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Insurer insurer = (Insurer) auth.getPrincipal();
        refreshTokenRepository.deleteAllByInsurerId(insurer.getId());
    }
}