package br.com.vah.protocolo.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

/**
 * Logging producer for injectable log4j logger
 *
 * @author Cem Ikta www.devsniper.com
 */
public class LoggerProducer {
   
    /**
    * @param injectionPoint
    * @return logger
    */
    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}