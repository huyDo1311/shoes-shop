
import type { Brand } from '@/types/brand.type';
import type { SuccessResponse } from '@/types/utils.type';
import http from '@/utils/http';



const URL_BRAND = 'brands'

const brandApi = {
  getBrand: () => http.get<SuccessResponse<Brand[]>>(URL_BRAND)
}

export default brandApi