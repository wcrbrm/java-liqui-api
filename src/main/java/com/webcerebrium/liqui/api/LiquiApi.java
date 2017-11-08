package com.webcerebrium.liqui.api;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.google.gson.JsonObject;
import com.webcerebrium.liqui.datatype.LiquiTradingStats;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@Data
public class LiquiApi {


    /* Actual API key and Secret Key that will be used */
    public String apiKey;
    public String secretKey;

    public LiquiConfig config = new LiquiConfig();

    /**
     * API Base URL
     */
    public String baseUrl = "https://api.liqui.io/";

    /**
     * Guava Class Instance for escaping
     */
    private Escaper esc = UrlEscapers.urlFormParameterEscaper();


    /**
     * Constructor of API when you exactly know the keys
     * @param apiKey Public API Key
     * @param secretKey Secret API Key
     * @throws LiquiApiException in case of any error
     */
    public LiquiApi(String apiKey, String secretKey) throws LiquiApiException {

        this.apiKey = apiKey;
        this.secretKey = secretKey;
        validateCredentials();
    }

    /**
     * Constructor of API - keys are loaded from VM options, environment variables, resource files
     * @throws LiquiApiException in case of any error
     */
    public LiquiApi() {
        this.apiKey = config.getVariable("LIQUI_API_KEY");
        this.secretKey = config.getVariable("LIQUI_SECRET_KEY");
    }

    /**
     * Validation we have API keys set up
     * @throws LiquiApiException in case of any error
     */
    protected void validateCredentials() throws LiquiApiException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(this.getApiKey()))
            throw new LiquiApiException("Missing LIQUI_API_KEY. " + humanMessage);
        if (Strings.isNullOrEmpty(this.getSecretKey()))
            throw new LiquiApiException("Missing LIQUI_SECRET_KEY. " + humanMessage);
    }

    public LiquiTradingStats getMarketsInfo() throws LiquiApiException {
        JsonObject jsonObject = new LiquiRequest(baseUrl + "api/3/info").read().asJsonObject();
        return new LiquiTradingStats(jsonObject);
    }

    /**
     * Getting set of coins that are available on certain market (ETH/BTC/USDT)
     * @param coin
     * @return
     */
    public Set<String> getCoinsOf(String coin) {
        try {
            LiquiTradingStats marketsInfo = new LiquiApi().getMarketsInfo();
            return marketsInfo.getCoinsOf(coin.toLowerCase());
        } catch (Exception e) {
            log.error("LIQUI UNCAUGHT EXCEPTION {}", e.getMessage());
        } catch (LiquiApiException e) {
            log.warn("LIQUI ERROR {}", e.getMessage());
        }
        return ImmutableSet.of();
    }
}
