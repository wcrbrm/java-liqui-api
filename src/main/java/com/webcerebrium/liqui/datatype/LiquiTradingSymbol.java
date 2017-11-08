package com.webcerebrium.liqui.datatype;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.webcerebrium.liqui.api.LiquiApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Data
@Slf4j
public class LiquiTradingSymbol {

    LiquiSymbol symbol;

    BigDecimal decimalPlaces;
    BigDecimal minPrice;
    BigDecimal maxPrice;
    BigDecimal minAmount;
    BigDecimal maxAmount;
    BigDecimal minTotal;
    boolean hidden;
    BigDecimal fee;

    public LiquiTradingSymbol(LiquiSymbol symbol) {
        this.symbol = this.symbol;
    }

    private void jsonExpect(JsonObject obj, Set<String> fields) throws LiquiApiException {
        Set<String> missing = new HashSet<>();
        for (String f: fields) { if (!obj.has(f) || obj.get(f).isJsonNull()) missing.add(f); }
        if (missing.size() > 0) {
            log.warn("Missing fields {} in {}", missing.toString(), obj.toString());
            throw new LiquiApiException("Missing fields " + missing.toString());
        }
    }

    private Long safeLong(JsonObject obj, String field) {
        if (obj.has(field) && obj.get(field).isJsonPrimitive() && obj.get(field) != null) {
            try {
                return obj.get(field).getAsLong();
            } catch (java.lang.NumberFormatException nfe) {
                log.info("Number format exception in field={} value={} trade={}", field, obj.get(field), obj.toString());
            }
        }
        return 0L;
    }
    private BigDecimal safeDecimal(JsonObject obj, String field) {
        if (obj.has(field) && obj.get(field).isJsonPrimitive() && obj.get(field) != null) {
            try {
                return obj.get(field).getAsBigDecimal();
            } catch (java.lang.NumberFormatException nfe) {
                log.info("Number format exception in field={} value={} trade={}", field, obj.get(field), obj.toString());
            }
        }
        return null;
    }

    public LiquiTradingSymbol(LiquiSymbol symbol, JsonObject obj) throws LiquiApiException {
        this.symbol = symbol;

        jsonExpect(obj, ImmutableSet.of("decimal_places", "min_price", "max_price", "min_amount", "fee"));

        decimalPlaces = safeDecimal(obj, "decimal_places");
        minPrice = safeDecimal(obj, "min_price");
        maxPrice = safeDecimal(obj, "max_price");
        minAmount = safeDecimal(obj, "min_amount");
        maxAmount = safeDecimal(obj, "max_amount");
        minTotal = safeDecimal(obj, "min_total");
        fee = safeDecimal(obj, "fee");

        hidden = obj.get("hidden").getAsLong() > 0;
    }
}

