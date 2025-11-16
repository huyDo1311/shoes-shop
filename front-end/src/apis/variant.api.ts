// import { Variant } from './../types/variant.type';
// import {ProductListParam, ProductListType} from '@/types/product.type';
import type {SuccessResponse} from '@/types/utils.type';
import {type VariantResponse,type ProductVariantBodyType, type VariantUpdateResponse} from '@/types/variant.type';
import http from '@/utils/http';

const URL_VARIANT = 'variants'

const variantApi = {
  createVariant: (body: ProductVariantBodyType) => http.post<SuccessResponse<VariantResponse>>(URL_VARIANT, body),
  updateVariant: (sku: string, body: ProductVariantBodyType) => http.put<SuccessResponse<VariantUpdateResponse>>(`${URL_VARIANT}/${sku}`, body),
  deleteVariant: (sku: string) => http.delete<SuccessResponse<string>>(`${URL_VARIANT}/${sku}`)
}

export default variantApi;