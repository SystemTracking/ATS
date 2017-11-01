package com.ATS.trackerbackend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "notevikramApi",
        version = "v1",
        resource = "notevikram",
        namespace = @ApiNamespace(
                ownerDomain = "trackerbackend.ATS.com",
                ownerName = "trackerbackend.ATS.com",
                packagePath = ""
        )
)
public class NotevikramEndpoint {

    private static final Logger logger = Logger.getLogger(NotevikramEndpoint.class.getName());

    /**
     * This method gets the <code>Notevikram</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Notevikram</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getNotevikram")
    public Notevikram getNotevikram(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getNotevikram method");
        return null;
    }

    /**
     * This inserts a new <code>Notevikram</code> object.
     *
     * @param notevikram The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertNotevikram")
    public Notevikram insertNotevikram(Notevikram notevikram) {
        // TODO: Implement this function
        logger.info("Calling insertNotevikram method");
        return notevikram;
    }
}