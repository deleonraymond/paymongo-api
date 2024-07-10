package com.moneygment.data.security.paymongo;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import org.apache.commons.codec.binary.Base64;

public class PaymongoController {

    private static String securityKey = "sk_test_ATde889VXjQCwvgYBPMtfRT9";

    public static JSONObject createPaymentIntent() throws IOException {
        JSONObject requestData = new JSONObject();

        JSONObject data = new JSONObject();
        JSONObject attributes = new JSONObject();

        attributes.put("amount", 20000);

        JSONArray paymentMethodAllowed = new JSONArray()
                .put("qrph")
                .put("card")
                .put("dob")
                .put("paymaya")
                .put("billease")
                .put("gcash")
                .put("grab_pay");
        attributes.put("payment_method_allowed", paymentMethodAllowed);

        JSONObject paymentMethodOptions = new JSONObject();
        JSONObject card = new JSONObject();
        card.put("request_three_d_secure", "any");
        paymentMethodOptions.put("card", card);
        attributes.put("payment_method_options", paymentMethodOptions);

        attributes.put("currency", "PHP");
        attributes.put("capture_type", "automatic");
        attributes.put("description", "description content");
        attributes.put("statement_descriptor", "statement_descriptor content");

        data.put("attributes", attributes);
        requestData.put("data", data);

        return httpRequestCall("POST", "https://api.paymongo.com/v1/payment_intents", requestData.toString());
    }

    public static JSONObject createPaymentMethod() throws IOException {
        JSONObject requestData = new JSONObject();

        JSONObject data = new JSONObject();
        JSONObject attributes = new JSONObject();
        attributes.put("livemode", false);
        attributes.put("type", "gcash");
        attributes.put("created_at", "1720517178");
        attributes.put("updated_at", "1720517178");

        data.put("id", "pm_EQs5tu33tn3Cuxj8fkNTEDT6");
        data.put("type", "payment_method");
        data.put("attributes", attributes);
        requestData.put("data", data);

        return httpRequestCall("POST", "https://api.paymongo.com/v1/payment_methods", requestData.toString());
    }

    public static JSONObject attachPaymentIntent(String paymentIntentId, String clientKey, String paymentMethodId, String returnUrl) throws IOException {
        JSONObject requestData = new JSONObject();

        JSONObject data = new JSONObject();
        JSONObject attributes = new JSONObject();
        attributes.put("client_key", clientKey);
        attributes.put("payment_method", paymentMethodId);
        attributes.put("return_url", returnUrl);

        data.put("attributes", attributes);
        requestData.put("data", data);

        String url = "https://api.paymongo.com/v1/payment_intents/" + paymentIntentId+ "/attach";
        return httpRequestCall("POST", url, requestData.toString());
    }

    private static JSONObject httpRequestCall(String method, String url, String requestData) throws IOException {
        OkHttpClient client = new OkHttpClient();

        byte[] encodedBytes = Base64.encodeBase64(securityKey.getBytes());
        String encodedString = new String(encodedBytes);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestData);
        Request request = new Request.Builder()
                .url(url)
                .method(method, body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Basic " + encodedString)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            // Convert the response body to a string
            assert response.body() != null;
            String jsonResponse = response.body().string();

            // Parse the string as JSON
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Print the JSON response
            System.out.println(jsonObject); // Pretty print with 4 spaces indentation

            return jsonObject;
        } else {
            System.err.println("Request not successful: " + response.code());
            return null;
        }
    }
}
