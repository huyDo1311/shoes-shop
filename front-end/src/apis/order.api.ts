import type { AddToCartProps } from "@/components/shared/AddToCart"
import type { DeleteItemCartProps } from "@/components/shared/DeleteService"
import type { Cart } from "@/types/order.type"
import type { SuccessResponse } from "@/types/utils.type"
import http from "@/utils/http"
import type { omit } from "lodash"

const URL_ORDER = "orders"
const URL_CART = "cart"

export const orderAPI = {
  addToCart: (body: AddToCartProps) => http.post<SuccessResponse<Cart>>(`${URL_ORDER}/${URL_CART}`, body),
  getCart: (body: {
    email: string
  }) => http.post<SuccessResponse<Cart>>(`${URL_ORDER}/${URL_CART}/get`, body),
  deleteItemCart: (body: Omit<DeleteItemCartProps, "children">) =>
     http.delete<SuccessResponse<Cart>>(`${URL_ORDER}/cart/items/delete`, {data: body}),
}