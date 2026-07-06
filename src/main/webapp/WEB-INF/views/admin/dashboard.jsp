<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<div class="container-fluid">
    <h1 class="mt-4 mb-4">Admin Dashboard</h1>

    <div class="row">
        <div class="col-md-3">
            <div class="card text-white bg-primary mb-3">
                <div class="card-header">Doanh thu</div>
                <div class="card-body">
                    <h5 class="card-title">
                        <fmt:formatNumber value="${dashboardData.totalRevenue}" type="currency" currencySymbol="VNĐ" maxFractionDigits="0"/>
                    </h5>
                    <p class="card-text">Tổng doanh thu thành công</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-white bg-success mb-3">
                <div class="card-header">Đơn hàng mới</div>
                <div class="card-body">
                    <h5 class="card-title">${dashboardData.newOrdersCount}</h5>
                    <p class="card-text">Số đơn hàng chờ xử lý</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-white bg-warning mb-3">
                <div class="card-header">Sản phẩm sắp hết hàng</div>
                <div class="card-body">
                    <h5 class="card-title">${dashboardData.lowStockProductsCount}</h5>
                    <p class="card-text">Sản phẩm cần nhập thêm</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-white bg-info mb-3">
                <div class="card-header">Tài khoản khách</div>
                <div class="card-body">
                    <h5 class="card-title">${dashboardData.totalUsersCount}</h5>
                    <p class="card-text">Tổng số khách hàng</p>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header">Biểu đồ biến động doanh số</div>
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
