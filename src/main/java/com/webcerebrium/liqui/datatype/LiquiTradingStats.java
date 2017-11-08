package com.webcerebrium.liqui.datatype;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.webcerebrium.liqui.api.LiquiApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Data
public class LiquiTradingStats {

    List<LiquiTradingSymbol> listTradingSymbols = new LinkedList<>();

    public LiquiTradingStats(JsonObject response) throws LiquiApiException {
        if (!response.has("pairs")) {
            throw new LiquiApiException("Missing pairs in response object while trying to trading stats");
        }
        JsonObject pairs = response.get("pairs").getAsJsonObject();
        listTradingSymbols.clear();
        for (Map.Entry<String, JsonElement> entry: pairs.entrySet()) {
            LiquiSymbol s = new LiquiSymbol(entry.getKey());
            LiquiTradingSymbol symbol = new LiquiTradingSymbol(s, entry.getValue().getAsJsonObject());
            listTradingSymbols.add(symbol);
        }
    }

    public List<LiquiTradingSymbol> getMarketsOf(String coin) {
        List<LiquiTradingSymbol> result = new LinkedList<>();
        for (int i = 0; i < listTradingSymbols.size(); i++ ) {
            LiquiTradingSymbol tradingSymbol = listTradingSymbols.get(i);
            if (tradingSymbol.isHidden()) continue;
            if (tradingSymbol.getSymbol().contains(coin)) {
                result.add(tradingSymbol);
            }
        }
        return result;
    }

    public Set<LiquiSymbol> getSymbolsOf(String coin) throws LiquiApiException {
        List<LiquiTradingSymbol> coins = getMarketsOf(coin);
        Set<LiquiSymbol> result = new TreeSet<>();
        for (LiquiTradingSymbol sym: coins) {
            result.add(sym.getSymbol());
        }
        return result;
    }

    public Set<String> getCoinsOf(String coin) throws LiquiApiException {
        List<LiquiTradingSymbol> coins = getMarketsOf(coin);
        Set<String> result = new TreeSet<>();
        for (LiquiTradingSymbol sym: coins) {
            result.add(sym.getSymbol().getOpposite(coin));
        }
        return result;
    }
}
