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

  const updateOrderStatusMutation = useMutation({
    mutationFn: (body: { vnp_TxnRef: string }) => paymentAPI.updateStatus(body),
  });

  // ✅ Gọi update khi thanh toán thành công
  const [hasUpdated, setHasUpdated] = useState(false);

  useEffect(() => {
    if (
      !hasUpdated &&
      paymentResult["vnp_ResponseCode"] === "00" &&
      paymentResult["vnp_TxnRef"]
    ) {
      updateOrderStatusMutation.mutate({ vnp_TxnRef: paymentResult["vnp_TxnRef"] });
      setHasUpdated(true);
    }
  }, [paymentResult, hasUpdated, updateOrderStatusMutation]);

  // useEffect(() => {
  //   if (updateOrderStatusMutation.isSuccess) {
  //     setTimeout(() => {
  //       sessionStorage.removeItem("paymentUpdated"); // cho phép cập nhật lại trong tương lai
  //       window.location.reload();
  //     }, 2000);
  //   }
  // }, [updateOrderStatusMutation.isSuccess]);

  if (!Object.keys(paymentResult).length) {
    return <p className="text-center mt-10 text-gray-500">Đang xử lý kết quả thanh toán...</p>;
  }

  const isSuccess = paymentResult["vnp_ResponseCode"] === "00";

  return (
    <div className="max-w-lg mx-auto mt-10 p-6 rounded-2xl shadow-md bg-white">
      <h1 className="text-2xl font-semibold text-center mb-4">
        {isSuccess ? "✅ Thanh toán thành công!" : "❌ Thanh toán thất bại"}
      </h1>

      <div className="space-y-2 text-gray-700">
        <p><strong>Mã giao dịch:</strong> {paymentResult["vnp_TxnRef"]}</p>
        <p><strong>Số tiền:</strong> {Number(paymentResult["vnp_Amount"]) / 100} VND</p>
        <p><strong>Ngân hàng:</strong> {paymentResult["vnp_BankCode"] || "Không xác định"}</p>
        <p><strong>Mã phản hồi:</strong> {paymentResult["vnp_ResponseCode"]}</p>
        <p><strong>Thời gian thanh toán:</strong> {paymentResult["vnp_PayDate"]}</p>
      </div>

      {updateOrderStatusMutation.isPending && (
        <p className="text-center mt-4 text-blue-500">⏳ Đang cập nhật trạng thái đơn hàng...</p>
      )}

      {updateOrderStatusMutation.isError && (
        <p className="text-center mt-4 text-red-500">❌ Lỗi khi cập nhật trạng thái đơn hàng</p>
      )}

      {updateOrderStatusMutation.isSuccess && (
        <p className="text-center mt-4 text-green-500">✅ Đã cập nhật đơn hàng thành công!</p>
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
