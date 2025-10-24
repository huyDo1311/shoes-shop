import type { Color } from '@/types/color.type';
import type { Size } from '@/types/size.type';
import type { SuccessResponse } from '@/types/utils.type';
import http from '@/utils/http';



const URL_SIZE = 'sizes'

const sizeApi = {
  getSize: () => http.get<SuccessResponse<Size[]>>(URL_SIZE)
}

export default sizeApi