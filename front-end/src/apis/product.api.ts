import type { Product, ProductListParam, ProductListType } from '@/types/product.type';
import type { SuccessResponse } from '@/types/utils.type';
import http from '@/utils/http';



const URL_PRODUCT = 'products'

const productApi = {
  getProducts: (params: ProductListParam) => http.get<SuccessResponse<ProductListType>>(URL_PRODUCT, {params}),
  getProductDetail: (id: string) => http.get<SuccessResponse<Product>>(`${URL_PRODUCT}/${id}`),
  searchProductName: (name: string) => http.get<SuccessResponse<Product[]>>(`${URL_PRODUCT}/search`, {params: {name}})
}

export default productApi