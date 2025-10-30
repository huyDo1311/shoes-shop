import http from "@/utils/http";
import type {Cart} from '@/types/order.type';
import type {SuccessResponse} from '@/types/utils.type';
import { URL_ORDER } from "@/apis/order.api";

export const paymentAPI = {
  createPaymentUrl: (body: { amount: number; bankCode?: string; language: string; email: string}) =>
    http.post<{ data: string }>("/payments/create_payment_url", body),
  updateStatus: (body: { vnp_TxnRef: string }) => http.post<SuccessResponse<Cart>>(`${URL_ORDER}/update/status`, body)
};