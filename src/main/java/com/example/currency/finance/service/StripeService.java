package com.example.currency.finance.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class StripeService {

    // TODO: Replace with your actual Stripe Secret Key (sk_test_...)
    private String stripeApiKey = "sk_test_51SZm8oHgev38l4UcLVivdVkflgsnnfilbMTs65gYyEwbesNBL2BNT1RCwVLkbhVHWFj6FyLsnFzcEchKBRvZzuZx00nVXUT6w4";

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    // Creates a Hosted Checkout Session (The "Gateway")
    public String createCheckoutSession(Double amount, String currency, String productName, String successUrl, String cancelUrl) {
        try {
            long amountInCents = (long) (amount * 100);

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(currency)
                                    .setUnitAmount(amountInCents)
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(productName)
                                            .build())
                                    .build())
                            .build())
                    .build();

            Session session = Session.create(params);
            return session.getUrl(); // Returns the URL to the Stripe Hosted Page

        } catch (StripeException e) {
            e.printStackTrace();
            return null;
        }
    }
}