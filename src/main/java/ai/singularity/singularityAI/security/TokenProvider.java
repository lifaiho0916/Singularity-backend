package ai.singularity.singularityAI.security;

import ai.singularity.singularityAI.config.AppProperties;
import ai.singularity.singularityAI.service.dto.InvitationDTO;
import ai.singularity.singularityAI.service.dto.MemberDTO;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }
    
    public String createInvitationToken(MemberDTO member, Long projectID, Long inviter) {
    	Date now = new Date();
    	Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
    	
    	return Jwts.builder()
    			.claim("memberEmail", member.getEmail())
    			.claim("memberPosition", member.getPosition().toString())
    			.claim("projectID", projectID)
    			.claim("inviter", inviter)
    			.setIssuedAt(now)
    			.setExpiration(expiryDate)
    			.signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
    			.compact();
    }
    
    public InvitationDTO getInvitationInfoFromToken(String token) {
    	Claims claims = Jwts.parser()
	      .setSigningKey(appProperties.getAuth().getTokenSecret())
	      .build()
	      .parseClaimsJws(token)
	      .getBody();
    	
    	InvitationDTO invitationDTO = new InvitationDTO();
    	invitationDTO.setEmail(claims.get("memberEmail").toString());
    	invitationDTO.setPosition(claims.get("memberPosition").toString());
    	invitationDTO.setProjectID(Long.parseLong(claims.get("projectID").toString()));
    	invitationDTO.setInviterID(Long.parseLong(claims.get("inviter").toString()));
    	
    	return invitationDTO;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}