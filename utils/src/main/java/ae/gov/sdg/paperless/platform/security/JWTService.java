package ae.gov.sdg.paperless.platform.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import ae.gov.sdg.paperless.platform.common.model.LOGIN_SOURCE;
import ae.gov.sdg.paperless.platform.exceptions.UserAuthenticationFailedException;

/**
 * @author c_waqas.ahmad
 * @Date Jan 29, 2020
 *
 */
@Component
public class JWTService {

    private static final Log logger = LogFactory.getLog(JWTService.class);
    
    private JWTService() {}
  
    public Certificate loadCertificateFromJKS(final String certificatePath, final String certPwd, final String certAlies) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

    	try(InputStream fileStream = new FileInputStream(new File(certificatePath))){
    		final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    		keystore.load(fileStream, certPwd.toCharArray());
    		return keystore.getCertificate(certAlies);
    	}catch (Exception e) {
    		logger.error("file not found due to: {}"+e.getMessage());
    		return null;
    	}
    }

    public Certificate loadCertificateFromCertificateFactory(final String certificatePath) throws  CertificateException, UserAuthenticationFailedException {

        final InputStream fileStream = JWTService.class.getClassLoader().getResourceAsStream(certificatePath);

        final CertificateFactory factory = CertificateFactory.getInstance("X.509");
        try {
            return factory.generateCertificate(fileStream);
        } catch (final Exception e) {
            throw new UserAuthenticationFailedException("Unable to load certificate");
        }
    }


    public boolean isValidSignature(final Certificate certificate, final String idToken) throws ParseException, JOSEException {

        final RSAPublicKey publicKey = (RSAPublicKey) certificate.getPublicKey();

        final SignedJWT signedJWT = SignedJWT.parse(idToken);
        final JWSVerifier verifier = new RSASSAVerifier(publicKey);
        return signedJWT.verify(verifier);
    }

    public boolean isValidToken(final String idToken) {
        SignedJWT signedJWT = null;
        ReadOnlyJWTClaimsSet claims = null;
        try {
             signedJWT = SignedJWT.parse(idToken);
             claims = signedJWT.getJWTClaimsSet();
        } catch (final Exception e) {
        	logger.error(e.getMessage());
        }

        if (signedJWT == null || claims == null) {
             logger.error("signedJWT is null");
             return false;
        }

        final Date currentDate = new Date();
        if (claims.getExpirationTime() == null
                  || claims.getExpirationTime().before(currentDate)) {
            logger.error("Invalid idToken expiry time");
            return false;
        }
        return true;
    }
    
    public LOGIN_SOURCE getLoginSource(final String idToken) throws ParseException {
            final SignedJWT signedJWT = SignedJWT.parse(idToken);
            final ReadOnlyJWTClaimsSet claims = signedJWT.getJWTClaimsSet();
             final String[] userInfo = claims.getSubject().split(":");
             return LOGIN_SOURCE.getLoginSource(userInfo[0]);
        
    }

}
