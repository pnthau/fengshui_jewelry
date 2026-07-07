package com.fengshui.controller;

import com.fengshui.util.VnpayConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "VnpayPaymentServlet", value = "/payment/vnpay")
public class VnpayPaymentController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String amountStr = request.getParameter("amount");
        if (amountStr == null) amountStr = "0";
        BigDecimal price = new BigDecimal(amountStr);
        long amountForVnpay = price.multiply(new BigDecimal("100")).longValue();
        
        String txnRef = request.getParameter("orderId"); 
        if (txnRef == null || txnRef.isEmpty()) {
            txnRef = VnpayConfig.getRandomNumber(8);
        }

        // Đã đổi sang TreeMap để tự động sắp xếp A-Z
        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", VnpayConfig.VNP_VERSION); 
        vnpParams.put("vnp_Command", VnpayConfig.VNP_COMMAND);
        vnpParams.put("vnp_TmnCode", VnpayConfig.VNP_TMN_CODE);
        vnpParams.put("vnp_Amount", String.valueOf(amountForVnpay));   
        vnpParams.put("vnp_CurrCode", "VND");                  
        vnpParams.put("vnp_BankCode", ""); 
        vnpParams.put("vnp_TxnRef", txnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang: " + txnRef);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", VnpayConfig.VNP_RETURN_URL); 
        
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        StringBuilder hashData = new StringBuilder(); 
        StringBuilder query = new StringBuilder();    
        
        // Bỏ qua List trung gian, duyệt trực tiếp TreeMap
        Iterator<Map.Entry<String, String>> itr = vnpParams.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, String> entry = itr.next();
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();
            
            if (fieldValue != null && fieldValue.length() > 0) {
                String encodedName = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString());
                String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString());
                
                hashData.append(fieldName).append('=').append(encodedValue);
                query.append(encodedName).append('=').append(encodedValue);
                
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        
        String vnpSecureHash = VnpayConfig.hmacSHA512(VnpayConfig.SECRET_KEY, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnpSecureHash);
        
        String paymentUrl = VnpayConfig.VNP_PAY_URL + "?" + query.toString();
        response.sendRedirect(paymentUrl);
    }
}
