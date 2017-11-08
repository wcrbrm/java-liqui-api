package com.webcerebrium.liqui.datatype;

import com.google.common.base.Strings;
import com.webcerebrium.liqui.api.LiquiApiException;

public class LiquiSymbol {

    String symbol = "";

    public LiquiSymbol(String symbol)  throws LiquiApiException {
        // sanitizing symbol, preventing from common user-input errors
        if (Strings.isNullOrEmpty(symbol)) {
            throw new LiquiApiException("Symbol cannot be empty. Example: eth_btc");
        }
        if (symbol.contains(" ")) {
            throw new LiquiApiException("Symbol cannot contain spaces. Example: eth_btc");
        }
        this.symbol = symbol.toLowerCase();
        if (!this.symbol.endsWith("_btc") && !this.symbol.endsWith("_eth") && !this.symbol.endsWith("_usdt")) {
            throw new LiquiApiException("Market Symbol should be ending with _btc, _eth. Example: eth_btc. Given " + this.symbol);
        }
    }

    public String get(){ return this.symbol; }

    public String getSymbol(){ return this.symbol; }

    public String getCoin(){
        if (symbol.equals("eth_btc")) return "eth";
        return this.symbol.replace("_btc", "").replace("_eth", "").replace("_usdt", "");
    }
    public String getBaseCoin(){
        if ((symbol.endsWith("_btc") || symbol.endsWith("_eth"))) return symbol.substring(4);
        if (symbol.equals("eth_btc")) return "btc";

        if (symbol.endsWith("_btc")) return "btc";
        if (symbol.endsWith("_eth")) return "eth";
        if (symbol.endsWith("_usdt")) return "usdt";
        return "";
    }

    public String getOpposite(String coin) {
        if (symbol.startsWith(coin + "_")) {
            return symbol.substring((coin + "_").length());
        }
        if (symbol.endsWith("_" + coin)) {
            int index = symbol.length() - ("_" + coin).length();
            return symbol.substring(0, index);
        }
        return "";
    }

    public String toString() { return this.get(); }

    public static LiquiSymbol valueOf(String s) throws LiquiApiException {
        return new LiquiSymbol(s);
    }

    public static LiquiSymbol BTC(String pair) throws LiquiApiException {
        return LiquiSymbol.valueOf(pair.toLowerCase() + "_btc");
    }

    public static LiquiSymbol ETH(String pair) throws LiquiApiException {
        return LiquiSymbol.valueOf(pair.toLowerCase() + "_eth");
    }

    public static LiquiSymbol ETH_BTC() throws LiquiApiException {
        return LiquiSymbol.valueOf("eth_btc");
    }

    public boolean contains(String coin) {
        return (symbol.endsWith("_" + coin.toLowerCase())) || (symbol.startsWith(coin.toLowerCase() + "_"));
    }
}
