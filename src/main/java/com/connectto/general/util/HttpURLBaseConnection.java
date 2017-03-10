package com.connectto.general.util;


import com.connectto.general.exception.HttpConnectionDeniedException;
import com.connectto.general.exception.MerchantApiException;
import com.connectto.wallet.encryption.WalletEncription;
import com.connectto.wallet.model.transaction.merchant.deposit.MerchantDeposit;
import com.connectto.wallet.model.transaction.merchant.withdraw.MerchantWithdraw;
import com.connectto.wallet.model.wallet.ExchangeRate;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.web.action.wallet.dto.CarDto;
import com.connectto.wallet.web.action.wallet.dto.RideDto;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by htdev001 on 5/20/14.
 */
public class HttpURLBaseConnection {

    private static final Logger logger = Logger.getLogger(HttpURLBaseConnection.class.getSimpleName());

    /*Withdraw*/

    public static synchronized MerchantWithdraw merchantCancelWithdraw(String walletHost, JSONObject jsonObject, boolean decript) throws MerchantApiException, HttpConnectionDeniedException {

//        String httpUrl = walletHost + "/wallet/preview-withdraw.htm";
        String httpUrl = "http://127.0.0.1:8787/merchant/withdraw-wallet-cancel.htm";

        return walletTransactionMerchantWithdrawAction(httpUrl, jsonObject, decript);

    }

    public static synchronized MerchantWithdraw merchantApproveWithdraw(String walletHost, JSONObject jsonObject, boolean decript) throws MerchantApiException, HttpConnectionDeniedException {

//        String httpUrl = walletHost + "/wallet/preview-withdraw.htm";
        String httpUrl = "http://127.0.0.1:8787/merchant/withdraw-wallet-approve.htm";

        return walletTransactionMerchantWithdrawAction(httpUrl, jsonObject, decript);

    }

    private static synchronized MerchantWithdraw walletTransactionMerchantWithdrawAction(String httpUrl, JSONObject jsonObject, boolean decript) throws MerchantApiException, HttpConnectionDeniedException {

        URL url;
        HttpURLConnection urlConn = null;
        StringBuffer responseResult = new StringBuffer();

        DataOutputStream wr = null;
        BufferedWriter writer = null;
        BufferedReader in = null;

        try {

            url = new URL(httpUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestMethod("POST");
            urlConn.connect();

            if (jsonObject != null) {
                wr = new DataOutputStream(urlConn.getOutputStream());
                writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                writer.write(jsonObject.toString());

                if (writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        logger.info(e);
                    }
                }
            }


            in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String inputLine;
            responseResult = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                responseResult.append(inputLine);
            }

            return convertMerchantWithdraw(responseResult.toString(), decript);

        } catch (UnsupportedEncodingException e) {
            throw new HttpConnectionDeniedException(e);
        } catch (ProtocolException e) {
            throw new HttpConnectionDeniedException(e);
        } catch (MalformedURLException e) {
            throw new HttpConnectionDeniedException(e);
        } catch (IOException e) {
            throw new HttpConnectionDeniedException(e);
        } finally {

            if (wr != null) {
                try {
                    wr.flush();
                    wr.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }

            if (urlConn != null) {
                try {
                    urlConn.disconnect();
                } catch (Exception e) {
                    logger.info(e);
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.info(e);
                }
            }

        }
    }

    private static synchronized MerchantWithdraw convertMerchantWithdraw(String json, boolean decript) throws MerchantApiException {

        if (Utils.isEmpty(json)) {
            return null;
        }

        if (json.startsWith("{") && json.endsWith("}")) {
            JSONParser parser = new JSONParser();
            Object unitsObj = null;
            try {
                unitsObj = parser.parse(json);
                JSONObject responseMessage = (JSONObject) unitsObj;
                if (responseMessage != null && responseMessage.get("transactionReviewDto") != null && responseMessage.get("responseDto") != null) {

                    JSONObject responseDto = (JSONObject) responseMessage.get("responseDto");
                    String status = (String) responseDto.get("status");
                    if (!"SUCCESS".equals(status)) {
                        String messages = (String) responseDto.get("messages");
                        throw new MerchantApiException(messages);
                    }

//                    JSONObject transactionReviewDto = (JSONObject) responseMessage.get("transactionReviewDto");
//
//                    String transactionId = (String) transactionReviewDto.get("transactionId");
//                    String actionDate = (String) transactionReviewDto.get("actionDate");
//
//                    String walletId = (String) transactionReviewDto.get("walletId");
//                    String setupId = (String) transactionReviewDto.get("setupId");
//
//                    String walletTotalPrice = (String) transactionReviewDto.get("walletTotalPrice");
//                    String walletCurrencyType = (String) transactionReviewDto.get("walletCurrencyType");
//
//                    String setupTotalAmount = (String) transactionReviewDto.get("setupTotalAmount");
//                    String setupCurrencyType = (String) transactionReviewDto.get("setupCurrencyType");
//
//                    if (decript) {
//
//                        walletTotalPrice = WalletEncription.decrypt(walletTotalPrice);
//                        walletCurrencyType = WalletEncription.decrypt(walletCurrencyType);
//
//                        setupTotalAmount = WalletEncription.decrypt(setupTotalAmount);
//                        setupCurrencyType = WalletEncription.decrypt(setupCurrencyType);
//
//                    }

                    MerchantWithdraw merchantWithdraw = new MerchantWithdraw();

//                    merchantWithdraw.setTransactionId(Long.parseLong(transactionId));
//                    merchantWithdraw.setActionDate(DataConverter.toMerchantDate(actionDate));
//
//
//                    merchantWithdraw.setWalletId(Long.parseLong(walletId));
//                    merchantWithdraw.setSetupId(Long.parseLong(setupId));
//
//                    merchantWithdraw.setPaidTaxToWalletSetupPrice(Double.parseDouble(walletTotalPrice));
//                    merchantWithdraw.setWalletCurrencyType(CurrencyType.valueOf(Integer.parseInt(walletCurrencyType)));
//
//                    merchantWithdraw.setPaidTaxToWalletSetup(Double.parseDouble(setupTotalAmount));
//                    merchantWithdraw.setSetupCurrencyType(CurrencyType.valueOf(Integer.parseInt(setupCurrencyType)));

                    return merchantWithdraw;
                }
            } catch (Exception e) {
                throw new MerchantApiException(e);
            }
        }
        return null;
    }

    /*Deposit*/

    public static synchronized MerchantDeposit merchantCancelDeposit(String walletHost, JSONObject jsonObject, boolean decript) throws MerchantApiException, HttpConnectionDeniedException {

//        String httpUrl = walletHost + "/wallet/preview-withdraw.htm";
        String httpUrl = "http://127.0.0.1:8787/merchant/withdraw-wallet-cancel.htm";

        return walletTransactionMerchantDepositAction(httpUrl, jsonObject, decript);

    }

    public static synchronized MerchantDeposit merchantApproveDeposit(String walletHost, JSONObject jsonObject, boolean decript) throws MerchantApiException, HttpConnectionDeniedException {

//        String httpUrl = walletHost + "/wallet/preview-withdraw.htm";
        String httpUrl = "http://127.0.0.1:8787/merchant/withdraw-wallet-approve.htm";

        return walletTransactionMerchantDepositAction(httpUrl, jsonObject, decript);

    }

    private static synchronized MerchantDeposit walletTransactionMerchantDepositAction(String httpUrl, JSONObject jsonObject, boolean decript) throws MerchantApiException, HttpConnectionDeniedException {

        URL url;
        HttpURLConnection urlConn = null;
        StringBuffer responseResult = new StringBuffer();

        DataOutputStream wr = null;
        BufferedWriter writer = null;
        BufferedReader in = null;

        try {

            url = new URL(httpUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestMethod("POST");
            urlConn.connect();

            if (jsonObject != null) {
                wr = new DataOutputStream(urlConn.getOutputStream());
                writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                writer.write(jsonObject.toString());

                if (writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        logger.info(e);
                    }
                }
            }


            in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String inputLine;
            responseResult = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                responseResult.append(inputLine);
            }

            return convertMerchantDeposit(responseResult.toString(), decript);

        } catch (UnsupportedEncodingException e) {
            throw new HttpConnectionDeniedException(e);
        } catch (ProtocolException e) {
            throw new HttpConnectionDeniedException(e);
        } catch (MalformedURLException e) {
            throw new HttpConnectionDeniedException(e);
        } catch (IOException e) {
            throw new HttpConnectionDeniedException(e);
        } finally {

            if (wr != null) {
                try {
                    wr.flush();
                    wr.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }

            if (urlConn != null) {
                try {
                    urlConn.disconnect();
                } catch (Exception e) {
                    logger.info(e);
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.info(e);
                }
            }

        }
    }

    private static synchronized MerchantDeposit convertMerchantDeposit(String json, boolean decript) throws MerchantApiException {

        if (Utils.isEmpty(json)) {
            return null;
        }

        if (json.startsWith("{") && json.endsWith("}")) {
            JSONParser parser = new JSONParser();
            Object unitsObj = null;
            try {
                unitsObj = parser.parse(json);
                JSONObject responseMessage = (JSONObject) unitsObj;
                if (responseMessage != null && responseMessage.get("transactionReviewDto") != null && responseMessage.get("responseDto") != null) {

                    JSONObject responseDto = (JSONObject) responseMessage.get("responseDto");
                    String status = (String) responseDto.get("status");
                    if (!"SUCCESS".equals(status)) {
                        String messages = (String) responseDto.get("messages");
                        throw new MerchantApiException(messages);
                    }

//                    JSONObject transactionReviewDto = (JSONObject) responseMessage.get("transactionReviewDto");
//
//                    String transactionId = (String) transactionReviewDto.get("transactionId");
//                    String actionDate = (String) transactionReviewDto.get("actionDate");
//
//                    String walletId = (String) transactionReviewDto.get("walletId");
//                    String setupId = (String) transactionReviewDto.get("setupId");
//
//                    String walletTotalPrice = (String) transactionReviewDto.get("walletTotalPrice");
//                    String walletCurrencyType = (String) transactionReviewDto.get("walletCurrencyType");
//
//                    String setupTotalAmount = (String) transactionReviewDto.get("setupTotalAmount");
//                    String setupCurrencyType = (String) transactionReviewDto.get("setupCurrencyType");
//
//                    if (decript) {
//
//                        walletTotalPrice = WalletEncription.decrypt(walletTotalPrice);
//                        walletCurrencyType = WalletEncription.decrypt(walletCurrencyType);
//
//                        setupTotalAmount = WalletEncription.decrypt(setupTotalAmount);
//                        setupCurrencyType = WalletEncription.decrypt(setupCurrencyType);
//
//                    }

                    MerchantDeposit merchantDeposit = new MerchantDeposit();

//                    merchantDeposit.setTransactionId(Long.parseLong(transactionId));
//                    merchantDeposit.setActionDate(DataConverter.toMerchantDate(actionDate));
//
//
//                    merchantDeposit.setWalletId(Long.parseLong(walletId));
//                    merchantDeposit.setSetupId(Long.parseLong(setupId));
//
//                    merchantDeposit.setPaidTaxToWalletSetupPrice(Double.parseDouble(walletTotalPrice));
//                    merchantDeposit.setWalletCurrencyType(CurrencyType.valueOf(Integer.parseInt(walletCurrencyType)));
//
//                    merchantDeposit.setPaidTaxToWalletSetup(Double.parseDouble(setupTotalAmount));
//                    merchantDeposit.setSetupCurrencyType(CurrencyType.valueOf(Integer.parseInt(setupCurrencyType)));

                    return merchantDeposit;
                }
            } catch (Exception e) {
                throw new MerchantApiException(e);
            }
        }
        return null;
    }



    public static synchronized String transportationJsonAction(String httpUrl, JSONObject jsObject) {

        //String httpUrl = Initializer.getTransportationLoginUrl();
        URL url;
        HttpURLConnection urlConn = null;
        StringBuffer responseResult = new StringBuffer();

        DataOutputStream wr = null;
        BufferedWriter writer = null;
        BufferedReader in = null;

        try {

            url = new URL(httpUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestMethod("POST");
            urlConn.connect();

            if (jsObject != null) {
                wr = new DataOutputStream(urlConn.getOutputStream());
                writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                writer.write(jsObject.toString());

                if (writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        logger.info(e);
                    }
                }
            }


            in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String inputLine;
            responseResult = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                responseResult.append(inputLine);
            }

            if (in != null) {
                in.close();
            }

        } catch (Exception e) {
            logger.error(e);
        } finally {

            if (wr != null) {
                try {
                    wr.flush();
                    wr.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }

            if (urlConn != null) {
                try {
                    urlConn.disconnect();
                } catch (Exception e) {
                    logger.info(e);
                }
            }

        }

        return responseResult.toString();
    }

    public static synchronized RideDto getRideInfoJsonAction(String httpUrl, JSONObject jsObject) {
        //String httpUrl = Initializer.getTransportationLoginUrl();
        URL url;
        HttpURLConnection urlConn = null;
        StringBuffer responseResult = new StringBuffer();

        DataOutputStream wr = null;
        BufferedWriter writer = null;
        BufferedReader in = null;

        try {

            url = new URL(httpUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestMethod("POST");
            urlConn.connect();

            if (jsObject != null) {
                wr = new DataOutputStream(urlConn.getOutputStream());
                writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                writer.write(jsObject.toString());

                if (writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        logger.info(e);
                    }
                }
            }


            in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String inputLine;
            responseResult = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                responseResult.append(inputLine);
            }

            return  convertJsonToRideDto(responseResult.toString());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        } finally {

            if (wr != null) {
                try {
                    wr.flush();
                    wr.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }

            if (urlConn != null) {
                try {
                    urlConn.disconnect();
                } catch (Exception e) {
                    logger.info(e);
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.info(e);
                }
            }

        }

        return null;
    }

    public static synchronized String transportationLoginTest(JSONObject jsObject, String httpUrl) {

        URL url;
        HttpURLConnection urlConn = null;
        StringBuffer responseResult = new StringBuffer();

        DataOutputStream wr = null;
        BufferedWriter writer = null;

        try {

            url = new URL(httpUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.connect();

            wr = new DataOutputStream(urlConn.getOutputStream());
            writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(jsObject.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String inputLine;
            responseResult = new StringBuffer();
            try {
                while ((inputLine = in.readLine()) != null) {
                    responseResult.append(inputLine);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {

            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if (wr != null) {
                try {
                    wr.flush();
                    wr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (urlConn != null) {
                urlConn.disconnect();
            }

        }

        return responseResult.toString();
    }

    public static synchronized CaptchaResponse googleReCaptchaSiteVerify(String secretParameter, String recap, String remoteAddress) {

        CaptchaResponse captchaResponse = new CaptchaResponse();
        captchaResponse.setSuccess(false);
        // Send get request to Google reCaptcha server with secret key
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL("https://www.google.com/recaptcha/api/siteverify?secret=" + secretParameter + "&response=" + recap + "&remoteip=" + remoteAddress);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            String line, outputString = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                outputString += line;
            }
            convertCaptchaResponse(captchaResponse, outputString);
        } catch (MalformedURLException e) {
            logger.error(e);
        } catch (ProtocolException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }


        return captchaResponse;
    }

    public static synchronized List<ExchangeRate> loadExchangeRates(Set<CurrencyType> availableCurrencyTypes, Set<CurrencyType> baseCurrencyTypes, int sourceId, String httpUrl) throws IOException, ParseException, HttpConnectionDeniedException {

        trustAllHosts();

        URL url = null;
        HttpsURLConnection urlConn = null;
        BufferedReader in = null;
        List<ExchangeRate> exchangeRates = new ArrayList<ExchangeRate>();

        try {
            //todo change url, include them, base and available currencies as symbols
            //current account not allowed that , for it  we need price able account
            //https://openexchangerates.org/api/latest.json?app_id = YOUR_APP_ID&symbols = USD,GBP,HKD,EUR,AED,CAD
            url = new URL(httpUrl);
            urlConn = (HttpsURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.connect();

            in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String inputLine;
            int i = 0;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }

            convertJsonToExchangeRates(availableCurrencyTypes,baseCurrencyTypes, sourceId, builder.toString(), exchangeRates);

        } finally {

            if (urlConn != null) {
                urlConn.disconnect();
            }

            if (in != null) {
                in.close();
            }


        }
        return exchangeRates;
    }

    private static synchronized void convertJsonToExchangeRates(Set<CurrencyType> availableCurrencyTypes, Set<CurrencyType> baseCurrencyTypes, int sourceId, String json, List<ExchangeRate> exchangeRates) {

        if (json.startsWith("{") && json.endsWith("}")) {
            JSONParser parser = new JSONParser();
            Object unitsObj = null;
            try {
                unitsObj = parser.parse(json);
                JSONObject responseMessage = (JSONObject) unitsObj;
                if (responseMessage != null) {

                    String base = (String) responseMessage.get("base");
                    CurrencyType baseCurrencyType = CurrencyType.codeOf(base);

                    Date date = new Date(System.currentTimeMillis());
                    JSONObject rates = (JSONObject) responseMessage.get("rates");

                    //lab:
                    for (CurrencyType currencyType : availableCurrencyTypes) {

                        if (rates.containsKey(currencyType.getCode()) && currencyType.getId() != baseCurrencyType.getId()) {

                            Double buySell = Double.parseDouble(rates.get(currencyType.getCode()).toString());

                            ExchangeRate baseToCurrency = new ExchangeRate();
                            baseToCurrency.setUpdatedDate(date);
                            baseToCurrency.setSourceId(sourceId);
                            baseToCurrency.setOneCurrency(baseCurrencyType);
                            baseToCurrency.setToCurrency(currencyType);
                            baseToCurrency.setBuy(buySell);
                            baseToCurrency.setSell(buySell);

                            ExchangeRate currencyToBase = new ExchangeRate();
                            currencyToBase.setUpdatedDate(date);
                            currencyToBase.setSourceId(sourceId);
                            currencyToBase.setOneCurrency(currencyType);
                            currencyToBase.setToCurrency(baseCurrencyType);
                            currencyToBase.setBuy(1 / buySell);
                            currencyToBase.setSell(1 / buySell);

                            exchangeRates.add(baseToCurrency);
                            exchangeRates.add(currencyToBase);

                        }
                    }


                    for (CurrencyType ct : baseCurrencyTypes) {

                        if(ct .getId() !=  baseCurrencyType.getId() ){

                            Double buy = searchBuyRateByBase(exchangeRates, ct, baseCurrencyType);

                            if(buy != null){
                                for (CurrencyType currencyType : availableCurrencyTypes) {
                                    //not usd
                                    if (ct.getId() != currencyType.getId() &&
                                            baseCurrencyType.getId() != currencyType.getId() ) {

                                        Double sell = searchBuyRateByBase(exchangeRates, currencyType, baseCurrencyType);

                                        ExchangeRate otherBaseToCurrency = new ExchangeRate();
                                        otherBaseToCurrency.setUpdatedDate(date);
                                        otherBaseToCurrency.setSourceId(sourceId);
                                        otherBaseToCurrency.setOneCurrency(ct);
                                        otherBaseToCurrency.setToCurrency(currencyType);
                                        otherBaseToCurrency.setBuy(buy/sell);
                                        otherBaseToCurrency.setSell(buy/sell);

                                        ExchangeRate currencyToOther = new ExchangeRate();
                                        currencyToOther.setUpdatedDate(date);
                                        currencyToOther.setSourceId(sourceId);
                                        currencyToOther.setOneCurrency(currencyType);
                                        currencyToOther.setToCurrency(ct);
                                        currencyToOther.setBuy(sell / buy);
                                        currencyToOther.setSell(sell / buy);

                                        exchangeRates.add(otherBaseToCurrency);
                                        exchangeRates.add(currencyToOther);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    private static synchronized RideDto convertJsonToRideDto(String json) {

        if(Utils.isEmpty(json)){
            return null;
        }

        if (json.startsWith("{") && json.endsWith("}")) {
            JSONParser parser = new JSONParser();
            Object unitsObj = null;
            try {
                unitsObj = parser.parse(json);
                JSONObject responseMessage = (JSONObject) unitsObj;
                if (responseMessage != null && responseMessage.get("ride") != null) {
                    JSONObject ride = (JSONObject) responseMessage.get("ride");

                    JSONObject car = (JSONObject) ride.get("car");
                    if(car == null){
                        return null;
                    }

                    CarDto carDto = new CarDto();
                    carDto.setModel((String) car.get("model"));
                    carDto.setLocation((String) car.get("location"));
                    carDto.setName((String) car.get("name"));
                    carDto.setCarClass((String) car.get("class"));
                    carDto.setLicensePlate((String) car.get("licensePlate"));

                    JSONObject driver = (JSONObject) ride.get("driver");

                    RideDto rideDto = new RideDto();
                    rideDto.setCarDto(carDto);

                    if(driver != null){
                        rideDto.setDriverName((String)driver.get("name"));
                    }

                    String startAddress = (String) ride.get("startAddress");
                    String endAddress = (String) ride.get("endAddress");

                    Double distance = (Double) ride.get("distance");
                    String distanceType = (String) ride.get("distanceType");

                    String startDate = (String) ride.get("startDate");
                    String endDate = (String) ride.get("endDate");

                    String detailedUrl = (String) ride.get("detailedUrl");

                    rideDto.setStartAddress(startAddress);
                    rideDto.setEndAddress(endAddress);
                    rideDto.setDistance(distance != null ? distance.toString() : null);
                    rideDto.setDistanceType(distanceType);
                    rideDto.setStartDate(Utils.toDateyyyyMMddhhmmssS(startDate));
                    rideDto.setEndDate(Utils.toDateyyyyMMddhhmmssS(endDate));
                    rideDto.setDetailedUrl(detailedUrl);

                    return rideDto;
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return null;
    }

    private static Double searchBuyRateByBase(List<ExchangeRate> exchangeRates, CurrencyType search, CurrencyType base) {

        Double buy = null;

        for (ExchangeRate exchangeRate : exchangeRates) {
            if (exchangeRate.getOneCurrency().getId() == base.getId() &&
                    exchangeRate.getToCurrency().getId() == search.getId()
                    ) {
                return exchangeRate.getBuy();
            }
        }
        return null;
    }

    private static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }
        };
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void convertCaptchaResponse(CaptchaResponse captchaResponse, String request) {
        if (request.startsWith("{") && request.endsWith("}")) {
            JSONParser parser = new JSONParser();
            Object unitsObj = null;
            try {
                unitsObj = parser.parse(request);
                JSONObject responseMessage = (JSONObject) unitsObj;
                if (responseMessage != null) {
                    Boolean success = (Boolean) responseMessage.get("success");
                    Object errorCodes = responseMessage.get("error-codes");
                    captchaResponse.setSuccess(success);
                    if (errorCodes != null) {
                        logger.error(errorCodes.toString());
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }
}
