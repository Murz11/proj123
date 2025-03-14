package ru.sfedu.tripshelperpack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.tripshelperpack.cli.TripsHelperCLI;
import ru.sfedu.tripshelperpack.utils.ConfigurationUtil;

import java.io.IOException;
import java.sql.SQLException;

public class TripsHelperClient {

    private static final Logger log = LogManager.getLogger(TripsHelperClient.class);


    public static void main(String[] args) throws SQLException {
        TripsHelperCLI cli = new TripsHelperCLI();
        cli.start();
    }
}
