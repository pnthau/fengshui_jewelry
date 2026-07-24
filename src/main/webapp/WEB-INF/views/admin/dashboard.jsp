<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<div class="container-xl">
    <div class="page-header d-print-none">
        <div class="row align-items-center">
            <div class="col">
                <h2 class="page-title">
                    Dashboard
                </h2>
            </div>
        </div>
    </div>

    <%-- Determine current user role for dashboard visibility --%>
    <% String currentRole = "";
       if (session.getAttribute("currentUser") != null) {
           com.fengshui.entity.User cu = (com.fengshui.entity.User) session.getAttribute("currentUser");
           currentRole = cu.getRole() != null ? cu.getRole().toLowerCase() : "";
       }
    %>

    <div class="row row-cards">
        <%-- Tổng doanh thu: chỉ ADMIN và ACCOUNTANT và SALES (if you want sales to see revenue, include sales) --%>
        <% if ("admin".equals(currentRole) || "accountant".equals(currentRole) || "sales".equals(currentRole)) { %>
        <div class="col-sm-6 col-lg-3">
            <div class="card card-sm">
                <div class="card-body">
                    <div class="row align-items-center">
                        <div class="col-auto">
                            <span class="bg-primary text-white avatar">
                                <i class="ti ti-currency-dollar"></i>
                            </span>
                        </div>
                        <div class="col">
                            <div class="font-weight-medium">
                                <fmt:formatNumber value="${dashboardData.totalRevenue}" type="currency" currencySymbol="VNĐ" maxFractionDigits="0"/>
                            </div>
                            <div class="text-muted">Tổng doanh thu</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <% } %>

        <%-- Đơn hàng mới: admin + sales --%>
        <% if ("admin".equals(currentRole) || "sales".equals(currentRole)) { %>
        <div class="col-sm-6 col-lg-3">
            <div class="card card-sm">
                <div class="card-body">
                    <div class="row align-items-center">
                        <div class="col-auto">
                            <span class="bg-success text-white avatar">
                                <i class="ti ti-shopping-cart"></i>
                            </span>
                        </div>
                        <div class="col">
                            <div class="font-weight-medium">
                                ${dashboardData.newOrdersCount}
                            </div>
                            <div class="text-muted">Đơn hàng mới</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <% } %>

        <%-- Sản phẩm sắp hết hàng: admin + warehouse --%>
        <% if ("admin".equals(currentRole) || "warehouse".equals(currentRole)) { %>
        <div class="col-sm-6 col-lg-3">
            <div class="card card-sm">
                <div class="card-body">
                    <div class="row align-items-center">
                        <div class="col-auto">
                            <span class="bg-warning text-white avatar">
                                <i class="ti ti-alert-triangle"></i>
                            </span>
                        </div>
                        <div class="col">
                            <div class="font-weight-medium">
                                ${dashboardData.lowStockProductsCount}
                            </div>
                            <div class="text-muted">Sản phẩm sắp hết hàng</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <% } %>

        <%-- Tổng số khách hàng: only admin --%>
        <% if ("admin".equals(currentRole)) { %>
        <div class="col-sm-6 col-lg-3">
            <div class="card card-sm">
                <div class="card-body">
                    <div class="row align-items-center">
                        <div class="col-auto">
                            <span class="bg-info text-white avatar">
                                <i class="ti ti-users"></i>
                            </span>
                        </div>
                        <div class="col">
                            <div class="font-weight-medium">
                                ${dashboardData.totalUsersCount}
                            </div>
                            <div class="text-muted">Tổng số khách hàng</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
    </div>

    <div class="row mt-4">
        <div class="col-12">
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Biểu đồ biến động doanh số</h3>
                </div>
                <div class="card-body">
                    <canvas id="revenueChart"></canvas>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    const revenueLabels = [${chartLabels}];
    const revenueDataPoints = [${chartData}];

    const revenueData = {
        labels: revenueLabels,
        datasets: [{
            label: 'Doanh thu (VNĐ)',
            data: revenueDataPoints,
            borderColor: 'rgb(75, 192, 192)',
            tension: 0.1,
            fill: false
        }]
    };

    const config = {
        type: 'line',
        data: revenueData,
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Biến động doanh số theo tháng'
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value, index, ticks) {
                            return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
                        }
                    }
                }
            }
        }
    };

    var myChart = new Chart(
        document.getElementById('revenueChart'),
        config
    );
</script>