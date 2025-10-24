import type { Product, ProductList, ProductListParam } from '@/types/product.type';
import type { SuccessResponse } from '@/types/utils.type';
import http from '@/utils/http';



const URL_PRODUCT = 'products'

const productApi = {
  getProducts: (params: ProductListParam) => http.get<SuccessResponse<ProductList>>(URL_PRODUCT, {params}),
  getProductDetail: (id: string) => http.get<SuccessResponse<Product>>(`${URL_PRODUCT}/${id}`)
}

export default productApi