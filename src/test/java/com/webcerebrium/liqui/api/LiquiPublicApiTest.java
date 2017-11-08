package com.webcerebrium.liqui.api;

import com.webcerebrium.liqui.datatype.LiquiTradingStats;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class LiquiPublicApiTest {

    LiquiApi api;

    @Before
    public void setUp() throws Exception, LiquiApiException {
        api = new LiquiApi();
    }

    @Test
    public void testName() throws Exception, LiquiApiException {
        LiquiTradingStats marketsInfo = api.getMarketsInfo();
        log.info("ETH COINS: {}", marketsInfo.getCoinsOf("eth"));
        log.info("BTC COINS: {}", marketsInfo.getCoinsOf("btc"));
    }
}
