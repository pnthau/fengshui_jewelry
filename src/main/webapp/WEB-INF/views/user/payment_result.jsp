<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết quả thanh toán - Phong Thủy Jewelry</title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #fdfbfb 0%, #ebedee 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            color: #333;
        }
        .container {
            background-color: rgba(255, 255, 255, 0.95);
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
            width: 100%;
            max-width: 500px;
            text-align: center;
            backdrop-filter: blur(10px);
        }
        .icon {
            font-size: 64px;
            margin-bottom: 20px;
        }
        .success .icon {
            color: #4CAF50;
        }
        .error .icon {
            color: #F44336;
        }
        h2 {
            margin-top: 0;
            font-weight: 600;
        }
        .success h2 {
            color: #2E7D32;
        }
        .error h2 {
            color: #C62828;
        }
        .details {
            text-align: left;
            margin-top: 30px;
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            border: 1px solid #eee;
        }
        .details p {
            margin: 8px 0;
            font-size: 14px;
            display: flex;
            justify-content: space-between;
        }
        .details span {
            font-weight: 600;
        }
        .btn-home {
            display: inline-block;
            margin-top: 30px;
            padding: 12px 24px;
            background-color: #D4AF37; /* Gold color */
            color: #fff;
            text-decoration: none;
            border-radius: 6px;
            font-weight: 500;
            transition: background-color 0.3s;
        }
        .btn-home:hover {
            background-color: #B5952F;
        }
    </style>
</head>
<body>

<div class="container ${status}">
    <div class="icon">
        <c:choose>
            <c:when test="${status == 'success'}">
                <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M22 11.08V12a10 10 10 0 1 1-5.93-9.14"></path>
                    <polyline points="22 4 12 14.01 9 11.01"></polyline>
                </svg>
            </c:when>
            <c:otherwise>
                <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <circle cx="12" cy="12" r="10"></circle>
                    <line x1="15" y1="9" x2="9" y2="15"></line>
                    <line x1="9" y1="9" x2="15" y2="15"></line>
                </svg>
            </c:otherwise>
        </c:choose>
    </div>
    
    <h2>${message}</h2>

    <c:if test="${not empty vnp_TxnRef}">
        <div class="details">
            <p>Mã đơn hàng: <span>${vnp_TxnRef}</span></p>
            <p>Mã giao dịch VNPay: <span>${vnp_TransactionNo}</span></p>
            <p>Số tiền: <span>
                <c:if test="${not empty vnp_Amount}">
                    <fmt:formatNumber value="${vnp_Amount / 100}" type="number" maxFractionDigits="0"/> VNĐ
                </c:if>
            </span></p>
            <p>Ngân hàng: <span>${vnp_BankCode}</span></p>
            <p>Nội dung: <span>${vnp_OrderInfo}</span></p>
            <p>Thời gian: <span>${vnp_PayDate}</span></p>
        </div>
    </c:if>

    <a href="${pageContext.request.contextPath}/" class="btn-home">Trở về Trang chủ</a>
</div>

</body>
</html>
