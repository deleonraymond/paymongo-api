package com.moneygment.data.security;

import com.moneygment.data.security.paymongo.PaymongoController;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class MainPaymongo {

    public static void main(String[] args) throws IOException {
        JSONObject paymentIntentResult = PaymongoController.createPaymentIntent();
        JSONObject paymentIntentData =  (JSONObject) paymentIntentResult.get("data");
        String paymentIntentId = (String) paymentIntentData.get("id");
        JSONObject attributes = (JSONObject) paymentIntentData.get("attributes");
        String clientKey = (String) attributes.get("client_key");

        JSONObject paymentMethodResult =  PaymongoController.createPaymentMethod();
        JSONObject paymentMethodData = (JSONObject) paymentMethodResult.get("data");
        String paymentMethodId = (String) paymentMethodData.get("id");

        JSONObject attachResult = PaymongoController.attachPaymentIntent(paymentIntentId, clientKey, paymentMethodId, "https://google.com");
        JSONObject attachData = (JSONObject) attachResult.get("data");
        JSONObject attrib = (JSONObject) attachData.get("attributes");
        JSONObject nextAction = (JSONObject) attrib.get("next_action");
        JSONObject redirect = (JSONObject) nextAction.get("redirect");
        String redirectUrl = (String) redirect.get("url");
        System.out.println("redirectUrl = " + redirectUrl);
    }

}
