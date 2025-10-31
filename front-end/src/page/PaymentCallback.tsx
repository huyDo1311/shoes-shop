import { paymentAPI } from "@/apis/payment.api";
import { useMutation } from "@tanstack/react-query";
import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

function PaymentCallback() {
  const location = useLocation();
  const [paymentResult, setPaymentResult] = useState<Record<string, string>>({});

  // ✅ Lấy query params từ URL
  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const result: Record<string, string> = {};
    queryParams.forEach((value, key) => {
      result[key] = value;
    });
    setPaymentResult(result);
  }, [location.search]);

  // ✅ Mutation cập nhật VNPay
  const updateOrderStatusMutation = useMutation({
    mutationFn: (body: { vnp_TxnRef: string }) => paymentAPI.updateStatus(body),
  });

  // ✅ Mutation cập nhật MoMo
  const updateOrderStatusMomoMutation = useMutation({
    mutationFn: (body: { momoTxnRef: string }) => paymentAPI.updateStatusMomo(body),
  });

  const [hasUpdated, setHasUpdated] = useState(false);

  useEffect(() => {
    if (hasUpdated || !Object.keys(paymentResult).length) return;

    // 🔹 Xác định loại thanh toán
    const isVNPay = paymentResult["vnp_TxnRef"];
    const isMoMo = paymentResult["orderId"];

    // ✅ VNPay: thành công khi vnp_ResponseCode === "00"
    if (isVNPay && paymentResult["vnp_ResponseCode"] === "00") {
      updateOrderStatusMutation.mutate({ vnp_TxnRef: paymentResult["vnp_TxnRef"] });
      setHasUpdated(true);
      return;
      console.log('success')
    }

    // ✅ MoMo: thành công khi resultCode === "0"
    if (isMoMo && paymentResult["resultCode"] === "0") {
      updateOrderStatusMomoMutation.mutate({ momoTxnRef: paymentResult["orderId"] });
      setHasUpdated(true);
      return;
    }
  }, [paymentResult, hasUpdated, updateOrderStatusMutation, updateOrderStatusMomoMutation]);

  if (!Object.keys(paymentResult).length) {
    return <p className="text-center mt-10 text-gray-500">Đang xử lý kết quả thanh toán...</p>;
  }

  // 🔹 Xác định loại và trạng thái
  const isVNPay = paymentResult["vnp_TxnRef"];
  const isMoMo = paymentResult["orderId"];

  const isSuccess =
    (isVNPay && paymentResult["vnp_ResponseCode"] === "00") ||
    (isMoMo && paymentResult["resultCode"] === "0");

  const amount = isVNPay
    ? Number(paymentResult["vnp_Amount"]) / 100
    : Number(paymentResult["amount"]);

  // ✅ Chọn mutation tương ứng
  const activeMutation = isVNPay
    ? updateOrderStatusMutation
    : updateOrderStatusMomoMutation;

  return (
    <div className="max-w-lg mx-auto mt-10 p-6 rounded-2xl shadow-md bg-white">
      <h1 className="text-2xl font-semibold text-center mb-4">
        {isSuccess ? "✅ Thanh toán thành công!" : "❌ Thanh toán thất bại"}
      </h1>

      <div className="space-y-2 text-gray-700">
        <p>
          <strong>Mã giao dịch:</strong>{" "}
          {isVNPay ? paymentResult["vnp_TxnRef"] : paymentResult["orderId"]}
        </p>
        <p>
          <strong>Số tiền:</strong> {amount.toLocaleString()} VND
        </p>

        {isVNPay && (
          <>
            <p>
              <strong>Ngân hàng:</strong>{" "}
              {paymentResult["vnp_BankCode"] || "Không xác định"}
            </p>
            <p>
              <strong>Mã phản hồi:</strong> {paymentResult["vnp_ResponseCode"]}
            </p>
            <p>
              <strong>Thời gian thanh toán:</strong>{" "}
              {paymentResult["vnp_PayDate"]}
            </p>
          </>
        )}

        {isMoMo && (
          <>
            <p>
              <strong>Kết quả MoMo:</strong> {paymentResult["message"]}
            </p>
            <p>
              <strong>Mã phản hồi:</strong> {paymentResult["resultCode"]}
            </p>
          </>
        )}
      </div>

      {activeMutation.isPending && (
        <p className="text-center mt-4 text-blue-500">
          ⏳ Đang cập nhật trạng thái đơn hàng...
        </p>
      )}

      {activeMutation.isError && (
        <p className="text-center mt-4 text-red-500">
          ❌ Lỗi khi cập nhật trạng thái đơn hàng
        </p>
      )}

      {activeMutation.isSuccess && (
        <p className="text-center mt-4 text-green-500">
          ✅ Đã cập nhật đơn hàng thành công!
        </p>
      )}

      <div className="text-center mt-6">
        <a
          href="/"
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
        >
          Quay lại trang chủ
        </a>
      </div>
    </div>
  );
}

export default PaymentCallback;
