import type { AddToCartProps } from '@/components/shared/AddToCart'
import type { Cart } from '@/types/order.type'
import type { SuccessResponse } from '@/types/utils.type'
import http from '@/utils/http'

export const URL_ORDER = 'orders'
const URL_CART = 'cart'

export type DeleteCartItemAPIRequest = {
  sku: string;
  email: string;
};

export const orderAPI = {
  addToCart: (body: AddToCartProps) => http.post<SuccessResponse<Cart>>(`${URL_ORDER}/${URL_CART}`, body),
  getCart: (body: { email: string }) => http.post<SuccessResponse<Cart>>(`${URL_ORDER}/${URL_CART}/get`, body),
  // deleteItemCart: (body: Omit<DeleteItemCartProps, 'children'>) =>
  //   http.delete<SuccessResponse<Cart>>(`${URL_ORDER}/cart/items/delete`, { data: body }),
  deleteItemCart: (body: DeleteCartItemAPIRequest) =>
  http.delete<SuccessResponse<Cart>>(`${URL_ORDER}/cart/items/delete`, {
    data: body
  }),
  updateItemQuantity: (body: { email: string; sku: string; quantity: number }) =>
    http.put<SuccessResponse<Cart>>(`${URL_ORDER}/${URL_CART}/items`, body),
  checkOut: (body: { email: string }) => http.post<SuccessResponse<Cart>>(`${URL_ORDER}/checkout`, body)
}
