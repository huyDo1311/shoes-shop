import { type Product, type ProductListParam, type ProductListType,type BodyCreateProduct} from '@/types/product.type';
import type { SuccessResponse } from '@/types/utils.type'
import http from '@/utils/http'

const URL_PRODUCT = 'products'

const productApi = {
  getProducts: (params: ProductListParam) => http.get<SuccessResponse<ProductListType>>(URL_PRODUCT, { params }),
  getProductDetail: (id: string) => http.get<SuccessResponse<Product>>(`${URL_PRODUCT}/${id}`),
  searchProductName: (name: string) =>
    http.get<SuccessResponse<Product[]>>(`${URL_PRODUCT}/search`, { params: { name } }),
  createProduct: (body: BodyCreateProduct) => http.post<SuccessResponse<Product>>(URL_PRODUCT, body),
  updateProduct: (id: string ,body: BodyCreateProduct) => http.put<SuccessResponse<Product>>(`${URL_PRODUCT}/${id}`, body),
  deleteProduct: (id: number) => http.delete<SuccessResponse<string>>(`${URL_PRODUCT}/${id}`)
}

export default productApi
