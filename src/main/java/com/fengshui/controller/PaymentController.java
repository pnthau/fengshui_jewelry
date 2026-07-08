package com.fengshui.controller;

import com.fengshui.service.IOrderService;
import com.fengshui.service.OrderService;
import com.fengshui.util.VnPayConfig;
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

@WebServlet(name = "PaymentController", urlPatterns = {"/payment/*"})
public class PaymentController extends HttpServlet {

    private final IOrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if ("/vnpay".equals(path)) {
            processVnpayPayment(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy luồng thanh toán POST");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if ("/vnpay_return".equals(path)) {
            handleVnpayReturn(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy luồng thanh toán GET");
        }
    }

    private void processVnpayPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String amountStr = request.getParameter("amount");
        if (amountStr == null) amountStr = "0";
        BigDecimal price = new BigDecimal(amountStr);
        long amountForVnpay = price.multiply(new BigDecimal("100")).longValue();

        String txnRef = request.getParameter("orderId");
        if (txnRef == null || txnRef.isEmpty()) {
            txnRef = VnPayConfig.getRandomNumber(8);
        }

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", VnPayConfig.VNP_VERSION);
        vnpParams.put("vnp_Command", VnPayConfig.VNP_COMMAND);
        vnpParams.put("vnp_TmnCode", VnPayConfig.VNP_TMN_CODE);
        vnpParams.put("vnp_Amount", String.valueOf(amountForVnpay));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_BankCode", "");
        vnpParams.put("vnp_TxnRef", txnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang: " + txnRef);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", VnPayConfig.VNP_RETURN_URL);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

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

        String vnpSecureHash = VnPayConfig.hmacSHA512(VnPayConfig.SECRET_KEY, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnpSecureHash);

        String paymentUrl = VnPayConfig.VNP_PAY_URL + "?" + query.toString();
        response.sendRedirect(paymentUrl);
    }

    private void handleVnpayReturn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> fields = new TreeMap<>();
        for (Map.Entry<String,String[]> entry : request.getParameterMap().entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue()[0];

            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }

        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                String encodedName = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII);
                String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII);
                hashData.append(encodedName).append('=').append(encodedValue).append('&');
            }
        }
        if (hashData.length() > 0) {
            hashData.setLength(hashData.length() - 1);
        }

        String signValue = VnPayConfig.hmacSHA512(VnPayConfig.SECRET_KEY, hashData.toString());

        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                // Success
                String orderIdStr = request.getParameter("vnp_TxnRef");
                try {
                    int orderId = Integer.parseInt(orderIdStr);
                    orderService.updateStatus(orderId, "SUCCESS");
                } catch (Exception e) {
                    System.out.println("vnp_TxnRef không phải là Order ID hợp lệ: " + orderIdStr);
                }
                request.getSession().setAttribute("paymentMessage", "Thanh toán đơn hàng thành công!");
                request.getSession().setAttribute("paymentStatus", "success");
            } else {
                request.getSession().setAttribute("paymentMessage", "Thanh toán thất bại hoặc đã bị hủy.");
                request.getSession().setAttribute("paymentStatus", "error");
            }
        } else {
            request.getSession().setAttribute("paymentMessage", "Chữ ký không hợp lệ (Sai Checksum)!");
            request.getSession().setAttribute("paymentStatus", "error");
        }

        request.getSession().setAttribute("vnp_TxnRef", request.getParameter("vnp_TxnRef"));
        String amountParam = request.getParameter("vnp_Amount");
        if (amountParam != null && !amountParam.isEmpty()) {
            try {
                long amount = Long.parseLong(amountParam) / 100;
                request.getSession().setAttribute("vnp_Amount", amount);
            } catch (Exception e) {}
        }

        response.sendRedirect(request.getContextPath() + "/home");
    }
}
