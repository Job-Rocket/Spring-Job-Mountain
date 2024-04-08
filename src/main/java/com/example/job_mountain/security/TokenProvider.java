package com.example.job_mountain.security;

import com.example.job_mountain.config.AppProperties;
import com.example.job_mountain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserRepository userRepository;

    @Value("${app.auth.tokenSecret}")
    private String secretKey;

    //HMAC-SHA 키를 생성
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 이 코드는 HMAC-SHA 키를 생성하는 데 사용되는 Base64 인코딩된 문자열을 디코딩하여 키를 초기화하는 용도로 사용
    @PostConstruct//의존성 주입이 이루어진 후 초기화를 수행하는 어노테이션
    public void init() {
        byte[] bytes = Base64.getDecoder()
                .decode(secretKey);// Base64로 인코딩된 값을 시크릿키 변수에 저장한 값을 디코딩하여 바이트 배열로 변환
        //* Base64 (64진법) : 바이너리(2진) 데이터를 문자 코드에 영향을 받지 않는 공통 ASCII문자로 표현하기 위해 만들어진 인코딩
        key = Keys.hmacShaKeyFor(
                bytes);//디코팅된 바이트 배열을 기반으로 HMAC-SHA 알고르즘을 사용해서 Key객체로 반환 , 이를 key 변수에 대입
    }


    // 토큰 생성 메소드
    public String createToken(Authentication authentication, boolean refresh) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        int time;
        if (refresh) {
            time = (int) (appProperties.getAuth().getTokenExpirationDay() * 3 * 60 * 24); // 15일
        } else {
            time = (int) (appProperties.getAuth().getTokenExpirationDay() * 60 * 24); // 5일
        }

        return Jwts.builder()
                .setSubject(String.valueOf(userPrincipal.getUserId()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusDays(time).toInstant()))
                .signWith( SignatureAlgorithm.HS256, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    // 토큰으로 UserId 가져오기
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    // 토큰 만료 여부 조회
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(appProperties.getAuth().getTokenSecret())
                    .parseClaimsJws(token)
                    .getBody();

            Date expirationDate = claims.getExpiration();
            return expirationDate.before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // 엑세스 토큰의 만료시간 조회
    public Long getExpiration(String accessToken) {
        // 엑세스 토큰 만료시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재시간
        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
